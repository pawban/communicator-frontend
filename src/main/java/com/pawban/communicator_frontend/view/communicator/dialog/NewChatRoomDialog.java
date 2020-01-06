package com.pawban.communicator_frontend.view.communicator.dialog;

import com.pawban.communicator_frontend.type.ChatRoomStatus;
import com.pawban.communicator_frontend.view.component.CustomizedDialog;
import com.vaadin.flow.component.combobox.ComboBox;
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

    public NewChatRoomDialog(BiConsumer<String, ChatRoomStatus> createAction) {
        super("New chat room");
        setOkButtonText("Create");

        TextField nameField = new TextField("Chat room name");
        nameField.setPlaceholder("Enter new chat room name...");
        nameField.setRequired(true);
        nameField.setWidthFull();
        nameField.focus();
        nameField.setTabIndex(1);

        ComboBox<ChatRoomStatus> statusComboBox = new ComboBox<>("Chat room status:");
        statusComboBox.setPlaceholder("Choose new chat room status...");
        statusComboBox.setRequired(true);
        statusComboBox.setAllowCustomValue(false);
        statusComboBox.setItemLabelGenerator(ChatRoomStatus::getValue);
        statusComboBox.setWidthFull();
        statusComboBox.setItems(ChatRoomStatus.values());
        statusComboBox.setTabIndex(2);

        add(nameField, statusComboBox);

        setWidth("350px");

        Binder<NewChatRoomDialog> binder = new Binder<>(NewChatRoomDialog.class);
        binder.setBean(this);
        binder.forField(nameField)
                .asRequired("Chat room name can't be empty.")
                .bind("newChatRoomName");
        binder.forField(statusComboBox)
                .asRequired("You have to choose chat room status.")
                .bind("newChatRoomStatus");

        setOkButtonClickListener(buttonClickEvent -> {
            if (binder.validate().isOk()) {
                createAction.accept(newChatRoomName, newChatRoomStatus);
                close();
            }
        });

        open();
    }

}
