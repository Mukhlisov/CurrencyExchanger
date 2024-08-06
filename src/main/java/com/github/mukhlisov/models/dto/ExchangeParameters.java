package com.github.mukhlisov.models.dto;


import com.github.mukhlisov.exceptions.IncorrectParametersException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ExchangeParameters {
    private String from;
    private String to;
    @Setter
    private double amount;

    public ExchangeParameters(String from, String to, double amount) throws IncorrectParametersException {
        if (from.equals(to)){
            throw new IncorrectParametersException("Same currency codes");
        }
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public void setFrom(String from) throws IncorrectParametersException {
        if (from.equals(this.to)){
            throw new IncorrectParametersException("Same currency codes");
        }
        this.from = from;
    }

    public void setTo(String to) throws IncorrectParametersException {
        if (to.equals(this.from)){
            throw new IncorrectParametersException("Same currency codes");
        }
        this.to = to;
    }
}
