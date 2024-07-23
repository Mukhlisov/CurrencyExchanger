package com.github.mukhlisov.servlets;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mukhlisov.exceptions.CurrencyNotFoundException;
import com.github.mukhlisov.exceptions.EmptyCurrencyException;
import com.github.mukhlisov.exceptions.DataBaseOperationException;
import com.github.mukhlisov.model.dto.CreateCurrencyDto;
import com.github.mukhlisov.service.CurrenciesService;
import com.github.mukhlisov.utils.RequestParser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/currencies/*")
public class CurrenciesServlet extends HttpServlet {

    private final CurrenciesService currenciesService;
    private final ObjectMapper objectMapper;

    public CurrenciesServlet(){
        currenciesService = new CurrenciesService();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){

        String json = "{\"message\":\"Валюта не найдена\"}";

        try {
            String code = RequestParser.getCodeFromRequestUri(req.getRequestURI());

            if (code == null){
                json = objectMapper.writeValueAsString(currenciesService.getAllCurrencies());
            } else{
                json = objectMapper.writeValueAsString(currenciesService.getCurrencyByCode(code));
            }

        } catch (DataBaseOperationException | JsonProcessingException e){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (EmptyCurrencyException e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (CurrencyNotFoundException e){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } finally {
            try (PrintWriter writer = resp.getWriter()){
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                writer.print(json);
            } catch (IOException e){
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try{
            String json = RequestParser.getJSONData(req.getInputStream());

            CreateCurrencyDto createCurrencyDto = objectMapper.readValue(json, CreateCurrencyDto.class);
            currenciesService.saveCurrency(createCurrencyDto);

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (DataBaseOperationException | IOException e){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


}
