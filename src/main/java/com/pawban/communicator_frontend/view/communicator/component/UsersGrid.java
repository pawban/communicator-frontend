package com.pawban.communicator_frontend.view.communicator.component;

import com.pawban.communicator_frontend.domain.ChatRoom;
import com.pawban.communicator_frontend.domain.Member;
import com.pawban.communicator_frontend.domain.User;
import com.pawban.communicator_frontend.type.MembershipRole;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.Objects;
import java.util.function.BiConsumer;

public class UsersGrid extends Grid<Member> {

    private final String BUTTONS_COLUMN_KEY = "buttonsColumn";

    private final Icon emptyIcon = new Icon();
    private final BiConsumer<User, ChatRoom> addMemberAction;
    private final BiConsumer<User, ChatRoom> removeMemberAction;

    private ChatRoom chatRoom;
    private boolean buttonsColumnPresent = false;

    public UsersGrid(final BiConsumer<User, ChatRoom> addMemberAction,
                     final BiConsumer<User, ChatRoom> removeMemberAction) {
        this.addMemberAction = addMemberAction;
        this.removeMemberAction = removeMemberAction;

        emptyIcon.getElement().setAttribute("icon", "empty");
        emptyIcon.setSize("16px");
        setSelectionMode(SelectionMode.NONE);
        addThemeVariants(GridVariant.LUMO_COMPACT);

        addComponentColumn(this::createFlagImage)
                .setFlexGrow(0)
                .setWidth("32px");
        addComponentColumn(this::createUsernameLabel)
                .setFlexGrow(1)
                .setAutoWidth(true);
    }

    private Image createFlagImage(final Member member) {
        return new FlagImage(member.getUser().getCountry().getFlagUrl(), 12);
    }

    private Label createUsernameLabel(final Member member) {
        Label label = new Label(member.getUser().getUsername());
        if (member.getRole() == null || member.getRole().equals(MembershipRole.OUTSIDER)) {
            label.getStyle()
                    .set("color", "var(--lumo-disabled-text-color)")
                    .set("font-style", "italic");
        }
        return label;
    }

    public void addButtonsColumn() {
        if (!buttonsColumnPresent) {
            addComponentColumn(member -> member.getRole() == null ? emptyIcon : createButton(member))
                    .setKey(BUTTONS_COLUMN_KEY)
                    .setFlexGrow(0)
                    .setWidth("32px");
            buttonsColumnPresent = true;
        }
    }

    private Icon createButton(final Member member) {
        switch (member.getRole()) {
            case OUTSIDER:
                Icon iconPlus = VaadinIcon.PLUS_CIRCLE.create();
                iconPlus.getElement().setAttribute("title", "Add this user to chat room members.");
                iconPlus.getStyle().set("cursor", "pointer");
                iconPlus.setSize("16px");
                iconPlus.addClickListener(iconClickEvent -> addMemberAction.accept(
                        member.getUser(),
                        Objects.requireNonNull(chatRoom)
                ));
                return iconPlus;
            case MEMBER:
                Icon iconMinus = VaadinIcon.MINUS_CIRCLE.create();
                iconMinus.getElement().setAttribute("title", "Remove this user from chat room members.");
                iconMinus.getStyle().set("cursor", "pointer");
                iconMinus.setSize("16px");
                iconMinus.addClickListener(iconClickEvent -> removeMemberAction.accept(
                        member.getUser(),
                        Objects.requireNonNull(chatRoom)
                ));
                return iconMinus;
            default:
                return emptyIcon;
        }
    }

    public void removeButtonsColumn() {
        if (buttonsColumnPresent) {
            removeColumnByKey(BUTTONS_COLUMN_KEY);
            buttonsColumnPresent = false;
        }
    }

    public void setChatRoom(final ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

}
