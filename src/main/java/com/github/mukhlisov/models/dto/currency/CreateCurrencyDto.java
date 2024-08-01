package com.github.mukhlisov.models.dto.currency;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCurrencyDto {
    private String code;
    private String fullName;
    private String sign;

}
