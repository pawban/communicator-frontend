package com.pawban.communicator_frontend.view.communicator.component;

import com.pawban.communicator_frontend.domain.ChatRoom;
import com.pawban.communicator_frontend.domain.User;
import com.pawban.communicator_frontend.session.CommunicatorSession;
import com.pawban.communicator_frontend.type.ChatRoomStatus;
import com.pawban.communicator_frontend.type.MembershipRole;
import com.pawban.communicator_frontend.view.communicator.dialog.ChangeChatRoomOwnerDialog;
import com.pawban.communicator_frontend.view.communicator.dialog.NewAccessRequestDialog;
import com.pawban.communicator_frontend.view.component.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ChatRoomsGrid extends Grid<ChatRoom> {

    private final CommunicatorSession session;
    private final NewAccessRequestDialog dialog;
    private final Consumer<ChatRoom> leaveChatRoomAction;
    private final BiConsumer<ChatRoom, User> changeChatRoomOwnerAction;
    private final BiConsumer<ChatRoom, ChatRoomStatus> changeChatRoomStatusAction;
    private final Function<ChatRoom, List<User>> usersSupplier;

    private final List<ChatRoom> chatRooms = new ArrayList<>();

    public ChatRoomsGrid(final CommunicatorSession session,
                         final NewAccessRequestDialog dialog,
                         final Consumer<ChatRoom> leaveChatRoomAction,
                         final BiConsumer<ChatRoom, User> changeChatRoomOwnerAction,
                         final BiConsumer<ChatRoom, ChatRoomStatus> changeChatRoomStatusAction,
                         final Function<ChatRoom, List<User>> usersSupplier) {
        this.session = session;
        this.dialog = dialog;
        this.leaveChatRoomAction = leaveChatRoomAction;
        this.changeChatRoomOwnerAction = changeChatRoomOwnerAction;
        this.changeChatRoomStatusAction = changeChatRoomStatusAction;
        this.usersSupplier = usersSupplier;

        this.dialog.setSenderName(session.getCurrentUser().getUsername());
        addThemeVariants(GridVariant.LUMO_COMPACT);

        setSelectionMode(SelectionMode.NONE);
        addComponentColumn(this::createVisibilityIcon)
                .setFlexGrow(0)
                .setWidth("32px");
        addColumn(chatRoom -> chatRoom.getName() + " (" + chatRoom.getOwner().getUsername() + ")")
                .setFlexGrow(1)
                .setAutoWidth(true);
        addComponentColumn(this::createButton)
                .setFlexGrow(0)
                .setWidth("32px");
    }

    private Icon createVisibilityIcon(final ChatRoom chatRoom) {
        ChatRoomStatus status = chatRoom.getStatus();
        Icon icon = new Icon(status.equals(ChatRoomStatus.PUBLIC) ? VaadinIcon.EYE : VaadinIcon.EYE_SLASH);
        if (session.isCurrentUserOwnerOf(chatRoom)) {
            icon.getElement().setAttribute("title", "Click to toggle status of this chat room.");
            icon.getStyle().set("cursor", "pointer");
            icon.addClickListener(iconClickEvent -> changeChatRoomStatusAction.accept(
                    chatRoom,
                    chatRoom.getStatus().equals(ChatRoomStatus.PUBLIC) ? ChatRoomStatus.PRIVATE : ChatRoomStatus.PUBLIC
            ));
        } else {
            icon.setColor("var(--lumo-disabled-text-color)");
            icon.getElement().setAttribute("title", status.toString());
        }
        icon.setSize("16px");
        return icon;
    }

    private Icon createButton(final ChatRoom chatRoom) {
        Icon icon;
        switch (getCurrentUserRole(chatRoom)) {
            case OUTSIDER:
                icon = VaadinIcon.PLUS_CIRCLE.create();
                icon.getElement().setAttribute("title", "Send access request to the owner of this " +
                        "chat room to join it");
                icon.addClickListener(iconClickEvent -> openDialog(chatRoom));
                break;
            case MEMBER:
                icon = VaadinIcon.MINUS_CIRCLE.create();
                icon.getElement().setAttribute("title", "Leave this chat room");
                icon.addClickListener(iconClickEvent -> openLeaveChatRoomConfirmDialog(chatRoom));
                break;
            case OWNER:
                icon = VaadinIcon.EXCHANGE.create();
                icon.getElement().setAttribute("title", "Pass the ownership of this chat room to " +
                        "another user.");
                icon.addClickListener(iconClickEvent ->
                        new ChangeChatRoomOwnerDialog(chatRoom, usersSupplier, changeChatRoomOwnerAction).open()
                );
                break;
            default:
                icon = VaadinIcon.EXCLAMATION_CIRCLE.create();
        }
        icon.getStyle().set("cursor", "pointer");
        icon.setSize("16px");
        return icon;
    }

    private void openLeaveChatRoomConfirmDialog(final ChatRoom chatRoom) {
        new ConfirmDialog(
                "Leave chat room confirmation",
                "Are you sure you want to leave this chat room? You'll loose access to it. " +
                        "New access request is required to rejoin this chat room.",
                "Leave chat room",
                () -> leaveChatRoomAction.accept(chatRoom)
        ).open();
    }

    private MembershipRole getCurrentUserRole(final ChatRoom chatRoom) {
        if (chatRoom.getOwner().equals(session.getCurrentUser())) {
            return MembershipRole.OWNER;
        }
        return session.getChatRooms().stream().anyMatch(cr -> cr.equals(chatRoom)) ?
                MembershipRole.MEMBER :
                MembershipRole.OUTSIDER;
    }

    private void openDialog(final ChatRoom chatRoom) {
        dialog.setChatRoom(chatRoom);
        dialog.open();
    }

    public void addChatRoom(final ChatRoom chatRoom) {
        chatRooms.add(chatRoom);
        setItems(new ArrayList<>(chatRooms));
    }

    @Override
    public void setItems(Collection<ChatRoom> items) {
        chatRooms.clear();
        chatRooms.addAll(items);
        super.setItems(items);
    }

}
