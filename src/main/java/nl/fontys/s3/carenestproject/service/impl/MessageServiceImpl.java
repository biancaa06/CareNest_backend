package nl.fontys.s3.carenestproject.service.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import nl.fontys.s3.carenestproject.persistance.entity.MessageEntity;
import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.MessageRepo;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.UserRepo;
import nl.fontys.s3.carenestproject.service.MessageService;
import nl.fontys.s3.carenestproject.service.exception.ObjectNotFoundException;
import nl.fontys.s3.carenestproject.service.exception.UnauthorizedException;
import nl.fontys.s3.carenestproject.service.mapping.MessageConverter;
import nl.fontys.s3.carenestproject.service.request.message.GetMessagesInConversationRequest;
import nl.fontys.s3.carenestproject.service.request.message.SendMessageRequest;
import nl.fontys.s3.carenestproject.service.response.message.GetExistingConversationsResponse;
import nl.fontys.s3.carenestproject.service.response.message.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Builder
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final UserRepo userRepo;
    private final MessageRepo messageRepo;

    private final MessageConverter messageConverter;

    @Override
    public MessageResponse sendMessage(Long receiverId, SendMessageRequest request, Long authenticatedUserId) {
        if(!Objects.equals(authenticatedUserId, request.getSenderId())) {
            throw new UnauthorizedException("Not authorized");
        }

        UserEntity sender = userRepo.findById(authenticatedUserId).orElseThrow(() -> new ObjectNotFoundException("Sender not found"));
        UserEntity receiver = userRepo.findById(receiverId).orElseThrow(() -> new ObjectNotFoundException("Receiver not found"));

        MessageEntity messageEntity= messageRepo.save(MessageEntity.builder()
                .text(request.getText())
                .date(LocalDateTime.now())
                .sender(sender)
                .receiver(receiver).build());

        return MessageResponse.builder()
                .id(messageEntity.getId())
                .text(messageEntity.getText())
                .date(messageEntity.getDate())
                .senderId(messageEntity.getSender().getId())
                .receiverId(messageEntity.getReceiver().getId())
                .build();
    }

    @Override
    public List<MessageResponse> getMessagesByParticipants(Long authenticatedUserId, GetMessagesInConversationRequest request, Long contactedUserId) {
        if(!Objects.equals(authenticatedUserId, request.getConnectedUserId())) {
            throw new UnauthorizedException("Not authorized");
        }

        UserEntity connectedUser = userRepo.findById(request.getConnectedUserId()).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        UserEntity contactedUser = userRepo.findById(contactedUserId).orElseThrow(() -> new ObjectNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getItemsPerPage(), Sort.by("date").descending());
        Page<MessageEntity> messageEntities = messageRepo.findMessageEntitiesByParticipants(connectedUser, contactedUser, pageable);

        List<MessageResponse> messages = new ArrayList<>();
        for(MessageEntity messageEntity : messageEntities) {
            messages.add(messageConverter.convertFromEntityToResponse(messageEntity));
        }

        Collections.reverse(messages);
        return messages;
    }

    @Override
    public List<GetExistingConversationsResponse> getExistingConversations(Long authenticatedUserId, Long connectedUserId) {
        if(!Objects.equals(authenticatedUserId, connectedUserId)) {
            throw new UnauthorizedException("Not authorized");
        }
        if(!userRepo.existsById(connectedUserId)) {
            throw new ObjectNotFoundException("User not found");
        }
        List<UserEntity> userEntities = userRepo.findUserEntitiesWithConversation(connectedUserId);

        List<GetExistingConversationsResponse> responses = new ArrayList<>();

        for(UserEntity userEntity : userEntities) {
            responses.add(GetExistingConversationsResponse.builder()
                    .userId(userEntity.getId())
                    .userImage(userEntity.getProfileImage())
                    .userName(userEntity.getFirstName() + " " + userEntity.getLastName())
                    .build());
        }

        return responses;
    }


}
