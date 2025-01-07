package nl.fontys.s3.carenestproject.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.configuration.security.auth.RequestAuthenticatedUserProvider;
import nl.fontys.s3.carenestproject.configuration.security.token.AccessToken;
import nl.fontys.s3.carenestproject.service.MessageService;
import nl.fontys.s3.carenestproject.service.request.message.GetMessagesInConversationRequest;
import nl.fontys.s3.carenestproject.service.request.message.SendMessageRequest;
import nl.fontys.s3.carenestproject.service.response.message.GetExistingConversationsResponse;
import nl.fontys.s3.carenestproject.service.response.message.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final RequestAuthenticatedUserProvider requestAuthenticatedUserProvider;
    private final NotificationController notificationController;

    @PostMapping("/{receiverId}")
    @RolesAllowed({"CARETAKER", "PATIENT"})
    public ResponseEntity<MessageResponse> sendMessage(@PathVariable Long receiverId, @RequestBody@Validated SendMessageRequest request){
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();

        MessageResponse response = messageService.sendMessage(receiverId, request, accessToken.getUserId());
        notificationController.sendMessageNotification(response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{contactedUserId}")
    @RolesAllowed({"CARETAKER", "PATIENT"})
    public ResponseEntity<List<MessageResponse>> getMessagesByParticipants(
            @PathVariable Long contactedUserId,
            @RequestParam() Long connectedUserId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int itemsPerPage){
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();

        GetMessagesInConversationRequest request = GetMessagesInConversationRequest.builder()
                .connectedUserId(connectedUserId)
                .itemsPerPage(itemsPerPage)
                .pageNumber(pageNumber)
                .build();

        List<MessageResponse> messages = messageService.getMessagesByParticipants(accessToken.getUserId(), request, contactedUserId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/conversations/{connectedUserId}")
    @RolesAllowed({"CARETAKER", "PATIENT"})
    public ResponseEntity<List<GetExistingConversationsResponse>> getExistingConversations(@PathVariable Long connectedUserId){
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();

        List<GetExistingConversationsResponse> response = messageService.getExistingConversations(accessToken.getUserId(), connectedUserId);
        return ResponseEntity.ok(response);
    }
}
