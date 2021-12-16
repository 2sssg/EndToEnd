package com.javaproject.endtoend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaproject.endtoend.DTO.koreanAPI.KoreanAPI;
import com.javaproject.endtoend.DTO.koreanAPI.KoreanApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.json.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


@Slf4j
public class APIService {
    public static JSONObject dictionary(String message){
        JSONObject returnJSON = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        JSONParser jsonParser = new JSONParser();
        try {
            KoreanAPI query = KoreanAPI.builder()
                    .q(message)
                    .req_type("json")
                    .build();
            log.info("날리는 쿼리는 : "+query.toString());
            URL url = new URL(query.toString());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder jsonString= new StringBuilder();
            while ((line = br.readLine()) != null) {
                log.info(line);
                jsonString.append(line);
            }
            br.close();
            JSONObject jsonObject = new JSONObject();
            KoreanApiResponseDTO koreanApiResponseDTO = new KoreanApiResponseDTO();
            if(!jsonString.toString().equals("")){
                jsonObject = (JSONObject) jsonParser.parse(jsonString.toString());
                koreanApiResponseDTO = mapper.readValue(jsonObject.get("channel").toString(),KoreanApiResponseDTO.class);
            }


            if(koreanApiResponseDTO.getTotal()==0){
                returnJSON.put("isWord",false);
                return returnJSON;
            }
            returnJSON.put("isWord",true);
            returnJSON.put("word",koreanApiResponseDTO.getItem().get(0).getWord());
            returnJSON.put("pos",koreanApiResponseDTO.getItem().get(0).getPos());
            returnJSON.put("definition",koreanApiResponseDTO.getItem().get(0).getSense().getDefinition());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnJSON;
    }




}



