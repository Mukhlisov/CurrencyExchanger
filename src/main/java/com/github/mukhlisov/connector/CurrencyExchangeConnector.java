package com.github.mukhlisov.connector;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Getter
public class CurrencyExchangeConnector {

    private final static String URL = "jdbc:sqlite:currency_exchange.db";
    private final Connection connection;

    public CurrencyExchangeConnector() throws SQLException {
        this.connection = DriverManager.getConnection(URL);
    }

    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    public void closeStatement(Statement statement) throws SQLException {
        if (statement != null) {
            statement.close();
        }
    }


}
