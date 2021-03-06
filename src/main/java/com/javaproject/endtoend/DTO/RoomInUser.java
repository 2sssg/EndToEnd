package com.javaproject.endtoend.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomInUser {
    private String userName;
    private String roomName;
    private int roomNum;
    private int whatUser;
    private int userLife;

    public RoomInUser(String userName, String roomName) {
        this.userName = userName;
        this.roomName = roomName;
    }
}
