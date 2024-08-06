package com.github.mukhlisov.services;

import com.github.mukhlisov.exceptions.CurrencyNotFoundException;
import com.github.mukhlisov.exceptions.DataBaseOperationException;
import com.github.mukhlisov.exceptions.IncorrectParametersException;
import com.github.mukhlisov.exceptions.RateNotFoundException;
import com.github.mukhlisov.models.dto.ExchangeCode;
import com.github.mukhlisov.models.dto.ExchangeParameters;
import com.github.mukhlisov.models.dto.ExchangeResult;
import com.github.mukhlisov.models.dto.exchangeRate.ExchangeRateOutputDto;
import com.github.mukhlisov.services.interfaces.IExchangeRateService;

public class ExchangeService {

    private final IExchangeRateService exchangeRateService;

    public ExchangeService(){
        this.exchangeRateService = new ExchangeRateService();
    }

    private ExchangeRateOutputDto getCalculatedExchangeRate(ExchangeParameters parameters) throws DataBaseOperationException {
        try{
            ExchangeRateOutputDto baseToUSD = exchangeRateService
                    .getOrCalculateExchangeRate(new ExchangeCode(parameters.getFrom(), "USD"));

            ExchangeRateOutputDto usdToTarget = exchangeRateService
                    .getOrCalculateExchangeRate(new ExchangeCode("USD", parameters.getTo()));

            return new ExchangeRateOutputDto(
                    0,
                    baseToUSD.getBaseCurrency(),
                    usdToTarget.getTargetCurrency(),
                    baseToUSD.getRate() * usdToTarget.getRate()
            );
        } catch (RateNotFoundException e2){
            throw new RateNotFoundException(e2.getMessage(), e2);
        }
    }

    public ExchangeResult exchange(ExchangeParameters parameters)
            throws DataBaseOperationException, RateNotFoundException, CurrencyNotFoundException, IncorrectParametersException {

        ExchangeRateOutputDto exchangeRateOutputDto;
        try{
            exchangeRateOutputDto = exchangeRateService
                    .getOrCalculateExchangeRate(new ExchangeCode(parameters.getFrom(), parameters.getTo()));
        } catch (RateNotFoundException e1) {
            if (parameters.getFrom().equals("USD") || parameters.getTo().equals("USD")) {
                throw new RateNotFoundException(e1.getMessage(), e1);
            }
            exchangeRateOutputDto = getCalculatedExchangeRate(parameters);
        }

        return new ExchangeResult(
                exchangeRateOutputDto.getBaseCurrency(),
                exchangeRateOutputDto.getTargetCurrency(),
                Math.round(exchangeRateOutputDto.getRate() * 1000000.0)/1000000.0,
                parameters.getAmount(),
                Math.round(parameters.getAmount() * exchangeRateOutputDto.getRate() * 100.0) / 100.0);
    }
}
