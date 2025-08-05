package com.raymedis.rxviewui.modules.study.study;
import com.raymedis.rxviewui.database.tables.study_table.StudyStatsEntity;
import com.raymedis.rxviewui.database.tables.study_table.StudyStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class IdGenerator {

    private static final  Logger logger = LoggerFactory.getLogger(IdGenerator.class);

    public static String prefix(String name,int size)
    {
        name = name.toUpperCase();
        if(name.length()>=size) {
            return name.substring(0,size);
        }
        else {
            // Append underscores to make the length equal to size
            int offset = size - name.length();
            name = name + "_".repeat(offset);
            return name;
        }
    }
    public static String getDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmssSSS");
        return now.format(formatter);
    }

    public static String generateSessionId(int size , String... name) {
        StringBuilder id = new StringBuilder();
        for(String temp : name){
            id.append(prefix(temp, size)).append("_");
        }
        return id + getDateTime() +String.format("%03d", new Random().nextInt(1000));
    }

    public static String generateStudyId() throws SQLException {
        LocalDateTime now = LocalDateTime.now();

        String studyId = String.format("%04d%02d%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        StudyStatsEntity studyStatsEntity = StudyStatsService.getInstance().findAllThisDayStudies(LocalDate.now());

        int studyCount=1;

        if(studyStatsEntity==null){
            studyStatsEntity = new StudyStatsEntity();
            studyStatsEntity.setStudyDate(LocalDate.now());
            studyStatsEntity.setStudyCount(studyCount);
            studyStatsEntity.setRejectedStudyCount(0);

            StudyStatsService.getInstance().save(studyStatsEntity);
        }
        else
        {
            studyCount = studyStatsEntity.getStudyCount()+1;
            studyStatsEntity.setStudyCount(studyCount);

            try {
                StudyStatsService.getInstance().update(studyStatsEntity.getId(),studyStatsEntity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        studyId += String.format("%04d", studyCount);
        return studyId;
    }


    public static String generateEmergencyPatientId() {
        // Format: EMERGENCY_yyyyMMdd_HHmmss
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        return "E" + timestamp;
    }



}

