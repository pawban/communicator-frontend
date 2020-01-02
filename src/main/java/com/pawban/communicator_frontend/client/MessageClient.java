package com.pawban.communicator_frontend.client;

import com.pawban.communicator_frontend.config.BackendConfig;
import com.pawban.communicator_frontend.config.EntityBuilder;
import com.pawban.communicator_frontend.domain.Message;
import com.pawban.communicator_frontend.dto.MessageDto;
import com.pawban.communicator_frontend.dto.NewMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MessageClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageClient.class);

    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;
    private final EntityBuilder entityBuilder;

    @Autowired
    public MessageClient(final RestTemplate restTemplate,
                         final BackendConfig backendConfig,
                         final EntityBuilder entityBuilder) {
        this.restTemplate = restTemplate;
        this.backendConfig = backendConfig;
        this.entityBuilder = entityBuilder;
    }

    public List<Message> getCurrentUserMessages(final UUID sessionId,
                                                final Boolean undeliveredOnly,
                                                final UUID chatRoomId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (undeliveredOnly != null) {
            params.add("undeliveredOnly", undeliveredOnly.toString());
        }
        if (chatRoomId != null) {
            params.add("chatRoomId", chatRoomId.toString());
        }
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getMessagesEndpoint())
                .queryParams(params)
                .build(true)
                .toUri();
        try {
            ResponseEntity<MessageDto[]> responseEntity =
                    restTemplate.exchange(url, HttpMethod.GET, entityBuilder.buildHeaders(sessionId), MessageDto[].class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                return Arrays.stream(Objects.requireNonNull(responseEntity.getBody()))
                        .map(Message::new)
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public Optional<Message> createMessage(final UUID sessionId,
                                           final String messageText,
                                           final UUID chatRoomId) {
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getMessagesEndpoint())
                .build(true)
                .toUri();
        try {
            ResponseEntity<MessageDto> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entityBuilder.buildHttpEntity(sessionId, new NewMessageDto(messageText, chatRoomId)),
                    MessageDto.class
            );
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                return Optional.of(new Message(Objects.requireNonNull(responseEntity.getBody())));
            }
            return Optional.empty();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

}
