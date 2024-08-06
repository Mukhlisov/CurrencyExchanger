package com.github.mukhlisov.services;

import com.github.mukhlisov.exceptions.*;
import com.github.mukhlisov.models.dto.currency.CreateCurrencyDto;
import com.github.mukhlisov.models.Currency;
import com.github.mukhlisov.models.dto.currency.UpdateCurrencyDto;
import com.github.mukhlisov.repositories.CurrenciesRepository;
import com.github.mukhlisov.services.interfaces.ICurrenciesService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CurrenciesService implements ICurrenciesService {

    private static final String CURRENCIES_TABLE = "currencies";

    @Override
    public List<Currency> getAllCurrencies() throws DataBaseOperationException {

        try (CurrenciesRepository currenciesRepository = new CurrenciesRepository(CURRENCIES_TABLE)) {
            return currenciesRepository.getAll();
        } catch (Exception e) {
            throw new DataBaseOperationException(e.getMessage(), e);
        }
    }

    @Override
    public Currency getCurrencyByCode(String code) throws DataBaseOperationException, CurrencyNotFoundException {
        Optional<Currency> currency;

        try (CurrenciesRepository currenciesRepository = new CurrenciesRepository(CURRENCIES_TABLE)){
            currency = currenciesRepository.getByCode(code);
        } catch (Exception e){
            throw new DataBaseOperationException(e.getMessage(), e);
        }
        return currency
                .orElseThrow(() -> new CurrencyNotFoundException("There is no currency with such code: " + code));
    }

    @Override
    public Currency saveCurrency(CreateCurrencyDto createCurrencyDto)
            throws DataBaseOperationException, EntityInsertException, IncorrectParametersException {

        try (CurrenciesRepository currenciesRepository = new CurrenciesRepository(CURRENCIES_TABLE)){
            currenciesRepository.insert(new Currency(
                    createCurrencyDto.getCode(),
                    createCurrencyDto.getFullName(),
                    createCurrencyDto.getSign()
            ));

            return currenciesRepository.getByCode(createCurrencyDto.getCode()).get();
        } catch (SQLException e){
            throw new EntityInsertException(e.getMessage(), e);
        } catch (IncorrectParametersException e) {
            throw new IncorrectParametersException(e.getMessage(), e);
        } catch (Exception e){
            throw new DataBaseOperationException(e.getMessage(), e);
        }
    }

    @Override
    public Currency updateCurrency(UpdateCurrencyDto updateCurrencyDto)
            throws DataBaseOperationException, EntityInsertException, IncorrectParametersException {

        try (CurrenciesRepository currenciesRepository = new CurrenciesRepository(CURRENCIES_TABLE)){
            return currenciesRepository.update(new Currency(
                    updateCurrencyDto.getId(),
                    updateCurrencyDto.getCode(),
                    updateCurrencyDto.getFullName(),
                    updateCurrencyDto.getSign()
            ));
        } catch (SQLException e){
            throw new EntityInsertException(e.getMessage(), e);
        } catch (IncorrectParametersException e) {
            throw new IncorrectParametersException(e.getMessage(), e);
        } catch (Exception e){
            throw new DataBaseOperationException(e.getMessage(), e);
        }
    }

    @Override
    public Currency getCurrencyById(Integer id) throws DataBaseOperationException, CurrencyNotFoundException {
        Optional<Currency> currency;

        try (CurrenciesRepository currenciesRepository = new CurrenciesRepository(CURRENCIES_TABLE)){
            currency = currenciesRepository.getById(id);
        } catch (Exception e){
            throw new DataBaseOperationException(e.getMessage(), e);
        }

        return currency.orElseThrow(
                () -> new CurrencyNotFoundException("There is no currency with such id: " + id));
    }
}
