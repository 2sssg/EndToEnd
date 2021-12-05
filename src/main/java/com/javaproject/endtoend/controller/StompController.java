package com.javaproject.endtoend.controller;


import com.javaproject.endtoend.Constant.Error;
import com.javaproject.endtoend.Constant.ReturnObject;
import com.javaproject.endtoend.DTO.Message;
import com.javaproject.endtoend.service.APIService;
import com.javaproject.endtoend.service.RoomContentService;
import lombok.RequiredArgsConstructor;
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
public class StompController {

    @Autowired
    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    RoomContentService roomContentService;

    @MessageMapping("/chat/message/{roomid}")
    public void chat(Message message, @DestinationVariable int roomid){
        ReturnObject returnObject = new ReturnObject();
        System.out.println(message);
        boolean flag = message.isAllLegal();
        returnObject.setSuccess(flag);
        if(!(flag&&roomContentService.isOverlap(message.getRoomNum(),message.getNowWord()))){
            message.getNowWordDictionaryInfo().setWord(message.getNowWord());
            returnObject.addJsonObject("dicResult",message.getNowWordDictionaryInfo());
            if(!message.isLegalWord()){
                returnObject.setError(Error.VIOLATE.toString(), "'끝'말을 이어줘");
            }else if(!message.isOverTwoLength()){
                returnObject.setError(Error.ONELETTER.toString(),"한글자는 안됩니다");
            }else if(!message.isDictionary()){
                returnObject.setError(Error.NOTEXIST.toString(),"사전에 없는 단어");
            }
        }else{
            roomContentService.saveOneWord(message.getRoomNum(),message.getNowWord());
            returnObject.addJsonObject("dicResult",message.getNowWordDictionaryInfo());
        }

        messagingTemplate.convertAndSend("/sub/chat/room/"+roomid, returnObject);
    }

    @MessageMapping("/chat/room/statuschange/{roomid}")
    public void chat(@DestinationVariable int roomid){
        Map<String,String> hm = new HashMap<>();
        messagingTemplate.convertAndSend("/sub/chat/room/statuschange/"+roomid,hm);
    }

}
