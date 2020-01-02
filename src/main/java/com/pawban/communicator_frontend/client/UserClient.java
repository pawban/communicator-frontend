package com.pawban.communicator_frontend.client;

import com.pawban.communicator_frontend.config.BackendConfig;
import com.pawban.communicator_frontend.config.EntityBuilder;
import com.pawban.communicator_frontend.domain.ChatRoom;
import com.pawban.communicator_frontend.domain.User;
import com.pawban.communicator_frontend.dto.ChatRoomDto;
import com.pawban.communicator_frontend.dto.SessionDto;
import com.pawban.communicator_frontend.dto.UserDto;
import com.pawban.communicator_frontend.session.SessionData;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserClient.class);

    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;
    private final EntityBuilder entityBuilder;

    @Autowired
    public UserClient(final RestTemplate restTemplate,
                      final BackendConfig backendConfig,
                      final EntityBuilder entityBuilder) {
        this.restTemplate = restTemplate;
        this.backendConfig = backendConfig;
        this.entityBuilder = entityBuilder;
    }

    public List<User> getVisibleUsers(final UUID sessionId) {
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUsersEndpoint())
                .build(true)
                .toUri();
        try {
            ResponseEntity<UserDto[]> responseEntity =
                    restTemplate.exchange(url, HttpMethod.GET, entityBuilder.buildHeaders(sessionId), UserDto[].class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                return Arrays.stream(Objects.requireNonNull(responseEntity.getBody()))
                        .map(User::new)
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public Optional<SessionData> getSessionData(final UUID sessionId) {
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUsersEndpoint() + "/sessions")
                .build(true)
                .toUri();
        try {
            ResponseEntity<SessionDto> responseEntity =
                    restTemplate.exchange(url, HttpMethod.GET, entityBuilder.buildHeaders(sessionId), SessionDto.class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                if (responseEntity.getBody() != null) {
                    return Optional.of(new SessionData(responseEntity.getBody()));
                }
            }
            return Optional.empty();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Set<ChatRoom> getCurrentUserChatRooms(final UUID sessionId) {
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUsersEndpoint() + "/sessions/chatRooms")
                .build(true)
                .toUri();
        try {
            ResponseEntity<ChatRoomDto[]> responseEntity =
                    restTemplate.exchange(url, HttpMethod.GET, entityBuilder.buildHeaders(sessionId), ChatRoomDto[].class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                return Arrays.stream(Objects.requireNonNull(responseEntity.getBody()))
                        .map(ChatRoom::new)
                        .collect(Collectors.toSet());
            }
            return Collections.emptySet();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptySet();
        }
    }

    public Optional<SessionData> createUser(final User user) {
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUsersEndpoint())
                .build(true)
                .toUri();
        try {
            SessionDto sessionResponse = restTemplate.postForObject(url, new UserDto(user), SessionDto.class);
            return sessionResponse != null ? Optional.of(new SessionData(sessionResponse)) : Optional.empty();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Optional<User> changeUserVisibility(final UUID sessionId,
                                               final boolean visible) {
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUsersEndpoint() + "?visible=" + visible)
                .build(true)
                .toUri();
        try {
            ResponseEntity<UserDto> responseEntity =
                    restTemplate.exchange(url, HttpMethod.PUT, entityBuilder.buildHeaders(sessionId), UserDto.class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                return Optional.of(new User(Objects.requireNonNull(responseEntity.getBody())));
            }
            return Optional.empty();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public boolean deleteUser(final UUID sessionId) {
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUsersEndpoint())
                .build(true)
                .toUri();
        try {
            ResponseEntity<Void> responseEntity =
                    restTemplate.exchange(url, HttpMethod.DELETE, entityBuilder.buildHeaders(sessionId), Void.class);
            return responseEntity.getStatusCode().equals(HttpStatus.OK);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    public Optional<Boolean> isUsernameAvailable(final String username) {
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUsernameValidationEndpoint())
                .build(true)
                .toUri();
        try {
            Boolean validationResponse = restTemplate.postForObject(url, username, Boolean.class);
            return validationResponse != null ? Optional.of(validationResponse) : Optional.empty();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

}
