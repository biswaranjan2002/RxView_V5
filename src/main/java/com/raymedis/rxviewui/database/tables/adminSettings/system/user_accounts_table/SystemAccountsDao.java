package com.raymedis.rxviewui.database.tables.adminSettings.system.user_accounts_table;

import com.raymedis.rxviewui.database.ConnectionManager;
import com.raymedis.rxviewui.database.GenericRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class SystemAccountsDao extends GenericRepository<SystemAccountsEntity,String> {

    private final Logger logger = LoggerFactory.getLogger(SystemAccountsDao.class);

    public SystemAccountsDao(String table_name, Map<String, String> columns, String primaryKey, RowMapper<SystemAccountsEntity, String> row_mapper, boolean isAutoIncrement, String dummyprimaryKeyValue) {
        super(table_name, columns, primaryKey, row_mapper, isAutoIncrement, dummyprimaryKeyValue);
    }


    public SystemAccountsEntity findByUserName(String userName) {
        String query = "SELECT * FROM SystemAccountsEntity WHERE userName = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();

            logger.info("resultSet: {}", resultSet);


            if (resultSet.next()) {
                SystemAccountsEntity systemAccountsEntity = new SystemAccountsEntity();
                systemAccountsEntity.setUserName(resultSet.getString("userName"));
                systemAccountsEntity.setUserId(resultSet.getString("userId"));
                systemAccountsEntity.setPassword(resultSet.getString("password"));
                systemAccountsEntity.setUserGroup(resultSet.getString("userGroup"));
                return systemAccountsEntity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public SystemAccountsEntity findByUserId(String userId) {

        String query = "SELECT * FROM SystemAccountsEntity WHERE userId = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                SystemAccountsEntity systemAccountsEntity = new SystemAccountsEntity();
                systemAccountsEntity.setUserName(resultSet.getString("userName"));
                systemAccountsEntity.setUserId(resultSet.getString("userId"));
                systemAccountsEntity.setPassword(resultSet.getString("password"));
                systemAccountsEntity.setUserGroup(resultSet.getString("userGroup"));
                return systemAccountsEntity;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return null;
    }

}
