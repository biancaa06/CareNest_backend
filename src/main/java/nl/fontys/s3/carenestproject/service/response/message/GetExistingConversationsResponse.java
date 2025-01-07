package nl.fontys.s3.carenestproject.service.response.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetExistingConversationsResponse {
    private Long userId;
    private String userName;
    private byte[] userImage;
}
