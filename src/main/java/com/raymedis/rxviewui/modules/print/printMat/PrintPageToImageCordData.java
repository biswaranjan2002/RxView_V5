package com.raymedis.rxviewui.modules.print.printMat;

import com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource.IDrawTools;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageConversionService;
import com.raymedis.rxviewui.modules.print.Layout.LayoutTab;
import com.raymedis.rxviewui.modules.print.printPage.PrintPage;
import com.raymedis.rxviewui.service.print.PrintController;
import com.raymedis.rxviewui.service.print.PrintService;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrintPageToImageCordData {

    private static final PrintPageToImageCordData instance = new PrintPageToImageCordData();
    public static PrintPageToImageCordData getInstance() {
        return instance;
    }

    private final Logger logger = LoggerFactory.getLogger(PrintPageToImageCordData.class);
    private final List<PrintImageCordData> printImageCordDataList = new ArrayList<>();

    public List<PrintImageCordData> createImageCordData(PrintPage printPage){

        printImageCordDataList.clear();

        String layoutCode = printPage.getLayoutCode();


        Pattern pattern = Pattern.compile("^(STANDARD|ROW|COL)\\\\(\\d+),(\\d+)(?:,(\\d+))?(?:,(\\d+))?$");
        Matcher matcher = pattern.matcher(layoutCode);

        if (matcher.find()) {
            String type = matcher.group(1);
            int value1 = Integer.parseInt(matcher.group(2));
            int value2 = Integer.parseInt(matcher.group(3));
            Integer value3 = matcher.group(4) != null ? Integer.parseInt(matcher.group(4)) : null;
            Integer value4 = matcher.group(5) != null ? Integer.parseInt(matcher.group(5)) : null;


            // Validate that STANDARD layout only has exactly two numbers.
            if ("STANDARD".equals(type) && (value3 != null || value4 != null)) {
                logger.info("Invalid layout format: STANDARD layout must have exactly 2 values.");
                return null;
            }

            switch (type) {
                case "STANDARD":
                    standardToPrintImageCord(printPage, value1, value2);
                    break;
                case "ROW":
                    if (value3 != null && value4 != null) {
                        rowToPrintImageCord(printPage, value1, value2,value3,value4);
                    } else {
                        if (value3 != null) {
                            rowToPrintImageCord(printPage, value1, value2,value3);
                        } else {
                            rowToPrintImageCord(printPage, value1, value2);
                        }
                    }
                    break;
                case "COL":
                    if (value3 != null && value4 != null) {
                        colToPrintImageCord(printPage, value1, value2,value3,value4);
                    } else {
                        if (value3 != null) {
                            colToPrintImageCord(printPage, value1, value2,value3);
                        } else {
                            colToPrintImageCord(printPage, value1, value2);
                        }
                    }
                    break;
                default:
                    logger.info("Unknown layout type");
            }
        }
        else {
            logger.info("Invalid layout format");
        }


        return printImageCordDataList;

    }

    public WritableImage getAnnotationMat(PrintPage printPage) {
        List<LayoutTab> layoutTabs = printPage.getLayoutTabHandler().getAllLayoutTabs();

        printPage.getLayoutTabHandler().setSelectLayout(null);

        for (LayoutTab layoutTab : layoutTabs) {
            if (layoutTab.getPatientPrintImageData() != null) {
                layoutTab.getHandleLayoutImageProcessing().getPrintCanvasMainChange().displayImage = null;
                layoutTab.getHandleLayoutImageProcessing().getPrintCanvasMainChange().redrawPrintAnnotations();
                layoutTab.getOverlayGridPane().setVisible(false);
            }
        }

        PrintController.getInstance().pageGridPane.setBackground(Background.EMPTY);
        makeTransparent(PrintController.getInstance().pageGridPane);


        // Step 1: Take snapshot
        WritableImage snapshot = captureOverlaySnapshot(PrintController.getInstance().pageGridPane);


        //reset the data
        PrintService.getInstance().pageMap.get(printPage.getId()).fire();

        return snapshot;
    }

    public WritableImage getInformationMat(PrintPage printPage) {

        List<LayoutTab> layoutTabs = printPage.getLayoutTabHandler().getAllLayoutTabs();

        printPage.getLayoutTabHandler().setSelectLayout(null);


        PrintController.getInstance().pageGridPane.setBackground(Background.EMPTY);
        makeTransparent(PrintController.getInstance().pageGridPane);

        for (LayoutTab layoutTab : layoutTabs) {
            if (layoutTab.getPatientPrintImageData() != null) {
                layoutTab.getHandleLayoutImageProcessing().getPrintCanvasMainChange().displayImage = null;
                layoutTab.getHandleLayoutImageProcessing().getPrintCanvasMainChange().redraw();
                layoutTab.getDrawingCanvas().setVisible(false);

                layoutTab.overlayLabels.forEach((label)->{

                    double fontSize = 32;
                    String fontFamily = "Century Gothic";

                        label.setStyle(
                        "-fx-font-family: '" + fontFamily + "';" +
                                "-fx-font-weight: bold;" +
                                "-fx-font-size: " + fontSize + "px;" +
                                "-fx-text-fill: white;" +
                                "-fx-padding: 0;"
                );
                });

                layoutTab.getDrawingCanvas().setVisible(false);
            }
        }


        // Step 1: Take snapshot
        WritableImage snapshot = captureOverlaySnapshot(PrintController.getInstance().pageGridPane);

        for (LayoutTab layoutTab : layoutTabs) {
            if (layoutTab.getPatientPrintImageData() != null) {
                layoutTab.getDrawingCanvas().setVisible(true);
            }
        }

        return snapshot;
    }

    public static void makeTransparent(Node node) {
        if (node instanceof Region region) {
            region.setBackground(Background.EMPTY);
            region.setStyle("-fx-background-color: transparent;");
        }

        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                makeTransparent(child);
            }
        }
    }


    private WritableImage captureOverlaySnapshot(Node gridPane) {
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        return gridPane.snapshot(parameters, null);
    }



    private void standardToPrintImageCord(PrintPage printPage, int value1, int value2) {

        double absoluteWidth = 0;
        double absoluteHeight = 0;

        List<LayoutTab> layoutTabs = printPage.getLayoutTabHandler().getAllLayoutTabs();

        int val=0;
        for (int i = 0; i < value2; i++) {
            for (int j = 0; j < value1; j++) {
                if(layoutTabs.get(val).getPatientPrintImageData() != null) {
                    PrintImageCordData printImageCordData = new PrintImageCordData();

                    Rect layoutRect = new Rect();

                    layoutRect.x = (int) (absoluteWidth);
                    layoutRect.y = (int) (absoluteHeight);
                    layoutRect.width = (int) layoutTabs.get(val).getGridPaneWidth();
                    layoutRect.height = (int)layoutTabs.get(val).getGridPaneHeight();


                    Mat mainLayoutMat = new Mat(layoutRect.height, layoutRect.width, CvType.CV_16UC4, new Scalar(0, 0, 0, 65535));

                    Mat org = layoutTabs.get(val).getPatientPrintImageData().getImageMat().clone();
                    Rect imageRect = layoutTabs.get(val).getRect();

                    org = new Mat(org, imageRect);

                    if (org.channels() != 4) {
                        Imgproc.cvtColor(org, org, org.channels() == 3 ? Imgproc.COLOR_BGR2BGRA : Imgproc.COLOR_GRAY2BGRA);
                    }

                    // Convert to 16-bit depth if needed
                    if (org.depth() != CvType.CV_16U) {
                        // Scale 8-bit values (0–255) up to 16-bit (0–65535)
                        double scale = (org.depth() == CvType.CV_8U) ? 256.0 : 1.0;
                        org.convertTo(org, CvType.CV_16UC4, scale);
                    }

                    Imgproc.resize(org, org, new Size(imageRect.width * layoutTabs.get(val).scaleX, imageRect.height* layoutTabs.get(val).scaleY), 0, 0, Imgproc.INTER_AREA);

                    int offsetX = (int) ((layoutTabs.get(val).getGridPaneWidth() - org.width()) / 2);
                    int offsetY = (int) ((layoutTabs.get(val).getGridPaneHeight() - org.height()) / 2);

                    org.copyTo(mainLayoutMat.submat(new Rect(offsetX, offsetY, org.cols(), org.rows())));

                    printImageCordData.setMat(mainLayoutMat);
                    printImageCordData.setLayoutRect(layoutRect);

                    printImageCordDataList.add(printImageCordData);

                }
                else{

                    PrintImageCordData printImageCordData = new PrintImageCordData();

                    Rect layoutRect = new Rect();

                    layoutRect.x = (int) (absoluteWidth);
                    layoutRect.y = (int) (absoluteHeight);
                    layoutRect.width = (int) layoutTabs.get(val).getGridPaneWidth();
                    layoutRect.height = (int)layoutTabs.get(val).getGridPaneHeight();

                    Mat mainLayoutMat = new Mat(layoutRect.height, layoutRect.width, CvType.CV_16UC4, new Scalar(0, 0, 0, 65535));

                    printImageCordData.setMat(mainLayoutMat);
                    printImageCordData.setLayoutRect(layoutRect);

                    printImageCordDataList.add(printImageCordData);

                }
                val++;
                absoluteWidth += layoutTabs.get(val-1).getGridPaneMain().getPrefWidth();

                if(j!=i-1){
                    absoluteWidth+=10;
                }
            }

            absoluteWidth = 0;
            absoluteHeight += layoutTabs.get(val-1).getGridPaneMain().getPrefHeight();
            absoluteHeight +=10;
        }
    }

    private void rowToPrintImageCord(PrintPage printPage, int... vals) {

        double absoluteWidth = 0;
        double absoluteHeight = 0;

        List<LayoutTab> layoutTabs = printPage.getLayoutTabHandler().getAllLayoutTabs();

        int val=0;


        for (int i : vals) {
            for (int j = 0; j < i; j++) {

                if (layoutTabs.get(val).getPatientPrintImageData() != null) {
                    PrintImageCordData printImageCordData = new PrintImageCordData();

                    Rect layoutRect = new Rect();

                    layoutRect.x = (int) (absoluteWidth);
                    layoutRect.y = (int) (absoluteHeight);
                    layoutRect.width = (int) layoutTabs.get(val).getGridPaneWidth();
                    layoutRect.height = (int)layoutTabs.get(val).getGridPaneHeight();

                    Mat mainLayoutMat = new Mat(layoutRect.height, layoutRect.width, CvType.CV_16UC4, new Scalar(0, 0, 0, 65535));

                    Mat org = layoutTabs.get(val).getPatientPrintImageData().getImageMat().clone();
                    Rect imageRect = layoutTabs.get(val).getRect();


                    org = new Mat(org, imageRect);

                    if (org.channels() != 4) {
                        Imgproc.cvtColor(org, org, org.channels() == 3 ? Imgproc.COLOR_BGR2BGRA : Imgproc.COLOR_GRAY2BGRA);
                    }

                    // Step 2: Convert to 16-bit depth if needed
                    if (org.depth() != CvType.CV_16U) {
                        // Scale 8-bit values (0–255) up to 16-bit (0–65535)
                        double scale = (org.depth() == CvType.CV_8U) ? 256.0 : 1.0;
                        org.convertTo(org, CvType.CV_16UC4, scale);
                    }

                    Imgproc.resize(org, org, new Size(imageRect.width * layoutTabs.get(val).scaleX, imageRect.height* layoutTabs.get(val).scaleY), 0, 0, Imgproc.INTER_AREA);

                    int offsetX = (int) ((layoutTabs.get(val).getGridPaneWidth() - org.width()) / 2);
                    int offsetY = (int) ((layoutTabs.get(val).getGridPaneHeight() - org.height()) / 2);

                    org.copyTo(mainLayoutMat.submat(new Rect(offsetX, offsetY, org.cols(), org.rows())));

                    printImageCordData.setMat(mainLayoutMat);
                    printImageCordData.setLayoutRect(layoutRect);

                    printImageCordDataList.add(printImageCordData);


                }
                else{

                    PrintImageCordData printImageCordData = new PrintImageCordData();

                    Rect layoutRect = new Rect();

                    layoutRect.x = (int) (absoluteWidth);
                    layoutRect.y = (int) (absoluteHeight);
                    layoutRect.width = (int) layoutTabs.get(val).getGridPaneWidth();
                    layoutRect.height = (int)layoutTabs.get(val).getGridPaneHeight();

                    Mat mainLayoutMat = new Mat(layoutRect.height, layoutRect.width, CvType.CV_16UC4, new Scalar(0, 0, 0, 65535));

                    printImageCordData.setMat(mainLayoutMat);
                    printImageCordData.setLayoutRect(layoutRect);

                    printImageCordDataList.add(printImageCordData);

                }
                val++;
                absoluteWidth += layoutTabs.get(val - 1).getGridPaneMain().getPrefWidth();

                if(j!=i-1){
                    absoluteWidth+=10;
                }
            }

            absoluteWidth = 0;
            absoluteHeight += layoutTabs.get(val-1).getGridPaneMain().getPrefHeight();
            absoluteHeight +=10;
        }
    }

    private void colToPrintImageCord(PrintPage printPage, int... vals) {
        double absoluteWidth = 0;
        double absoluteHeight = 0;

        List<LayoutTab> layoutTabs = printPage.getLayoutTabHandler().getAllLayoutTabs();

        int val=0;

        for (int i : vals) {
            for (int j = 0; j < i; j++) {

                if (layoutTabs.get(val).getPatientPrintImageData() != null) {
                    PrintImageCordData printImageCordData = new PrintImageCordData();

                    Rect layoutRect = new Rect();

                    layoutRect.x = (int) (absoluteWidth);
                    layoutRect.y = (int) (absoluteHeight);
                    layoutRect.width = (int) layoutTabs.get(val).getGridPaneWidth();
                    layoutRect.height = (int)layoutTabs.get(val).getGridPaneHeight();

                    Mat mainLayoutMat = new Mat(layoutRect.height, layoutRect.width, CvType.CV_16UC4, new Scalar(0, 0, 0, 65535));

                    Mat org = layoutTabs.get(val).getPatientPrintImageData().getImageMat().clone();
                    Rect imageRect = layoutTabs.get(val).getRect();


                    org = new Mat(org, imageRect);

                    if (org.channels() != 4) {
                        Imgproc.cvtColor(org, org, org.channels() == 3 ? Imgproc.COLOR_BGR2BGRA : Imgproc.COLOR_GRAY2BGRA);
                    }

                    // Step 2: Convert to 16-bit depth if needed
                    if (org.depth() != CvType.CV_16U) {
                        // Scale 8-bit values (0–255) up to 16-bit (0–65535)
                        double scale = (org.depth() == CvType.CV_8U) ? 256.0 : 1.0;
                        org.convertTo(org, CvType.CV_16UC4, scale);
                    }

                    Imgproc.resize(org, org, new Size(imageRect.width * layoutTabs.get(val).scaleX, imageRect.height* layoutTabs.get(val).scaleY), 0, 0, Imgproc.INTER_AREA);

                    int offsetX = (int) ((layoutTabs.get(val).getGridPaneWidth() - org.width()) / 2);
                    int offsetY = (int) ((layoutTabs.get(val).getGridPaneHeight() - org.height()) / 2);

                    org.copyTo(mainLayoutMat.submat(new Rect(offsetX, offsetY, org.cols(), org.rows())));

                    printImageCordData.setMat(mainLayoutMat);
                    printImageCordData.setLayoutRect(layoutRect);

                    printImageCordDataList.add(printImageCordData);
                }
                else{

                    PrintImageCordData printImageCordData = new PrintImageCordData();

                    Rect layoutRect = new Rect();

                    layoutRect.x = (int) (absoluteWidth);
                    layoutRect.y = (int) (absoluteHeight);
                    layoutRect.width = (int) layoutTabs.get(val).getGridPaneWidth();
                    layoutRect.height = (int)layoutTabs.get(val).getGridPaneHeight();

                    Mat mainLayoutMat = new Mat(layoutRect.height, layoutRect.width, CvType.CV_16UC4, new Scalar(0, 0, 0, 65535));

                    printImageCordData.setMat(mainLayoutMat);
                    printImageCordData.setLayoutRect(layoutRect);

                    printImageCordDataList.add(printImageCordData);

                }

                val++;
                absoluteHeight += layoutTabs.get(val - 1).getGridPaneMain().getPrefHeight();
                if(j!=i-1){
                    absoluteHeight +=10;
                }

            }

            absoluteHeight = 0;
            absoluteWidth += layoutTabs.get(val-1).getGridPaneMain().getPrefWidth();
            absoluteWidth+=10;
        }
    }


}
