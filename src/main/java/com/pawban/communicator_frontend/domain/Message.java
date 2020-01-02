package com.pawban.communicator_frontend.domain;

import com.pawban.communicator_frontend.dto.MessageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Message {

    private UUID id;
    private String sender;
    private String text;
    private LocalDateTime creationTime;
    private UUID chatRoomId;

    public Message(final MessageDto messageDto) {
        this(
                messageDto.getId(),
                messageDto.getSender(),
                messageDto.getText(),
                messageDto.getCreationTime(),
                messageDto.getChatRoomId()
        );
    }

}
