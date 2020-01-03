package com.pawban.communicator_frontend.view.communicator;

import com.pawban.communicator_frontend.domain.AccessRequest;
import com.pawban.communicator_frontend.domain.ChatRoom;
import com.pawban.communicator_frontend.domain.Member;
import com.pawban.communicator_frontend.domain.Message;
import com.pawban.communicator_frontend.domain.User;
import com.pawban.communicator_frontend.exception.RequestUnsuccessfulException;
import com.pawban.communicator_frontend.exception.UIInaccessibleException;
import com.pawban.communicator_frontend.service.AccessRequestService;
import com.pawban.communicator_frontend.service.ChatRoomService;
import com.pawban.communicator_frontend.service.MessageService;
import com.pawban.communicator_frontend.service.UserService;
import com.pawban.communicator_frontend.session.CommunicatorSession;
import com.pawban.communicator_frontend.type.ChatRoomStatus;
import com.pawban.communicator_frontend.type.MembershipRole;
import com.pawban.communicator_frontend.view.MainLayout;
import com.pawban.communicator_frontend.view.communicator.component.ChatRoomMessagesDiv;
import com.pawban.communicator_frontend.view.communicator.component.ChatRoomMessagesPanel;
import com.pawban.communicator_frontend.view.communicator.component.ChatRoomTab;
import com.pawban.communicator_frontend.view.communicator.component.ChatRoomsGrid;
import com.pawban.communicator_frontend.view.communicator.component.ChatRoomsPanel;
import com.pawban.communicator_frontend.view.communicator.component.ChatRoomsTabs;
import com.pawban.communicator_frontend.view.communicator.component.ChatRoomsTabsLayout;
import com.pawban.communicator_frontend.view.communicator.component.CreateNewChatRoomIcon;
import com.pawban.communicator_frontend.view.communicator.component.DeleteChatRoomIcon;
import com.pawban.communicator_frontend.view.communicator.component.DeleteUserIcon;
import com.pawban.communicator_frontend.view.communicator.component.EnterMessagePanel;
import com.pawban.communicator_frontend.view.communicator.component.FlagImage;
import com.pawban.communicator_frontend.view.communicator.component.HeaderLayout;
import com.pawban.communicator_frontend.view.communicator.component.LeaveChatRoomIcon;
import com.pawban.communicator_frontend.view.communicator.component.LeftPanel;
import com.pawban.communicator_frontend.view.communicator.component.MessageComponent;
import com.pawban.communicator_frontend.view.communicator.component.MessageInput;
import com.pawban.communicator_frontend.view.communicator.component.RightPanel;
import com.pawban.communicator_frontend.view.communicator.component.SendMessageButton;
import com.pawban.communicator_frontend.view.communicator.component.UsersGrid;
import com.pawban.communicator_frontend.view.communicator.component.UsersPanel;
import com.pawban.communicator_frontend.view.communicator.component.VisibilityIcon;
import com.pawban.communicator_frontend.view.communicator.dialog.ChatRoomCloseConfirmDialog;
import com.pawban.communicator_frontend.view.communicator.dialog.ChatRoomDeleteConfirmDialog;
import com.pawban.communicator_frontend.view.communicator.dialog.CustomNotification;
import com.pawban.communicator_frontend.view.communicator.dialog.ErrorDialog;
import com.pawban.communicator_frontend.view.communicator.dialog.NewAccessRequestDialog;
import com.pawban.communicator_frontend.view.communicator.dialog.NewChatRoomDialog;
import com.pawban.communicator_frontend.view.communicator.dialog.ReceivedAccessRequestDialog;
import com.pawban.communicator_frontend.view.communicator.dialog.UserDeleteConfirmDialog;
import com.pawban.communicator_frontend.view.newuser.NewUserView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouteConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class CommunicatorView extends HorizontalLayout {

    private final TaskScheduler taskScheduler;
    private final Set<ScheduledFuture> scheduledFutures = new HashSet<>();

    private final CommunicatorSession session;
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final AccessRequestService accessRequestService;
    private final MessageService messageService;

    private final ChatRoomsGrid chatRoomsGrid;
    private final ChatRoomsTabs chatRoomsTabs = new ChatRoomsTabs();
    private final UsersGrid usersGrid = new UsersGrid(this::addMemberToChatRoom, this::removeMemberFromChatRoom);
    private final ChatRoomMessagesPanel chatRoomMessagesPanel = new ChatRoomMessagesPanel();
    private final VisibilityIcon visibilityIcon;
    private final DeleteUserIcon deleteUserIcon = new DeleteUserIcon();
    private final ChatRoomsTabsLayout tabsLayout = new ChatRoomsTabsLayout();
    private final DeleteChatRoomIcon deleteChatRoomIcon = new DeleteChatRoomIcon();
    private final LeaveChatRoomIcon leaveChatRoomIcon = new LeaveChatRoomIcon();
    private final CreateNewChatRoomIcon createNewChatRoomIcon = new CreateNewChatRoomIcon();
    private final MessageInput messageInput = new MessageInput();
    private final SendMessageButton sendMessageButton = new SendMessageButton();

    private final AtomicReference<ChatRoomMessagesDiv> currentChatRoom = new AtomicReference<>();
    private final Map<ChatRoomTab, ChatRoomMessagesDiv> tabsToChatRooms = new HashMap<>();

    public CommunicatorView(@Autowired final CommunicatorSession session,
                            @Autowired final UserService userService,
                            @Autowired final ChatRoomService chatRoomService,
                            @Autowired final AccessRequestService accessRequestService,
                            @Autowired final MessageService messageService,
                            @Autowired final TaskScheduler taskScheduler) {
        this.session = session;
        this.userService = userService;
        this.chatRoomService = chatRoomService;
        this.accessRequestService = accessRequestService;
        this.messageService = messageService;
        this.taskScheduler = taskScheduler;

        chatRoomsGrid = new ChatRoomsGrid(
                this.session,
                new NewAccessRequestDialog(this::sendAccessRequest),
                this::removeCurrentUserFromChatRoom,
                this::changeOwnerOfChatRoom,
                this::changeStatusOfTheChatRoom,
                this::getMembersOfChatRoomExcludingCurrentUser
        );
        visibilityIcon = new VisibilityIcon(session.getUser().getVisible());
        refreshChatRooms();
        refreshUsers();
        deleteUserIcon.addClickListener(iconClickEvent -> new UserDeleteConfirmDialog(this::deleteCurrentUser));
        leaveChatRoomIcon.addClickListener(iconClickEvent -> new ChatRoomCloseConfirmDialog(
                () -> removeCurrentUserFromChatRoom(chatRoomsTabs.getSelectedTab().getChatRoom())
        ));
        deleteChatRoomIcon.addClickListener(iconClickEvent -> new ChatRoomDeleteConfirmDialog(
                () -> deleteChatRoom(chatRoomsTabs.getSelectedTab().getChatRoom())
        ));
        createNewChatRoomIcon.addClickListener(iconClickEvent -> new NewChatRoomDialog(this::createNewChatRoom));
        messageInput.addKeyDownListener(Key.ENTER, keyPressEvent -> sendMessageButton.click());
        sendMessageButton.addClickListener(buttonClickEvent -> sendMessage());

        session.getChatRooms().forEach(this::addChatRoomToView);

        buildView();

        registerSchedulerTasks();
    }

    private void registerSchedulerTasks() {
        scheduledFutures.add(taskScheduler.scheduleAtFixedRate(
                this::refreshView,
                Date.from(Instant.now().plusMillis(5000L)),
                5000L
        ));
        scheduledFutures.add(taskScheduler.scheduleAtFixedRate(
                this::refreshMessages,
                Date.from(Instant.now().plusMillis(2500L)),
                2500L
        ));
        scheduledFutures.add(taskScheduler.scheduleAtFixedRate(
                this::checkPendingAccessRequests,
                Date.from(Instant.now().plusMillis(30000L)),
                30000L
        ));
    }

    private void unregisterSchedulerTasks() {
        scheduledFutures.forEach(task -> task.cancel(true));
        scheduledFutures.clear();
    }

    private void refreshView() {
        UI ui = getUI().orElseThrow(UIInaccessibleException::new);
        ui.access(() -> {
            refreshSessionData();
            refreshChatRooms();
            refreshUsers();
        });
    }

    private void refreshMessages() {
        ChatRoomTab tab = chatRoomsTabs.getSelectedTab();
        if (tab != null) {
            List<Message> newMessages = messageService.getNewMessagesOfChatRoom(
                    session.getSessionId(),
                    tab.getChatRoom()
            );
            if (!newMessages.isEmpty()) {
                UI ui = getUI().orElseThrow(UIInaccessibleException::new);
                ui.access(() -> tabsToChatRooms.get(tab).add(newMessages.stream()
                        .map(MessageComponent::new)
                        .collect(Collectors.toList())
                ));
            }
        }
    }

    private void processAccessRequest(final UUID accessRequestId,
                                      @NotNull final Boolean accepted) {
        try {
            if (accepted) {
                accessRequestService.acceptAccessRequest(session.getSessionId(), accessRequestId);
            } else {
                accessRequestService.rejectAccessRequest(session.getSessionId(), accessRequestId);
            }
        } catch (RequestUnsuccessfulException e) {
            new ErrorDialog("Processing of access request has failed.");
        }
    }

    private void checkPendingAccessRequests() {
        try {
            UI ui = getUI().orElseThrow(UIInaccessibleException::new);
            ui.access(() -> {
                Set<AccessRequest> accessRequests =
                        accessRequestService.getPendingAccessRequestsOfChatRoomOwner(session.getSessionId());
                accessRequests.forEach(accessRequest -> new ReceivedAccessRequestDialog(
                        accessRequest,
                        this::processAccessRequest
                ));
            });
        } catch (RequestUnsuccessfulException e) {
        }
    }

    private void refreshSessionData() {
        this.session.setSession(userService.getSessionData(session.getSessionId()));
        this.session.setChatRooms(userService.getCurrentUserChatRooms(session.getSessionId()));
    }

    private void refreshChatRooms() {
        List<ChatRoom> availableChatRooms = chatRoomService.getAvailableChatRooms(session.getSessionId());
        chatRoomsGrid.setItems(availableChatRooms);
        Set<UUID> chatRoomsIds = availableChatRooms.stream()
                .map(ChatRoom::getId)
                .collect(Collectors.toSet());
        chatRoomsTabs.getDisplayedChatRooms().forEach(chatRoom -> {
            if (!chatRoomsIds.contains(chatRoom.getId())) {
                removeChatRoomFromView(chatRoom);
            }
        });
        session.getChatRooms().stream()
                .filter(chatRoomsTabs::isNotDisplayed)
                .forEach(this::addChatRoomToView);
    }

    private void removeChatRoomFromView(ChatRoom chatRoom) {
        session.getChatRooms().removeIf(cr -> cr.getId().equals(chatRoom.getId()));
        chatRoomsGrid.setItems(chatRoomService.getAvailableChatRooms(session.getSessionId()));
        if (chatRoomsTabs.getSelectedTab() != null &&
                chatRoomsTabs.getSelectedTab().getChatRoomId().equals(chatRoom.getId())) {
            chatRoomsTabs.setSelectedTab(null);
            currentChatRoom.set(null);
            if (session.isCurrentUserOwnerOf(chatRoom)) {
                tabsLayout.remove(deleteChatRoomIcon);
            } else {
                tabsLayout.remove(leaveChatRoomIcon);
            }
            refreshUsers();
        }
        ChatRoomTab tab = chatRoomsTabs.removeTab(chatRoom.getId());
        ChatRoomMessagesDiv messagesDiv = tabsToChatRooms.remove(tab);
        chatRoomMessagesPanel.remove(messagesDiv);
    }

    private void addChatRoomToView(final ChatRoom chatRoom) {
        if (!exists(chatRoom)) {
            ChatRoomTab tab = new ChatRoomTab(chatRoom);
            chatRoomsTabs.add(tab);
            ChatRoomMessagesDiv messagesDiv = new ChatRoomMessagesDiv(chatRoom.getId());
            chatRoomMessagesPanel.add(messagesDiv);
            tabsToChatRooms.put(tab, messagesDiv);
        }
    }

    // helper methods
    private boolean exists(final ChatRoom chatRoom) {
        return tabsToChatRooms.keySet().stream()
                .anyMatch(tab -> tab.getChatRoomId().equals(chatRoom.getId()));
    }

    private void sendMessage() {
        if (messageService.sendNewMessage(
                session.getSessionId(),
                messageInput.getValue(),
                chatRoomsTabs.getSelectedTab().getChatRoomId()
        )) {
            messageInput.clear();
        } else {
            new ErrorDialog("Sending new message has failed");
        }
    }

    private void changeStatusOfTheChatRoom(final ChatRoom chatRoom,
                                           final ChatRoomStatus newStatus) {
        try {
            chatRoomService.changeChatRoomStatus(session.getSessionId(), chatRoom.getId(), newStatus);
            refreshChatRooms();
        } catch (RequestUnsuccessfulException e) {
            new ErrorDialog("Changing chat room status has failed.");
        }
    }

    private List<User> getMembersOfChatRoomExcludingCurrentUser(final ChatRoom chatRoom) {
        try {
            List<Member> members = chatRoomService.getChatRoomMembers(session.getSessionId(), chatRoom.getId());
            return members.stream()
                    .filter(member -> !member.getRole().equals(MembershipRole.OWNER))
                    .map(Member::getUser)
                    .collect(Collectors.toList());
        } catch (RequestUnsuccessfulException e) {
            new ErrorDialog("Retrieving members of the chat room has failed.");
            return Collections.emptyList();
        }
    }

    private void changeOwnerOfChatRoom(final ChatRoom chatRoom,
                                       final User newOwner) {
        try {
            chatRoomService.changeChatRoomOwner(session.getSessionId(), chatRoom.getId(), newOwner.getId());
            session.getChatRoom(chatRoom.getId()).ifPresent(cr -> cr.setOwner(newOwner));
            refreshChatRooms();
            refreshUsers();
        } catch (RequestUnsuccessfulException e) {
            new ErrorDialog("Changing owner of the chat room has failed.");
        }
    }

    private void sendAccessRequest(final UUID chatRoomId,
                                   final String request) {
        try {
            accessRequestService.createAccessRequest(session.getSessionId(), chatRoomId, request);
            new CustomNotification("Access request has been sent.");
        } catch (RequestUnsuccessfulException e) {
            new ErrorDialog("Sending access request has failed.");
        }
    }

    private void deleteChatRoom(final ChatRoom chatRoom) {
        try {
            chatRoomService.deleteChatRoom(session.getSessionId(), chatRoom.getId());
            removeChatRoomFromView(chatRoom);
            chatRoomsGrid.setItems(chatRoomService.getAvailableChatRooms(session.getSessionId()));
            new CustomNotification("Chat room has been deleted.");
        } catch (RequestUnsuccessfulException e) {
            new ErrorDialog("Deletion of current chat room has failed.");
        }
    }

    private void createNewChatRoom(final String name,
                                   final ChatRoomStatus status) {
        try {
            ChatRoom createdChatRoom = chatRoomService.createChatRoom(session.getSessionId(), name, status);
            session.getChatRooms().add(createdChatRoom);
            chatRoomsGrid.addChatRoom(createdChatRoom);
            addChatRoomToView(createdChatRoom);
            new CustomNotification("New chat room has been created.");
        } catch (RequestUnsuccessfulException e) {
            new ErrorDialog("Creating new chat room has failed.");
        }
    }

    private void removeCurrentUserFromChatRoom(final ChatRoom chatRoom) {
        removeChatRoomFromView(chatRoom);
        removeMemberFromChatRoom(session.getUser().getId(), chatRoom);
        chatRoomsGrid.setItems(chatRoomService.getAvailableChatRooms(session.getSessionId()));
    }

    private void removeMemberFromChatRoom(final UUID memberId,
                                          @NotNull final ChatRoom chatRoom) {
        try {
            chatRoomService.removeMemberFromChatRoom(session.getSessionId(), chatRoom.getId(), memberId);
            refreshSessionData();
            refreshUsers();
            new CustomNotification("User has been removed from the chat room.");
        } catch (RequestUnsuccessfulException e) {
            new ErrorDialog("Removing member from chat room has failed.");
        }
    }

    private void addMemberToChatRoom(final UUID memberId,
                                     @NotNull final ChatRoom chatRoom) {
        try {
            chatRoomService.addMemberToChatRoom(session.getSessionId(), chatRoom.getId(), memberId);
            refreshUsers();
            new CustomNotification("New member has been added to the chat room.");
        } catch (RequestUnsuccessfulException e) {
            new ErrorDialog("Adding new member to chat room has failed.");
        }
    }

    private void refreshUsers() {
        List<Member> members = new ArrayList<>();
        ChatRoomTab selectedTab = chatRoomsTabs.getSelectedTab();
        if (selectedTab == null) {
            members.addAll(userService.getVisibleUsers(this.session.getSessionId()));
            usersGrid.setChatRoom(null);
            usersGrid.removeButtonsColumn();
        } else {
            members.addAll(chatRoomService.getChatRoomMembersWithPotentialMembers(
                    session.getSessionId(),
                    selectedTab.getChatRoomId()
            ));
            usersGrid.setChatRoom(selectedTab.getChatRoom());
            if (selectedTab.getChatRoom().getOwner().getId().equals(session.getUser().getId())) {
                usersGrid.addButtonsColumn();
            } else {
                usersGrid.removeButtonsColumn();
            }
        }
        usersGrid.setItems(members);
    }

    private void checkProcessedAccessRequests() {
        try {
            UI ui = getUI().orElseThrow(UIInaccessibleException::new);
            ui.access(() -> {
                Set<AccessRequest> accessRequests =
                        accessRequestService.getProcessedAccessRequestOfSender(session.getSessionId());
                accessRequests.forEach(ProcessedAccessRequestNotification::new);
            });
        } catch (RequestUnsuccessfulException e) {
        }
    }

    private void deleteCurrentUser() {
        try {
            userService.deleteCurrentUser(session.getSessionId());
            unregisterSchedulerTasks();
            RouteConfiguration configuration = RouteConfiguration.forSessionScope();
            configuration.removeRoute("");
            configuration.setRoute("", NewUserView.class, MainLayout.class);
            UI.getCurrent().getPage().reload();
        } catch (RequestUnsuccessfulException e) {
            new ErrorDialog("Deletion of current user has failed.");
        }
    }

    // building the view
    private void buildView() {
        setSizeFull();

        LeftPanel leftPanel = buildLeftPanel();
        add(leftPanel, buildRightPanel());
        expand(leftPanel);

        chatRoomsTabs.addSelectedChangeListener(event -> changeTab());
        visibilityIcon.addClickListener(iconClickEvent -> changeCurrentUserVisibility());
    }

    private LeftPanel buildLeftPanel() {
        LeftPanel leftPanel = new LeftPanel(buildHeader(session.getUser()),
                buildTabsLayout(),
                chatRoomMessagesPanel,
                buildEnterMessagePanel()
        );
        leftPanel.expand(chatRoomMessagesPanel);
        return leftPanel;
    }

    private RightPanel buildRightPanel() {
        RightPanel rightPanel = new RightPanel();
        rightPanel.addToPrimary(buildUsersPanel());
        rightPanel.addToSecondary(buildChatRoomsPanel());
        return rightPanel;
    }

    private void changeTab() {
        if (currentChatRoom.get() != null) {
            currentChatRoom.get().setVisible(false);
            tabsLayout.remove(tabsLayout.getComponentAt(1));
        }
        if (chatRoomsTabs.getSelectedTab() != null) {
            currentChatRoom.set(tabsToChatRooms.get(chatRoomsTabs.getSelectedTab()));
            currentChatRoom.get().setVisible(true);
            sendMessageButton.enable();
            messageInput.enable();
            if (session.isCurrentUserOwnerOf(chatRoomsTabs.getSelectedTab().getChatRoom())) {
                tabsLayout.add(deleteChatRoomIcon);
            } else {
                tabsLayout.add(leaveChatRoomIcon);
            }
            refreshUsers();
            refreshMessages();
        } else {
            sendMessageButton.disable();
            messageInput.disable();
        }
    }

    private void changeCurrentUserVisibility() {
        try {
            session.setUser(userService.changeCurrentUserVisibility(
                    session.getSessionId(),
                    !session.getUser().getVisible()
            ));
            visibilityIcon.toggleVisibility();
        } catch (RequestUnsuccessfulException e) {
            new ErrorDialog("Visibility change has failed.");
        }
    }

    private HeaderLayout buildHeader(final User user) {
        HeaderLayout header = new HeaderLayout();
        Span placeholder = new Span(" ");
        header.add(
                new FlagImage(user.getCountry().getFlagUrl(), 32),
                new Label(user.getUsername()),
                visibilityIcon,
                placeholder,
                deleteUserIcon
        );
        header.expand(placeholder);
        return header;
    }

    private ChatRoomsTabsLayout buildTabsLayout() {
        tabsLayout.add(chatRoomsTabs);
        tabsLayout.expand(chatRoomsTabs);
        return tabsLayout;
    }

    private EnterMessagePanel buildEnterMessagePanel() {
        EnterMessagePanel enterMessagePanel = new EnterMessagePanel();
        enterMessagePanel.add(messageInput, sendMessageButton);
        enterMessagePanel.expand(messageInput);
        return enterMessagePanel;
    }

    private UsersPanel buildUsersPanel() {
        UsersPanel usersPanel = new UsersPanel();
        usersPanel.add(usersGrid);
        usersPanel.expand(usersGrid);
        return usersPanel;
    }

    private ChatRoomsPanel buildChatRoomsPanel() {
        ChatRoomsPanel chatRoomsPanel = new ChatRoomsPanel(createNewChatRoomIcon);
        chatRoomsPanel.add(chatRoomsGrid);
        chatRoomsPanel.expand(chatRoomsGrid);
        return chatRoomsPanel;
    }

}
