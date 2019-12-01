package com.pawban.communicator_frontend.client;

import com.pawban.communicator_frontend.config.BackendConfig;
import com.pawban.communicator_frontend.domain.Session;
import com.pawban.communicator_frontend.domain.User;
import com.pawban.communicator_frontend.domain.UserStatus;
import com.pawban.communicator_frontend.dto.SessionDto;
import com.pawban.communicator_frontend.dto.UserDto;
import com.pawban.communicator_frontend.dto.UserStatusDto;
import com.pawban.communicator_frontend.dto.UsernameValidationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserClient.class);

    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;

    @Autowired
    public UserClient(final RestTemplate restTemplate, final BackendConfig backendConfig) {
        this.restTemplate = restTemplate;
        this.backendConfig = backendConfig;
    }

    public Optional<Session> createUser(final User user) {
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUsersEndpoint())
                .build(true)
                .toUri();
        try {
            SessionDto sessionResponse = restTemplate.postForObject(
                    url,
                    new UserDto(user),
                    SessionDto.class);
            return sessionResponse != null ? Optional.of(new Session(sessionResponse)) : Optional.empty();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public List<UserStatus> getUserStatuses() {
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUserStatusesEndpoint())
                .build(true)
                .toUri();
        try {
            UserStatusDto[] statusesResponse = restTemplate.getForObject(url, UserStatusDto[].class);
            return Arrays.stream(Optional.ofNullable(statusesResponse).orElse(new UserStatusDto[0]))
                    .map(UserStatus::new)
                    .collect(Collectors.toList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public Optional<Boolean> isUsernameAvailable(final String username) {
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUsernameValidationEndpoint())
                .build(true)
                .toUri();
        try {
            UsernameValidationDto validationResponse = restTemplate.postForObject(
                    url,
                    new UsernameValidationDto(username, null),
                    UsernameValidationDto.class);
            return validationResponse != null ? Optional.of(validationResponse.getAvailable()) : Optional.empty();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

}
