package com.pawban.communicator_frontend.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ChatRoomStatus {
    PRIVATE,
    PUBLIC;

    @JsonValue
    public String getValue() {
        return toString().toLowerCase();
    }

}
