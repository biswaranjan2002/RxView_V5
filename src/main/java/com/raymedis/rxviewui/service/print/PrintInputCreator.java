package com.raymedis.rxviewui.service.print;

import com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper.*;
import com.raymedis.rxviewui.modules.print.printInput.*;
import com.raymedis.rxviewui.modules.study.bodypart.BodyPartHandler;
import com.raymedis.rxviewui.modules.study.bodypart.BodyPartNode;
import com.raymedis.rxviewui.modules.study.bodypart.PatientBodyPart;
import com.raymedis.rxviewui.modules.study.params.ImagingParams;
import com.raymedis.rxviewui.modules.study.params.XrayParams;
import com.raymedis.rxviewui.modules.study.patient.PatientInfo;
import com.raymedis.rxviewui.modules.study.study.PatientStudy;
import com.raymedis.rxviewui.modules.study.tabmanagement.TabNode;

import java.util.ArrayList;
import java.util.List;

public class PrintInputCreator {

    private final static PrintInputCreator instance = new PrintInputCreator();

    public static PrintInputCreator getInstance() {
        return instance;
    }

    public PatientPrintData createPrintInput(PatientStudy patientStudy) {
        PatientPrintData patientPrintData = new PatientPrintData();

        if (patientStudy != null) {
            mapPatientInfoFields(patientPrintData, patientStudy.getPatientInfo());
            mapStudyFields(patientPrintData, patientStudy);
            mapDateTimeFields(patientPrintData, patientStudy);
            mapPhysicianAndCommentFields(patientPrintData, patientStudy);

            BodyPartHandler bodyPartHandler = patientStudy.getBodyPartHandler();
            if (bodyPartHandler != null) {
                processBodyPartHandler(patientPrintData, bodyPartHandler);
            }
        }
        return patientPrintData;
    }


    // region Patient Info Mapping
    private void mapPatientInfoFields(PatientPrintData target, PatientInfo source) {
        if (source == null) return;

        target.setPatientId(source.getPatientId());
        target.setPatientName(source.getPatientName());
        target.setAge(source.getAge());
        target.setHeight(source.getHeight());
        target.setWeight(source.getWeight());
        target.setBirthDate(source.getBirthDate());
        target.setPatientSize(source.getPatientSize());
        target.setSex(source.getSex());
        target.setPatientInstituteResidence(source.getPatientInstituteResidence());
    }
    // endregion

    // region Study Fields Mapping
    private void mapStudyFields(PatientPrintData target, PatientStudy source) {
        target.setStudyId(source.getStudyId());
        target.setStudyUid(source.getStudyUid());
        target.setRejected(source.isRejected());
        target.setAccessionNum(source.getAccessionNum());
        target.setModality(source.getModality());
        target.setRequestProcedurePriority(source.getRequestProcedurePriority());
        target.setAdditionalPatientHistory(source.getAdditionalPatientHistory());
        target.setAdmittingDiagnosisDescription(source.getAdmittingDiagnosisDescription());
        target.setStudyDescription(source.getStudyDescription());
        target.setProcedureCode(source.getProcedureCode());
    }
    // endregion

    // region DateTime Fields Mapping
    private void mapDateTimeFields(PatientPrintData target, PatientStudy source) {
        target.setStudyDateTime(source.getStudyDateTime());
        target.setRegisterDateTime(source.getRegisterDateTime());
        target.setScheduledDateTime(source.getScheduledDateTime());
        target.setExposedDateTime(source.getExposedDateTime());
    }
    // endregion

    // region Physician/Comment Fields Mapping
    private void mapPhysicianAndCommentFields(PatientPrintData target, PatientStudy source) {
        target.setPatientComments(source.getPatientComments());
        target.setReadingPhysician(source.getReadingPhysician());
        target.setPerformingPhysician(source.getPerformingPhysician());
        target.setReferringPhysician(source.getReferringPhysician());
        target.setInstitution(source.getInstitution());
    }
    // endregion

    // region BodyPartHandler Processing
    private void processBodyPartHandler(PatientPrintData target, BodyPartHandler bodyPartHandler) {
        List<PatientPrintImageData> imageDataList = new ArrayList<>();

        List<TabNode> tabs = bodyPartHandler.getAllTabs();
        if (tabs != null) {
            for (TabNode tab : tabs) {
                if (tab instanceof BodyPartNode bodyPartNode) {
                    imageDataList.add(createImageDataFromBodyPart(bodyPartNode));
                }
            }
        }

        target.setPatientPrintImageDataList(imageDataList);
    }

