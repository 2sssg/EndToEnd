package com.javaproject.endtoend.repository;


import com.javaproject.endtoend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository  extends JpaRepository<Room, Long> {
    int countByRoomFlag(int isSearching);
    List<Room> findByRoomFlag(int isSearching);
    List<Room> findAll();
    Optional<Room> findByRoomNum(int room_num);

}
