package com.github.mukhlisov.service;

import com.github.mukhlisov.exceptions.CouldNotConnectDataBaseException;
import com.github.mukhlisov.exceptions.CurrencyNotFoundException;
import com.github.mukhlisov.exceptions.EmptyCurrencyException;
import com.github.mukhlisov.exceptions.FailedToCloseConnectionException;
import com.github.mukhlisov.model.Currency;
import com.github.mukhlisov.repositories.CurrenciesRepository;

import java.util.List;

public class CurrenciesService {

    public List<Currency> getAllCurrencies() throws CouldNotConnectDataBaseException, FailedToCloseConnectionException {
        try (CurrenciesRepository repository = new CurrenciesRepository("currencies")){
            return repository.getAll();
        } catch (Exception e) {
            throw new FailedToCloseConnectionException(e.getMessage(), e);
        }
    }

    public Currency getCurrencyByCode(String code)
            throws CouldNotConnectDataBaseException, FailedToCloseConnectionException,
            CurrencyNotFoundException, EmptyCurrencyException {
        if (code.isEmpty()){
            throw new EmptyCurrencyException("Currency code is empty");
        }

        try (CurrenciesRepository repository = new CurrenciesRepository("currencies")){
            return repository.getByCode(code)
                    .orElseThrow(() -> new CurrencyNotFoundException("There is no currency with such code: " + code));
        } catch (Exception e){
            throw new FailedToCloseConnectionException(e.getMessage(), e);
        }
    }
}
