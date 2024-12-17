package ru.bokgosha.parking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.bokgosha.parking.service.PlaceService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ParkingApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaceService placeService;

    @Test
    public void testGetHome() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(""))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    public void testGetAllPlaces_UnauthorizedUser_RedirectToLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/places"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void testGetLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}
