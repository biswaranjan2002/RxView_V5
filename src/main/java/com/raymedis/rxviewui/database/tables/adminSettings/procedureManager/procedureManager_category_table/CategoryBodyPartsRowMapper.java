package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_category_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryBodyPartsRowMapper implements GenericRepository.RowMapper<CategoryBodyPartsEntity, String> {

    private static CategoryBodyPartsRowMapper innstance = new CategoryBodyPartsRowMapper();
    public static CategoryBodyPartsRowMapper getInstance(){
        return innstance;
    }

    @Override
    public CategoryBodyPartsEntity mapRow(ResultSet rs) throws SQLException {
        CategoryBodyPartsEntity bodyPartsEntity = new CategoryBodyPartsEntity();

        bodyPartsEntity.setBodyPartName(rs.getString("bodyPartName"));

        bodyPartsEntity.setMaleIsSelected(rs.getBoolean("maleIsSelected"));
        bodyPartsEntity.setMaleXPos(rs.getDouble("maleXPos"));
        bodyPartsEntity.setMaleYPos(rs.getDouble("maleYPos"));

        bodyPartsEntity.setFemaleIsSelected(rs.getBoolean("femaleIsSelected"));
        bodyPartsEntity.setFemaleXPos(rs.getDouble("femaleXPos"));
        bodyPartsEntity.setFemaleYPos(rs.getDouble("femaleYPos"));

        bodyPartsEntity.setInfantIsSelected(rs.getBoolean("infantIsSelected"));
        bodyPartsEntity.setInfantXPos(rs.getDouble("infantXPos"));
        bodyPartsEntity.setInfantYPos(rs.getDouble("infantYPos"));
        bodyPartsEntity.setIsStepExist(rs.getBoolean("isStepExist"));
        return bodyPartsEntity;
    }

    @Override
    public Object getValue(CategoryBodyPartsEntity entity, String columnName) {
        switch (columnName) {
            case "maleIsSelected":
                return entity.getMaleIsSelected();
            case "maleXPos":
                return entity.getMaleXPos();
            case "maleYPos":
                return entity.getMaleYPos();
            case "femaleIsSelected":
                return entity.getFemaleIsSelected();
            case "femaleXPos":
                return entity.getFemaleXPos();
            case "femaleYPos":
                return entity.getFemaleYPos();
            case "infantIsSelected":
                return entity.getInfantIsSelected();
            case "infantXPos":
                return entity.getInfantXPos();
            case "infantYPos":
                return entity.getInfantYPos();
            case "isStepExist":
                return entity.getIsStepExist();

            default:
                return null;
        }
    }

    @Override
    public Object getPrimaryKeyValue(CategoryBodyPartsEntity entity, String primaryKeyName) {
        return entity.getBodyPartName() == null ? "" : entity.getBodyPartName();
    }

}
