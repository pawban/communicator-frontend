package com.pawban.communicator_frontend.session;

import com.pawban.communicator_frontend.domain.User;
import com.pawban.communicator_frontend.dto.SessionDto;
import lombok.Getter;

import java.util.UUID;

@Getter
public class SessionData {

    private UUID sessionId;
    private User user;

    public SessionData(final SessionDto sessionDto) {
        this.sessionId = sessionDto.getSessionId();
        this.user = new User(sessionDto.getUserDto());
    }

}
