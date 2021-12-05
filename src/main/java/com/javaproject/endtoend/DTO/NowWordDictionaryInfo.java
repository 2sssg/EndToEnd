package com.javaproject.endtoend.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class NowWordDictionaryInfo {
    private String word;
    private String position;
    private String definition;
}
