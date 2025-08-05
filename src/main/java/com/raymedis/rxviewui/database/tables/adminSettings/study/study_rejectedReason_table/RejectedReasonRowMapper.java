package com.raymedis.rxviewui.database.tables.adminSettings.study.study_rejectedReason_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RejectedReasonRowMapper implements GenericRepository.RowMapper<RejectedReasonEntity, String>{

    @Override
    public RejectedReasonEntity mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String rejectedReason = rs.getString("RejectedReason");

        return new RejectedReasonEntity(id,rejectedReason);
    }


    @Override
    public Object getValue(RejectedReasonEntity entity, String columnName) {
        if (entity == null || columnName == null) {
            return null;
        }

        if (columnName.equals("id")) {
            return entity.getId();
        }
        return null;
    }


    @Override
    public Object getPrimaryKeyValue(RejectedReasonEntity entity, String primaryKeyName) {
        return entity.getRejectedReason();
    }
}
