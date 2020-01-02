package com.pawban.communicator_frontend.client;

import com.pawban.communicator_frontend.config.BackendConfig;
import com.pawban.communicator_frontend.config.EntityBuilder;
import com.pawban.communicator_frontend.domain.AccessRequest;
import com.pawban.communicator_frontend.dto.AccessRequestDto;
import com.pawban.communicator_frontend.dto.NewAccessRequestDto;
import com.pawban.communicator_frontend.type.AccessRequestRole;
import com.pawban.communicator_frontend.type.AccessRequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AccessRequestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessRequestClient.class);

    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;
    private final EntityBuilder entityBuilder;

    @Autowired
    public AccessRequestClient(final RestTemplate restTemplate,
                               final BackendConfig backendConfig,
                               final EntityBuilder entityBuilder) {
        this.restTemplate = restTemplate;
        this.backendConfig = backendConfig;
        this.entityBuilder = entityBuilder;
    }

    public Set<AccessRequest> getAccessRequests(final UUID sessionId,
                                                final AccessRequestRole role) {
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getAccessRequestsEndpoint())
                .queryParam("role", role.getValue())
                .build(true)
                .toUri();
        try {
            ResponseEntity<AccessRequestDto[]> responseEntity =
                    restTemplate.exchange(url, HttpMethod.GET, entityBuilder.buildHeaders(sessionId), AccessRequestDto[].class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                return Arrays.stream(Objects.requireNonNull(responseEntity.getBody()))
                        .map(AccessRequest::new)
                        .collect(Collectors.toSet());
            }
            return Collections.emptySet();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptySet();
        }
    }

    public Optional<AccessRequest> createAccessRequest(final UUID sessionId,
                                                       final UUID chatRoomId,
                                                       final String request) {
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getAccessRequestsEndpoint())
                .build(true)
                .toUri();
        try {
            ResponseEntity<AccessRequestDto> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entityBuilder.buildHttpEntity(sessionId, new NewAccessRequestDto(chatRoomId, request)),
                    AccessRequestDto.class
            );
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                return Optional.of(new AccessRequest(Objects.requireNonNull(responseEntity.getBody())));
            }
            return Optional.empty();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public boolean changeAccessRequestStatus(final UUID sessionId,
                                             final UUID accessRequestId,
                                             final AccessRequestStatus newStatus) {
        java.lang.String endpoint = backendConfig.getAccessRequestsEndpoint() + "/" + accessRequestId.toString();
        URI url = UriComponentsBuilder.fromHttpUrl(endpoint)
                .queryParam("status", newStatus.getValue())
                .build(true)
                .toUri();
        LOGGER.info(url.toString());
        try {
            ResponseEntity<AccessRequestDto> responseEntity =
                    restTemplate.exchange(url, HttpMethod.PUT, entityBuilder.buildHeaders(sessionId), AccessRequestDto.class);
            return responseEntity.getStatusCode().equals(HttpStatus.OK);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

}
