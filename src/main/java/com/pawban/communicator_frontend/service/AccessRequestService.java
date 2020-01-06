package com.pawban.communicator_frontend.service;

import com.pawban.communicator_frontend.client.AccessRequestClient;
import com.pawban.communicator_frontend.domain.AccessRequest;
import com.pawban.communicator_frontend.domain.ChatRoom;
import com.pawban.communicator_frontend.exception.RequestUnsuccessfulException;
import com.pawban.communicator_frontend.type.AccessRequestRole;
import com.pawban.communicator_frontend.type.AccessRequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class AccessRequestService {

    private final AccessRequestClient accessRequestClient;

    @Autowired
    public AccessRequestService(final AccessRequestClient accessRequestClient) {
        this.accessRequestClient = accessRequestClient;
    }

    public Set<AccessRequest> getProcessedAccessRequestOfSender(final UUID sessionId) {
        return accessRequestClient.getAccessRequests(sessionId, AccessRequestRole.SENDER);
    }

    public Set<AccessRequest> getPendingAccessRequestsOfChatRoomOwner(final UUID sessionId) {
        return accessRequestClient.getAccessRequests(sessionId, AccessRequestRole.CHAT_ROOM_OWNER);
    }

    public AccessRequest createAccessRequest(final UUID sessionId,
                                             final ChatRoom chatRoom,
                                             final String request) {
        return accessRequestClient.createAccessRequest(sessionId, chatRoom.getId(), request)
                .orElseThrow(() -> new RequestUnsuccessfulException(
                        sessionId,
                        "Unable to create new access request to join chat room with name '" +
                                chatRoom.getName() + "'."
                ));
    }

    public void acceptAccessRequest(final UUID sessionId,
                                    final AccessRequest accessRequest) {
        if (!accessRequestClient.changeAccessRequestStatus(sessionId, accessRequest.getId(), AccessRequestStatus.ACCEPTED)) {
            throw new RequestUnsuccessfulException(
                    sessionId,
                    "Unable to accept access request with id = '" + accessRequest.getId().toString() + "'.");
        }
    }

    public void rejectAccessRequest(final UUID sessionId,
                                    final AccessRequest accessRequest) {
        if (!accessRequestClient.changeAccessRequestStatus(sessionId, accessRequest.getId(), AccessRequestStatus.REJECTED)) {
            throw new RequestUnsuccessfulException(
                    sessionId,
                    "Unable to reject access request with id = '" + accessRequest.getId().toString() + "'.");
        }
    }

}
