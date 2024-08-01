package com.github.mukhlisov.models.dto.currency;


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
