package com.pawban.communicator_frontend.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AccessRequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED;

    @JsonValue
    public String getValue() {
        return toString().toLowerCase();
    }

}
