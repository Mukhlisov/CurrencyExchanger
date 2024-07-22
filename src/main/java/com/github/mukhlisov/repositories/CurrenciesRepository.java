package com.github.mukhlisov.repositories;

import com.github.mukhlisov.model.Currency;
import com.github.mukhlisov.exceptions.CouldNotConnectDataBaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CurrenciesRepository extends SQLiteRepository<Integer, Currency> {

    public CurrenciesRepository(String table) throws CouldNotConnectDataBaseException {
        super(table);
    }

    @Override
    protected Currency castToEntity(ResultSet result) throws SQLException {
        return new Currency(
                result.getInt("id"),
                result.getString("code"),
                result.getString("full_name"),
                result.getString("sign")
        );
    }

    @Override
    protected String getPlaceHolders() {
        return "(?, ?, ?)";
    }

    @Override
    protected String getColumns() {
        return "(code, full_name, sign)";
    }

    @Override
    protected String getUpdateColumns() {
        return "code = ?, full_name = ?, sign = ?";
    }

    @Override
    protected String getEntityId(Currency entity) {
        return entity.getId().toString();
    }

    @Override
    protected void setInsertValues(PreparedStatement preparedStatement, Currency entity) throws SQLException {
        preparedStatement.setString(1, entity.getCode());
        preparedStatement.setString(2, entity.getFullName());
        preparedStatement.setString(3, entity.getSign());
    }

    @Override
    protected void setUpdateValues(PreparedStatement preparedStatement, Currency entity) throws SQLException {
        preparedStatement.setString(1, entity.getCode());
        preparedStatement.setString(2, entity.getFullName());
        preparedStatement.setString(3, entity.getSign());
        preparedStatement.setString(4, entity.getId().toString());
    }

    public Optional<Currency> getByCode(String code) throws SQLException {
        String sql = "SELECT * FROM currencies WHERE code = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setString(1, code);
        ResultSet result = preparedStatement.executeQuery();
        return Optional.of(castToEntity(result));
    }
}
