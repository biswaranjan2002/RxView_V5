package com.raymedis.rxviewui.modules.ImageProcessing;

import com.ramedis.zioxa.bilateral.Bilateral;
import com.ramedis.zioxa.usm.USM;
import com.raymedis.rxviewui.modules.study.params.ImagingParams;
import org.jetbrains.annotations.NotNull;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;
import java.util.List;


public class ImageProcessService {

    private final static ImageProcessService instance = new ImageProcessService();
    public static ImageProcessService getInstance(){
        return instance;
    }

    private int frameCount = 0;
    public List<Mat> frames = new LinkedList<>();
    public Mat averageFrame;
    public Mat sumFrame;

    public Scalar zeroScalar = new Scalar(0, 0, 0);

    Rect roi = new Rect();

    public Mat resizeImageToSquareShape(Mat originalMat) {

        if (originalMat.empty()) {
            throw new IllegalArgumentException("Input image is empty.");
        }

        int originalWidth = originalMat.width();
        int originalHeight = originalMat.height();

        int desiredCanvasSize = Math.max(originalWidth, originalHeight);

        Scalar zeroScalar = new Scalar(0, 0, 0); // black background
        Mat canvasMat = new Mat(desiredCanvasSize, desiredCanvasSize, originalMat.type(), zeroScalar);

        int offsetX = (desiredCanvasSize - originalWidth) / 2;
        int offsetY = (desiredCanvasSize - originalHeight) / 2;

        Rect roi = new Rect(offsetX, offsetY, originalWidth, originalHeight);
        originalMat.copyTo(canvasMat.submat(roi));

        originalMat.release();

        return canvasMat;
    }


    public Mat resizeImageToSquareShape(Mat originalMat,int size) {

        int originalWidth = originalMat.width();
        int originalHeight = originalMat.height();

        int desiredCanvasSize = Math.max(originalWidth, originalHeight);

        //zeroScalar for black
        Mat canvasMat = new Mat(size, size, originalMat.type(), zeroScalar);

        int offsetX = (size - originalWidth) / 2;
        int offsetY = (size - originalHeight) / 2;

        roi.x = 100;
        roi.y = 0;
        roi.width = originalWidth;
        roi.height = originalHeight;

        originalMat.copyTo(canvasMat.submat(roi));


        originalMat.release();

        return canvasMat;
    }

    public Mat verticalFlip(Mat originalMat) {
        Mat flippedImage = new Mat();
        Core.flip(originalMat, flippedImage, 0);
        originalMat.release();
        return flippedImage;
    }

    public Mat horizontalFlip(Mat originalMat) {
        Mat flippedImage = new Mat();
        Core.flip(originalMat, flippedImage, 1);
        originalMat.release();
        return flippedImage;
    }

    public Mat adjustImageBrightnessAndContrast(@NotNull Mat originalMat, ImagingParams imagingParams) {
        Mat output = new Mat();

        originalMat.convertTo(output, -1,imagingParams.getWindowWidth() , imagingParams.getWindowLevel());

        output = applyDenoisingLatest(output, imagingParams.getDenoise());
        output = applySharpnessLatest(output, imagingParams.getSharpness());

        originalMat.release();

        return output;
    }

    public Mat rotateImage(Mat originalMat, double angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);

        Point imageCenter = new Point(originalMat.cols() / 2.0, originalMat.rows() / 2.0);

        Mat rotationMatrix = Imgproc.getRotationMatrix2D(imageCenter, angleDegrees, 1.0);

        Mat rotatedImage = new Mat();
        Imgproc.warpAffine(originalMat, rotatedImage, rotationMatrix, originalMat.size(), Imgproc.INTER_LINEAR);

        originalMat.release();
        rotationMatrix.release();

        return rotatedImage;
    }

    public Mat zoomImage(Mat originalMat, double zoom) {
        double zoomFactor = 1 / zoom;
        int newWidth = (int) (originalMat.cols() * zoomFactor);
        int newHeight = (int) (originalMat.rows() * zoomFactor);
        int x = Math.max((originalMat.cols() - newWidth) / 2, 0);
        int y = Math.max((originalMat.rows() - newHeight) / 2, 0);
        int width = Math.min(newWidth, originalMat.cols() - x);
        int height = Math.min(newHeight, originalMat.rows() - y);

        Rect cropRect = new Rect(x, y, width, height);
        Mat croppedMat = new Mat(originalMat, cropRect);

        Mat resizedMat = new Mat();
        Imgproc.resize(croppedMat, resizedMat, originalMat.size());

        croppedMat.release();
        originalMat.release();

        return resizedMat;
    }

//    public Mat frameAverage(Mat originalMat, int frameCountFactor) {
//
//
//    }


    public Mat frameAverage(Mat originalMat, int frameCountFactor) {
        if (averageFrame == null) {
            averageFrame = new Mat(originalMat.size(), CvType.CV_16UC1, zeroScalar);
        }

        if (sumFrame == null) {
            sumFrame = new Mat(originalMat.size(), CvType.CV_16UC1, zeroScalar);
        }

        if (frameCountFactor <= 0) {
            return originalMat;
        }

        if (frameCountFactor != frameCount) {
            frameCount = frameCountFactor;
            averageFrame.setTo(zeroScalar);
            sumFrame.setTo(zeroScalar);
            frames.clear();
        }

        Mat floatFrame = new Mat();
        originalMat.convertTo(floatFrame, CvType.CV_16UC1);

        frames.add(floatFrame.clone());

        Core.add(sumFrame, floatFrame, sumFrame);

        if (frames.size() > frameCount) {
            Mat oldestFrame = frames.removeFirst();

            Core.subtract(sumFrame, oldestFrame, sumFrame);
            oldestFrame.release();
        }

        if (frames.size() > 1) {
            Core.divide(sumFrame, new Scalar(frames.size()), averageFrame);
        }

        floatFrame.release();


        Mat normalizedResult = new Mat();
        Core.normalize(averageFrame, normalizedResult, 0, 255, Core.NORM_MINMAX);
        normalizedResult.convertTo(normalizedResult, CvType.CV_8UC1);

        return normalizedResult;

    }


    public Bilateral bilateral = new Bilateral();
    public USM usm = new USM();

    public Mat applySharpnessLatest(Mat inputMat,double sharpnessValue) {
        return usm.apply16BitImage(inputMat, (int) sharpnessValue, 1.0d, 3.0, 0);
    }

    public Mat applyDenoisingLatest(Mat inputMat,double denoiseValue) {
        return bilateral.apply16BitImage(inputMat, (int) denoiseValue, 75, 75);
    }




}
