package com.javaproject.endtoend.Constant;

public enum ConstantKoreanAPI {
    APIKEY("B6CD0670ED60BEF217B146CEE4DF2CA5"),
    ADDRESS("https://stdict.korean.go.kr/api/search.do?"),
    ;

    private final String value;
    ConstantKoreanAPI(String value){this.value = value;}
    public String getValue(){return value;
    }
}
