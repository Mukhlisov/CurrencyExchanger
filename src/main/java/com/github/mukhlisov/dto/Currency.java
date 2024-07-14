package com.github.mukhlisov.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    private Integer id;
    private String code;
    private String fullName;
    private String sign;

}
