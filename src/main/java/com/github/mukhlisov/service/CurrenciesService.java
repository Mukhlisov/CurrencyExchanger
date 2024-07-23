package com.github.mukhlisov.service;

import com.github.mukhlisov.exceptions.CurrencyNotFoundException;
import com.github.mukhlisov.exceptions.DataBaseOperationException;
import com.github.mukhlisov.model.CreateCurrency;
import com.github.mukhlisov.model.Currency;
import com.github.mukhlisov.repositories.CurrenciesRepository;

import java.util.List;
import java.util.Optional;

public class CurrenciesService {

    private static final String CURRENCIES_TABLE = "currencies";

    public List<Currency> getAllCurrencies() throws DataBaseOperationException {
        try (CurrenciesRepository repository = new CurrenciesRepository(CURRENCIES_TABLE)){
            return repository.getAll();
        } catch (Exception e) {
            throw new DataBaseOperationException(e.getMessage(), e);
        }
    }

    public Currency getCurrencyByCode(String code) throws DataBaseOperationException, CurrencyNotFoundException {
        Optional<Currency> currency;

        try (CurrenciesRepository repository = new CurrenciesRepository(CURRENCIES_TABLE)){
            currency = repository.getByCode(code);
        } catch (Exception e){
            throw new DataBaseOperationException(e.getMessage(), e);
        }
        return currency
                .orElseThrow(() -> new CurrencyNotFoundException("There is no currency with such code: " + code));
    }

    public void saveCurrency(CreateCurrency createCurrency) throws DataBaseOperationException {
        try (CurrenciesRepository currenciesRepository = new CurrenciesRepository(CURRENCIES_TABLE)){
            currenciesRepository.insert(new Currency(
                    createCurrency.getCode(),
                    createCurrency.getFullName(),
                    createCurrency.getSign()
            ));
        } catch (Exception e){
            throw new DataBaseOperationException(e.getMessage(), e);
        }
    }
}
