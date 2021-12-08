package com.javaproject.endtoend.DTO;

import com.javaproject.endtoend.model.RoomContent;
import com.javaproject.endtoend.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private Long roomId;

    private String roomName;

    private int roomNum;

    private int roomFlag;

    private User firstUser;

    private User secondUser;

    private int firstUserLife;

    private int secondUserLife;

    private List<RoomContent> roomContents = new ArrayList<>();

}
