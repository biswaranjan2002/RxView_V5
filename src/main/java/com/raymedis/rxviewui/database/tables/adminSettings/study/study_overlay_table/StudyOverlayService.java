package com.raymedis.rxviewui.database.tables.adminSettings.study.study_overlay_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudyOverlayService {

    public static StudyOverlayService instance =new StudyOverlayService();
    public static StudyOverlayService getInstance(){
        return instance;
    }


    private final GenericRepository<PrintOverlayEntity, Integer> studyOverlayDao;

    public StudyOverlayService(){
        Map<String, String> columns = new HashMap<>();

        columns.put("positionType", "VARCHAR(50)");
        columns.put("itemContent", "VARCHAR(255)");

        studyOverlayDao = new GenericRepository<>("studyOverlayEntity",columns,"id", new StudyOverlayRowMapper(),true, 1);
    }

    public void save(PrintOverlayEntity printOverlayEntity) throws SQLException {
        studyOverlayDao.save(printOverlayEntity);
    }

    public void update(int id, PrintOverlayEntity printOverlayEntity) throws SQLException {
        studyOverlayDao.update(id, printOverlayEntity);
    }

    public void delete(int id) throws SQLException {
        studyOverlayDao.deleteByPrimaryKey(id);
    }

    public ArrayList<PrintOverlayEntity> findAll() throws SQLException {
        return (ArrayList<PrintOverlayEntity>) studyOverlayDao.findAll();
    }



}
