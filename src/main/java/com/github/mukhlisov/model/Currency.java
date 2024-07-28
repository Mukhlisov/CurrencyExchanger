package com.github.mukhlisov.model;


import com.github.mukhlisov.exceptions.IncorrectParametersException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Currency {
    private Integer id;
    private String code;
    private String fullName;
    private String sign;

    public Currency() {}

    public Currency(Integer id, String code, String fullName, String sign) throws IncorrectParametersException {
        if (code.isEmpty() || fullName.isEmpty() || sign.isEmpty()) {
            throw new IncorrectParametersException("Currency has empty fields");
        }
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public Currency(String code, String fullName, String sign) throws IncorrectParametersException {
        if (code.isEmpty() || fullName.isEmpty() || sign.isEmpty()) {
            throw new IncorrectParametersException("Currency has empty fields");
        }
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }
}
