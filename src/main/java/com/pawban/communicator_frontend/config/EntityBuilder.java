package com.pawban.communicator_frontend.config;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

@Component
public class EntityBuilder {

    public HttpEntity<Object> buildHeaders(final UUID sessionId) {
        return buildHttpEntity(sessionId, null);
    }

    public HttpEntity<Object> buildHttpEntity(final UUID sessionId,
                                              final Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(BackendConfig.AUTH_HEADER, sessionId.toString());
        return new HttpEntity<>(body, headers);
    }

}
