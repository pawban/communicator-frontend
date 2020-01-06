package com.pawban.communicator_frontend.view.communicator.dialog;

import com.pawban.communicator_frontend.domain.ChatRoom;
import com.pawban.communicator_frontend.view.component.CustomizedDialog;
import com.vaadin.flow.component.textfield.TextField;

import java.util.UUID;
import java.util.function.BiConsumer;

public class NewAccessRequestDialog extends CustomizedDialog {

    private final TextField senderField = new TextField("Sender");
    private final TextField chatRoomField = new TextField("Chat room");
    private final RequestTextArea request = new RequestTextArea();

    private ChatRoom chatRoom;

    public NewAccessRequestDialog(final BiConsumer<UUID, String> sendAction) {
        super("New access request");
        setOkButtonText("Send");

        senderField.setReadOnly(true);
        senderField.setWidthFull();

        chatRoomField.setReadOnly(true);
        chatRoomField.setWidthFull();

        request.focus();
        request.setTabIndex(1);

        setOkButtonClickListener(buttonClickEvent -> {
            if (request.isValid()) {
                sendAction.accept(chatRoom.getId(), request.getValue());
                close();
            }
        });

        add(senderField, chatRoomField, request);
        setWidth("500px");
    }

    @Override
    public void close() {
        request.clear();
        super.close();
    }

    public void setSenderName(final String name) {
        senderField.setValue(name);
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        chatRoomField.setValue(chatRoom.getName());
    }

}
