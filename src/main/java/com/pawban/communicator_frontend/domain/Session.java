package com.pawban.communicator_frontend.domain;

import com.pawban.communicator_frontend.dto.SessionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Session {

    private UUID sessionId;
    private User user;

    public Session(final SessionDto sessionDto) {
        this(
                sessionDto.getSessionId(),
                new User(sessionDto.getUserDto())
        );
    }

}
