package ru.bokgosha.parking.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.bokgosha.parking.DTO.UserDTO;
import ru.bokgosha.parking.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    User add(UserDTO userDTO) throws Exception;
    User getUser(String username);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    List<User> getTopUsers();
}
