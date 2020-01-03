package com.pawban.communicator_frontend.view.communicator.dialog;

import com.pawban.communicator_frontend.domain.AccessRequest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.UUID;
import java.util.function.BiConsumer;

public class ReceivedAccessRequestDialog extends Dialog {

    public ReceivedAccessRequestDialog(final AccessRequest accessRequest,
                                       final BiConsumer<UUID, Boolean> clickAction) {
        H4 title = new H4("You have received new access request");
        title.getStyle().set("margin", "auto");

        Label infoLabel = new Label("<b>" + accessRequest.getSender().getUsername() +
                "</b> has sent you request to join to your chat room <b>" + accessRequest.getChatRoom().getName() +
                "</b> with following explanation:");
        infoLabel.setWidthFull();

        Label requestLabel = new Label(accessRequest.getRequest());
        requestLabel.setWidthFull();
        requestLabel.getStyle().set("font_style", "italic");

        Button rejectButton = new Button("Reject");
        rejectButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        rejectButton.addClickListener(buttonClickEvent -> {
            clickAction.accept(accessRequest.getId(), false);
            this.close();
        });

        Button acceptButton = new Button("Accept");
        acceptButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        acceptButton.getStyle().set("margin-left", "auto");
        acceptButton.addClickListener(buttonClickEvent -> {
            clickAction.accept(accessRequest.getId(), true);
            this.close();
        });

        HorizontalLayout buttons = new HorizontalLayout(rejectButton, acceptButton);
        buttons.setWidthFull();

        VerticalLayout mainLayout = new VerticalLayout(title, infoLabel, requestLabel, buttons);
        add(mainLayout);

        setWidth("500px");
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        open();
    }

}
