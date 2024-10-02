package nl.fontys.s3.carenestproject.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.Announcement;
import nl.fontys.s3.carenestproject.service.AnnouncementService;
import nl.fontys.s3.carenestproject.service.request.CreateAnnouncementRequest;
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

    /*@GetMapping()
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }*/

    @GetMapping("/id:{id}")
    public ResponseEntity<Announcement>getAnnouncementById(@PathVariable(value = "id") final long id){
        final Announcement announcement = announcementService.getAnnouncementById(id);
        if (announcement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(announcement);
    }

    @GetMapping("/title:{title}")
    public ResponseEntity<Announcement> getAnnouncementByTitle(@PathVariable(value = "title") final String title){
        final Announcement announcement = announcementService.getAnnouncementByTitle(title);
        if (announcement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(announcement);
    }

    /*@PostMapping()
    public ResponseEntity<CreateAnnouncementResponse> addAnnouncement(@RequestBody @Validated CreateAnnouncementRequest announcement){
        CreateAnnouncementResponse response = announcementService.createAnnouncement(announcement);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(response);
    }*/

    @DeleteMapping("/id:{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable(value="id") final long id){
        announcementService.deleteAnnouncement(id);

        return ResponseEntity.noContent().build();
    }

}
