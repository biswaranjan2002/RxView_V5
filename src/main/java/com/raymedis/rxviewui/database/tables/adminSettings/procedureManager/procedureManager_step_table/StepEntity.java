package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table;

public class StepEntity {

    private int stepId;
    private String stepName;
    private String patientSize;
    private boolean autoCrop;
    private double imageProcessParamKey;
    private int detectorNo;
    private String lrLabel;
    private double lrPos;
    private String text;
    private String textPos;
    private boolean flip;
    private boolean mirror;
    private boolean rotation;
    private boolean imageRotation;
    private double consoleKv;
    private double consoleMas;
    private double consoleMa;
    private double consoleMs;
    private boolean consoleAec;
    private boolean consoleAecLeft;
    private boolean consoleAecRight;
    private boolean consoleAecCenter;
    private int consoleDen;
    private String generatorP1;
    private String generatorP2;
    private int imageProcType;
    private double sid;
    private int imageParamType;
    private double armAngle;
    private double detectorAngle;
    private double tubeTiltAngle;
    private double tubeRotateAngle;
    private double ceilingPosition;
    private double detectorArmPosition;
    private double tubeArmPosition;

    private int procedureId;

    //constructor

    public StepEntity() {
    }

    public StepEntity(int stepId, String stepName, String patientSize, boolean autoCrop, double imageProcessParamKey, int detectorNo,
                      String lrLabel, double lrPos, String text, String textPos, boolean flip, boolean mirror, boolean rotation,
                      boolean imageRotation, double consoleKv, double consoleMas, double consoleMa, double consoleMs, boolean consoleAec,
                      boolean consoleAecLeft, boolean consoleAecRight, boolean consoleAecCenter, int consoleDen, String generatorP1,
                      String generatorP2, int imageProcType, double sid, int imageParamType, double armAngle, double detectorAngle,
                      double tubeTiltAngle, double tubeRotateAngle, double ceilingPosition, double detectorArmPosition, double tubeArmPosition,int procedureId) {
        this.stepId = stepId;
        this.stepName = stepName;
        this.patientSize = patientSize;
        this.autoCrop = autoCrop;
        this.imageProcessParamKey = imageProcessParamKey;
        this.detectorNo = detectorNo;
        this.lrLabel = lrLabel;
        this.lrPos = lrPos;
        this.text = text;
        this.textPos = textPos;
        this.flip = flip;
        this.mirror = mirror;
        this.rotation = rotation;
        this.imageRotation = imageRotation;
        this.consoleKv = consoleKv;
        this.consoleMas = consoleMas;
        this.consoleMa = consoleMa;
        this.consoleMs = consoleMs;
        this.consoleAec = consoleAec;
        this.consoleAecLeft = consoleAecLeft;
        this.consoleAecRight = consoleAecRight;
        this.consoleAecCenter = consoleAecCenter;
        this.consoleDen = consoleDen;
        this.generatorP1 = generatorP1;
        this.generatorP2 = generatorP2;
        this.imageProcType = imageProcType;
        this.sid = sid;
        this.imageParamType = imageParamType;
        this.armAngle = armAngle;
        this.detectorAngle = detectorAngle;
        this.tubeTiltAngle = tubeTiltAngle;
        this.tubeRotateAngle = tubeRotateAngle;
        this.ceilingPosition = ceilingPosition;
        this.detectorArmPosition = detectorArmPosition;
        this.tubeArmPosition = tubeArmPosition;
        this.procedureId =procedureId;
    }

    //setters and getters


    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getPatientSize() {
        return patientSize;
    }

    public void setPatientSize(String patientSize) {
        this.patientSize = patientSize;
    }

    public boolean getAutoCrop() {
        return autoCrop;
    }

    public void setAutoCrop(boolean autoCrop) {
        this.autoCrop = autoCrop;
    }

    public double getImageProcessParamKey() {
        return imageProcessParamKey;
    }

    public void setImageProcessParamKey(double imageProcessParamKey) {
        this.imageProcessParamKey = imageProcessParamKey;
    }

    public int getDetectorNo() {
        return detectorNo;
    }

    public void setDetectorNo(int detectorNo) {
        this.detectorNo = detectorNo;
    }

    public String getLrLabel() {
        return lrLabel;
    }

    public void setLrLabel(String lrLabel) {
        this.lrLabel = lrLabel;
    }

    public double getLrPos() {
        return lrPos;
    }

