package com.github.mukhlisov.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mukhlisov.exceptions.CurrencyNotFoundException;
import com.github.mukhlisov.exceptions.DataBaseOperationException;
import com.github.mukhlisov.exceptions.IncorrectParametersException;
import com.github.mukhlisov.exceptions.RateNotFoundException;
import com.github.mukhlisov.models.dto.ExchangeParameters;
import com.github.mukhlisov.models.dto.ExchangeResult;
import com.github.mukhlisov.services.ExchangeService;
import com.github.mukhlisov.utils.RequestParser;
import com.github.mukhlisov.utils.ResponseWriter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = "/exchange")
public class ExchangeServlet extends HttpServlet {

    private static final String EXCHANGE_FAILED = "{\"message\":\"Ошибка во время обмена\"}";
    private static final String EXCHANGE_RATE_NOT_FOUND = "{\"message\":\"Курс не найден\"}";

    private final ExchangeService exchangeService;
    private final ObjectMapper objectMapper;

    public ExchangeServlet(){
        this.exchangeService = new ExchangeService();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        ExchangeParameters exchangeParameters = RequestParser.getExchangeParameters(request);
        try{
            ExchangeResult exchangeResult = exchangeService.exchange(exchangeParameters);

            ResponseWriter.write(response, objectMapper.writeValueAsString(exchangeResult), HttpServletResponse.SC_OK);
        } catch (DataBaseOperationException | JsonProcessingException e) {
            ResponseWriter.write(response, EXCHANGE_FAILED, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IncorrectParametersException e){
            ResponseWriter.write(response, EXCHANGE_FAILED, HttpServletResponse.SC_BAD_REQUEST);
        } catch (CurrencyNotFoundException | RateNotFoundException e){
            ResponseWriter.write(response, EXCHANGE_RATE_NOT_FOUND, HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
