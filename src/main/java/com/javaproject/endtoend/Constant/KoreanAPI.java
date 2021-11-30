package com.javaproject.endtoend.Constant;

public enum KoreanAPI {
    APIKEY("key=B6CD0670ED60BEF217B146CEE4DF2CA5"),
    ADDRESS("https://stdict.korean.go.kr/api/search.do?"),
    REQ_TYPE_JSON("&req_type=json"),
    REQ_TYPE_XML("&req_type=xml"),
    ;

    private final String value;
    KoreanAPI(String value){this.value = value;}
    public String getValue(){return value;
    }
}
