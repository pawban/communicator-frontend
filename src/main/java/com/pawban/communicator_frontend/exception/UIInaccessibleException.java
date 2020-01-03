package com.pawban.communicator_frontend.exception;

public class UIInaccessibleException extends RuntimeException {

    public UIInaccessibleException() {
        super("Unable to get access to the Vaadin UI object.");
    }

}
