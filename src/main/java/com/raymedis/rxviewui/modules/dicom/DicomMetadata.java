package com.raymedis.rxviewui.modules.dicom;

import java.util.Date;

public class DicomMetadata {

    String patientName;
    String patientId;
    String patientDOB;
    String patientSex;
    String accessionNumber;
    Date studyDateTime;
    Date acquisitionDateTime;
    String referringPhysician;
    String studyDescription;

    String kVp;
    String xrayTubeCurrent;
    String areaDoseProduct;
    String viewPosition;

    String studyId;
    String seriesNumber;
    String instanceNumber;
    String modality;
    String sopClass;

    String hospitalName;
    String hospitalAddress;
    String manufacturer;

    String bodyPart;
    String procedureCode;
    boolean burnedInAnnotation;


    public DicomMetadata() {
    }

    public DicomMetadata(String patientName, String patientId, String patientDOB, String patientSex, String accessionNumber, Date studyDateTime, Date acquisitionDateTime, String referringPhysician, String studyDescription, String kVp, String xrayTubeCurrent, String areaDoseProduct, String viewPosition, String studyId, String seriesNumber, String instanceNumber, String modality, String sopClass, String hospitalName, String hospitalAddress, String manufacturer, String bodyPart, String procedureCode, boolean burnedInAnnotation) {
        this.patientName = patientName;
        this.patientId = patientId;
        this.patientDOB = patientDOB;
        this.patientSex = patientSex;
        this.accessionNumber = accessionNumber;
        this.studyDateTime = studyDateTime;
        this.acquisitionDateTime = acquisitionDateTime;
        this.referringPhysician = referringPhysician;
        this.studyDescription = studyDescription;
        this.kVp = kVp;
        this.xrayTubeCurrent = xrayTubeCurrent;
        this.areaDoseProduct = areaDoseProduct;
        this.viewPosition = viewPosition;
        this.studyId = studyId;
        this.seriesNumber = seriesNumber;
        this.instanceNumber = instanceNumber;
        this.modality = modality;
        this.sopClass = sopClass;
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.manufacturer = manufacturer;
        this.bodyPart = bodyPart;
        this.procedureCode = procedureCode;
        this.burnedInAnnotation = burnedInAnnotation;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientDOB() {
        return patientDOB;
    }

    public void setPatientDOB(String patientDOB) {
        this.patientDOB = patientDOB;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public Date getStudyDateTime() {
        return studyDateTime;
    }

    public void setStudyDateTime(Date studyDateTime) {
        this.studyDateTime = studyDateTime;
    }

    public Date getAcquisitionDateTime() {
        return acquisitionDateTime;
    }

    public void setAcquisitionDateTime(Date acquisitionDateTime) {
        this.acquisitionDateTime = acquisitionDateTime;
    }

    public String getReferringPhysician() {
        return referringPhysician;
    }

    public void setReferringPhysician(String referringPhysician) {
        this.referringPhysician = referringPhysician;
    }

    public String getStudyDescription() {
        return studyDescription;
    }

    public void setStudyDescription(String studyDescription) {
        this.studyDescription = studyDescription;
    }

    public String getkVp() {
        return kVp;
    }

    public void setkVp(String kVp) {
        this.kVp = kVp;
    }

    public String getXrayTubeCurrent() {
        return xrayTubeCurrent;
    }

    public void setXrayTubeCurrent(String xrayTubeCurrent) {
        this.xrayTubeCurrent = xrayTubeCurrent;
    }

    public String getAreaDoseProduct() {
        return areaDoseProduct;
    }

    public void setAreaDoseProduct(String areaDoseProduct) {
        this.areaDoseProduct = areaDoseProduct;
    }

    public String getViewPosition() {
        return viewPosition;
    }

    public void setViewPosition(String viewPosition) {
        this.viewPosition = viewPosition;
    }

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public String getInstanceNumber() {
        return instanceNumber;
    }

    public void setInstanceNumber(String instanceNumber) {
        this.instanceNumber = instanceNumber;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getSopClass() {
        return sopClass;
    }

    public void setSopClass(String sopClass) {
        this.sopClass = sopClass;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public boolean isBurnedInAnnotation() {
        return burnedInAnnotation;
    }

    public void setBurnedInAnnotation(boolean burnedInAnnotation) {
        this.burnedInAnnotation = burnedInAnnotation;
    }
}
