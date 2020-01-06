package com.pawban.communicator_frontend.service;

import com.pawban.communicator_frontend.client.ChatRoomClient;
import com.pawban.communicator_frontend.domain.ChatRoom;
import com.pawban.communicator_frontend.domain.Member;
import com.pawban.communicator_frontend.domain.Message;
import com.pawban.communicator_frontend.domain.User;
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
                                           final ChatRoom chatRoom) {
        return chatRoomClient.getChatRoomMembers(sessionId, chatRoom.getId(), false);
    }

    public List<Member> getChatRoomMembersWithPotentialMembers(final UUID sessionId,
                                                               final ChatRoom chatRoom) {
        return chatRoomClient.getChatRoomMembers(sessionId, chatRoom.getId(), true);
    }

    public List<Message> getNewChatRoomMessages(final UUID sessionId,
                                                final ChatRoom chatRoom) {
        return chatRoomClient.getChatRoomMessages(sessionId, chatRoom.getId(), true);
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
                                         final ChatRoom chatRoom,
                                         final ChatRoomStatus status) {
        return chatRoomClient.changeChatRoomStatus(sessionId, chatRoom.getId(), status)
                .orElseThrow(() -> new RequestUnsuccessfulException(
                        sessionId,
                        "Unable to change the status of chat room with name '" +
                                chatRoom.getName() + "'."
                ));
    }

    public ChatRoom changeChatRoomOwner(final UUID sessionId,
                                        final ChatRoom chatRoom,
                                        final User newOwner) {
        return chatRoomClient.changeChatRoomOwner(sessionId, chatRoom.getId(), newOwner.getId())
                .orElseThrow(() -> new RequestUnsuccessfulException(
                        sessionId,
                        "Unable to pass the ownership of chat room with name '" +
                                chatRoom.getName() + "' to user '" +
                                newOwner.getUsername() + "'."
                ));
    }

    public void addMemberToChatRoom(final UUID sessionId,
                                    final ChatRoom chatRoom,
                                    final User newMember) {
        if (!chatRoomClient.addMemberToChatRoom(sessionId, chatRoom.getId(), newMember.getId())) {
            throw new RequestUnsuccessfulException(
                    sessionId,
                    "Unable to add user '" +
                            newMember.getUsername() + "' to members of chat room with name '" +
                            chatRoom.getName() + "'."
            );
        }
    }

    public void deleteChatRoom(final UUID sessionId,
                               final ChatRoom chatRoom) {
        if (!chatRoomClient.deleteChatRoom(sessionId, chatRoom.getId())) {
            throw new RequestUnsuccessfulException(
                    sessionId,
                    "Unable to delete chat room with name '" + chatRoom.getName() + "'."
            );
        }
    }

    public void removeMemberFromChatRoom(final UUID sessionId,
                                         final ChatRoom chatRoom,
                                         final User member) {
        if (!chatRoomClient.removeMemberFromChatRoom(sessionId, chatRoom.getId(), member.getId())) {
            throw new RequestUnsuccessfulException(
                    sessionId,
                    "Unable to remove user '" +
                            member.getUsername() + "' from members of chat room with name = '" +
                            chatRoom.getName() + "'."
            );
        }
    }

}
