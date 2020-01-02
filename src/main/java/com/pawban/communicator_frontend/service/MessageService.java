package com.pawban.communicator_frontend.service;

import com.pawban.communicator_frontend.client.MessageClient;
import com.pawban.communicator_frontend.domain.ChatRoom;
import com.pawban.communicator_frontend.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageClient messageClient;

    @Autowired
    public MessageService(final MessageClient messageClient) {
        this.messageClient = messageClient;
    }

    public List<Message> getNewMessagesOfChatRoom(final UUID sessionId,
                                                  final ChatRoom chatRoom) {
        return messageClient.getCurrentUserMessages(sessionId, true, chatRoom.getId());
    }

    public boolean sendNewMessage(final UUID sessionId,
                                  final String messageText,
                                  final UUID chatRoomId) {
        return messageClient.createMessage(sessionId, messageText, chatRoomId).isPresent();
    }

}
