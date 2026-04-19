package ru.bokgosha.parking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bokgosha.parking.dto.PlaceDto;
import ru.bokgosha.parking.model.User;
import ru.bokgosha.parking.service.PlaceService;
import ru.bokgosha.parking.service.UserService;

import java.util.List;

@RequestMapping("/places")
@Controller
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final UserService userService;

    @GetMapping
    public String getPlaces(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<PlaceDto> places = placeService.getPlaces();
        User currentUser = userService.getUserByUsername(userDetails.getUsername());

        model.addAttribute("places", places);
        model.addAttribute("userId", currentUser.getId());
        model.addAttribute("userDetails", userDetails);

        return "places";
    }

    @GetMapping("/{placeId}")
    public String getPlace(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long placeId, Model model) {
        PlaceDto placeDTO = placeService.getPlaceDetails(placeId);

        model.addAttribute("place", placeDTO);
        model.addAttribute("userDetails", userDetails);

        return "place";
    }

    @GetMapping("/sorted")
    public String getPlacesSortedByNumber(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<PlaceDto> places = placeService.getPlacesSortedByNumber();
        User currentUser = userService.getUserByUsername(userDetails.getUsername());

        model.addAttribute("places", places);
        model.addAttribute("userId", currentUser.getId());
        model.addAttribute("userDetails", userDetails);

        return "places";
    }

    @GetMapping("/filtered")
    public String getFilteredPlaces(@AuthenticationPrincipal UserDetails userDetails,
                                    @RequestParam("available") boolean available,
                                    Model model) {
        List<PlaceDto> places = placeService.getFilteredPlacesByAvailable(available);
        User currentUser = userService.getUserByUsername(userDetails.getUsername());

        model.addAttribute("places", places);
        model.addAttribute("userId", currentUser.getId());
        model.addAttribute("userDetails", userDetails);

        return "places";
    }
}
