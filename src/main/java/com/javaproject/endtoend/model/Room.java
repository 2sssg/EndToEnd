package com.javaproject.endtoend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long room_id;

    @Column
    private String roomName;

    @Column(unique = true)
    private int roomNum;

    @Column
    private int froomsearching;

    @JoinColumn(name = "firstUserId")
    @OneToOne(fetch = FetchType.LAZY  , cascade = CascadeType.ALL)
    private User firstUser;

    @JoinColumn(name = "secondUserId")
    @OneToOne(fetch = FetchType.LAZY  , cascade = CascadeType.ALL)
    private User secondUser;


}
