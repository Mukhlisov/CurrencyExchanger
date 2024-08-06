package com.github.mukhlisov.models.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeParameters {
    private String from;
    private String to;
    private double amount;
}
