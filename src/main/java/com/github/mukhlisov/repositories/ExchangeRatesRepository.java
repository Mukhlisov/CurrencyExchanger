package com.github.mukhlisov.repositories;

import com.github.mukhlisov.exceptions.CouldNotConnectDataBaseException;
import com.github.mukhlisov.models.ExchangeRate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ExchangeRatesRepository extends SQLiteRepository<Integer, ExchangeRate>{

    public ExchangeRatesRepository(String table) throws CouldNotConnectDataBaseException {
        super(table);
    }

    @Override
    protected ExchangeRate castToEntity(ResultSet result) throws SQLException {
        return new ExchangeRate(
                result.getInt("id"),
                result.getInt("base_currency_id"),
                result.getInt("target_currency_id"),
                result.getDouble("rate")
        );
    }

    @Override
    protected String getPlaceHolders() {
        return "(?, ?, ?)";
    }

    @Override
    protected String getColumns() {
        return "(base_currency_id, target_currency_id, rate)";
    }

    @Override
    protected String getUpdateColumns() {
        return "base_currency_id = ?, target_currency_id = ?, rate = ?";
    }

    @Override
    protected String getEntityId(ExchangeRate entity) {
        return entity.getId().toString();
    }

    @Override
    protected void setInsertValues(PreparedStatement preparedStatement, ExchangeRate entity) throws SQLException {
        preparedStatement.setInt(1, entity.getBaseCurrencyId());
        preparedStatement.setInt(2, entity.getTargetCurrencyId());
        preparedStatement.setDouble(3, entity.getRate());
    }

    @Override
    protected void setUpdateValues(PreparedStatement preparedStatement, ExchangeRate entity) throws SQLException {
        preparedStatement.setInt(1, entity.getBaseCurrencyId());
        preparedStatement.setInt(2, entity.getTargetCurrencyId());
        preparedStatement.setDouble(3, entity.getRate());
        preparedStatement.setInt(4, entity.getId());
    }

    public Optional<ExchangeRate> getByBaseAndTarget(Integer baseId, Integer targetId)
            throws SQLException, CouldNotConnectDataBaseException {

        String sql = "SELECT * FROM exchange_rates WHERE base_currency_id = ? AND target_currency_id = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setInt(1, baseId);
        preparedStatement.setInt(2, targetId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return Optional.of(castToEntity(resultSet));
        }
        return Optional.empty();
    }
}
