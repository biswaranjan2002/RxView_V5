package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StepRowMapper implements GenericRepository.RowMapper<StepEntity, Integer> {

    private static final StepRowMapper instance = new StepRowMapper();
    public static StepRowMapper getInstance() {
        return instance;
    }

    @Override
    public StepEntity mapRow(ResultSet rs) throws SQLException {
        StepEntity stepEntity = new StepEntity();

        stepEntity.setStepId(rs.getInt("stepId"));
        stepEntity.setStepName(rs.getString("stepName"));
        stepEntity.setPatientSize(rs.getString("patientSize"));
        stepEntity.setAutoCrop(rs.getBoolean("autoCrop"));
        stepEntity.setImageProcessParamKey(rs.getDouble("imageProcessParamKey"));
        stepEntity.setDetectorNo(rs.getInt("detectorNo"));
        stepEntity.setLrLabel(rs.getString("lrLabel"));
        stepEntity.setLrPos(rs.getDouble("lrPos"));
        stepEntity.setText(rs.getString("text"));
        stepEntity.setTextPos(rs.getString("textPos"));
        stepEntity.setFlip(rs.getBoolean("flip"));
        stepEntity.setMirror(rs.getBoolean("mirror"));
        stepEntity.setRotation(rs.getBoolean("rotation"));
        stepEntity.setImageRotation(rs.getBoolean("imageRotation"));
        stepEntity.setConsoleKv(rs.getDouble("consoleKv"));
        stepEntity.setConsoleMas(rs.getDouble("consoleMas"));
        stepEntity.setConsoleMa(rs.getDouble("consoleMa"));
        stepEntity.setConsoleMs(rs.getDouble("consoleMs"));
        stepEntity.setConsoleAec(rs.getBoolean("consoleAec"));
        stepEntity.setConsoleAecLeft(rs.getBoolean("consoleAecLeft"));
        stepEntity.setConsoleAecRight(rs.getBoolean("consoleAecRight"));
        stepEntity.setConsoleAecCenter(rs.getBoolean("consoleAecCenter"));
        stepEntity.setConsoleDen(rs.getInt("consoleDen"));
        stepEntity.setGeneratorP1(rs.getString("generatorP1"));
        stepEntity.setGeneratorP2(rs.getString("generatorP2"));
        stepEntity.setImageProcType(rs.getInt("imageProcType"));
        stepEntity.setSid(rs.getDouble("sid"));
        stepEntity.setImageParamType(rs.getInt("imageParamType"));
        stepEntity.setArmAngle(rs.getDouble("armAngle"));
        stepEntity.setDetectorAngle(rs.getDouble("detectorAngle"));
        stepEntity.setTubeTiltAngle(rs.getDouble("tubeTiltAngle"));
        stepEntity.setTubeRotateAngle(rs.getDouble("tubeRotateAngle"));
        stepEntity.setCeilingPosition(rs.getDouble("ceilingPosition"));
        stepEntity.setDetectorArmPosition(rs.getDouble("detectorArmPosition"));
        stepEntity.setTubeArmPosition(rs.getDouble("tubeArmPosition"));
        stepEntity.setProcedureId(rs.getInt("procedureId"));

        return stepEntity;
    }



    @Override
    public Object getValue(StepEntity entity, String columnName) {
        if (entity == null || columnName == null || columnName.isEmpty()) {
            return null;
        }

        return switch (columnName) {
            case "stepId" -> entity.getStepId();
            case "stepName" -> entity.getStepName();
            case "patientSize" -> entity.getPatientSize();
            case "autoCrop" -> entity.getAutoCrop();
            case "imageProcessParamKey" -> entity.getImageProcessParamKey();
            case "detectorNo" -> entity.getDetectorNo();
            case "lrLabel" -> entity.getLrLabel();
            case "lrPos" -> entity.getLrPos();
            case "text" -> entity.getText();
            case "textPos" -> entity.getTextPos();
            case "flip" -> entity.getFlip();
            case "mirror" -> entity.getMirror();
            case "rotation" -> entity.getRotation();
            case "imageRotation" -> entity.getImageRotation();
            case "consoleKv" -> entity.getConsoleKv();
            case "consoleMas" -> entity.getConsoleMas();
            case "consoleMa" -> entity.getConsoleMa();
            case "consoleMs" -> entity.getConsoleMs();
            case "consoleAec" -> entity.getConsoleAec();
            case "consoleAecLeft" -> entity.getConsoleAecLeft();
            case "consoleAecRight" -> entity.getConsoleAecRight();
            case "consoleAecCenter" -> entity.getConsoleAecCenter();
            case "consoleDen" -> entity.getConsoleDen();
            case "generatorP1" -> entity.getGeneratorP1();
            case "generatorP2" -> entity.getGeneratorP2();
            case "imageProcType" -> entity.getImageProcType();
            case "sid" -> entity.getSid();
            case "imageParamType" -> entity.getImageParamType();
            case "armAngle" -> entity.getArmAngle();
            case "detectorAngle" -> entity.getDetectorAngle();
            case "tubeTiltAngle" -> entity.getTubeTiltAngle();
            case "tubeRotateAngle" -> entity.getTubeRotateAngle();
            case "ceilingPosition" -> entity.getCeilingPosition();
            case "detectorArmPosition" -> entity.getDetectorArmPosition();
            case "tubeArmPosition" -> entity.getTubeArmPosition();
            case "procedureId" -> entity.getProcedureId();
            default -> null;
        };
    }


    @Override
    public Object getPrimaryKeyValue(StepEntity entity, String primaryKeyName) {
        return entity.getStepId();
    }
}
