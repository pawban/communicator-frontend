package com.pawban.communicator_frontend.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BackendConfig {

    public static final String AUTH_HEADER = "X-Auth-Key";

    @Getter(AccessLevel.PRIVATE)
    private final String rest = "rest";
    @Getter(AccessLevel.PRIVATE)
    private final String rpc = "rpc";
    @Getter(AccessLevel.PRIVATE)
    @Value("${backend.address}")
    private String address;
    @Getter(AccessLevel.PRIVATE)
    @Value("${backend.version}")
    private String version;
    @Getter(lazy = true)
    private final String countriesEndpoint = getAddress() + "/" + getVersion() + "/" + getRest() + "/countries";
    @Getter(lazy = true)
    private final String usersEndpoint = getAddress() + "/" + getVersion() + "/" + getRest() + "/users";
    @Getter(lazy = true)
    private final String usernameValidationEndpoint = getAddress() + "/" + getVersion() + "/" + getRpc() +
            "/isUsernameAvailable";
    @Getter(lazy = true)
    private final String chatRoomsEndpoint = getAddress() + "/" + getVersion() + "/" + getRest() + "/chatRooms";
    @Getter(lazy = true)
    private final String accessRequestsEndpoint = getAddress() + "/" + getVersion() + "/" + getRest() +
            "/users/accessRequests";
    @Getter(lazy = true)
    private final String messagesEndpoint = getAddress() + "/" + getVersion() + "/" + getRest() + "/messages";

}
