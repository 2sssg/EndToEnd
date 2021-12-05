package com.javaproject.endtoend.DTO;

import com.javaproject.endtoend.service.APIService;
import com.javaproject.endtoend.service.RoomContentService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Message {
    @Autowired
    RoomContentService roomContentService;

    private String beforeWord;

    private String nowWord;

    private int roomNum;

    private NowWordDictionaryInfo nowWordDictionaryInfo = new NowWordDictionaryInfo();



    //게임의 시작인지 확인하는 메서드
    public boolean isStart(){
        return this.beforeWord.length() == 0;
    }

    // 이전 단어의 마지막 글자와 현재 단어의 첫 글자가 맞는지 검사하는 메서드
    // 끝말잇기 규칙
    public boolean isLegalWord(){
        if(isStart()){
            return true;
        }else{
            return this.beforeWord.substring(beforeWord.length() - 1)
                    .equals(nowWord.substring(0, 1));
        }
    }

    //한 글자 단어 막기용
    public boolean isOverTwoLength(){
        return this.nowWord.length()>1;
    }


    //사전에 있는 단어인지
    public boolean isDictionary(){
        JSONObject dicInfo = APIService.dictionary(this.nowWord);
        if((Boolean)dicInfo.get("isWord")){
            nowWordDictionaryInfo.setWord(dicInfo.get("word").toString());
            nowWordDictionaryInfo.setPosition(dicInfo.get("pos").toString());
            nowWordDictionaryInfo.setDefinition(dicInfo.get("definition").toString());
        }
        return (Boolean)dicInfo.get("isWord");
    }

    //다 통과 했는지
    public boolean isAllLegal(){
        return isLegalWord()
                &&isOverTwoLength()
                &&isDictionary();
    }



}
