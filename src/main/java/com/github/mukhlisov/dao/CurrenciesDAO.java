package com.github.mukhlisov.dao;

import com.github.mukhlisov.dto.CreateCurrency;
import com.github.mukhlisov.dto.Currency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesDAO {

    /*private final CurrencyExchangeConnector connector;

    public CurrenciesDAO() throws SQLException {
        this.connector = new CurrencyExchangeConnector();
    }

    public Currency getCurrencyByCode(String code) {
        Currency currency = null;
        try (Statement = connector.createStatement()){
            statement.executeUpdate("select * from currencies;");
        } catch (SQLException e){
            e.printStackTrace();
        }
        return currency;
    }*/

    private static final String URL = "jdbc:sqlite:currency_exchange.db";
    private final Connection connection;

    public CurrenciesDAO() throws SQLException {
        this.connection = DriverManager.getConnection(URL);
    }

    public List<Currency> getCurrencies() throws RuntimeException {
        String sql = "SELECT * FROM currencies";

        List<Currency> currencies = new ArrayList<>();

        try (Statement statement = this.connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                currencies.add(new Currency(resultSet.getInt("id"),
                        resultSet.getString("code"),
                        resultSet.getString("full_name"),
                        resultSet.getString("sign")));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return currencies;
    }

    public void createCurrency(CreateCurrency createCurrency) throws RuntimeException {
        String sql = "INSERT INTO currencies(code, full_name, sign) VALUES(?, ?, ?)";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)){
            statement.setString(1, createCurrency.getCode());
            statement.setString(2, createCurrency.getFull_name());
            statement.setString(3, createCurrency.getSign());
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
