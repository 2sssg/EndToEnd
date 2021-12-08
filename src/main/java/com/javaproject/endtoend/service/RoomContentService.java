package com.javaproject.endtoend.service;

import com.javaproject.endtoend.model.Room;
import com.javaproject.endtoend.model.RoomContent;
import com.javaproject.endtoend.repository.RoomContentRepository;
import com.javaproject.endtoend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Controller
@RequiredArgsConstructor
public class RoomContentService {
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    RoomContentRepository roomContentRepository;

    //단어 저장
    public String saveOneWord(int roomnum, String word){
        //중복 한번더 검사
        if(isOverlap(roomnum,word)){
            RoomContent roomContent = RoomContent.builder()
                    .room(roomRepository.findByRoomNum(roomnum).orElse(null))
                    .word(word)
                    .build();
            try{
                roomContentRepository.save(roomContent);
            }catch(Exception e){
                e.printStackTrace();
                return "fail";
            }
        }
        return "success";
    }

    public boolean isOverlap(int roomnum, String word){
        return roomContentRepository.countByRoom_RoomNumAndWord(roomnum, word) == 0;
    }
}
