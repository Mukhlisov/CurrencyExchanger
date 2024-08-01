package com.github.mukhlisov.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ExchangeRate {
    private Integer id;
    private Integer baseCurrencyId;
    private Integer targetCurrencyId;
    private Double rate;

    public ExchangeRate(Integer baseCurrencyId, Integer targetCurrencyId, Double rate) {
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = Math.round(rate*1000000)/1000000.0;
    }

    public ExchangeRate(Integer id, Integer baseCurrencyId, Integer targetCurrencyId, Double rate) {
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = Math.round(rate*1000000)/1000000.0;
    }
}
