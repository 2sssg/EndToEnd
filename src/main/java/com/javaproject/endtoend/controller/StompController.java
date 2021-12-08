package com.javaproject.endtoend.controller;


import com.javaproject.endtoend.Constant.EnumV.Error;
import com.javaproject.endtoend.Constant.ClassV.ReturnObject;
import com.javaproject.endtoend.DTO.Message;
import com.javaproject.endtoend.DTO.RoomDTO;
import com.javaproject.endtoend.DTO.RoomInUser;
import com.javaproject.endtoend.model.PhoneticRule;
import com.javaproject.endtoend.model.Room;
import com.javaproject.endtoend.repository.PhoneticRepository;
import com.javaproject.endtoend.repository.RoomRepository;
import com.javaproject.endtoend.service.RoomContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

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


    //단어 받았을 때
    @MessageMapping("/chat/message/{roomid}")
    public void chat(Message message, RoomInUser roomInUser ,@DestinationVariable int roomid){
        //방 넘버로 방찾기
        Room room = roomRepository.findByRoomNum(roomInUser.getRoomNum()).orElse(new Room());
        ReturnObject returnObject = new ReturnObject();
        //1번 유저가 보냈는지
        //2번 유저가 보냈는지
        //판단해서 생명력 저장해놓기
        roomInUser.setUserLife(
                roomInUser.getWhatUser()==1?
                        room.getFirstUserLife():
                        room.getSecondUserLife()
        );
        //단어가 끝말잇기 규칙에 맞는지 확인
        boolean flag = message.isAllLegal();
        //실패인지 아닌지 저장해놓기
        returnObject.setSuccess(flag);

        ///////////////////// 단어가 끝말잇기 규칙에 맞지 않을 때///////////////////////
        if(!(flag&&roomContentService.isOverlap(roomid,message.getNowWord()))){
            message.getNowWordDictionaryInfo().setWord(message.getNowWord());
            returnObject.addJsonObject("dicResult",message.getNowWordDictionaryInfo());
            //끝 말이 이어지지 않았을 때
            if(!message.isLeg()){
                //두음법칙 적용해도 안될 때
                if(!phoneticRepository
                        .findByFirstOP(
                                message.getBeforeWord().substring(message.getBeforeWord().length() - 1)
                        ).orElse(new PhoneticRule("compare")).getSecondOP()
                        .equals(message.getNowWord().substring(0,1))
                ){
                    roomInUser.setUserLife(roomInUser.getUserLife()-15);
                    returnObject.setError(Error.VIOLATE.toString(), "'끝'말을 이어주쎄용");
                }
                // 두음법칙 적용 했을 때 끝말이 이어질 경우
                else{
                    // 한번더 2글자 이상, 사전에있는단어, 중복되지 않았는지를 검사
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
                    // 두음법칙 적용후 모든 검사 통과하면 맞는 단어로 판단 후 정상 리턴
                    else{
                        returnObject.setSuccess(true);
                        roomContentService.saveOneWord(message.getRoomNum(),message.getNowWord());
                        returnObject.addJsonObject("dicResult",message.getNowWordDictionaryInfo());
                    }
                }
            }
            //한 글자 일때
            else if(!message.isTwo()){
                returnObject.setSuccess(false);
                roomInUser.setUserLife(roomInUser.getUserLife()-5);
                returnObject.setError(Error.ONELETTER.toString(),"한글자는 안됩니다");
            }
            //사전에 없는 단어일 때
            else if(!message.isDic()){
                returnObject.setSuccess(false);
                roomInUser.setUserLife(roomInUser.getUserLife()-20);
                returnObject.setError(Error.NOTEXIST.toString(),"사전에 없는 단어입니당");
            }
            //중복된 단어일때
            // 중복검사 메서드를 호출해서 확인해야 하지만 db액세스 효율 때문에 일단 else처리
            // 후에 다른 규칙 추가시 고쳐야됨
            else{
                returnObject.setSuccess(false);
                roomInUser.setUserLife(roomInUser.getUserLife()-5);
                returnObject.setError(Error.DUPLICATE.toString(),"중복된 단어입니다");
            }
        }
        //끝말잇기 규칙에 모두 통과했을 때
        else{
            roomContentService.saveOneWord(message.getRoomNum(),message.getNowWord());
            returnObject.addJsonObject("dicResult",message.getNowWordDictionaryInfo());
        }
        returnObject.addJsonObject("roomInUser",roomInUser);


        // 생명력을 둘다 갱신 후 저장
        if (roomInUser.getWhatUser() == 1) {
            room.setFirstUserLife(roomInUser.getUserLife());
        } else {
            room.setSecondUserLife(roomInUser.getUserLife());
        }
        roomRepository.save(room);

        // 반환
        messagingTemplate.convertAndSend("/sub/chat/room/"+roomid, returnObject);
    }


    //방에 누가 들어왔다 알림 메서드
    @MessageMapping("/chat/room/statuschange/{roomid}")
    public void chat(@DestinationVariable int roomid){
        ReturnObject returnObject = new ReturnObject();
        returnObject.setSuccess(true);
        Room room = roomRepository.findByRoomNum(roomid).get();
        returnObject.addJsonObject("room",room);
        messagingTemplate.convertAndSend("/sub/chat/room/statuschange/"+roomid,returnObject);
    }

    // 표준국어대사전 api가 너무 느려서
    // 호출 기다리면 타임아웃이 나는 문제때문에
    // 전송누르면 일단 타이머 멈춰놓기 위해서 만든 메서드
    @MessageMapping("/timer/{roomid}")
    public void timer(@DestinationVariable int roomid){
        messagingTemplate.convertAndSend("/sub/chat/room/timer/"+roomid,new ReturnObject());
    }



    // 타임아웃으로 인한 생명력 까짐 구현
    @MessageMapping("/timeout/{roomid}")
    public void timeout(@DestinationVariable int roomid,RoomInUser roomInUser){
        ReturnObject returnObject = new ReturnObject();
        Room room = roomRepository.findByRoomNum(roomid).get();
        int life;
        RoomDTO roomDTO;
        // 유저 판단해서 생명력 깎기
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
        messagingTemplate.convertAndSend("/sub/timeout/"+roomid, returnObject);
    }



}
