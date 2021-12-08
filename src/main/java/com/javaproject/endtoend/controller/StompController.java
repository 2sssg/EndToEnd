package com.javaproject.endtoend.controller;


import com.javaproject.endtoend.Constant.Error;
import com.javaproject.endtoend.Constant.ReturnObject;
import com.javaproject.endtoend.DTO.Message;
import com.javaproject.endtoend.DTO.RoomDTO;
import com.javaproject.endtoend.DTO.RoomInUser;
import com.javaproject.endtoend.model.PhoneticRule;
import com.javaproject.endtoend.model.Room;
import com.javaproject.endtoend.repository.PhoneticRepository;
import com.javaproject.endtoend.repository.RoomRepository;
import com.javaproject.endtoend.service.APIService;
import com.javaproject.endtoend.service.RoomContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StompController {

    @Autowired
    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    RoomContentService roomContentService;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    PhoneticRepository phoneticRepository;

    @MessageMapping("/chat/message/{roomid}")
    public void chat(Message message, RoomInUser roomInUser ,@DestinationVariable int roomid){
        Room room = roomRepository.findByRoomNum(roomInUser.getRoomNum()).orElse(new Room());
        log.info("roomInUser : "+ roomInUser.toString());
        roomInUser.setUserLife(
                roomInUser.getWhatUser()==1?
                        room.getFirstUserLife():
                        room.getSecondUserLife()
        );
        ReturnObject returnObject = new ReturnObject();
        log.info("message: "+ message.toString());
        boolean flag = message.isAllLegal();
        returnObject.setSuccess(flag);
        if(!(flag&&roomContentService.isOverlap(roomid,message.getNowWord()))){
            log.info("flag : "+flag);
            log.info("isOverlap : "+ roomContentService.isOverlap(roomid,message.getNowWord()));
            log.info("nowWord : "+message.getNowWord());
            message.getNowWordDictionaryInfo().setWord(message.getNowWord());

            returnObject.addJsonObject("dicResult",message.getNowWordDictionaryInfo());
            if(!message.isLeg()){
                if(!phoneticRepository
                        .findByFirstOP(
                                message.getBeforeWord().substring(message.getBeforeWord().length() - 1)
                        ).orElse(new PhoneticRule("꿹ffffff")).getSecondOP()
                        .equals(message.getNowWord().substring(0,1))
                ){
                    roomInUser.setUserLife(roomInUser.getUserLife()-15);
                    returnObject.setError(Error.VIOLATE.toString(), "'끝'말을 이어주쎄용");
                }else{
                    if(!message.isTwo()){
                        returnObject.setSuccess(false);
                        roomInUser.setUserLife(roomInUser.getUserLife()-5);
                        returnObject.setError(Error.ONELETTER.toString(),"한글자는 안됩니다");
                    }else if(!message.isDic()) {
                        returnObject.setSuccess(false);
                        roomInUser.setUserLife(roomInUser.getUserLife() - 20);
                        returnObject.setError(Error.NOTEXIST.toString(), "사전에 없는 단어입니당");
                    }else if(!roomContentService.isOverlap(roomid,message.getNowWord())){
                        returnObject.setSuccess(false);
                        roomInUser.setUserLife(roomInUser.getUserLife() - 20);
                        returnObject.setError(Error.DUPLICATE.toString(), "중복된 단어입니다.");
                    }
                    else{
                        returnObject.setSuccess(true);
                        roomContentService.saveOneWord(message.getRoomNum(),message.getNowWord());
                        returnObject.addJsonObject("dicResult",message.getNowWordDictionaryInfo());
                    }
                }
            }else if(!message.isTwo()){
                returnObject.setSuccess(false);
                roomInUser.setUserLife(roomInUser.getUserLife()-5);
                returnObject.setError(Error.ONELETTER.toString(),"한글자는 안됩니다");
            }else if(!message.isDic()){
                returnObject.setSuccess(false);
                roomInUser.setUserLife(roomInUser.getUserLife()-20);
                returnObject.setError(Error.NOTEXIST.toString(),"사전에 없는 단어입니당");
            }else{
                returnObject.setSuccess(false);
                roomInUser.setUserLife(roomInUser.getUserLife()-5);
                returnObject.setError(Error.DUPLICATE.toString(),"중복된 단어입니다");
            }
        }else{
            roomContentService.saveOneWord(message.getRoomNum(),message.getNowWord());
            returnObject.addJsonObject("dicResult",message.getNowWordDictionaryInfo());
        }
        returnObject.addJsonObject("roomInUser",roomInUser);

        if (roomInUser.getWhatUser() == 1) {
            room.setFirstUserLife(roomInUser.getUserLife());
        } else {
            room.setSecondUserLife(roomInUser.getUserLife());
        }
        roomRepository.save(room);
        messagingTemplate.convertAndSend("/sub/chat/room/"+roomid, returnObject);
    }

    @MessageMapping("/chat/room/statuschange/{roomid}")
    public void chat(@DestinationVariable int roomid){
        ReturnObject returnObject = new ReturnObject();
        returnObject.setSuccess(true);
        Room room = roomRepository.findByRoomNum(roomid).get();
        returnObject.addJsonObject("room",room);
        messagingTemplate.convertAndSend("/sub/chat/room/statuschange/"+roomid,returnObject);
    }

    @MessageMapping("/timer/{roomid}")
    public void timer(@DestinationVariable int roomid){
        log.info("여기 들어옴");
        messagingTemplate.convertAndSend("/sub/chat/room/timer/"+roomid,new ReturnObject());
    }


    @MessageMapping("/timeout/{roomid}")
    public void timeout(@DestinationVariable int roomid,RoomInUser roomInUser){
        ReturnObject returnObject = new ReturnObject();
        Room room = roomRepository.findByRoomNum(roomid).get();
        int life;
        RoomDTO roomDTO;
        if(roomInUser.getWhatUser()==1){
            life = room.getFirstUserLife();
            room.setFirstUserLife(life-10);
        }else{
            life = room.getSecondUserLife();
            room.setSecondUserLife(life - 10);
        }
        roomRepository.save(room);
        roomDTO = RoomDTO.builder()
                        .firstUserLife(room.getFirstUserLife())
                        .secondUserLife(room.getSecondUserLife())
                        .build();

        returnObject.setSuccess(true);
        returnObject.addJsonObject("roomDTO",roomDTO);
        log.info("여기까진 잘된듯?");
        log.info(roomDTO.toString());
        messagingTemplate.convertAndSend("/sub/timeout/"+roomid, returnObject);
    }


}
