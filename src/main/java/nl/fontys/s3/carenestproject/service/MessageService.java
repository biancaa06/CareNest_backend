package nl.fontys.s3.carenestproject.service;

import nl.fontys.s3.carenestproject.service.request.message.GetMessagesInConversationRequest;
import nl.fontys.s3.carenestproject.service.request.message.SendMessageRequest;
import nl.fontys.s3.carenestproject.service.response.message.GetExistingConversationsResponse;
import nl.fontys.s3.carenestproject.service.response.message.MessageResponse;

import java.util.List;

public interface MessageService {
    MessageResponse sendMessage(Long receiverId, SendMessageRequest request, Long authenticatedUserId);
    List<MessageResponse> getMessagesByParticipants (Long authenticatedUserId, GetMessagesInConversationRequest request, Long contactedUserId);
    List<GetExistingConversationsResponse> getExistingConversations ( Long authenticatedUserId, Long connectedUserId);
}
