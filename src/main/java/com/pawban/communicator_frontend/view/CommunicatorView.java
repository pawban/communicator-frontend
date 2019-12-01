package com.pawban.communicator_frontend.view;

import com.pawban.communicator_frontend.session.CommunicatorSession;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;

public class CommunicatorView extends HorizontalLayout {

    private final CommunicatorSession session;

    public CommunicatorView(@Autowired final CommunicatorSession session) {
        this.session = session;
    }

}
