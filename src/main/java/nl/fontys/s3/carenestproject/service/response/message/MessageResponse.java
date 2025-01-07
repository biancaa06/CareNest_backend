package nl.fontys.s3.carenestproject.service.response.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private Long id;
    private String text;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime date;
}
