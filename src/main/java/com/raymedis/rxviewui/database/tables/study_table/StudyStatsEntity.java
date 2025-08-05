package com.raymedis.rxviewui.database.tables.study_table;

import java.time.LocalDate;

public class StudyStatsEntity {

    private int id;
    private LocalDate studyDate;
    private int studyCount;
    private int rejectedStudyCount;

    //constructors
    public StudyStatsEntity() {
    }

    public StudyStatsEntity(int id,LocalDate studyDate, int studyCount, int rejectedStudyCount) {
        this.id=id;
        this.studyDate = studyDate;
        this.studyCount = studyCount;
        this.rejectedStudyCount = rejectedStudyCount;
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(LocalDate studyDate) {
        this.studyDate = studyDate;
    }

    public int getStudyCount() {
        return studyCount;
    }

    public void setStudyCount(int studyCount) {
        this.studyCount = studyCount;
    }

    public int getRejectedStudyCount() {
        return rejectedStudyCount;
    }

    public void setRejectedStudyCount(int rejectedStudyCount) {
        this.rejectedStudyCount = rejectedStudyCount;
    }

    @Override
    public String toString() {
        return "StudyStatsEntity{" +
                "id=" + id +
                ", studyDate=" + studyDate +
                ", studyCount=" + studyCount +
                ", rejectedStudyCount=" + rejectedStudyCount +
                '}';
    }
}
