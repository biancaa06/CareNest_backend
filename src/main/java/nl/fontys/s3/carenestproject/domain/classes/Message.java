package nl.fontys.s3.carenestproject.domain.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.users.User;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message {
    private Long id;
    private User sender;
    private User receiver;
    private LocalDateTime date;
    private String text;
}
