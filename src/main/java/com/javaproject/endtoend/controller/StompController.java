package com.javaproject.endtoend.controller;


import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yaml.snakeyaml.scanner.ScannerImpl;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StompController {

    @Autowired
    private final SimpMessageSendingOperations messagingTemplate;

    APIcontroller apicontroller = new APIcontroller();

    @MessageMapping("/chat/message/{roomid}")
    public void chat(JSONObject jsonObject, @DestinationVariable int roomid){
        System.out.println("여기들어옴");
        boolean flag =true;
        String beforeMessage =jsonObject.get("beforeMessage").toString();
        String message = jsonObject.get("message").toString();

        apicontroller.tmp(message);
        if(!beforeMessage.equals("")){
            System.out.println("씹년아!!!!!!!!!!!!!!!"+beforeMessage);
            flag =beforeMessage.substring(beforeMessage.length() - 1).equals(message.substring(0, 1));
        }
        Map<String, String> returnmap = new HashMap<>();
        returnmap.put("msg", message);
        returnmap.put("flag",flag?"true":"false");
        returnmap.put("dicResult",apicontroller.tmp(message));
        messagingTemplate.convertAndSend("/sub/chat/room/"+roomid, returnmap);
    }

    @MessageMapping("/chat/room/statuschange/{roomid}")
    public void chat(@DestinationVariable int roomid){
        System.out.println("여기들어옴ㅋㅋㅋㅋ");
        Map<String,String> hm = new HashMap<>();
        messagingTemplate.convertAndSend("/sub/chat/room/statuschange/"+roomid,hm);
    }

}
