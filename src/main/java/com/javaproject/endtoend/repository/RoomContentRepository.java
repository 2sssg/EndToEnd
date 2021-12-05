package com.javaproject.endtoend.repository;

import com.javaproject.endtoend.model.Room;
import com.javaproject.endtoend.model.RoomContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomContentRepository extends JpaRepository<RoomContent,Long> {
    int countByRoom_RoomNumAndWord(int roomnum, String word);
}
