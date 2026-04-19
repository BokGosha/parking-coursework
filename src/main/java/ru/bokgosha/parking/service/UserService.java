package ru.bokgosha.parking.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.bokgosha.parking.dto.ProfileDto;
import ru.bokgosha.parking.dto.TopUserDto;
import ru.bokgosha.parking.dto.UserDto;
import ru.bokgosha.parking.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    User createUser(UserDto userDTO);

    User getUserById(Long id);

    User getUserByUsername(String username);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    List<TopUserDto> getTopUsers();

    ProfileDto getUserProfile(String username);
}
