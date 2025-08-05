package com.raymedis.rxviewui.service.study;

import com.jfoenix.controls.JFXSlider;
import com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource.CanvasMainChange;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageConversionService;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ProcessingToolsService {

    @FXML public JFXSlider denoiseSlider;
    @FXML public JFXSlider sharpnessSlider;
    @FXML public JFXSlider contrastSlider;
    @FXML public JFXSlider brightnessSlider;

    private final Logger logger = LoggerFactory.getLogger(ProcessingToolsService.class);


    private static final ProcessingToolsService instance = new ProcessingToolsService();
    public Canvas histogramCanvas;

    public static ProcessingToolsService getInstance() {
        return instance;
    }

    public void process(JFXSlider slider) {
        if (slider.equals(denoiseSlider)) {
            logger.info("Processing Denoise Slider with value: {}", slider.getValue());

            CanvasMainChange.mainDrawTool.denoise= slider.getValue();
            CanvasMainChange.mainDrawTool.imageProcess();

        } else if (slider.equals(sharpnessSlider)) {
            logger.info("Processing Sharpness Slider with value: {}", slider.getValue());

            if (slider.getValue() % 2 != 0) {
                CanvasMainChange.mainDrawTool.sharpness= slider.getValue();
                CanvasMainChange.mainDrawTool.imageProcess();
            } else {
                double val = slider.getValue() + 1;
                if ((val) <= slider.getMax()) {
                    CanvasMainChange.mainDrawTool.sharpness = val;
                    CanvasMainChange.mainDrawTool.imageProcess();
                    slider.setValue(val);
                } else {
                    CanvasMainChange.mainDrawTool.sharpness = slider.getValue()-1;
                    CanvasMainChange.mainDrawTool.imageProcess();
                    slider.setValue(slider.getValue() - 1);
                }
            }

        } else if (slider.equals(contrastSlider)) {
            //logger.info("Processing Contrast Slider with value: {}", slider.getValue());
            CanvasMainChange.mainDrawTool.contrastChangeFromSlider(slider.getValue());
        } else if (slider.equals(brightnessSlider)) {
            //logger.info("Processing Brightness Slider with value: {}", slider.getValue());
            CanvasMainChange.mainDrawTool.brightnessChangeFromSlider(slider.getValue());
        }



    }

    public static void drawHistogramOnCanvas8(Mat image) {
        Canvas canvas = ProcessingToolsService.getInstance().histogramCanvas;
        if (image == null || image.empty() || image.type() != CvType.CV_16U) {
            System.err.println("Image must be a non-empty 16-bit grayscale (CV_16U) Mat.");
            return;
        }

        int histSize = 256; // You can change this
        float rangeStart = 0f;
        float rangeEnd = 65536f; // Full 16-bit range
        Mat hist = new Mat();

        Imgproc.calcHist(
                Arrays.asList(image),
                new MatOfInt(0),
                new Mat(),
                hist,
                new MatOfInt(histSize),
                new MatOfFloat(rangeStart, rangeEnd)
        );

        // Normalize for display
        Core.normalize(hist, hist, 0, canvas.getHeight(), Core.NORM_MINMAX);

        // Drawing
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setStroke(Color.DEEPSKYBLUE);
        gc.setLineWidth(2);

        int binWidth = (int) (canvas.getWidth() / histSize);
        for (int i = 1; i < histSize; i++) {
            double y1 = canvas.getHeight() - hist.get(i - 1, 0)[0];
            double y2 = canvas.getHeight() - hist.get(i, 0)[0];

            gc.strokeLine(
                    (i - 1) * binWidth, y1,
                    i * binWidth, y2
            );
        }

        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        WritableImage snapshot = canvas.snapshot(snapshotParameters, null);
        // Save the histogram as an image file

        Imgcodecs.imwrite("histogram.png", ImageConversionService.writableImageToMat(snapshot));
    }

    public static void drawHistogramOnCanvas(Mat image) {
        Canvas canvas = ProcessingToolsService.getInstance().histogramCanvas;
        if (image == null || image.empty() ) {
            System.err.println("Image must be a non-empty 16-bit grayscale (CV_16U) Mat.");
            return;
        }

        int[] histogram = new int[256];

        // Fill histogram by scaling 16-bit to 8-bit
        for (int row = 0; row < image.rows(); row++) {
            for (int col = 0; col < image.cols(); col++) {
                int val16 = (int) image.get(row, col)[0]; // 16-bit unsigned
                int val8 = val16 >> 8; // Equivalent to val16 / 256
                histogram[val8]++;
            }
        }

        // Normalize for drawing
        int maxCount = 0;
        for (int count : histogram) {
            if (count > maxCount) maxCount = count;
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        double binWidth = width / 256.0;

        // Clear canvas with transparent background
        gc.clearRect(0, 0, width, height);

        // Prepare points for filled polygon
        double[] xPoints = new double[256 + 2];
        double[] yPoints = new double[256 + 2];

        // Start at bottom-left corner
        xPoints[0] = 0;
        yPoints[0] = height;

        // Histogram points
        for (int i = 0; i < 256; i++) {
            xPoints[i + 1] = i * binWidth;
            double normalizedHeight = ((double) histogram[i] / maxCount) * height;
            yPoints[i + 1] = height - normalizedHeight;
        }

        // End at bottom-right corner
        xPoints[257] = width;
        yPoints[257] = height;

        // Draw filled histogram shape
        gc.setStroke(new Color(0.929, 0.384, 0.110, 1.0));
        gc.setLineWidth(2);
        gc.strokePolygon(xPoints, yPoints, xPoints.length);
    }





}
