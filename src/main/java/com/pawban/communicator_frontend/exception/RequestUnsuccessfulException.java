package com.pawban.communicator_frontend.exception;

import java.util.UUID;

public class RequestUnsuccessfulException extends RuntimeException {

    public RequestUnsuccessfulException(UUID sessionId, String message) {
        super("SESSION_ID: " + sessionId.toString() + "; " + message);
    }

    public RequestUnsuccessfulException(String message) {
        super(message);
    }

}
