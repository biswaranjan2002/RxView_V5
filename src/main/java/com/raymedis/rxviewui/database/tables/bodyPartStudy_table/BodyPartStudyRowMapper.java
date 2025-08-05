package com.raymedis.rxviewui.database.tables.bodyPartStudy_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class BodyPartStudyRowMapper implements GenericRepository.RowMapper<BodyPartStudyEntity, Integer> {

    @Override
    public BodyPartStudyEntity mapRow(ResultSet rs) throws SQLException {
        BodyPartStudyEntity entity = new BodyPartStudyEntity();

        entity.setId(rs.getInt("id"));
        entity.setStudyId(rs.getString("studyId"));
        entity.setBodyPartName(rs.getString("bodyPartName"));
        entity.setPosition(rs.getString("position"));
        entity.setSeriesId(rs.getString("seriesId"));
        entity.setSeriesUid(rs.getString("seriesUid"));
        entity.setInstanceId(rs.getString("instanceId"));
        entity.setInstanceUid(rs.getString("instanceUid"));
        entity.setIsRejected(rs.getBoolean("isRejected"));
        entity.setIsExposed(rs.getBoolean("isExposed"));
        entity.setWindowWidth(rs.getDouble("windowWidth"));
        entity.setWindowLevel(rs.getDouble("windowLevel"));
        entity.setKv(rs.getDouble("kv"));
        entity.setMa(rs.getDouble("ma"));
        entity.setMas(rs.getDouble("mas"));
        entity.setMs(rs.getDouble("ms"));
        entity.setExposedDate(rs.getObject("exposedDate", LocalDateTime.class));

        return entity;
    }


    @Override
    public Object getValue(BodyPartStudyEntity entity, String columnName) {
        return switch (columnName) {
            case "studyId" -> entity.getStudyId();
            case "bodyPartName" -> entity.getBodyPartName();
            case "position" -> entity.getPosition();
            case "seriesId" -> entity.getSeriesId();
            case "seriesUid" -> entity.getSeriesUid();
            case "instanceId" -> entity.getInstanceId();
            case "instanceUid" -> entity.getInstanceUid();
            case "isRejected" -> entity.getIsRejected();
            case "isExposed" -> entity.getIsExposed();
            case "windowWidth" -> entity.getWindowWidth();
            case "windowLevel" -> entity.getWindowLevel();
            case "kv" -> entity.getKv();
            case "ma" -> entity.getMa();
            case "mas" -> entity.getMas();
            case "ms" -> entity.getMs();
            case "exposedDate" -> entity.getExposedDate();
            default -> throw new IllegalArgumentException("Unknown column name: " + columnName);
        };
    }


    @Override
    public Object getPrimaryKeyValue(BodyPartStudyEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
