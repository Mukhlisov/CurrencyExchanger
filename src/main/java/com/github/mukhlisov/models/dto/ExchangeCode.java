package com.github.mukhlisov.models.dto;


import com.github.mukhlisov.exceptions.IncorrectParametersException;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExchangeCode {
    private String baseCode;
    private String targetCode;

    public ExchangeCode(String code) throws IncorrectParametersException {
        try {
            this.baseCode = code.substring(0, 3);
            this.targetCode = code.substring(3);
        } catch (IndexOutOfBoundsException e) {
            throw new IncorrectParametersException("This code is not valid");
        }
    }

    public ExchangeCode(String baseCode, String targetCode) throws IncorrectParametersException {
        if (baseCode.isEmpty() || targetCode.isEmpty() || baseCode.length() != 3 || targetCode.length() != 3) {
            throw new IncorrectParametersException("This code is not valid");
        }
        this.baseCode = baseCode;
        this.targetCode = targetCode;
    }
}
