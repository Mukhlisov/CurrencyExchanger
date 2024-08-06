package com.github.mukhlisov.models.dto.exchangeRate;

import com.github.mukhlisov.exceptions.IncorrectParametersException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class CreateExchangeRateDto {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    @Setter
    private Double rate;

    public CreateExchangeRateDto(String baseCurrencyCode, String targetCurrencyCode, Double rate) throws IncorrectParametersException {
        if (baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3) {
            throw new IncorrectParametersException("Invalid Currency Code");
        }
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) throws IncorrectParametersException {
        if (baseCurrencyCode.length() != 3) {
            throw new IncorrectParametersException("Invalid Currency Code");
        }
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) throws IncorrectParametersException{
        if (targetCurrencyCode.length() != 3) {
            throw new IncorrectParametersException("Invalid Currency Code");
        }
        this.targetCurrencyCode = targetCurrencyCode;
    }

}
