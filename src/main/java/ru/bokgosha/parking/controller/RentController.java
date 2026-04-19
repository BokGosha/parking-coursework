package ru.bokgosha.parking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bokgosha.parking.dto.PlaceDto;
import ru.bokgosha.parking.dto.RentDto;
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
    public String setRent(@AuthenticationPrincipal UserDetails userDetails,
                          @RequestParam("placeId") Long placeId,
                          Model model) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        RentDto rentDTO = placeService.getPlaceById(placeId).isAvailable()
                ? rentService.createRent(currentUser.getId(), placeId)
                : rentService.getRentByCarIdAndUserId(placeId, currentUser.getId());

        PlaceDto placeDTO = PlaceDto.from(placeService.getPlaceById(rentDTO.getPlaceId()));

        model.addAttribute("rent", rentDTO);
        model.addAttribute("place", placeDTO);
        model.addAttribute("userDetails", userDetails);

        return "rent";
    }

    @PostMapping("/finish")
    public String finishRent(@AuthenticationPrincipal UserDetails userDetails,
                             Model model,
                             @RequestParam("rentId") Long rentId) {
        rentService.finishRent(rentId);

        RentDto rentDTO = rentService.getRentById(rentId);
        PlaceDto placeDTO = PlaceDto.from(placeService.getPlaceById(rentDTO.getPlaceId()));

        model.addAttribute("rent", rentDTO);
        model.addAttribute("place", placeDTO);
        model.addAttribute("userDetails", userDetails);

        return "rent";
    }
}
