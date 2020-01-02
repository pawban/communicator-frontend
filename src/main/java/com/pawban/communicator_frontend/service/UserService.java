package com.pawban.communicator_frontend.service;

import com.pawban.communicator_frontend.client.UserClient;
import com.pawban.communicator_frontend.domain.ChatRoom;
import com.pawban.communicator_frontend.domain.Member;
import com.pawban.communicator_frontend.domain.User;
import com.pawban.communicator_frontend.exception.RequestUnsuccessfulException;
import com.pawban.communicator_frontend.session.SessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserClient userClient;

    @Autowired
    public UserService(final UserClient userClient) {
        this.userClient = userClient;
    }

    public List<Member> getVisibleUsers(final UUID sessionId) {
        return userClient.getVisibleUsers(sessionId).stream()
                .map(user -> new Member(user, null))
                .collect(Collectors.toList());
    }

    public SessionData getSessionData(final UUID sessionId) {
        return userClient.getSessionData(sessionId)
                .orElseThrow(() -> new RequestUnsuccessfulException(
                        sessionId,
                        "The session id '" + sessionId + "' is invalid."
                ));
    }

    public Set<ChatRoom> getCurrentUserChatRooms(final UUID sessionId) {
        return userClient.getCurrentUserChatRooms(sessionId);
    }

    public SessionData createUser(final User user) {
        return userClient.createUser(user)
                .orElseThrow(() -> new RequestUnsuccessfulException(
                        "Remote service refused to create user with username '" + user.getUsername() + "'."
                ));
    }

    public User changeCurrentUserVisibility(final UUID sessionId,
                                            final boolean visible) {
        return userClient.changeUserVisibility(sessionId, visible)
                .orElseThrow(() -> new RequestUnsuccessfulException(
                        sessionId,
                        "Unable to change user visibility."
                ));
    }

    public void deleteCurrentUser(final UUID sessionId) {
        if (!userClient.deleteUser(sessionId)) {
            throw new RequestUnsuccessfulException(
                    sessionId,
                    "Current user deletion ended up with no success."
            );
        }
    }

    public boolean isUsernameAvailable(final String username) {
        return userClient.isUsernameAvailable(username)
                .orElseThrow(() -> new RequestUnsuccessfulException(
                        "Unable to determinate if the username '" + username + "' is available."
                ));
    }

}
