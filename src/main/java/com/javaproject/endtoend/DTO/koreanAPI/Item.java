package com.javaproject.endtoend.DTO.koreanAPI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Item {
    private int sup_no;
    private String word;
    private String target_code;
    private Sense sense;
    private String pos;
}
