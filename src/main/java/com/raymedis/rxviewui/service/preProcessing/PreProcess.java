package com.raymedis.rxviewui.service.preProcessing;

import com.ramedis.zioxa.pipeline.*;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreProcess {

    private final static Logger logger = LoggerFactory.getLogger(PreProcess.class);

    private final static PreProcess instance = new PreProcess();
    public static PreProcess getInstance(){
        return instance;
    }


    public Mat process(Mat mat, String bodyPartName){

        bodyPartName = bodyPartName.toLowerCase();

        logger.info("Processing body part: {}", bodyPartName);
        int depth = mat.depth();

        //if depth is not 8-bit or 16-bit, return null
        if (depth != CvType.CV_8U && depth != CvType.CV_16U) {
            logger.error("Unsupported Mat depth: {}", depth);
            return null;
        }

        Mat preProcessedMat = null;
        PreProcessingOutput preProcessingOutput = null;

        switch (bodyPartName){
            case "abdomen","hip","sacrum":
                //do abdomenProcessing

                AbdomenPreProcessingPipeline abdomenPreProcessingPipeline = new AbdomenPreProcessingPipeline();


                if (depth == CvType.CV_8U) {
                    logger.info("Mat is 8-bit unsigned");
                    preProcessingOutput = abdomenPreProcessingPipeline.process8BitImages(mat);
                } else if (depth == CvType.CV_16U) {
                    logger.info("Mat is 16-bit unsigned");
                    preProcessingOutput = abdomenPreProcessingPipeline.process16BitImages(mat);
                }else{
                    return null;
                }

                preProcessedMat = preProcessingOutput.getOutputMat();


                break;
            case "ankle":
                //do HandProcessing

                AnklePreProcessingPipeline anklePreProcessingPipeline = new AnklePreProcessingPipeline();

                if (depth == CvType.CV_8U) {
                    logger.info("Mat is 8-bit unsigned");
                    preProcessingOutput = anklePreProcessingPipeline.process8BitImages(mat);
                } else if (depth == CvType.CV_16U) {
                    logger.info("Mat is 16-bit unsigned");
                    preProcessingOutput = anklePreProcessingPipeline.process16BitImages(mat);
                }else{
                    return null;
                }

                preProcessedMat = preProcessingOutput.getOutputMat();

                break;
            case "chest","ribs","sternum":
                //do ChestProcessing


                ChestPreProcessingPipeline chestPreProcessingPipeline = new ChestPreProcessingPipeline();

                if (depth == CvType.CV_8U) {
                    logger.info("Mat is 8-bit unsigned");
                    preProcessingOutput = chestPreProcessingPipeline.process8BitImages(mat);
                } else if (depth == CvType.CV_16U) {
                    logger.info("Mat is 16-bit unsigned");
                    preProcessingOutput = chestPreProcessingPipeline.process16BitImages(mat);
                }else{
                    return null;
                }

                preProcessedMat = preProcessingOutput.getOutputMat();

                break;
            case "coccyx":
                //do CoccyxProcessing

                CoccyxPreProcessingPipeline coccyxPreProcessingPipeline = new CoccyxPreProcessingPipeline();

                if (depth == CvType.CV_8U) {
                    logger.info("Mat is 8-bit unsigned");
                    preProcessingOutput = coccyxPreProcessingPipeline.process8BitImages(mat);
                } else if (depth == CvType.CV_16U) {
                    logger.info("Mat is 16-bit unsigned");
                    preProcessingOutput = coccyxPreProcessingPipeline.process16BitImages(mat);
                }else{
                    return null;
                }

                preProcessedMat = preProcessingOutput.getOutputMat();

                break;
            case "cspine":
                //do CspineProcessing

                CspinePreProcessingPipeline cspinePreProcessingPipeline = new CspinePreProcessingPipeline();

                if (depth == CvType.CV_8U) {
                    logger.info("Mat is 8-bit unsigned");
                    preProcessingOutput = cspinePreProcessingPipeline.process8BitImages(mat);
                } else if (depth == CvType.CV_16U) {
                    logger.info("Mat is 16-bit unsigned");
                    preProcessingOutput = cspinePreProcessingPipeline.process16BitImages(mat);
                }else{
                    return null;
                }

                preProcessedMat = preProcessingOutput.getOutputMat();


                break;
            case "hand","calcaneus","elbow","femur","finger","foot","forearm","patella","tibia","toe","wrist":
                //do HandProcessing

                HandPreProcessingPipeline handPreProcessingPipeline = new HandPreProcessingPipeline();

                if (depth == CvType.CV_8U) {
                    logger.info("Mat is 8-bit unsigned");
                    preProcessingOutput = handPreProcessingPipeline.process8BitImages(mat);
                } else if (depth == CvType.CV_16U) {
                    logger.info("Mat is 16-bit unsigned");
                    preProcessingOutput = handPreProcessingPipeline.process16BitImages(mat);
                }else{
                    return null;
                }

                preProcessedMat = preProcessingOutput.getOutputMat();


                break;
            case "knee":
                //do KneeProcessing

                KneePreProcessingPipeline kneePreProcessingPipeline = new KneePreProcessingPipeline();

                if (depth == CvType.CV_8U) {
                    logger.info("Mat is 8-bit unsigned");
                    preProcessingOutput = kneePreProcessingPipeline.process8BitImages(mat);
                } else if (depth == CvType.CV_16U) {
                    logger.info("Mat is 16-bit unsigned");
                    preProcessingOutput = kneePreProcessingPipeline.process16BitImages(mat);
                }else{
                    return null;
                }

                preProcessedMat = preProcessingOutput.getOutputMat();


                break;
            case "lspine":
                //do LspineProcessing

                LspinePreProcessingPipeline lspinePreProcessingPipeline = new LspinePreProcessingPipeline();

                if (depth == CvType.CV_8U) {
                    logger.info("Mat is 8-bit unsigned");
                    preProcessingOutput = lspinePreProcessingPipeline.process8BitImages(mat);
                } else if (depth == CvType.CV_16U) {
                    logger.info("Mat is 16-bit unsigned");
                    preProcessingOutput = lspinePreProcessingPipeline.process16BitImages(mat);
                }else{
                    return null;
                }

                preProcessedMat = preProcessingOutput.getOutputMat();

                break;
            case "mandible":
                //do MandibleProcessing

                MandiblePreProcessingPipeline mandiblePreProcessingPipeline = new MandiblePreProcessingPipeline();

                if (depth == CvType.CV_8U) {
                    logger.info("Mat is 8-bit unsigned");
                    preProcessingOutput = mandiblePreProcessingPipeline.process8BitImages(mat);
                } else if (depth == CvType.CV_16U) {
                    logger.info("Mat is 16-bit unsigned");
                    preProcessingOutput = mandiblePreProcessingPipeline.process16BitImages(mat);
                }else{
                    return null;
                }

                preProcessedMat = preProcessingOutput.getOutputMat();



                break;
            case "pelvis":
                //do PelvisProcessing

                PelvisPreProcessingPipeline pelvisPreProcessingPipeline = new PelvisPreProcessingPipeline();

                if (depth == CvType.CV_8U) {
                    logger.info("Mat is 8-bit unsigned");
                    preProcessingOutput = pelvisPreProcessingPipeline.process8BitImages(mat);
                } else if (depth == CvType.CV_16U) {
                    logger.info("Mat is 16-bit unsigned");
                    preProcessingOutput = pelvisPreProcessingPipeline.process16BitImages(mat);
                }else{
                    return null;
                }

                preProcessedMat = preProcessingOutput.getOutputMat();

                break;

            case "shoulder","clavicle","humerus","scapula":
                //do ShoulderProcessing

                ShoulderPreProcessingPipeline shoulderPreProcessingPipeline = new ShoulderPreProcessingPipeline();

                if (depth == CvType.CV_8U) {
                    logger.info("Mat is 8-bit unsigned");
                    preProcessingOutput = shoulderPreProcessingPipeline.process8BitImages(mat);
                } else if (depth == CvType.CV_16U) {
                    logger.info("Mat is 16-bit unsigned");
                    preProcessingOutput = shoulderPreProcessingPipeline.process16BitImages(mat);
                }else{
                    return null;
                }

                preProcessedMat = preProcessingOutput.getOutputMat();

                break;

            case "skull","mastoid","nasal bone","orbit","petrous bone","sella turcica","sinuses waters","stenvers","zygomatic arche":
                //do SkullProcessing

                SkullPreProcessingPipeline skullPreProcessingPipeline = new SkullPreProcessingPipeline();

                if (depth == CvType.CV_8U) {
                    logger.info("Mat is 8-bit unsigned");
                    preProcessingOutput = skullPreProcessingPipeline.process8BitImages(mat);
                } else if (depth == CvType.CV_16U) {
                    logger.info("Mat is 16-bit unsigned");
                    preProcessingOutput = skullPreProcessingPipeline.process16BitImages(mat);
                }else{
                    return null;
                }

                preProcessedMat = preProcessingOutput.getOutputMat();
                break;

            case "tspine":

                //do TspineProcessing
                TspinePreProcessingPipeline tspinePreProcessingPipeline = new TspinePreProcessingPipeline();

                if (depth == CvType.CV_8U) {
                    logger.info("Mat is 8-bit unsigned");
                    preProcessingOutput = tspinePreProcessingPipeline.process8BitImages(mat);
                } else if (depth == CvType.CV_16U) {
                    logger.info("Mat is 16-bit unsigned");
                    preProcessingOutput = tspinePreProcessingPipeline.process16BitImages(mat);
                }else{
                    return null;
                }

                preProcessedMat = preProcessingOutput.getOutputMat();
                break;
        }


        return preProcessedMat;

    }



}
