package nl.fontys.s3.carenestproject.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.configuration.security.auth.RequestAuthenticatedUserProvider;
import nl.fontys.s3.carenestproject.configuration.security.token.AccessToken;
import nl.fontys.s3.carenestproject.domain.classes.Announcement;
import nl.fontys.s3.carenestproject.service.AnnouncementService;
import nl.fontys.s3.carenestproject.service.request.CreateAnnouncementRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateAnnouncementRequest;
import nl.fontys.s3.carenestproject.service.response.CreateAnnouncementResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/announcements")
@AllArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;
    private final RequestAuthenticatedUserProvider requestAuthenticatedUserProvider;
    private final NotificationController notificationController;

    @GetMapping()
    @RolesAllowed({"MANAGER", "CARETAKER", "PATIENT"})
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }

    @GetMapping("/id:{id}")
    @RolesAllowed({"MANAGER", "CARETAKER", "PATIENT"})
    public ResponseEntity<Announcement>getAnnouncementById(@PathVariable(value = "id") final long id){
        final Announcement announcement = announcementService.getAnnouncementById(id);
        if (announcement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(announcement);
    }

    @GetMapping("/title:{title}")
    @RolesAllowed({"MANAGER", "CARETAKER", "PATIENT"})
    public ResponseEntity<Announcement> getAnnouncementByTitle(@PathVariable(value = "title") final String title){
        final Announcement announcement = announcementService.getAnnouncementByTitle(title);
        if (announcement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(announcement);
    }

    @GetMapping("/author/{authorId}")
    @RolesAllowed({"MANAGER"})
    public ResponseEntity<List<Announcement>>getAnnouncementsByAuthor(@PathVariable(value = "authorId") final long authorId){
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();

        if (accessToken == null || accessToken.getUserId() == null){
            return ResponseEntity.status(401).build();
        }

        final List<Announcement> announcements = announcementService.getAnnouncementsByAuthor(authorId);
        return ResponseEntity.ok().body(announcements);
    }

    @PostMapping()
    @RolesAllowed({"MANAGER"})
    public ResponseEntity<CreateAnnouncementResponse> addAnnouncement(@RequestBody @Validated CreateAnnouncementRequest announcement){
        CreateAnnouncementResponse response = announcementService.createAnnouncement(announcement);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        notificationController.sendAnnouncementNotification(
                response.getAuthor().getBaseUser().getFirstName() + response.getAuthor().getBaseUser().getLastName(),
                response.getTitle());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed({"MANAGER"})
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable(value="id") final long id){
        announcementService.deleteAnnouncementById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @RolesAllowed({"MANAGER"})
    public ResponseEntity<Void> updateAnnouncement(@PathVariable(value="id") final long id, @RequestBody @Validated UpdateAnnouncementRequest request){
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();

        if (accessToken == null || accessToken.getUserId() == null){
            return ResponseEntity.status(401).build();
        }

        announcementService.updateAnnouncement(id, request, accessToken.getUserId());
        return ResponseEntity.ok().build();
    }

}
