package nl.fontys.s3.carenestproject.service.request.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMessagesInConversationRequest {
    private Long connectedUserId;
    private int pageNumber;
    private int itemsPerPage;
}
