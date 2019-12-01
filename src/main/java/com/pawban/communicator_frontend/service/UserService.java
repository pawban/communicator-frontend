package com.pawban.communicator_frontend.service;

import com.pawban.communicator_frontend.client.UserClient;
import com.pawban.communicator_frontend.domain.Session;
import com.pawban.communicator_frontend.domain.User;
import com.pawban.communicator_frontend.domain.UserStatus;
import com.pawban.communicator_frontend.exception.UnableToCreateUserException;
import com.pawban.communicator_frontend.exception.UsernameAvailabilityUnknownException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserClient userClient;

    @Autowired
    public UserService(final UserClient userClient) {
        this.userClient = userClient;
    }

    public List<UserStatus> getUserStatuses() {
        List<UserStatus> statuses = userClient.getUserStatuses();
        statuses.forEach(s -> s.setStatus(s.getStatus().toLowerCase()));
        return statuses;
    }

    public Session createUser(final User user) {
        return userClient.createUser(user).orElseThrow(
                () -> new UnableToCreateUserException(
                        "Remote service refused to create user with username '" + user.getUsername() + "'"));
    }

    public boolean isUsernameAvailable(final String username) {
        return userClient.isUsernameAvailable(username).orElseThrow(
                () -> new UsernameAvailabilityUnknownException(
                        "Unable to determinate if the username '" + username + "' is available"));
    }

}
