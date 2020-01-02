package com.pawban.communicator_frontend.dto;

import com.pawban.communicator_frontend.type.ChatRoomStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewChatRoomDto {

    private String name;
    private ChatRoomStatus status;

}
