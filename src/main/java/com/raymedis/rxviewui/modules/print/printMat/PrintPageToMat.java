package com.raymedis.rxviewui.modules.print.printMat;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PrintPageToMat {

    private static  final Logger logger = LoggerFactory.getLogger(PrintPageToMat.class);

    public static Mat createMat(double width, double height, List<PrintImageCordData> printImageCordDataList) {
        // Create destination matrix with 16-bit unsigned 3-channel format


        //CvType.CV_16UC3 , new Scalar(0, 0, 0)
        Mat original = printImageCordDataList.getFirst().getMat();
        int type = original.type();

        // Create the new Mat
        //Mat mat = Mat.zeros((int) height, (int) width, CvType.CV_16UC4,);
        Mat mat = new Mat((int) height, (int) width, type, new Scalar(65535, 65535, 65535, 65535));

        for (PrintImageCordData printImageCordData : printImageCordDataList) {
            Mat processedInput = printImageCordData.getMat();
            Rect rect = printImageCordData.getLayoutRect();

            // Check for valid ROI dimensions
            if (rect.x < 0 || rect.y < 0 || rect.x + rect.width > width || rect.y + rect.height > height) {
                logger.info("Warning: ROI dimensions out of bounds: " + rect + " : " + width + "x" + height);
                break;
            }

            // Create a region of interest in the destination matrix
            Mat roi = mat.submat(rect);

            // Handle the input matrix
            if (processedInput == null) {
                logger.info("Filled ROI with black (null input)");
            }
            else {

                // Step 1: If grayscale (1 channel), convert to 3 channels
                if (processedInput.channels() == 1) {
                    Imgproc.cvtColor(processedInput, processedInput, Imgproc.COLOR_GRAY2BGRA);
                }

                // Step 2: Convert to 16-bit depth if needed
                if (processedInput.depth() != CvType.CV_16U) {
                    // Scale 8-bit values (0–255) up to 16-bit (0–65535)
                    double scale = (processedInput.depth() == CvType.CV_8U) ? 256.0 : 1.0;
                    processedInput.convertTo(processedInput, CvType.CV_16UC4, scale);
                }


                // Resize if needed
                if (processedInput.size().width != roi.size().width ||
                        processedInput.size().height != roi.size().height) {
                    Imgproc.resize(processedInput, processedInput, roi.size());
                }

                //Copy to ROI
                processedInput.copyTo(roi);

            }
        }


        return mat;
    }


}