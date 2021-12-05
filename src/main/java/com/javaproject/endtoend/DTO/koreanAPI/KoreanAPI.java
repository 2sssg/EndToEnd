package com.javaproject.endtoend.DTO.koreanAPI;


import antlr.build.ANTLR;
import com.javaproject.endtoend.Constant.ConstantKoreanAPI;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KoreanAPI {
    // 인증 키
    @Builder.Default
    private String key = ConstantKoreanAPI.APIKEY.getValue();

    // 검색어
    private String q;

    // 요청 타입
    @Builder.Default
    private String req_type = "xml";

    // 검색의 시작 번호
    @Builder.Default
    private int start = 1;

    // 결과 출력 건수 (10~100)
    @Builder.Default
    private int num = 10;

    //자세히 찾기 여부
    @Builder.Default
    private String advanced = "n";



    @Override
    public String toString() {
        return "https://stdict.korean.go.kr/api/search.do?"+
                "key=" + key +
                "&q=" + q +
                "&req_type=" + req_type+
                "&start=" + start +
                "&num=" + num +
                "&advanced=" + advanced;
    }

}
