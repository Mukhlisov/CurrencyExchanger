package com.github.mukhlisov.services.interfaces;

import com.github.mukhlisov.exceptions.DataBaseOperationException;
import com.github.mukhlisov.exceptions.EntityInsertException;
import com.github.mukhlisov.models.dto.ExchangeCode;
import com.github.mukhlisov.models.dto.exchangeRate.CreateExchangeRateDto;
import com.github.mukhlisov.models.dto.exchangeRate.ExchangeRateOutputDto;
import com.github.mukhlisov.models.dto.exchangeRate.UpdateExchangeRateDto;

import java.util.List;

public interface IExchangeRateService {
    List<ExchangeRateOutputDto> getAllExchangeRates() throws DataBaseOperationException;
    ExchangeRateOutputDto getExchangeRate(ExchangeCode exchangeCode) throws DataBaseOperationException;
    ExchangeRateOutputDto saveExchangeRate(CreateExchangeRateDto createExchangeRateDto) throws DataBaseOperationException, EntityInsertException;
    ExchangeRateOutputDto updateExchangeRate(ExchangeCode exchangeCode, UpdateExchangeRateDto updateExchangeRateDto) throws DataBaseOperationException, EntityInsertException;
    ExchangeRateOutputDto getOrCalculateExchangeRate(ExchangeCode exchangeCode) throws DataBaseOperationException;
}
