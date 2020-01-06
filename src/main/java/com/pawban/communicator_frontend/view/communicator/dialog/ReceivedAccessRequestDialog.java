package com.pawban.communicator_frontend.view.communicator.dialog;

import com.pawban.communicator_frontend.domain.AccessRequest;
import com.pawban.communicator_frontend.view.component.CustomizedDialog;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;

import java.util.UUID;
import java.util.function.BiConsumer;

public class ReceivedAccessRequestDialog extends CustomizedDialog {

    public ReceivedAccessRequestDialog(final AccessRequest accessRequest,
                                       final BiConsumer<UUID, Boolean> clickAction) {
        super("You have received new access request");
        setOkButtonText("Accept");
        setOkButtonThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        setCancelButtonText("Reject");
        setCancelButtonThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        Span usernameSpan = new Span(accessRequest.getSender().getUsername());
        usernameSpan.getStyle().set("font-weight", "bold");
        Span chatRoomSpan = new Span(accessRequest.getChatRoom().getName());
        chatRoomSpan.getStyle().set("font-weight", "bold");
        Label infoLabel = new Label();
        infoLabel.add(
                usernameSpan,
                new Text(" has sent to you request to join to your chat room "),
                chatRoomSpan,
                new Text(" with following explanation:")
        );
        infoLabel.setWidthFull();

        Label requestLabel = new Label(accessRequest.getRequest());
        requestLabel.setWidthFull();
        requestLabel.getStyle().set("font-style", "italic");

        setCancelButtonClickListener(buttonClickEvent -> {
            clickAction.accept(accessRequest.getId(), false);
            close();
        });

        setOkButtonClickListener(buttonClickEvent -> {
            clickAction.accept(accessRequest.getId(), true);
            this.close();
        });

        add(infoLabel, requestLabel);
        setWidth("500px");
        setCloseOnEsc(false);
        open();
    }

}
