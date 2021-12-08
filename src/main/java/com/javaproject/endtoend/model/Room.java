package com.javaproject.endtoend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomId;

    @Column
    private String roomName;

    @Column(unique = true)
    private int roomNum;

    @Column
    private int roomFlag;

    @JoinColumn(name = "firstUserId")
    @OneToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private User firstUser;

    @JoinColumn(name = "secondUserId")
    @OneToOne(fetch = FetchType.EAGER  , cascade = CascadeType.ALL)
    private User secondUser;


    @Column
    private int firstUserLife;

    @Column
    private int secondUserLife;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room", fetch = FetchType.EAGER)
    private List<RoomContent> roomContents = new ArrayList<>();

}
