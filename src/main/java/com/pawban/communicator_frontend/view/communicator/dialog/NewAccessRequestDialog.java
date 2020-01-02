package com.pawban.communicator_frontend.view.communicator.dialog;

import com.pawban.communicator_frontend.domain.ChatRoom;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.UUID;
import java.util.function.BiConsumer;

public class NewAccessRequestDialog extends Dialog {

    private final TextField senderField = new TextField("Sender");
    private final TextField chatRoomField = new TextField("Chat room");
    private final RequestTextArea request = new RequestTextArea();

    private ChatRoom chatRoom;

    public NewAccessRequestDialog(final BiConsumer<UUID, String> clickAction) {
        H4 title = new H4("New access request");
        title.getStyle().set("margin", "auto");

        senderField.setReadOnly(true);
        senderField.setWidthFull();

        chatRoomField.setReadOnly(true);
        chatRoomField.setWidthFull();

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyle().set("margin-left", "auto");
        cancelButton.addClickListener(buttonClickEvent -> this.close());

        Button sendButton = new Button("Send");
        sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        sendButton.addClickListener(buttonClickEvent -> {
            if (request.isValid()) {
                clickAction.accept(chatRoom.getId(), request.getValue());
                this.close();
            }
        });

        HorizontalLayout buttons = new HorizontalLayout(cancelButton, sendButton);

        VerticalLayout mainLayout = new VerticalLayout(title, senderField, chatRoomField, request, buttons);

        add(mainLayout);
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
