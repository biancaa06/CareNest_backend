package nl.fontys.s3.carenestproject.controller;

import nl.fontys.s3.carenestproject.persistance.entity.SicknessEntity;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.SicknessRepo;
import nl.fontys.s3.carenestproject.service.request.UpdateSicknessRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SicknessControllerTest {

    @MockBean
    private SicknessRepo sicknessRepo;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getSickness_ShouldReturn200AndList() throws Exception {
        // Arrange
        var sicknessList = List.of(
                SicknessEntity.builder().id(1L).name("Flu").build(),
                SicknessEntity.builder().id(2L).name("Cold").build()
        );
        when(sicknessRepo.findAll()).thenReturn(sicknessList);

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
    }

    /*@Test
    void getSicknessById_ShouldReturn200AndSickness() throws Exception {
        // Arrange
        SicknessEntity sickness = SicknessEntity.builder().id(1L).name("Flu").build();
        when(sicknessRepo.findById(1L)).thenReturn(Optional.of(sickness));

        // Act & Assert
        mockMvc.perform(get("/sickness/id:1") // Correct endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expecting 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Content type should be application/json
                .andExpect(content().json("""
                {
                    "id": 1,
                    "name": "Flu"
                }
                """)); // Single object, not an array
    }*/


    @Test
    @WithMockUser(roles = {"MANAGER"})
    void createSickness_ShouldReturn200AndResponse() throws Exception {
        // Arrange
        var sickness = SicknessEntity.builder().id(1L).name("Flu").build();
        when(sicknessRepo.save(any())).thenReturn(sickness);

        var requestBody = """
                {
                    "name": "Flu"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/sickness")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {"id":1,"name":"Flu"}
                        """));
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    void deleteSickness_ShouldReturn204WhenAuthorized() throws Exception {
        // Arrange
        doNothing().when(sicknessRepo).deleteById(1L);
        when(sicknessRepo.existsById(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/sickness/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"PATIENT"})
    void deleteSickness_ShouldReturn403WhenUnauthorizedRole() throws Exception {
        // Arrange
        when(sicknessRepo.existsById(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/sickness/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(sicknessRepo, never()).deleteById(1L);
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    void updateSickness_ShouldReturn200() throws Exception {
        // Arrange
        long id = 1L;
        String newName = "Updated Sickness Name";

        UpdateSicknessRequest updateRequest = UpdateSicknessRequest.builder()
                .newSicknessName(newName)
                .build();

        SicknessEntity existingSickness = SicknessEntity.builder().id(id).name("Old Sickness Name").build();
        SicknessEntity updatedSickness = SicknessEntity.builder().id(id).name(newName).build();

        when(sicknessRepo.findSicknessEntityById(id)).thenReturn(existingSickness);
        when(sicknessRepo.save(existingSickness)).thenReturn(updatedSickness);

        String requestBody = """
            {
                "newSicknessName": "Updated Sickness Name"
            }
            """;

        // Act & Assert
        mockMvc.perform(put("/sickness/id:1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }


}
