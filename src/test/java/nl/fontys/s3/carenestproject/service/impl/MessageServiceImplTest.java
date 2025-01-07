package nl.fontys.s3.carenestproject.service.impl;

import nl.fontys.s3.carenestproject.persistance.entity.MessageEntity;
import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.MessageRepo;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.UserRepo;
import nl.fontys.s3.carenestproject.service.exception.ObjectNotFoundException;
import nl.fontys.s3.carenestproject.service.mapping.MessageConverter;
import nl.fontys.s3.carenestproject.service.request.message.GetMessagesInConversationRequest;
import nl.fontys.s3.carenestproject.service.request.message.SendMessageRequest;
import nl.fontys.s3.carenestproject.service.response.message.GetExistingConversationsResponse;
import nl.fontys.s3.carenestproject.service.response.message.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private UserRepo mockUserRepo;
    @Mock
    private MessageRepo mockMessageRepo;

    private MessageServiceImpl messageServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        messageServiceImplUnderTest = new MessageServiceImpl(mockUserRepo, mockMessageRepo, new MessageConverter());
    }

    @Test
    void testSendMessage() {
        // Setup
        final SendMessageRequest request = SendMessageRequest.builder()
                .text("text")
                .senderId(0L)
                .build();
        final MessageResponse expectedResult = MessageResponse.builder()
                .id(0L)
                .text("text")
                .senderId(0L)
                .receiverId(0L)
                .date(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .build();

        // Configure UserRepo.findById(...).
        final Optional<UserEntity> userEntity = Optional.of(UserEntity.builder()
                .id(0L)
                .firstName("firstName")
                .lastName("lastName")
                .profileImage("content".getBytes())
                .build());
        when(mockUserRepo.findById(0L)).thenReturn(userEntity);

        // Configure MessageRepo.save(...).
        final MessageEntity messageEntity = MessageEntity.builder()
                .id(0L)
                .sender(UserEntity.builder()
                        .id(0L)
                        .firstName("firstName")
                        .lastName("lastName")
                        .profileImage("content".getBytes())
                        .build())
                .receiver(UserEntity.builder()
                        .id(0L)
                        .firstName("firstName")
                        .lastName("lastName")
                        .profileImage("content".getBytes())
                        .build())
                .date(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .text("text")
                .build();
        when(mockMessageRepo.save(any(MessageEntity.class))).thenReturn(messageEntity);

        // Run the test
        final MessageResponse result = messageServiceImplUnderTest.sendMessage(0L, request, 0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testSendMessage_UserRepoReturnsAbsent() {
        // Setup
        final SendMessageRequest request = SendMessageRequest.builder()
                .text("text")
                .senderId(0L)
                .build();
        when(mockUserRepo.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> messageServiceImplUnderTest.sendMessage(0L, request, 0L))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testGetMessagesByParticipants() {
        // Setup
        final GetMessagesInConversationRequest request = GetMessagesInConversationRequest.builder()
                .connectedUserId(0L)
                .pageNumber(1)
                .itemsPerPage(10)
                .build();
        final List<MessageResponse> expectedResult = List.of(MessageResponse.builder()
                .id(0L)
                .text("text")
                .senderId(0L)
                .receiverId(0L)
                .date(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .build());

        // Configure UserRepo.findById(...).
        final Optional<UserEntity> userEntity = Optional.of(UserEntity.builder()
                .id(0L)
                .firstName("firstName")
                .lastName("lastName")
                .profileImage("content".getBytes())
                .build());
        when(mockUserRepo.findById(0L)).thenReturn(userEntity);

        // Configure MessageRepo.findMessageEntitiesByParticipants(...).
        final Page<MessageEntity> messageEntities = new PageImpl<>(List.of(MessageEntity.builder()
                .id(0L)
                .sender(UserEntity.builder()
                        .id(0L)
                        .firstName("firstName")
                        .lastName("lastName")
                        .profileImage("content".getBytes())
                        .build())
                .receiver(UserEntity.builder()
                        .id(0L)
                        .firstName("firstName")
                        .lastName("lastName")
                        .profileImage("content".getBytes())
                        .build())
                .date(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .text("text")
                .build()));
        when(mockMessageRepo.findMessageEntitiesByParticipants(eq(UserEntity.builder()
                .id(0L)
                .firstName("firstName")
                .lastName("lastName")
                .profileImage("content".getBytes())
                .build()), eq(UserEntity.builder()
                .id(0L)
                .firstName("firstName")
                .lastName("lastName")
                .profileImage("content".getBytes())
                .build()), any(Pageable.class))).thenReturn(messageEntities);

        // Run the test
        final List<MessageResponse> result = messageServiceImplUnderTest.getMessagesByParticipants(0L, request, 0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetMessagesByParticipants_UserRepoReturnsAbsent() {
        // Setup
        final GetMessagesInConversationRequest request = GetMessagesInConversationRequest.builder()
                .connectedUserId(0L)
                .pageNumber(0)
                .itemsPerPage(0)
                .build();
        when(mockUserRepo.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> messageServiceImplUnderTest.getMessagesByParticipants(0L, request, 0L))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testGetMessagesByParticipants_MessageRepoReturnsNoItems() {
        // Setup
        final GetMessagesInConversationRequest request = GetMessagesInConversationRequest.builder()
                .connectedUserId(0L)
                .pageNumber(1)
                .itemsPerPage(10)
                .build();

        // Configure UserRepo.findById(...).
        final Optional<UserEntity> userEntity = Optional.of(UserEntity.builder()
                .id(0L)
                .firstName("firstName")
                .lastName("lastName")
                .profileImage("content".getBytes())
                .build());
        when(mockUserRepo.findById(0L)).thenReturn(userEntity);

        when(mockMessageRepo.findMessageEntitiesByParticipants(eq(UserEntity.builder()
                .id(0L)
                .firstName("firstName")
                .lastName("lastName")
                .profileImage("content".getBytes())
                .build()), eq(UserEntity.builder()
                .id(0L)
                .firstName("firstName")
                .lastName("lastName")
                .profileImage("content".getBytes())
                .build()), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        // Run the test
        final List<MessageResponse> result = messageServiceImplUnderTest.getMessagesByParticipants(0L, request, 0L);

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetExistingConversations() {
        // Setup
        final List<GetExistingConversationsResponse> expectedResult = List.of(GetExistingConversationsResponse.builder()
                .userId(0L)
                .userName("firstName lastName")
                .userImage("content".getBytes())
                .build());
        when(mockUserRepo.existsById(0L)).thenReturn(true);

        // Configure UserRepo.findUserEntitiesWithConversation(...).
        final List<UserEntity> userEntities = List.of(UserEntity.builder()
                .id(0L)
                .firstName("firstName")
                .lastName("lastName")
                .profileImage("content".getBytes())
                .build());
        when(mockUserRepo.findUserEntitiesWithConversation(0L)).thenReturn(userEntities);

        // Run the test
        final List<GetExistingConversationsResponse> result = messageServiceImplUnderTest.getExistingConversations(0L,
                0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetExistingConversations_UserRepoExistsByIdReturnsFalse() {
        // Setup
        when(mockUserRepo.existsById(0L)).thenReturn(false);

        // Run the test
        assertThatThrownBy(() -> messageServiceImplUnderTest.getExistingConversations(0L, 0L))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testGetExistingConversations_UserRepoFindUserEntitiesWithConversationReturnsNoItems() {
        // Setup
        when(mockUserRepo.existsById(0L)).thenReturn(true);
        when(mockUserRepo.findUserEntitiesWithConversation(0L)).thenReturn(Collections.emptyList());

        // Run the test
        final List<GetExistingConversationsResponse> result = messageServiceImplUnderTest.getExistingConversations(0L,
                0L);

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }
}
