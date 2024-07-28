package com.github.mukhlisov.model.dto.currency;


import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCurrencyDto {
    private Integer id;
    private String code;
    private String fullName;
    private String sign;

}
