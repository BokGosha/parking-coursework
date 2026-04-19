package ru.bokgosha.parking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.bokgosha.parking.dto.ProfileDto;
import ru.bokgosha.parking.dto.TopUserDto;
import ru.bokgosha.parking.dto.UserDto;
import ru.bokgosha.parking.exception.UserAlreadyExistsException;
import ru.bokgosha.parking.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping({"/", "/home"})
    public String homePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<TopUserDto> topUsers = userService.getTopUsers();

        model.addAttribute("topUsers", topUsers);
        model.addAttribute("userDetails", userDetails);

        return "home";
    }

    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        ProfileDto profile = userService.getUserProfile(userDetails.getUsername());

        model.addAttribute("profile", profile);
        model.addAttribute("userDetails", userDetails);

        return "profile";
    }

    @GetMapping("/login")
    public String loginPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("userDetails", userDetails);

        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("user", new UserDto());
        model.addAttribute("userDetails", userDetails);

        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") UserDto userDTO) {
        try {
            userService.createUser(userDTO);
        } catch (UserAlreadyExistsException e) {
            return "redirect:/registration?name_invalid";
        }

        return "redirect:/login";
    }
}
