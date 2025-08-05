package com.raymedis.rxviewui.database.tables.adminSettings.register.register_manual_table;


import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationManualService {

    public static RegistrationManualService instance =new RegistrationManualService();
    public static RegistrationManualService getInstance(){
        return instance;
    }

    private final GenericRepository<CandidateTagsEntity, String> registerManualDao;

    public RegistrationManualService(){
        Map<String, String> columns = new HashMap<>();
        columns.put("isSelected", "BIT NOT NULL");
        registerManualDao = new GenericRepository<>("candidateTags",columns,"tagName", new CandidateTagsRowMapper(),false, "");
    }



    public void saveCandidateKey(CandidateTagsEntity candidateTagsEntity) throws SQLException {
        registerManualDao.save(candidateTagsEntity);
    }


    public void deleteCandidateTag(String val){
        try {
            registerManualDao.deleteByPrimaryKey(val);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateCandidateTag(String tagName,CandidateTagsEntity candidateTagsEntity) throws SQLException {
        registerManualDao.update(tagName, candidateTagsEntity);
    }


    public ArrayList<CandidateTagsEntity> getAllCandidateTags(){
        try {
            return (ArrayList<CandidateTagsEntity>) registerManualDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<CandidateTagsEntity> selectedTags = new ArrayList<>();
    public ArrayList<CandidateTagsEntity> getAllSelectedCandidateTags(){
        selectedTags.clear();
        try {
            for (CandidateTagsEntity c : registerManualDao.findAll()){
                if(c.getSelected().equals(true)){
                    selectedTags.add(c);
                }
            }
            return (ArrayList<CandidateTagsEntity>) selectedTags;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
