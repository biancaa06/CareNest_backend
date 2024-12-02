package nl.fontys.s3.carenestproject.controller;

import nl.fontys.s3.carenestproject.domain.classes.Sickness;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.SicknessRepo;
import nl.fontys.s3.carenestproject.service.SicknessService;
import nl.fontys.s3.carenestproject.service.response.CreateSicknessResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class SicknessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SicknessService sicknessService;
    @Autowired
    private SicknessRepo sicknessRepo;

    @BeforeEach
    void setUp() {
        reset(sicknessService);
    }

    @Test
    void getSickness_ShouldReturn200AndList() throws Exception {
        // Arrange
        var sicknesses = List.of(
                new Sickness(1L, "Flu"),
                new Sickness(2L, "Cold")
        );
        when(sicknessService.getAllSicknesses()).thenReturn(sicknesses);

        // Act & Assert
        mockMvc.perform(get("/sickness")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        [
                            {"id":1,"name":"Flu"},
                            {"id":2,"name":"Cold"}
                        ]
                        """));

        verify(sicknessService, times(1)).getAllSicknesses();
    }

    @Test
    void getSicknessById_ShouldReturn200AndSickness() throws Exception {
        // Arrange
        Sickness sickness = new Sickness(1L, "Flu");
        when(sicknessService.getSicknessById(anyLong())).thenReturn(sickness);

        // Act & Assert
        mockMvc.perform(get("/sickness/id:1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                    {"id":1,"name":"Flu"}
                    """));

        verify(sicknessService, times(1)).getSicknessById(1L);
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    void createSickness_ShouldReturn200AndResponse() throws Exception {
        // Arrange
        var requestBody = """
                {
                    "name": "Flu"
                }
                """;
        CreateSicknessResponse response = new CreateSicknessResponse(1L, "Flu");

        when(sicknessService.createSickness(any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/sickness")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {"id":1,"name":"Flu"}
                        """));

        verify(sicknessService, times(1)).createSickness(any());
    }
    @Test
    void createSickness_UnauthorizedShouldReturn401() throws Exception {
        // Arrange
        var requestBody = """
                {
                    "name": "Flu"
                }
                """;
        CreateSicknessResponse response = new CreateSicknessResponse(1L, "Flu");

        when(sicknessService.createSickness(any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/sickness")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is(401));

        verify(sicknessService, times(0)).createSickness(any());
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    void deleteSickness_ShouldReturn204WhenAuthorized() throws Exception {
        // Arrange
        doNothing().when(sicknessService).deleteSicknessById(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/sickness/1")
                        .header("Authorization", "Bearer valid-token") // Mock valid token
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(sicknessService, times(1)).deleteSicknessById(1L);
    }

    @Test
    @WithMockUser(roles = {"PATIENT"})
    void deleteSickness_ShouldReturn403() throws Exception {
        // Arrange
        doNothing().when(sicknessService).deleteSicknessById(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/sickness/1")
                        .header("Authorization", "Bearer valid-token") // Mock valid token
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));

        verify(sicknessService, times(0)).deleteSicknessById(1L);
    }

}
