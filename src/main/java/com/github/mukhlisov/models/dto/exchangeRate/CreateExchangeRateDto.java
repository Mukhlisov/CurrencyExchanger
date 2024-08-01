package com.github.mukhlisov.models.dto.exchangeRate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateExchangeRateDto {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private Double rate;
}
