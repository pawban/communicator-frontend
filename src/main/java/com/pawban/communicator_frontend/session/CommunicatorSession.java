package com.pawban.communicator_frontend.session;

import com.pawban.communicator_frontend.domain.Country;
import com.pawban.communicator_frontend.domain.Session;
import com.pawban.communicator_frontend.domain.User;
import com.pawban.communicator_frontend.domain.UserStatus;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@VaadinSessionScope
@Getter
@Setter
public class CommunicatorSession {

    //    private UUID sessionId = null;
    private UUID sessionId = UUID.randomUUID();
    //    private User user = null;
    private User user = new User(
            UUID.randomUUID(),
            "gawelx",
            new Country("Poland", "POL", ""),
            new UserStatus("visible", true));

    public void setSession(final Session session) {
        this.sessionId = session.getSessionId();
        this.user = session.getUser();
    }

}
