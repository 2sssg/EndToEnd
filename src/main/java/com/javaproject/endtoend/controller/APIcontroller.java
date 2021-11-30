package com.javaproject.endtoend.controller;

import com.javaproject.endtoend.Constant.KoreanAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
public class APIcontroller {
    public String tmp(String message){
        try {
            String key = KoreanAPI.APIKEY.getValue();
            String word = message;
            String query = KoreanAPI.ADDRESS.getValue() + key + "&q=" + word+KoreanAPI.REQ_TYPE_JSON+"&num=10";
            System.out.println(query);
            URL url = new URL(query);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}



