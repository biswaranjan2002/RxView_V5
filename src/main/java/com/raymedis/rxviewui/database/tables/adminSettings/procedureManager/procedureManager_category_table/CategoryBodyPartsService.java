package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_category_table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryBodyPartsService {


    public static CategoryBodyPartsService instance =new CategoryBodyPartsService();
    public static CategoryBodyPartsService getInstance(){
        return instance;
    }

    private final CategoryBodyPartsDao categoryDao;

    public CategoryBodyPartsService(){
        Map<String, String> columns = new HashMap<>();

        columns.put("maleIsSelected", "BIT NOT NULL");
        columns.put("femaleIsSelected", "BIT NOT NULL");
        columns.put("infantIsSelected", "BIT NOT NULL");

        columns.put("maleXPos", "DOUBLE");
        columns.put("maleYPos", "DOUBLE");
        columns.put("femaleXPos", "DOUBLE");
        columns.put("femaleYPos", "DOUBLE");
        columns.put("infantXPos", "DOUBLE");
        columns.put("infantYPos", "DOUBLE");
        columns.put("isStepExist", "BIT NOT NULL");
        categoryDao = new CategoryBodyPartsDao("BodyPartsEntity",columns,"bodyPartName", new CategoryBodyPartsRowMapper(),false, "");
    }

    public void saveBodyPart(CategoryBodyPartsEntity bodyPartsEntity) throws SQLException {
        categoryDao.save(bodyPartsEntity);
    }


    public void deleteBodyPart(String val){
        try {
            categoryDao.deleteByPrimaryKey(val);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateBodyPart(String bodyPartName, CategoryBodyPartsEntity bodyPartsEntity) throws SQLException {
        categoryDao.update(bodyPartName, bodyPartsEntity);
    }



    public ArrayList<CategoryBodyPartsEntity> getAllBodyParts(){
        try {
            return (ArrayList<CategoryBodyPartsEntity>) categoryDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<CategoryBodyPartsEntity> selectedBodyParts = new ArrayList<>();
    public ArrayList<CategoryBodyPartsEntity> getAllSelectedBodyParts(String gender){
        selectedBodyParts.clear();
        try {
            for (CategoryBodyPartsEntity c : categoryDao.findAll()){
                switch (gender){
                    case "male":
                        if(c.getMaleIsSelected().equals(true)){
                            selectedBodyParts.add(c);
                        }
                        break;
                    case "female":
                        if(c.getFemaleIsSelected().equals(true)){
                            selectedBodyParts.add(c);
                        }
                        break;
                    case "infant":
                        if(c.getInfantIsSelected().equals(true)){
                            selectedBodyParts.add(c);
                        }
                        break;
                    default:
                        System.out.println("Unknown Gender : " + gender);
                }

            }
            return (ArrayList<CategoryBodyPartsEntity>) selectedBodyParts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CategoryBodyPartsEntity getCategoryBodyPartByName(String bodyPart){
        return categoryDao.findByBodyPartName(bodyPart);
    }

    public ArrayList<CategoryBodyPartsEntity> getAllStepExistsBodyParts(){
        return categoryDao.getAllStepExistsBodyParts();
    }

}
