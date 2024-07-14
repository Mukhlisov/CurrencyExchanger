package com.github.mukhlisov.servlets;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mukhlisov.dao.CurrenciesDAO;
import com.github.mukhlisov.dto.CreateCurrency;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class CurrenciesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = resp.getWriter()){
            CurrenciesDAO currenciesDAO = new CurrenciesDAO();
            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(currenciesDAO.getCurrencies());
            resp.setStatus(HttpServletResponse.SC_OK);
            writer.write(json);
        }catch (Exception e){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        try{
            Scanner scanner = new Scanner(req.getInputStream(), StandardCharsets.UTF_8);
            String json = scanner.useDelimiter("\\A").next();
            scanner.close();

            ObjectMapper mapper = new ObjectMapper();
            CreateCurrency createCurrency = mapper.readValue(json, CreateCurrency.class);

            CurrenciesDAO currenciesDAO = new CurrenciesDAO();
            currenciesDAO.createCurrency(createCurrency);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            writer.write(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
