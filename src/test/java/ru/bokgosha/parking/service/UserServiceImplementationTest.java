package ru.bokgosha.parking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.bokgosha.parking.dto.UserDto;
import ru.bokgosha.parking.exception.UserAlreadyExistsException;
import ru.bokgosha.parking.model.Role;
import ru.bokgosha.parking.model.User;
import ru.bokgosha.parking.repository.RoleRepository;
import ru.bokgosha.parking.repository.UserRepository;
import ru.bokgosha.parking.service.implementations.UserServiceImplementation;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImplementation userService;

    @Test
    void testAddExistingUser() {
        UserDto userDTO = new UserDto();
        userDTO.setUsername("existingUser");
        userDTO.setPassword("password");

        when(userRepository.findUserByUsername("existingUser")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserByUsername() {
        User testUser = new User();
        testUser.setUsername("testUser");

        when(userRepository.findUserByUsername("testUser")).thenReturn(Optional.of(testUser));

        User retrievedUser = userService.getUserByUsername("testUser");

        assertNotNull(retrievedUser);
        assertEquals("testUser", retrievedUser.getUsername());
    }

    @Test
    void testLoadUserByUsername() {
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("hashedPassword");
        testUser.setRoles(List.of(new Role("ROLE_ADMIN")));

        when(userRepository.findUserByUsername("testUser")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("hashedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().containsAll(mapRolesToAuthorities(testUser.getRoles())));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }
}