    private PatientPrintImageData createImageDataFromBodyPart(BodyPartNode bodyPartNode) {
        PatientPrintImageData imageData = new PatientPrintImageData();
        if (bodyPartNode == null) return imageData;

        PatientBodyPart patientBodyPart = bodyPartNode.getBodyPart();
        if (patientBodyPart == null) return imageData;

        // Basic fields mapping
        mapBasicImageFields(imageData, bodyPartNode, patientBodyPart);

        // Complex object mappings with null checks
        mapImagingParameters(imageData, patientBodyPart.getImagingParams());
        mapXrayParameters(imageData, patientBodyPart.getXrayParams());
        mapCropData(imageData, patientBodyPart.getCropLayoutWrapper());

        // Annotation mappings with null-safe lists
        mapAngleDrawWrappers(imageData, patientBodyPart);
        mapDistanceDrawWrappers(imageData, patientBodyPart);
        mapTextDrawWrappers(imageData, patientBodyPart);
        mapLeftRightDrawWrapper(imageData, patientBodyPart);

        return imageData;
    }

    private void mapBasicImageFields(PatientPrintImageData target, BodyPartNode sourceNode, PatientBodyPart sourceBodyPart) {
        target.setImageSessionId(sourceNode.getImageSessionId());
        target.setExposed(sourceBodyPart.isExposed());
        target.setRejected(sourceBodyPart.isRejected());
        target.setBodyPartName(sourceBodyPart.getBodyPartName());
        target.setBodyPartPosition(sourceBodyPart.getBodyPartPosition());
        target.setThumbNail(sourceBodyPart.getThumbNail());

        if(sourceBodyPart.getImageMat()!=null){
            target.setImageMat(sourceBodyPart.getImageMat().clone());
        }

        if(sourceBodyPart.getEditedMat()!=null){
            target.setEditedMat(sourceBodyPart.getEditedMat().clone());
        }


        target.setExposureDateTime(sourceBodyPart.getExposureDateTime());
        target.setInstanceUid(sourceBodyPart.getInstanceUid());
        target.setSeriesUid(sourceBodyPart.getSeriesUid());
        target.setInstanceId(sourceBodyPart.getInstanceId());
        target.setSeriesId(sourceBodyPart.getSeriesId());
        target.setStudyInstanceUID(sourceBodyPart.getStudyInstanceUID());
        target.setCrop(sourceBodyPart.isCrop());
    }
    // endregion

    // region Parameter Mappings
    private void mapImagingParameters(PatientPrintImageData target, ImagingParams params) {
        if (params == null) return;

        target.setWindowLevel(params.getWindowLevel());
        target.setDenoise(params.getDenoise());
        target.setSharpness(params.getSharpness());
        target.setWindowWidth(params.getWindowWidth());
        target.setZoomFactor(params.getZoomFactor());
        target.setAngleFactor(params.getAngleFactor());
        target.setImageLeft(params.getImageLeft());
        target.setImageTop(params.getImageTop());
        target.setHFlip(params.isHFlip());
        target.setVFlip(params.isVFlip());
        target.setInvert(params.isInvert());
    }

    private void mapXrayParameters(PatientPrintImageData target, XrayParams params) {
        if (params == null) return;

        target.setmAs(params.getmAs());
        target.setmA(params.getmA());
        target.setKV(params.getKV());
        target.setMs(params.getMs());
    }

    private void mapCropData(PatientPrintImageData target, CropLayoutWrapper crop) {
        if (crop == null) return;

        target.setRectX(crop.getRectX());
        target.setRectY(crop.getRectY());
        target.setRectWidth(crop.getRectWidth());
        target.setRectHeight(crop.getRectHeight());
    }
    // endregion

    // region Annotation Mappings
    private void mapAngleDrawWrappers(PatientPrintImageData target, PatientBodyPart source) {
        List<PatientPrintAngleDrawWrapper> angleWrappers = new ArrayList<>();

        List<AngleDrawWrapper> angleDrawList = source.getAngleDrawList();
        if (angleDrawList != null) {
            for (AngleDrawWrapper angleDrawWrapper : angleDrawList) {
                if (angleDrawWrapper == null) continue;
                PatientPrintAngleDrawWrapper wrapper = new PatientPrintAngleDrawWrapper();

                // Safe label handling
                LabelWrapper label = angleDrawWrapper.getDegreeLabel();

                if (label != null) {
                    wrapper.setLabelWrapper(new PatientPrintLabelWrapper(
                            label.getValue(), label.getX(), label.getY()
                    ));
                }

                // Safe ellipse mapping
                wrapper.setEllipsesWrapperList(
                        mapEllipseWrappers(angleDrawWrapper.getEllipses())
                );

                // Safe line mapping
                wrapper.setLineWrapperList(
                        mapLineWrappers(angleDrawWrapper.getLines())
                );

                angleWrappers.add(wrapper);
            }


        }

        target.setPatientPrintAngleDrawWrappers(angleWrappers);
    }

