package ru.bokgosha.parking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bokgosha.parking.DTO.PlaceDTO;
import ru.bokgosha.parking.DTO.RentDTO;
import ru.bokgosha.parking.model.User;
import ru.bokgosha.parking.service.PlaceService;
import ru.bokgosha.parking.service.RentService;
import ru.bokgosha.parking.service.UserService;

@RequestMapping("/rents")
@Controller
@RequiredArgsConstructor
public class RentController {

    private final RentService rentService;
    private final PlaceService placeService;
    private final UserService userService;

    @GetMapping
    public String setRent(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("placeId") Long placeId, Model model) {
        User currentUser = userService.getUser(userDetails.getUsername());
        RentDTO rentDTO;

        if (placeService.getPlace(placeId).isAvailable()) {
            rentDTO = rentService.setRent(currentUser.getId(), placeId);
        } else {
            rentDTO = rentService.getRentByCarIdAndUserId(placeId, currentUser.getId());
        }

        PlaceDTO placeDTO = placeService.getPlace(rentDTO.getPlaceId());

        model.addAttribute("rent", rentDTO);
        model.addAttribute("place", placeDTO);
        model.addAttribute("userDetails", userDetails);

        return "rent";
    }

    @GetMapping("/finish")
    public String finishRent(@AuthenticationPrincipal UserDetails userDetails, Model model, @RequestParam("rentId") Long rentId) {
        if (!placeService.getPlace(rentService.getRent(rentId).getPlaceId()).isAvailable()) {
            rentService.finishRent(rentId);
        }

        RentDTO rentDTO = rentService.getRent(rentId);
        PlaceDTO placeDTO = placeService.getPlace(rentDTO.getPlaceId());

        model.addAttribute("rent", rentDTO);
        model.addAttribute("place", placeDTO);
        model.addAttribute("userDetails", userDetails);

        return "rent";
    }
}
