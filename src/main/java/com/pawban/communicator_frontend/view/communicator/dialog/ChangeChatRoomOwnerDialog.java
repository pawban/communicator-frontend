package com.pawban.communicator_frontend.view.communicator.dialog;

import com.pawban.communicator_frontend.domain.ChatRoom;
import com.pawban.communicator_frontend.domain.User;
import com.pawban.communicator_frontend.view.component.CustomizedDialog;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Getter
@Setter
public class ChangeChatRoomOwnerDialog extends CustomizedDialog {

    private User chosenUser;

    public ChangeChatRoomOwnerDialog(final ChatRoom chatRoom,
                                     final Function<ChatRoom, List<User>> usersSupplier,
                                     final BiConsumer<ChatRoom, User> changeOwnerAction) {
        super("Change owner of the chat room");
        setOkButtonText("Pass ownership");

        TextField chatRoomNameField = new TextField("Chat room name");
        chatRoomNameField.setReadOnly(true);
        chatRoomNameField.setWidthFull();
        chatRoomNameField.setValue(chatRoom.getName());

        ComboBox<User> userComboBox = new ComboBox<>("New chat room owner");
        userComboBox.setWidthFull();
        userComboBox.setItemLabelGenerator(User::getUsername);
        userComboBox.setRequired(true);
        userComboBox.setItems(usersSupplier.apply(chatRoom));
        userComboBox.focus();
        userComboBox.setTabIndex(1);

        add(chatRoomNameField, userComboBox);

        Binder<ChangeChatRoomOwnerDialog> binder = new Binder<>(ChangeChatRoomOwnerDialog.class);
        binder.setBean(this);
        binder.forField(userComboBox)
                .asRequired("You have to choose new owner of chat room.")
                .bind("chosenUser");

        setOkButtonClickListener(buttonClickEvent -> {
            if (binder.validate().isOk()) {
                changeOwnerAction.accept(chatRoom, chosenUser);
                close();
            }
        });
    }

}
