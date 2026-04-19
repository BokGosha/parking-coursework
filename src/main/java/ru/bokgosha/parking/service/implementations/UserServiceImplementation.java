package ru.bokgosha.parking.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bokgosha.parking.dto.ProfileDto;
import ru.bokgosha.parking.dto.RentDto;
import ru.bokgosha.parking.dto.TopUserDto;
import ru.bokgosha.parking.dto.UserDto;
import ru.bokgosha.parking.exception.UserAlreadyExistsException;
import ru.bokgosha.parking.model.Role;
import ru.bokgosha.parking.model.User;
import ru.bokgosha.parking.repository.RentRepository;
import ru.bokgosha.parking.repository.RoleRepository;
import ru.bokgosha.parking.repository.UserRepository;
import ru.bokgosha.parking.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private static final String DEFAULT_ROLE = "ROLE_USER";
    private static final int TOP_USERS_LIMIT = 10;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RentRepository rentRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(UserDto userDTO) {
        if (userRepository.findUserByUsername(userDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с именем " + userDTO.getUsername() + " уже существует");
        }

        Role userRole = roleRepository.findRoleByName(DEFAULT_ROLE)
                .orElseGet(() -> roleRepository.save(new Role(DEFAULT_ROLE)));

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(List.of(userRole));

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с id=" + id + " не найден"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с username=" + username + " не найден"));
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Неверный username или пароль"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    public List<TopUserDto> getTopUsers() {
        Pageable pageable = PageRequest.ofSize(TOP_USERS_LIMIT);
        return userRepository.findTopUsers(pageable);
    }

    public ProfileDto getUserProfile(String username) {
        List<RentDto> rents = rentRepository.findRentsByUsernameWithPlace(username)
                .stream()
                .map(RentDto::from)
                .toList();

        return new ProfileDto(username, rents);
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
