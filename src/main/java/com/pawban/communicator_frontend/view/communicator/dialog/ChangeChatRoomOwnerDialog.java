package com.pawban.communicator_frontend.view.communicator.dialog;

import com.pawban.communicator_frontend.domain.ChatRoom;
import com.pawban.communicator_frontend.domain.User;
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
        H4 title = new H4("Change chat room owner");
        title.getStyle().set("margin", "auto");

        TextField chatRoomNameField = new TextField("Chat room name");
        chatRoomNameField.setReadOnly(true);
        chatRoomNameField.setWidthFull();
        chatRoomNameField.setValue(chatRoom.getName());

        ComboBox<User> userComboBox = new ComboBox<>("New chat room owner");
        userComboBox.setWidthFull();
        userComboBox.setItemLabelGenerator(User::getUsername);
        userComboBox.setRequired(true);
        userComboBox.setItems(usersSupplier.apply(chatRoom));

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(buttonClickEvent -> this.close());

        Button sendButton = new Button("Send");
        sendButton.getStyle().set("margin-left", "auto");
        sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttons = new HorizontalLayout(cancelButton, sendButton);
        buttons.setWidthFull();

        VerticalLayout mainLayout = new VerticalLayout(title, chatRoomNameField, userComboBox, buttons);
        add(mainLayout);

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

    public User getChosenUser() {
        return chosenUser;
    }

    public void setChosenUser(User chosenUser) {
        this.chosenUser = chosenUser;
    }

}
