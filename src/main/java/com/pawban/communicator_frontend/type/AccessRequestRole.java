package com.pawban.communicator_frontend.type;

public enum AccessRequestRole {
    SENDER,
    CHAT_ROOM_OWNER;

    public String getValue() {
        return toString().toLowerCase();
    }

}
