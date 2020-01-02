package com.pawban.communicator_frontend.service;

import com.pawban.communicator_frontend.client.ChatRoomClient;
import com.pawban.communicator_frontend.domain.ChatRoom;
import com.pawban.communicator_frontend.domain.Member;
import com.pawban.communicator_frontend.domain.Message;
import com.pawban.communicator_frontend.exception.RequestUnsuccessfulException;
import com.pawban.communicator_frontend.type.ChatRoomStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatRoomService {

    private final ChatRoomClient chatRoomClient;

    @Autowired
    public ChatRoomService(final ChatRoomClient chatRoomClient) {
        this.chatRoomClient = chatRoomClient;
    }

    public List<ChatRoom> getAvailableChatRooms(final UUID sessionId) {
        return chatRoomClient.getAvailableChatRooms(sessionId);
    }

    public List<Member> getChatRoomMembers(final UUID sessionId,
                                           final UUID chatRoomId) {
        return chatRoomClient.getChatRoomMembers(sessionId, chatRoomId, false);
    }

    public List<Member> getChatRoomMembersWithPotentialMembers(final UUID sessionId,
                                                               final UUID chatRoomId) {
        return chatRoomClient.getChatRoomMembers(sessionId, chatRoomId, true);
    }

    public List<Message> getNewChatRoomMessages(final UUID sessionId,
                                                final UUID chatRoomId) {
        return chatRoomClient.getChatRoomMessages(sessionId, chatRoomId, true);
    }

    public ChatRoom createChatRoom(final UUID sessionId,
                                   final String name,
                                   final ChatRoomStatus status) {
        return chatRoomClient.createChatRoom(sessionId, name, status)
                .orElseThrow(() -> new RequestUnsuccessfulException(
                        sessionId,
                        "Unable to create chat room with name '" + name + "'."
                ));
    }

    public ChatRoom changeChatRoomStatus(final UUID sessionId,
                                         final UUID chatRoomId,
                                         final ChatRoomStatus status) {
        return chatRoomClient.changeChatRoomStatus(sessionId, chatRoomId, status)
                .orElseThrow(() -> new RequestUnsuccessfulException(
                        sessionId,
                        "Unable to change the status of chat room with id = '" +
                                chatRoomId.toString() + "'."
                ));
    }

    public ChatRoom changeChatRoomOwner(final UUID sessionId,
                                        final UUID chatRoomId,
                                        final UUID newOwnerId) {
        return chatRoomClient.changeChatRoomOwner(sessionId, chatRoomId, newOwnerId)
                .orElseThrow(() -> new RequestUnsuccessfulException(
                        sessionId,
                        "Unable to pass the ownership of chat room with id = '" +
                                chatRoomId.toString() + "' to user with id = '" +
                                newOwnerId.toString() + "'."
                ));
    }

    public void addMemberToChatRoom(final UUID sessionId,
                                    final UUID chatRoomId,
                                    final UUID newMemberId) {
        if (!chatRoomClient.addMemberToChatRoom(sessionId, chatRoomId, newMemberId)) {
            throw new RequestUnsuccessfulException(
                    sessionId,
                    "Unable to add member with id = '" +
                            newMemberId.toString() + "' to chat room with id = '" +
                            chatRoomId.toString() + "'."
            );
        }
    }

    public void deleteChatRoom(final UUID sessionId,
                               final UUID chatRoomId) {
        if (!chatRoomClient.deleteChatRoom(sessionId, chatRoomId)) {
            throw new RequestUnsuccessfulException(
                    sessionId,
                    "Unable to delete chat room with id = '" + chatRoomId.toString() + "'."
            );
        }
    }

    public void removeMemberFromChatRoom(final UUID sessionId,
                                         final UUID chatRoomId,
                                         final UUID memberId) {
        if (!chatRoomClient.removeMemberFromChatRoom(sessionId, chatRoomId, memberId)) {
            throw new RequestUnsuccessfulException(
                    sessionId,
                    "Unable to remove member with id = '" +
                            memberId.toString() + "' from chat room with id = '" +
                            chatRoomId.toString() + "'."
            );
        }
    }

}
