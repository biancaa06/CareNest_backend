package nl.fontys.s3.carenestproject.service.mapping;

import nl.fontys.s3.carenestproject.domain.classes.Message;
import nl.fontys.s3.carenestproject.persistance.entity.MessageEntity;
import nl.fontys.s3.carenestproject.service.response.message.MessageResponse;
import org.springframework.stereotype.Component;

@Component
public final class MessageConverter {
    public Message convertFromEntityToBase(MessageEntity message) {
        return Message.builder()
                .id(message.getId())
                .date(message.getDate())
                .text(message.getText())
                .receiver(BaseUserConverter.convertFromEntityToBase(message.getReceiver()))
                .sender(BaseUserConverter.convertFromEntityToBase(message.getSender()))
                .build();
    }

    public MessageResponse convertFromEntityToResponse(MessageEntity message) {
        return MessageResponse.builder()
                .id(message.getId())
                .date(message.getDate())
                .text(message.getText())
                .receiverId(message.getReceiver().getId())
                .senderId(message.getSender().getId())
                .build();
    }
}
