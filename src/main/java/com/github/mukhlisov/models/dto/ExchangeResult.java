package com.github.mukhlisov.models.dto;

import com.github.mukhlisov.models.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeResult {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private double rate;
    private double amount;
    private double convertedAmount;
}
