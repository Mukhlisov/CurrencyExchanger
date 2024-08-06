package com.github.mukhlisov.services;

import com.github.mukhlisov.exceptions.CurrencyNotFoundException;
import com.github.mukhlisov.exceptions.DataBaseOperationException;
import com.github.mukhlisov.exceptions.EntityInsertException;
import com.github.mukhlisov.exceptions.RateNotFoundException;
import com.github.mukhlisov.models.Currency;
import com.github.mukhlisov.models.dto.ExchangeCode;
import com.github.mukhlisov.models.dto.exchangeRate.CreateExchangeRateDto;
import com.github.mukhlisov.models.dto.exchangeRate.ExchangeRateOutputDto;
import com.github.mukhlisov.models.ExchangeRate;
import com.github.mukhlisov.models.dto.exchangeRate.UpdateExchangeRateDto;
import com.github.mukhlisov.repositories.ExchangeRatesRepository;
import com.github.mukhlisov.services.interfaces.ICurrenciesService;
import com.github.mukhlisov.services.interfaces.IExchangeRateService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService implements IExchangeRateService {

    private static final String EXCHANGE_RATES_TABLE = "exchange_rates";

    private final ICurrenciesService currenciesService;

    public ExchangeRateService(){
        currenciesService = new CurrenciesService();
    }

    @Override
    public List<ExchangeRateOutputDto> getAllExchangeRates()
            throws DataBaseOperationException, CurrencyNotFoundException {

        List<ExchangeRateOutputDto> exchangeRates = new ArrayList<>();

        try (ExchangeRatesRepository exchangeRatesRepository = new ExchangeRatesRepository(EXCHANGE_RATES_TABLE)) {
            List<ExchangeRate> result = exchangeRatesRepository.getAll();

            for (ExchangeRate exchangeRate : result) {
                exchangeRates.add(new ExchangeRateOutputDto(
                        exchangeRate.getId(),
                        currenciesService.getCurrencyById(exchangeRate.getBaseCurrencyId()),
                        currenciesService.getCurrencyById(exchangeRate.getTargetCurrencyId()),
                        exchangeRate.getRate()
                ));
            }
        } catch (CurrencyNotFoundException e){
            throw new CurrencyNotFoundException(e.getMessage(), e);
        } catch (Exception e) {
            throw new DataBaseOperationException(e.getMessage(), e);
        }
        return exchangeRates;
    }

    @Override
    public ExchangeRateOutputDto getExchangeRate(ExchangeCode exchangeCode)
            throws DataBaseOperationException, RateNotFoundException, CurrencyNotFoundException {

        Optional<ExchangeRate> result;
        Currency baseCurrency;
        Currency targetCurrency;
        try (ExchangeRatesRepository exchangeRatesRepository = new ExchangeRatesRepository(EXCHANGE_RATES_TABLE)) {
            baseCurrency = currenciesService.getCurrencyByCode(exchangeCode.getBaseCode());
            targetCurrency = currenciesService.getCurrencyByCode(exchangeCode.getTargetCode());

            result = exchangeRatesRepository.getByBaseAndTarget(baseCurrency.getId(), targetCurrency.getId());
        } catch (CurrencyNotFoundException e){
            throw new CurrencyNotFoundException(e.getMessage(), e);
        } catch (Exception e) {
            throw new DataBaseOperationException(e.getMessage(), e);
        }
        ExchangeRate exchangeRate = result
                .orElseThrow(() -> new RateNotFoundException("There is no rate between this currencies"));

        return new ExchangeRateOutputDto(
                exchangeRate.getId(),
                baseCurrency,
                targetCurrency,
                exchangeRate.getRate()
        );
    }

    @Override
    public ExchangeRateOutputDto saveExchangeRate(CreateExchangeRateDto exchangeRateDto)
            throws DataBaseOperationException, CurrencyNotFoundException, EntityInsertException, RateNotFoundException {

        Optional<ExchangeRate> result;
        Currency baseCurrency;
        Currency targetCurrency;
        try (ExchangeRatesRepository exchangeRatesRepository = new ExchangeRatesRepository(EXCHANGE_RATES_TABLE)){
            baseCurrency = currenciesService.getCurrencyByCode(exchangeRateDto.getBaseCurrencyCode());
            targetCurrency = currenciesService.getCurrencyByCode(exchangeRateDto.getTargetCurrencyCode());

            exchangeRatesRepository.insert(new ExchangeRate(
                    baseCurrency.getId(),
                    targetCurrency.getId(),
                    exchangeRateDto.getRate()
            ));

            result = exchangeRatesRepository.getByBaseAndTarget(baseCurrency.getId(), targetCurrency.getId());
        } catch (SQLException e){
            throw new EntityInsertException(e.getMessage(), e);
        } catch (CurrencyNotFoundException e){
            throw new CurrencyNotFoundException(e.getMessage(), e);
        } catch (Exception e){
            throw new DataBaseOperationException(e.getMessage(), e);
        }
        ExchangeRate exchangeRate = result
                .orElseThrow(() -> new RateNotFoundException("There is no rate between this currencies"));

        return new ExchangeRateOutputDto(
                exchangeRate.getId(),
                baseCurrency,
                targetCurrency,
                exchangeRate.getRate()
        );
    }

    @Override
    public ExchangeRateOutputDto updateExchangeRate(ExchangeCode exchangeCode, UpdateExchangeRateDto updateExchangeRateDto)
            throws DataBaseOperationException, EntityInsertException, CurrencyNotFoundException, RateNotFoundException {

        Optional<ExchangeRate> result;
        Currency baseCurrency;
        Currency targetCurrency;
        try (ExchangeRatesRepository exchangeRatesRepository = new ExchangeRatesRepository(EXCHANGE_RATES_TABLE)){
            baseCurrency = currenciesService.getCurrencyByCode(exchangeCode.getBaseCode());
            targetCurrency = currenciesService.getCurrencyByCode(exchangeCode.getTargetCode());

            exchangeRatesRepository.updateByBaseAndTarget(
                    baseCurrency.getId(),
                    targetCurrency.getId(),
                    updateExchangeRateDto.getRate()
            );

            result = exchangeRatesRepository.getByBaseAndTarget(baseCurrency.getId(), targetCurrency.getId());
        } catch (SQLException e){
            throw new EntityInsertException(e.getMessage(), e);
        } catch (CurrencyNotFoundException e){
            throw new CurrencyNotFoundException(e.getMessage(), e);
        } catch (Exception e){
            throw new DataBaseOperationException(e.getMessage(), e);
        }
        ExchangeRate exchangeRate = result
                .orElseThrow(() -> new RateNotFoundException("There is no rate between this currencies"));

        return new ExchangeRateOutputDto(
                exchangeRate.getId(),
                baseCurrency,
                targetCurrency,
                exchangeRate.getRate()
        );
    }

    public ExchangeRateOutputDto getOrCalculateExchangeRate(ExchangeCode exchangeCode)
            throws DataBaseOperationException, RateNotFoundException, CurrencyNotFoundException {

        try{
            return getExchangeRate(exchangeCode);
        } catch (RateNotFoundException e1){
            try{
                ExchangeRateOutputDto result = getExchangeRate(
                        new ExchangeCode(exchangeCode.getTargetCode(), exchangeCode.getBaseCode())
                );

                return new ExchangeRateOutputDto(
                        0,
                        result.getTargetCurrency(),
                        result.getBaseCurrency(),
                        Math.round(1000000.0 / result.getRate()) / 1000000.0
                );
            } catch (RateNotFoundException e2){
                throw new RateNotFoundException("Can't find exchange rate for target code");
            }
        }
    }
}
