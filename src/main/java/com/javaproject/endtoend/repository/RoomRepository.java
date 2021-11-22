package com.javaproject.endtoend.repository;


import com.javaproject.endtoend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository  extends JpaRepository<Room, Long> {
    int countByFroomsearching(int isSearching);


    Optional<Room> findOneByFroomsearching(int isSearching);

    List<Room> findByFroomsearching(int isSearching);
    List<Room> findAll();

    Room findByRoomNum(int room_num);
}
