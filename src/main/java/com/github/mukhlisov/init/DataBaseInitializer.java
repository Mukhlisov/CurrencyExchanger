package com.github.mukhlisov.init;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener
public class DataBaseInitializer implements ServletContextListener {

    private static final String CREATE_TABLE_CURRENCIES = """
            CREATE TABLE IF NOT EXISTS currencies (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            code TEXT UNIQUE NOT NULL,
            full_name TEXT NOT NULL,
            sign TEXT NOT NULL);
            """;

    private static final String CREATE_TABLE_EXCHANGE_RATES = """
            CREATE TABLE IF NOT EXISTS exchange_rates (
                id INTEGER PRIMARY KEY AUTOINCREMENT ,
                base_currency_id INTEGER NOT NULL,
                target_currency_id INTEGER NOT NULL,
                rate REAL NOT NULL,
                UNIQUE (base_currency_id, target_currency_id),
                FOREIGN KEY (base_currency_id) REFERENCES currencies (id),
                FOREIGN KEY (target_currency_id) REFERENCES currencies (id)
            );
            """;

    private static final String URL = "jdbc:sqlite:currency_exchange.db";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(URL);
                 Statement statement = connection.createStatement()){

                statement.execute(CREATE_TABLE_CURRENCIES);
                statement.execute(CREATE_TABLE_EXCHANGE_RATES);
            }
        }catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Could not initialize database", e);
        }
    }
}
