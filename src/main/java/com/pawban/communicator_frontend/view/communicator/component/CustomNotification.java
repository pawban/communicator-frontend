package com.pawban.communicator_frontend.view.communicator.component;

import com.vaadin.flow.component.notification.Notification;

public class CustomNotification extends Notification {

    public CustomNotification(String message) {
        super(message, 3000, Position.MIDDLE);
        open();
    }

}
