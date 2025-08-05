package com.raymedis.rxviewui.modules.print.Layout;

import com.raymedis.rxviewui.modules.print.printPage.PrintPage;
import com.raymedis.rxviewui.modules.print.printPage.PrintPageHandler;
import javafx.geometry.Pos;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.*;

public class LayoutCreator {

    private Logger logger = LoggerFactory.getLogger(LayoutCreator.class);

    private double globalWidth;
    private double globalHeight;



    public GridPane createLayoutFormat(String layoutCode,double globalWidth,double globalHeight) {

        logger.info("Creating layout with code: {} {} {}", layoutCode, globalWidth, globalHeight);

        this.globalWidth=globalWidth;
        this.globalHeight=globalHeight;
        //logger.info("Input: {}", layoutCode);

        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(globalWidth,globalHeight);
        gridPane.setMinSize(globalWidth,globalHeight);
        gridPane.setMaxSize(globalWidth,globalHeight);


        gridPane.setAlignment(Pos.CENTER);


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

            // Process based on type
            switch (type) {
                case "STANDARD":
                    //logger.info("Processing STANDARD layout with values: {}, {}", value1, value2);
                    gridPane.getChildren().add(standardCreator(value1,value2));
                    break;
                case "ROW":
                    if (value3 != null && value4 != null) {
                        //logger.info("Processing ROW layout with values: {}, {}, {}, {}", value1, value2, value3, value4);
                        gridPane.getChildren().add(rowCreator(value1,value2,value3,value4));
                    } else {
                        // Accept two or three values for ROW
                        if (value3 != null) {
                            //logger.info("Processing ROW layout with values: {}, {}, {}", value1, value2, value3);
                            gridPane.getChildren().add(rowCreator(value1,value2,value3));
                        } else {
                            //logger.info("Processing ROW layout with values: {}, {}", value1, value2);
                            gridPane.getChildren().add(rowCreator(value1,value2));
                        }
                    }
                    break;
                case "COL":
                    if (value3 != null && value4 != null) {
                        //logger.info("Processing COLUMN layout with values: {}, {}, {}, {}", value1, value2, value3, value4);
                        gridPane.getChildren().add(columnCreator(value1,value2,value3,value4));
                    } else {
                        // Accept two or three values for COL
                        if (value3 != null) {
                            //logger.info("Processing COLUMN layout with values: {}, {}, {}", value1, value2, value3);
                            gridPane.getChildren().add(columnCreator(value1,value2,value3));
                        } else {
                            //logger.info("Processing COLUMN layout with values: {}, {}", value1, value2);
                            gridPane.getChildren().add(columnCreator(value1,value2));
                        }
                    }
                    break;
                default:
                    logger.info("Unknown layout type");
            }
        } else {
            logger.info("Invalid layout format");
        }
        return gridPane;
    }

    private VBox standardCreator(int value1, int value2) {
        double width = globalWidth;
        double space = 10*(value2-1);
        globalHeight -= space;
        double height = globalHeight/value2;

        VBox globalGridPane = new VBox();
        globalGridPane.setPrefSize(globalWidth,globalHeight);
        globalGridPane.setMaxSize(globalWidth,globalHeight);
        globalGridPane.setMinSize(globalWidth,globalHeight);
        globalGridPane.setAlignment(Pos.CENTER);
        globalGridPane.setSpacing(10);


        for (int i=0;i<value2;i++) {
            //logger.info("width : {} , height : {} , row : {}", width, height, value1);

            HBox gridPane = createGridPaneForRows(width,height,value1);
            globalGridPane.getChildren().add(gridPane);
        }

        return globalGridPane;
    }

    private VBox rowCreator(int... rows){
        double space = 10*(rows.length-1);
        double width = globalWidth;
        globalHeight=globalHeight-space;
        double height = (globalHeight/rows.length);

        VBox globalGridPane = new VBox();
        globalGridPane.setPrefSize(globalWidth,globalHeight);
        globalGridPane.setMaxSize(globalWidth,globalHeight);
        globalGridPane.setMinSize(globalWidth,globalHeight);
        globalGridPane.setAlignment(Pos.CENTER);
        globalGridPane.setSpacing(10);


        for (int row : rows) {
            //logger.info("width : {} , height : {} , row : {}", width, height, row);

            HBox gridPane = createGridPaneForRows(width,height,row);
            globalGridPane.getChildren().add(gridPane);
        }


        return globalGridPane;
    }

    private HBox columnCreator(int... columns){
        double space = 10*(columns.length-1);
        globalWidth=globalWidth-space;
        double width = globalWidth/columns.length;
        double height = globalHeight;

        HBox globalGridPane = new HBox();
        globalGridPane.setPrefSize(globalWidth,globalHeight);
        globalGridPane.setMaxSize(globalWidth,globalHeight);
        globalGridPane.setMinSize(globalWidth,globalHeight);
        globalGridPane.setAlignment(Pos.CENTER);
        globalGridPane.setSpacing(10);


        for (int col : columns) {
           // logger.info("width : {} , height : {} , col : {}", width, height, col);
            VBox gridPane = createGridPaneForCols(width,height,col);
            globalGridPane.getChildren().add(gridPane);
        }

        return globalGridPane;
    }

    private VBox createGridPaneForCols(double width,double height,int colItems){
        double space = 10*(colItems-1);
        height = height-space;
        VBox vBox = new VBox();
        vBox.setPrefSize(width,height);
        vBox.setMaxSize(width,height);
        vBox.setMinSize(width,height);
        vBox.setAlignment(Pos.CENTER);

        vBox.setSpacing(10);

        for(int i=0;i<colItems;i++){
            double w = width;
            double h = height/colItems;
            //logger.info("width : {} , height : {} ", w, h);
            LayoutTab layoutTab = new LayoutTab(w,h);
            PrintPage printPage = PrintPageHandler.getInstance().getCurrentPrintPage();
            if(printPage!=null){
                printPage.getLayoutTabHandler().addLayout(layoutTab.getId(),layoutTab);
            }

            GridPane gridPane = layoutTab.getGridPaneMain();
            vBox.getChildren().add(gridPane);

            gridPane.setOnMousePressed(Event-> layoutClick(layoutTab));
        }


        return vBox;

    }

    private HBox createGridPaneForRows(double width, double height, int rowItems){
        double space = 10*(rowItems-1);
        width=width-space;
        HBox hBox = new HBox();
        hBox.setPrefSize(width,height);
        hBox.setMaxSize(width,height);
        hBox.setMinSize(width,height);
        hBox.setAlignment(Pos.CENTER);

        hBox.setSpacing(10);

        for(int i=0;i<rowItems;i++){
            double w = width/rowItems;

            LayoutTab layoutTab = new LayoutTab(w, height);

            PrintPage printPage = PrintPageHandler.getInstance().getCurrentPrintPage();
            if(printPage!=null){
                printPage.getLayoutTabHandler().addLayout(layoutTab.getId(),layoutTab);
            }

            GridPane gridPane = layoutTab.getGridPaneMain();
            hBox.getChildren().add(gridPane);

            gridPane.setOnMousePressed(Event-> layoutClick(layoutTab));
        }
        return hBox;
    }



    private void layoutClick(LayoutTab layoutTab) {

        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().setSelectLayout(layoutTab);


        if (layoutTab.getHandleLayoutImageProcessing()!=null && layoutTab.getHandleLayoutImageProcessing().getPrintMainDrawTool().isImageToolEnable) {
            // Remove drag-related handlers
            layoutTab.getCanvasStackPane().setOnDragDetected(null);
            layoutTab.getCanvasStackPane().setOnDragOver(null);
            layoutTab.getCanvasStackPane().setOnDragDropped(null);
            layoutTab.getCanvasStackPane().setOnDragDone(null);
        } else {
            layoutTab.setupDragAndDrop(layoutTab.getPatientPrintImageData());
            layoutTab.getCanvasStackPane().addEventFilter(DragEvent.DRAG_OVER, layoutTab::handleDragOver);
            layoutTab.getCanvasStackPane().setOnDragDropped(layoutTab::handleDragDropped);

        }

    }

}
