package com.javaproject.endtoend.controller;

import com.javaproject.endtoend.Constant.DbDefault.RoomName;
import com.javaproject.endtoend.DTO.RoomInUser;
import com.javaproject.endtoend.model.Room;
import com.javaproject.endtoend.model.User;
import com.javaproject.endtoend.repository.RoomRepository;
import com.javaproject.endtoend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@Service
@Slf4j
public class MainController {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;




    @Transactional
    @RequestMapping()
    //메인페이지
    public String mainController(Model model){
        // 게임중이 아닌방 찾기
        List<Room> rooms = roomRepository.findByRoomFlag(1);
        model.addAttribute("rooms",rooms);
        //thymeleaf사용 객체 위해서 컨트롤러에서 만들어서 반환
        model.addAttribute("roomInUser",new RoomInUser());
        System.out.println(model);
        return "index";
    }

    @Transactional
    @RequestMapping(value = "/gameIn")
    public String loginController(User user, Model model){
        Room room;
        Date date = new Date();
        int count=0;
        //게스트 닉네임을 위해서 난수 생성
        for(int temp: Arrays.stream(String.valueOf(date.hashCode()<0?date.hashCode()*-1:date.hashCode()).split("")).mapToInt(Integer::parseInt).toArray()){
            count+=temp;
        }
        //guest+난수
        user.setUserName("geust"+count);

        //방이 있으면
        if(roomRepository.countByRoomFlag(1)>0){
            //게임 시작 전 flag 변경 후 user 넣어주기
            room = roomRepository.findByRoomFlag(1).get(0);
            room.setRoomFlag(0);
            room.setSecondUser(user);
        }else{
            //없으면 하나 만들어서 반환
            room = Room.builder()
                    .roomName(RoomName.roomName[(int)((Math.random()*10000)%10)])
                    .roomFlag(1)
                    .roomNum(new Date().hashCode())
                    .firstUser(user)
                    .firstUserLife(100)
                    .secondUserLife(100)
                    .build();
        }
        //db저장하고 끝냄
        userRepository.save(user);
        roomRepository.save(room);
        model.addAttribute("room", room);

        return "gameIn";
    }


    //방만들기
    @RequestMapping("/routemakeroom")
    public String routemakeRoomController(Model model, RoomInUser roomInUser){
        return "makeroom";
    }


    //방만들기 페이지
    @Transactional
    @RequestMapping("/makeroom")
    public String makeRoomController(Model model, RoomInUser roomInUser){

        User user = User.builder()
                .userName(roomInUser.getUserName())
                .build();

        Room room = Room.builder()
                .roomFlag(1)
                .roomName(roomInUser.getRoomName())
                .roomNum(new Date().hashCode())
                .firstUser(user)
                .firstUserLife(100)
                .secondUserLife(100)
                .build();

        userRepository.save(user);
        roomRepository.save(room);
        model.addAttribute("room", room);

        return "gameIn";
    }


    //입장하기
    @Transactional
    @RequestMapping("/specificroom")
    public String specificroomController(Model model,RoomInUser roomInUser){


        System.out.println("dasdas"+roomInUser);

        User user = User.builder()
                .userName(roomInUser.getUserName())
                .build();

        Room room = roomRepository.findByRoomNum(roomInUser.getRoomNum()).orElse(new Room());
        room.setSecondUser(user);
        room.setRoomFlag(0);
        userRepository.save(user);
        roomRepository.save(room);
        model.addAttribute(room);
        return "gameIn";
    }


}
