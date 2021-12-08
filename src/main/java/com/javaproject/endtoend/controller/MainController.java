package com.javaproject.endtoend.controller;

import com.javaproject.endtoend.Constant.RoomName;
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
    public String mainController(Model model){

        List<Room> rooms = roomRepository.findByRoomFlag(1);
        model.addAttribute("rooms",rooms);

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
        for(int temp: Arrays.stream(String.valueOf(date.hashCode()<0?date.hashCode()*-1:date.hashCode()).split("")).mapToInt(Integer::parseInt).toArray()){
            count+=temp;
        }
        user.setUserName("geust"+count);
        System.out.println("gameIn");
        if(roomRepository.countByRoomFlag(1)>0){
            room = roomRepository.findByRoomFlag(1).get(0);
            room.setRoomFlag(0);
            room.setSecondUser(user);
        }else{
            room = Room.builder()
                    .roomName(RoomName.roomName[(int)((Math.random()*10000)%10)])
                    .roomFlag(1)
                    .roomNum(new Date().hashCode())
                    .firstUser(user)
                    .firstUserLife(100)
                    .secondUserLife(100)
                    .build();
        }
        userRepository.save(user);
        roomRepository.save(room);
        model.addAttribute("room", room);

        return "gameIn";
    }


    @RequestMapping("/routemakeroom")
    public String routemakeRoomController(Model model, RoomInUser roomInUser){
        return "makeroom";
    }

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


    @Transactional
    @RequestMapping("/temp")
    public String temp(Model model, User user){

        Room room;
        Date date = new Date();
        int count=0;
        for(int temp: Arrays.stream(String.valueOf(date.hashCode()<0?date.hashCode()*-1:date.hashCode()).split("")).mapToInt(Integer::parseInt).toArray()){
            count+=temp;
        }
        user.setUserName("geust"+count);
        System.out.println("gameIn");
        if(roomRepository.countByRoomFlag(1)>0){
            room = roomRepository.findByRoomFlag(1).get(0);
            room.setRoomFlag(0);
            room.setSecondUser(user);
        }else{
            room = Room.builder()
                    .roomName(RoomName.roomName[(int)((Math.random()*10000)%10)])
                    .roomFlag(1)
                    .roomNum(new Date().hashCode())
                    .firstUser(user)
                    .firstUserLife(100)
                    .secondUserLife(100)
                    .build();
        }
        userRepository.save(user);
        roomRepository.save(room);
        model.addAttribute("room", room);
        return "temp";
    }
}
