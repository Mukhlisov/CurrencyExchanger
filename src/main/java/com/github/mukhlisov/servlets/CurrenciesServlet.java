package com.github.mukhlisov.servlets;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mukhlisov.exceptions.CouldNotConnectDataBaseException;
import com.github.mukhlisov.exceptions.CurrencyNotFoundException;
import com.github.mukhlisov.exceptions.EmptyCurrencyException;
import com.github.mukhlisov.exceptions.FailedToCloseConnectionException;
import com.github.mukhlisov.model.Currency;
import com.github.mukhlisov.repositories.CurrenciesRepository;
import com.github.mukhlisov.service.CurrenciesService;
import com.github.mukhlisov.utils.CurrencyExchangeConnector;
import com.github.mukhlisov.utils.RequestParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CurrenciesServlet extends HttpServlet {

    public CurrenciesService service;

    public CurrenciesServlet(){
        service = new CurrenciesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){

        String json = "{\"message\":\"Валюта не найдена\"}";

        try {
            ObjectMapper mapper = new ObjectMapper();
            String code = RequestParser.getCodeFromRequestUri(req.getRequestURI());

            if (code == null){
                json = mapper.writeValueAsString(service.getAllCurrencies());
            } else{
                json = mapper.writeValueAsString(service.getCurrencyByCode(code));
            }

        } catch (FailedToCloseConnectionException | CouldNotConnectDataBaseException | JsonProcessingException e){
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*try{
            Scanner scanner = new Scanner(req.getInputStream(), StandardCharsets.UTF_8);
            String jsonE = scanner.useDelimiter("\\A").next();
            scanner.close();

            ObjectMapper mapper = new ObjectMapper();
            CreateCurrency createCurrency = mapper.readValue(jsonE, CreateCurrency.class);

            CurrenciesDAO currenciesDAO = new CurrenciesDAO();
            currenciesDAO.createCurrency(createCurrency);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            currenciesDAO.closeConnection();
        } catch (RuntimeException | SQLException e) {
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }*/
    }
}
