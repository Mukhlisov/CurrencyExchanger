package com.github.mukhlisov.service;

import com.github.mukhlisov.exceptions.CurrencyNotFoundException;
import com.github.mukhlisov.exceptions.DataBaseOperationException;
import com.github.mukhlisov.exceptions.EntityInsertException;
import com.github.mukhlisov.exceptions.RateNotFoundException;
import com.github.mukhlisov.model.Currency;
import com.github.mukhlisov.model.ExchangeCode;
import com.github.mukhlisov.model.dto.exchangeRate.CreateExchangeRateDto;
import com.github.mukhlisov.model.dto.exchangeRate.ExchangeRateOutputDto;
import com.github.mukhlisov.model.ExchangeRate;
import com.github.mukhlisov.repositories.ExchangeRatesRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateService {

    private static final String EXCHANGE_RATES_TABLE = "exchange_rates";

    private final CurrenciesService currenciesService;

    public ExchangeRateService(){
        currenciesService = new CurrenciesService();
    }

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

    public ExchangeRateOutputDto getExchangeRate(ExchangeCode code)
            throws DataBaseOperationException, RateNotFoundException {

        try (ExchangeRatesRepository exchangeRatesRepository = new ExchangeRatesRepository(EXCHANGE_RATES_TABLE)) {
            Currency baseCurrency = currenciesService.getCurrencyByCode(code.getBaseCode());
            Currency targetCurrency = currenciesService.getCurrencyByCode(code.getTargetCode());

            //Проверить на Throw. Репозиторий вроде как не должен закрыться и будет выброшен 500 HTTP.
            ExchangeRate exchangeRate = exchangeRatesRepository.getByBaseAndTarget(
                            baseCurrency.getId(),
                            targetCurrency.getId()
                    )
                    .orElseThrow(() -> new RateNotFoundException("There is no rate between this currencies"));

            return new ExchangeRateOutputDto(
                    exchangeRate.getId(),
                    baseCurrency,
                    targetCurrency,
                    exchangeRate.getRate()
            );
        } catch (Exception e) {
            throw new DataBaseOperationException(e.getMessage(), e);
        }
    }

    public void saveExchangeRate(CreateExchangeRateDto exchangeRateDto)
            throws DataBaseOperationException, CurrencyNotFoundException, EntityInsertException {

        try (ExchangeRatesRepository exchangeRatesRepository = new ExchangeRatesRepository(EXCHANGE_RATES_TABLE)){
            Currency baseCurrency = currenciesService.getCurrencyByCode(exchangeRateDto.getBaseCurrencyCode());
            Currency targetCurrency = currenciesService.getCurrencyByCode(exchangeRateDto.getTargetCurrencyCode());

            exchangeRatesRepository.insert(new ExchangeRate(
                    baseCurrency.getId(),
                    targetCurrency.getId(),
                    exchangeRateDto.getRate()
            ));
        } catch (SQLException e){
            throw new EntityInsertException(e.getMessage(), e);
        } catch (CurrencyNotFoundException e){
            throw new CurrencyNotFoundException(e.getMessage(), e);
        } catch (Exception e){
            throw new DataBaseOperationException(e.getMessage(), e);
        }
    }
}