    private void mapDistanceDrawWrappers(PatientPrintImageData target, PatientBodyPart source) {
        List<PatientPrintDistanceDrawWrapper> distanceWrappers = new ArrayList<>();

        List<DistanceDrawWrapper> distanceDrawList = source.getDistanceDrawList();
        if (distanceDrawList != null) {
            for (DistanceDrawWrapper distanceDrawWrapper : distanceDrawList) {
                if (distanceDrawWrapper != null) {
                    distanceWrappers.add(getPatientPrintDistanceDrawWrapper(distanceDrawWrapper));
                }
            }
        }

        target.setPatientPrintDistanceDrawWrappers(distanceWrappers);
    }

    private void mapTextDrawWrappers(PatientPrintImageData target, PatientBodyPart source) {
        List<PatientPrintTextDrawWrapper> textWrappers = new ArrayList<>();

        List<TextDrawWrapper> textDrawList = source.getTextDrawList();
        if (textDrawList != null) {
            for (TextDrawWrapper textDrawWrapper : textDrawList) {
                if (textDrawWrapper != null) {
                    textWrappers.add(new PatientPrintTextDrawWrapper(
                            textDrawWrapper.getText(),
                            textDrawWrapper.getX(),
                            textDrawWrapper.getY()
                    ));
                }
            }
        }

        target.setPatientPrintTextDrawWrappers(textWrappers);
    }

    private void mapLeftRightDrawWrapper(PatientPrintImageData target, PatientBodyPart source) {
        LeftRightDrawWrapper wrapper = source.getLeftRightDrawWrapper();
        if (wrapper != null) {
            target.setOffsetX(wrapper.getOffsetX());
            target.setOffsetY(wrapper.getOffsetY());
            target.setText(wrapper.getText());
        }
    }
    // endregion

    // region Helper Methods with Null Safety
    private List<PatientPrintEllipseWrapper> mapEllipseWrappers(List<EllipseWrapper> ellipses) {
        List<PatientPrintEllipseWrapper> result = new ArrayList<>();
        if (ellipses != null) {
            for (EllipseWrapper ellipse : ellipses) {

                //System.out.println("EllipseWrapper: " + ellipse);

                if (ellipse != null) {
                    result.add(new PatientPrintEllipseWrapper(
                            ellipse.getCenterX(),
                            ellipse.getCenterY(),
                            ellipse.getRadiusX(),
                            ellipse.getRadiusY()
                    ));
                }
            }
        }
        return result;
    }

    private List<PatientPrintLineWrapper> mapLineWrappers(List<LineWrapper> lines) {
        List<PatientPrintLineWrapper> result = new ArrayList<>();
        if (lines != null) {
            for (LineWrapper line : lines) {
                if (line != null) {
                    result.add(new PatientPrintLineWrapper(
                            line.getStartX(),
                            line.getStartY(),
                            line.getEndX(),
                            line.getEndY()
                    ));
                }
            }
        }
        return result;
    }

    private PatientPrintDistanceDrawWrapper getPatientPrintDistanceDrawWrapper(DistanceDrawWrapper source) {
        PatientPrintDistanceDrawWrapper wrapper = new PatientPrintDistanceDrawWrapper();
        if (source == null) return wrapper;

        // Safe ellipse mapping
        wrapper.setEllipses(mapEllipseWrappers(source.getEllipses()));

        // Safe line handling
        LineWrapper line = source.getLines();
        if (line != null) {
            wrapper.setLines(new PatientPrintLineWrapper(
                    line.getStartX(), line.getStartY(),
                    line.getEndX(), line.getEndY()
            ));
        }

        // Safe label handling
        LabelWrapper label = source.getLabelWrapper();
        if (label != null) {
            wrapper.setLabelWrapper(new PatientPrintLabelWrapper(
                    label.getValue(), label.getX(), label.getY()
            ));
        }

        return wrapper;
    }
    // endregion

}