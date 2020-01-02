package com.pawban.communicator_frontend.client;

import com.pawban.communicator_frontend.config.BackendConfig;
import com.pawban.communicator_frontend.config.EntityBuilder;
import com.pawban.communicator_frontend.domain.ChatRoom;
import com.pawban.communicator_frontend.domain.Member;
import com.pawban.communicator_frontend.domain.Message;
import com.pawban.communicator_frontend.dto.ChatRoomDto;
import com.pawban.communicator_frontend.dto.MemberDto;
import com.pawban.communicator_frontend.dto.MessageDto;
import com.pawban.communicator_frontend.dto.NewChatRoomDto;
import com.pawban.communicator_frontend.type.ChatRoomStatus;
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
public class ChatRoomClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatRoomClient.class);

    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;
    private final EntityBuilder entityBuilder;

    @Autowired
    public ChatRoomClient(final RestTemplate restTemplate,
                          final BackendConfig backendConfig,
                          final EntityBuilder entityBuilder) {
        this.restTemplate = restTemplate;
        this.backendConfig = backendConfig;
        this.entityBuilder = entityBuilder;
    }

    public List<ChatRoom> getAvailableChatRooms(final UUID sessionId) {
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getChatRoomsEndpoint())
                .build(true)
                .toUri();
        try {
            ResponseEntity<ChatRoomDto[]> responseEntity =
                    restTemplate.exchange(url, HttpMethod.GET, entityBuilder.buildHeaders(sessionId), ChatRoomDto[].class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                return Arrays.stream(Objects.requireNonNull(responseEntity.getBody()))
                        .map(ChatRoom::new)
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Member> getChatRoomMembers(final UUID sessionId,
                                           final UUID chatRoomId,
                                           final boolean includePotentialMembers) {
        String endpoint = backendConfig.getChatRoomsEndpoint() + "/" + chatRoomId.toString() + "/members";
        URI url = UriComponentsBuilder.fromHttpUrl(endpoint)
                .queryParam("includePotentialMembers", includePotentialMembers)
                .build(true)
                .toUri();
        try {
            ResponseEntity<MemberDto[]> responseEntity =
                    restTemplate.exchange(url, HttpMethod.GET, entityBuilder.buildHeaders(sessionId), MemberDto[].class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                return Arrays.stream(Objects.requireNonNull(responseEntity.getBody()))
                        .map(Member::new)
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Message> getChatRoomMessages(final UUID sessionId,
                                             final UUID chatRoomId,
                                             final Boolean undeliveredOnly) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (undeliveredOnly != null) {
            params.add("undeliveredOnly", undeliveredOnly.toString());
        }
        String endpoint = backendConfig.getChatRoomsEndpoint() + "/" + chatRoomId.toString() + "/messages";
        URI url = UriComponentsBuilder.fromHttpUrl(endpoint)
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

    public Optional<ChatRoom> createChatRoom(final UUID sessionId,
                                             final String name,
                                             final ChatRoomStatus status) {
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getChatRoomsEndpoint())
                .build(true)
                .toUri();
        try {
            ResponseEntity<ChatRoomDto> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entityBuilder.buildHttpEntity(sessionId, new NewChatRoomDto(name, status)),
                    ChatRoomDto.class
            );
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                return Optional.of(new ChatRoom(Objects.requireNonNull(responseEntity.getBody())));
            }
            return Optional.empty();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Optional<ChatRoom> changeChatRoomStatus(final UUID sessionId,
                                                   final UUID chatRoomId,
                                                   final ChatRoomStatus status) {
        return changeChatRoom(sessionId, chatRoomId, null, status);
    }

    public Optional<ChatRoom> changeChatRoom(final UUID sessionId,
                                             final UUID chatRoomId,
                                             final UUID ownerId,
                                             final ChatRoomStatus status) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (ownerId != null) {
            params.add("ownerId", ownerId.toString());
        }
        if (status != null) {
            params.add("status", status.getValue());
        }
        java.lang.String endpoint = backendConfig.getChatRoomsEndpoint() + "/" + chatRoomId.toString();
        URI url = UriComponentsBuilder.fromHttpUrl(endpoint)
                .queryParams(params)
                .build(true)
                .toUri();
        try {
            ResponseEntity<ChatRoomDto> responseEntity =
                    restTemplate.exchange(url, HttpMethod.PUT, entityBuilder.buildHeaders(sessionId), ChatRoomDto.class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                return Optional.of(new ChatRoom(Objects.requireNonNull(responseEntity.getBody())));
            }
            return Optional.empty();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Optional<ChatRoom> changeChatRoomOwner(final UUID sessionId,
                                                  final UUID chatRoomId,
                                                  final UUID ownerId) {
        return changeChatRoom(sessionId, chatRoomId, ownerId, null);
    }

    public boolean addMemberToChatRoom(final UUID sessionId,
                                       final UUID chatRoomId,
                                       final UUID newMemberId) {
        String endpoint = backendConfig.getChatRoomsEndpoint() + "/" + chatRoomId.toString() +
                "/members/" + newMemberId.toString();
        URI url = UriComponentsBuilder.fromHttpUrl(endpoint)
                .build(true)
                .toUri();
        try {
            ResponseEntity<Void> responseEntity =
                    restTemplate.exchange(url, HttpMethod.PUT, entityBuilder.buildHeaders(sessionId), Void.class);
            return responseEntity.getStatusCode().equals(HttpStatus.OK);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    public boolean deleteChatRoom(final UUID sessionId,
                                  final UUID chatRoomId) {
        String endpoint = backendConfig.getChatRoomsEndpoint() + "/" + chatRoomId.toString();
        URI url = UriComponentsBuilder.fromHttpUrl(endpoint)
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

    public boolean removeMemberFromChatRoom(final UUID sessionId,
                                            final UUID chatRoomId,
                                            final UUID memberId) {
        String endpoint = backendConfig.getChatRoomsEndpoint() + "/" + chatRoomId.toString() +
                "/members/" + memberId.toString();
        URI url = UriComponentsBuilder.fromHttpUrl(endpoint)
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

}