    public void setLrPos(double lrPos) {
        this.lrPos = lrPos;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextPos() {
        return textPos;
    }

    public void setTextPos(String textPos) {
        this.textPos = textPos;
    }

    public boolean getFlip() {
        return flip;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public boolean getMirror() {
        return mirror;
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    public boolean getRotation() {
        return rotation;
    }

    public void setRotation(boolean rotation) {
        this.rotation = rotation;
    }

    public boolean getImageRotation() {
        return imageRotation;
    }

    public void setImageRotation(boolean imageRotation) {
        this.imageRotation = imageRotation;
    }

    public double getConsoleKv() {
        return consoleKv;
    }

    public void setConsoleKv(double consoleKv) {
        this.consoleKv = consoleKv;
    }

    public double getConsoleMas() {
        return consoleMas;
    }

    public void setConsoleMas(double consoleMas) {
        this.consoleMas = consoleMas;
    }

    public double getConsoleMa() {
        return consoleMa;
    }

    public void setConsoleMa(double consoleMa) {
        this.consoleMa = consoleMa;
    }

    public double getConsoleMs() {
        return consoleMs;
    }

    public void setConsoleMs(double consoleMs) {
        this.consoleMs = consoleMs;
    }

    public boolean getConsoleAec() {
        return consoleAec;
    }

    public void setConsoleAec(boolean consoleAec) {
        this.consoleAec = consoleAec;
    }

    public boolean getConsoleAecLeft() {
        return consoleAecLeft;
    }

    public void setConsoleAecLeft(boolean consoleAecLeft) {
        this.consoleAecLeft = consoleAecLeft;
    }

    public boolean getConsoleAecRight() {
        return consoleAecRight;
    }

    public void setConsoleAecRight(boolean consoleAecRight) {
        this.consoleAecRight = consoleAecRight;
    }

    public boolean getConsoleAecCenter() {
        return consoleAecCenter;
    }

    public void setConsoleAecCenter(boolean consoleAecCenter) {
        this.consoleAecCenter = consoleAecCenter;
    }

    public int getConsoleDen() {
        return consoleDen;
    }

    public void setConsoleDen(int consoleDen) {
        this.consoleDen = consoleDen;
    }

    public String getGeneratorP1() {
        return generatorP1;
    }

    public void setGeneratorP1(String generatorP1) {
        this.generatorP1 = generatorP1;
    }

    public String getGeneratorP2() {
        return generatorP2;
    }

    public void setGeneratorP2(String generatorP2) {
        this.generatorP2 = generatorP2;
    }

    public int getImageProcType() {
        return imageProcType;
    }

    public void setImageProcType(int imageProcType) {
        this.imageProcType = imageProcType;
    }

    public double getSid() {
        return sid;
    }

    public void setSid(double sid) {
        this.sid = sid;
    }

    public int getImageParamType() {
        return imageParamType;
    }

    public void setImageParamType(int imageParamType) {
        this.imageParamType = imageParamType;
    }

    public double getArmAngle() {
        return armAngle;
    }

    public void setArmAngle(double armAngle) {
        this.armAngle = armAngle;
    }

    public double getDetectorAngle() {
        return detectorAngle;
    }

    public void setDetectorAngle(double detectorAngle) {
        this.detectorAngle = detectorAngle;
    }

    public double getTubeTiltAngle() {
        return tubeTiltAngle;
    }

    public void setTubeTiltAngle(double tubeTiltAngle) {
        this.tubeTiltAngle = tubeTiltAngle;
    }

    public double getTubeRotateAngle() {
        return tubeRotateAngle;
    }

    public void setTubeRotateAngle(double tubeRotateAngle) {
        this.tubeRotateAngle = tubeRotateAngle;
    }

    public double getCeilingPosition() {
        return ceilingPosition;
    }

    public void setCeilingPosition(double ceilingPosition) {
        this.ceilingPosition = ceilingPosition;
    }

    public double getDetectorArmPosition() {
        return detectorArmPosition;
    }

    public void setDetectorArmPosition(double detectorArmPosition) {
        this.detectorArmPosition = detectorArmPosition;
    }

    public double getTubeArmPosition() {
        return tubeArmPosition;
    }

    public void setTubeArmPosition(double tubeArmPosition) {
        this.tubeArmPosition = tubeArmPosition;
    }

    public int getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(int procedureId) {
        this.procedureId = procedureId;
    }


    @Override
    public String toString() {
        return "StepEntity{" +
                "stepId=" + stepId +
                ", stepName='" + stepName + '\'' +
                ", patientSize='" + patientSize + '\'' +
                ", autoCrop=" + autoCrop +
                ", imageProcessParamKey=" + imageProcessParamKey +
                ", detectorNo=" + detectorNo +
                ", lrLabel='" + lrLabel + '\'' +
                ", lrPos=" + lrPos +
                ", text='" + text + '\'' +
                ", textPos='" + textPos + '\'' +
                ", flip=" + flip +
                ", mirror=" + mirror +
                ", rotation=" + rotation +
                ", imageRotation=" + imageRotation +
                ", consoleKv=" + consoleKv +
                ", consoleMas=" + consoleMas +
                ", consoleMa=" + consoleMa +
                ", consoleMs=" + consoleMs +
                ", consoleAec=" + consoleAec +
                ", consoleAecLeft=" + consoleAecLeft +
                ", consoleAecRight=" + consoleAecRight +
                ", consoleAecCenter=" + consoleAecCenter +
                ", consoleDen=" + consoleDen +
                ", generatorP1='" + generatorP1 + '\'' +
                ", generatorP2='" + generatorP2 + '\'' +
                ", imageProcType=" + imageProcType +
                ", sid=" + sid +
                ", imageParamType=" + imageParamType +
                ", armAngle=" + armAngle +
                ", detectorAngle=" + detectorAngle +
                ", tubeTiltAngle=" + tubeTiltAngle +
                ", tubeRotateAngle=" + tubeRotateAngle +
                ", ceilingPosition=" + ceilingPosition +
                ", detectorArmPosition=" + detectorArmPosition +
                ", tubeArmPosition=" + tubeArmPosition +
                ", procedureId=" + procedureId +
                '}';
    }
}
