package com.github.mukhlisov.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mukhlisov.exceptions.*;
import com.github.mukhlisov.models.dto.ExchangeCode;
import com.github.mukhlisov.models.dto.exchangeRate.CreateExchangeRateDto;
import com.github.mukhlisov.models.dto.exchangeRate.ExchangeRateOutputDto;
import com.github.mukhlisov.models.dto.exchangeRate.UpdateExchangeRateDto;
import com.github.mukhlisov.services.ExchangeRateService;
import com.github.mukhlisov.services.interfaces.IExchangeRateService;
import com.github.mukhlisov.utils.RequestParser;
import com.github.mukhlisov.utils.ResponseWriter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/exchange-rates/*")
public class ExchangeRatesServlet extends HttpServlet {

    private static final String EXCHANGE_RATE_WAS_NOT_FOUND = "{\"message\":\"Курс обмена не найден\"}";
    private static final String EXCHANGE_RATE_WAS_NOT_CREATED = "{\"message\":\"Курс обмена не был создан\"}";
    private static final String EXCHANGE_RATE_WAS_NOT_CHANGED = "{\"message\":\"Курс обмена не был обновлен\"}";

    private final IExchangeRateService exchangeRateService;
    private final ObjectMapper objectMapper;

    public ExchangeRatesServlet() {
        objectMapper = new ObjectMapper();
        exchangeRateService = new ExchangeRateService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try{
            String code = RequestParser.getCodeFromRequestUri(request.getRequestURI());
            String json;

            if (code == null){
                json = objectMapper.writeValueAsString(exchangeRateService.getAllExchangeRates());
            } else {
                ExchangeCode exchangeCode = new ExchangeCode(code);
                json = objectMapper.writeValueAsString(exchangeRateService.getExchangeRate(exchangeCode));
            }

            ResponseWriter.write(response, json, HttpServletResponse.SC_OK);
        } catch (EmptyURIVariableException | IncorrectParametersException e){
            ResponseWriter.write(response, EXCHANGE_RATE_WAS_NOT_FOUND, HttpServletResponse.SC_BAD_REQUEST);
        } catch (CurrencyNotFoundException | RateNotFoundException e){
            ResponseWriter.write(response, EXCHANGE_RATE_WAS_NOT_FOUND, HttpServletResponse.SC_NOT_FOUND);
        } catch (DataBaseOperationException | JsonProcessingException e) {
            ResponseWriter.write(response, EXCHANGE_RATE_WAS_NOT_FOUND, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try{
            String json = RequestParser.getJSONData(request.getInputStream());

            CreateExchangeRateDto createExchangeRateDto = objectMapper.readValue(json, CreateExchangeRateDto.class);
            ExchangeRateOutputDto exchangeRateOutputDto = exchangeRateService.saveExchangeRate(createExchangeRateDto);

            ResponseWriter.write(
                    response,
                    objectMapper.writeValueAsString(exchangeRateOutputDto),
                    HttpServletResponse.SC_CREATED
            );
        } catch (DataBaseOperationException | IOException e){
            ResponseWriter.write(response, EXCHANGE_RATE_WAS_NOT_CREATED, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (CurrencyNotFoundException | RateNotFoundException e){
            ResponseWriter.write(response, EXCHANGE_RATE_WAS_NOT_CREATED, HttpServletResponse.SC_NOT_FOUND);
        } catch (EntityInsertException e){
            ResponseWriter.write(response, EXCHANGE_RATE_WAS_NOT_CREATED, HttpServletResponse.SC_CONFLICT);
        } catch (IncorrectParametersException e){
            ResponseWriter.write(response, EXCHANGE_RATE_WAS_NOT_CREATED, HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {

        try{
            String json = RequestParser.getJSONData(request.getInputStream());
            ExchangeCode exchangeCode = new ExchangeCode(RequestParser.getCodeFromRequestUri(request.getRequestURI()));

            UpdateExchangeRateDto updateExchangeRateDto = objectMapper.readValue(json, UpdateExchangeRateDto.class);
            ExchangeRateOutputDto exchangeRateOutputDto = exchangeRateService.updateExchangeRate(exchangeCode, updateExchangeRateDto);

            ResponseWriter.write(response,
                    objectMapper.writeValueAsString(exchangeRateOutputDto),
                    HttpServletResponse.SC_OK
            );
        } catch (DataBaseOperationException | IOException e){
            ResponseWriter.write(response, EXCHANGE_RATE_WAS_NOT_CHANGED, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (EntityInsertException e){
            ResponseWriter.write(response, EXCHANGE_RATE_WAS_NOT_CHANGED, HttpServletResponse.SC_CONFLICT);
        } catch (CurrencyNotFoundException | RateNotFoundException e){
            ResponseWriter.write(response, EXCHANGE_RATE_WAS_NOT_CHANGED, HttpServletResponse.SC_NOT_FOUND);
        } catch (IncorrectParametersException e){
            ResponseWriter.write(response, EXCHANGE_RATE_WAS_NOT_CHANGED, HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
