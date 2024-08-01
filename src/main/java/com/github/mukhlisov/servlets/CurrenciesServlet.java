package com.github.mukhlisov.servlets;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mukhlisov.exceptions.*;
import com.github.mukhlisov.models.dto.currency.CreateCurrencyDto;
import com.github.mukhlisov.models.dto.currency.UpdateCurrencyDto;
import com.github.mukhlisov.services.CurrenciesService;
import com.github.mukhlisov.utils.RequestParser;
import com.github.mukhlisov.utils.ResponseWriter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/currencies/*")
public class CurrenciesServlet extends HttpServlet {

    private static final String CURRENCY_WAS_NOT_FOUND = "{\"message\":\"Валюта не найдена\"}";
    private static final String CURRENCY_WAS_NOT_CREATED = "{\"message\":\"Валюта не была создана\"}";
    private static final String CURRENCY_WAS_NOT_CHANGED = "{\"message\":\"Валюта не была обновлена\"}";

    private final CurrenciesService currenciesService;
    private final ObjectMapper objectMapper;

    public CurrenciesServlet(){
        currenciesService = new CurrenciesService();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            String json;
            String code = RequestParser.getCodeFromRequestUri(request.getRequestURI());

            if (code == null){
                json = objectMapper.writeValueAsString(currenciesService.getAllCurrencies());
            } else{
                json = objectMapper.writeValueAsString(currenciesService.getCurrencyByCode(code));
            }

            ResponseWriter.write(response, json, HttpServletResponse.SC_OK);
        } catch (DataBaseOperationException | JsonProcessingException e){
            ResponseWriter.write(response, CURRENCY_WAS_NOT_FOUND, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (EmptyURIVariableException e){
            ResponseWriter.write(response, CURRENCY_WAS_NOT_FOUND, HttpServletResponse.SC_BAD_REQUEST);
        } catch (CurrencyNotFoundException e) {
            ResponseWriter.write(response, CURRENCY_WAS_NOT_FOUND, HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try{
            String json = RequestParser.getJSONData(request.getInputStream());

            CreateCurrencyDto createCurrencyDto = objectMapper.readValue(json, CreateCurrencyDto.class);
            currenciesService.saveCurrency(createCurrencyDto);

            ResponseWriter.write(response,
                    objectMapper.writeValueAsString(
                            currenciesService.getCurrencyByCode(createCurrencyDto.getCode())
                    ),
                    HttpServletResponse.SC_CREATED);

        } catch (DataBaseOperationException | IOException e){
            ResponseWriter.write(response, CURRENCY_WAS_NOT_CREATED, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (EntityInsertException e){
            ResponseWriter.write(response, CURRENCY_WAS_NOT_CREATED, HttpServletResponse.SC_CONFLICT);
        } catch (IncorrectParametersException e){
            ResponseWriter.write(response, CURRENCY_WAS_NOT_CREATED, HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response){
        try{
            String json = RequestParser.getJSONData(request.getInputStream());

            UpdateCurrencyDto updateCurrencyDto = objectMapper.readValue(json, UpdateCurrencyDto.class);
            currenciesService.updateCurrency(updateCurrencyDto);

            ResponseWriter.write(response, json, HttpServletResponse.SC_OK);
        } catch (DataBaseOperationException | IOException e){
            ResponseWriter.write(response, CURRENCY_WAS_NOT_CHANGED, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (EntityInsertException e){
            ResponseWriter.write(response, CURRENCY_WAS_NOT_CHANGED, HttpServletResponse.SC_CONFLICT);
        } catch (IncorrectParametersException e){
            ResponseWriter.write(response, CURRENCY_WAS_NOT_CHANGED, HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
