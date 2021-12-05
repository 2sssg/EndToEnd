package com.javaproject.endtoend.DTO.koreanAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KoreanApiResponseDTO {
    private int total;
    private int num;
    private String title;
    private int start;
    private String description;
    private List<Item> item;



}
