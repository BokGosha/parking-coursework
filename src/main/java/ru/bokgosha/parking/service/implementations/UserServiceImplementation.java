package ru.bokgosha.parking.service.implementations;

import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bokgosha.parking.DTO.UserDTO;
import ru.bokgosha.parking.model.Role;
import ru.bokgosha.parking.model.User;
import ru.bokgosha.parking.repository.UserRepository;
import ru.bokgosha.parking.service.UserService;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User add(UserDTO userDTO) throws Exception {
        User newuser = userRepository.getByUsername(userDTO.getUsername());

        if (newuser != null) {
            throw new Exception();
        } else {
            User user = new User(userDTO.getUsername(),
                    passwordEncoder.encode(userDTO.getPassword()),
                    List.of(new Role("ROLE_ADMIN")));

            return userRepository.save(user);
        }
    }

    public User getUser(String username) {
        User user = userRepository.getByUsername(username);

        Hibernate.initialize(user.getRents());

        return user;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new org.springframework.security
                .core.userdetails.User(user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    public List<User> getTopUsers() {
        List<User> allUsers = userRepository.findAll();
        List<User> sortedUsers = allUsers.stream()
                .sorted(Comparator.comparingInt(user -> -user.getRents().size()))
                .collect(Collectors.toList());

        return sortedUsers.subList(0, Math.min(sortedUsers.size(), 10));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
