package com.github.mukhlisov.services.interfaces;

import com.github.mukhlisov.exceptions.DataBaseOperationException;
import com.github.mukhlisov.exceptions.EntityInsertException;
import com.github.mukhlisov.models.Currency;
import com.github.mukhlisov.models.dto.currency.CreateCurrencyDto;
import com.github.mukhlisov.models.dto.currency.UpdateCurrencyDto;

import java.util.List;

public interface ICurrenciesService {
    Currency getCurrencyById(Integer id) throws DataBaseOperationException;
    Currency getCurrencyByCode(String code) throws DataBaseOperationException;
    List<Currency> getAllCurrencies() throws DataBaseOperationException;
    Currency saveCurrency(CreateCurrencyDto createCurrencyDto) throws DataBaseOperationException, EntityInsertException;
    Currency updateCurrency(UpdateCurrencyDto updateCurrencyDto) throws DataBaseOperationException, EntityInsertException;
}
