package com.javaproject.endtoend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomContent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String word;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM")
    private Room room;
}
