package com.github.mukhlisov.model.dto.exchangeRate;


import com.github.mukhlisov.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateOutputDto {
    private Integer id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private Double rate;
}
