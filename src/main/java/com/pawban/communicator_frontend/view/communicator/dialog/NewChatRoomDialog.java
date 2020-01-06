package com.pawban.communicator_frontend.view.communicator.dialog;

import com.pawban.communicator_frontend.type.ChatRoomStatus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BiConsumer;

@Getter
@Setter
public class NewChatRoomDialog extends CustomizedDialog {

    private String newChatRoomName;
    private ChatRoomStatus newChatRoomStatus;

    public NewChatRoomDialog(BiConsumer<String, ChatRoomStatus> clickAction) {
        H4 title = new H4("New chat room");
        title.getStyle().set("margin", "auto");

        TextField nameField = new TextField("Chat room name");
        nameField.setPlaceholder("Enter new chat room name...");
        nameField.setRequired(true);
        nameField.setWidthFull();

        ComboBox<ChatRoomStatus> statusComboBox = new ComboBox<>("Chat room status:");
        statusComboBox.setPlaceholder("Choose new chat room status...");
        statusComboBox.setRequired(true);
        statusComboBox.setAllowCustomValue(false);
        statusComboBox.setItemLabelGenerator(ChatRoomStatus::getValue);
        statusComboBox.setWidthFull();
        statusComboBox.setItems(ChatRoomStatus.values());

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyle().set("margin-left", "auto");
        cancelButton.addClickListener(buttonClickEvent -> this.close());

        Button createButton = new Button("Create");
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttons = new HorizontalLayout(cancelButton, createButton);
        buttons.setWidthFull();
//        buttons.setPadding(false);

        VerticalLayout mainLayout = new VerticalLayout(title, nameField, statusComboBox, buttons);

        add(mainLayout);
        setWidth("350px");

        Binder<NewChatRoomDialog> binder = new Binder<>(NewChatRoomDialog.class);
        binder.setBean(this);
        binder.forField(nameField)
                .asRequired("Chat room name can't be empty.")
                .bind("newChatRoomName");
        binder.forField(statusComboBox)
                .asRequired("You have to choose chat room status.")
                .bind("newChatRoomStatus");

        createButton.addClickListener(buttonClickEvent -> {
            if (binder.validate().isOk()) {
                clickAction.accept(newChatRoomName, newChatRoomStatus);
                close();
            }
        });

        open();
    }

}
