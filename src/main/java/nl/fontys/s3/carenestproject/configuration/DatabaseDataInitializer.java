package nl.fontys.s3.carenestproject.configuration;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.users.Manager;
import nl.fontys.s3.carenestproject.persistance.entity.AddressEntity;
import nl.fontys.s3.carenestproject.persistance.entity.AnnouncementEntity;
import nl.fontys.s3.carenestproject.persistance.entity.ManagerEntity;
import nl.fontys.s3.carenestproject.service.repoInterfaces.AnnouncementRepo;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class DatabaseDataInitializer {

    private final AnnouncementRepo announcementRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void populateDatabaseInitialDummyData() {
        if(announcementRepo.countAnnouncements() == 0){
            announcementRepo.createAnnouncement(AnnouncementEntity.builder()
                    .id(1L)
                    .title("health")
                    .description("take care")
                    .author(
                        ManagerEntity.builder()
                                .id(1)
                                .email("email")
                                .firstName("first")
                                .gender("MALE")
                                .position("MEDICAL")
                                .address(AddressEntity.builder()
                                        .id(1L)
                                        .country("country")
                                        .city("city")
                                        .street("street")
                                        .number(56)
                                        .build()
                                )
                                .build()
                    )
                    .date(LocalDate.now())
                    .build());
        }
    }
}
