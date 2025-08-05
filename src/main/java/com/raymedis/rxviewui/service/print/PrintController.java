package com.raymedis.rxviewui.service.print;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.ViewBox;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_print_table.DicomPrintEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_print_table.DicomPrintService;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printLayout_table.PrintLayoutEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printLayout_table.PrintLayoutService;
import com.raymedis.rxviewui.modules.print.Layout.LayoutIconCreator;
import com.raymedis.rxviewui.modules.print.Layout.LayoutTab;
import com.raymedis.rxviewui.modules.print.Layout.LayoutTabHandler;
import com.raymedis.rxviewui.modules.print.imageProcessing.PrintImageTools;
import com.raymedis.rxviewui.modules.print.printInput.PatientPrintImageData;
import com.raymedis.rxviewui.modules.print.printPage.PrintPageHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;

public class PrintController {

    public VBox layoutBox;

    private final static PrintController instance = new PrintController();

    public GridPane pageGridPane;
    public GridPane mainViewBoxParent;

    public static PrintController getInstance(){
        return instance;
    }

    public String orientation = "";
    private final PrintLayoutService printLayoutService = PrintLayoutService.getInstance();
    private ArrayList<PrintLayoutEntity> layoutList = new ArrayList<>();
    public final LinkedHashMap<String, JFXButton> layoutsButtonMap = new LinkedHashMap<>();


    public ViewBox imageContainerViewBox;
    public VBox toolsBox;
    public JFXButton portraitButton;
    public JFXButton landScapeButton;
    public JFXButton distanceButton;
    public JFXButton angleButton;
    public JFXButton labelLButton;
    public JFXButton labelRButton;
    public JFXButton textButton;
    public JFXButton deleteButton;

    public JFXButton selectButton;
    public JFXButton zoomButton;
    public JFXButton panButton;
    public JFXButton contrastButton;
    public JFXButton fitButton;
    public JFXButton cropButton;
    public JFXButton vFlipButton;
    public JFXButton hFlipButton;
    public JFXButton brightnessButton;
    public JFXButton cwButton;
    public JFXButton ccwButton;
    public JFXButton inversionButton;
    public JFXButton rightLabelButton;
    public JFXButton leftLabelButton;
    public JFXButton hideButton;
    public JFXButton resetButton;

    public VBox bodyPartsContainer;
    public VBox pageContainer;

    public JFXComboBox aeTitleComboBox;
    public JFXComboBox sizeComboBox;

    private final Logger logger = LoggerFactory.getLogger(PrintController.class);
    private final PrintService printService = PrintService.getInstance();
    private final PrintImageTools printImageTools = PrintImageTools.getInstance();

    private final DicomPrintService dicomPrintService = DicomPrintService.getInstance();

    private ArrayList<DicomPrintEntity> printList =  new ArrayList<>();

    public void loadEvents() {
        bodyPartsContainer.setOnDragOver(this::handleDragOver);
        bodyPartsContainer.setOnDragDropped(this::handleDragDropped);
    }

    private void handleDragOver(DragEvent event) {
        // Accept drag over if we have a valid dragged item
        if (PrintService.DragAndDropContext.getDraggedItem() instanceof PatientPrintImageData) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }

    private void handleDragDropped(DragEvent event) {
        boolean success = false;

        Object draggedItem = PrintService.DragAndDropContext.getDraggedItem();

        if (draggedItem instanceof PatientPrintImageData patientPrintImageData) {

            LayoutTabHandler layoutTabHandler = PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler();
            LayoutTab layoutTab = layoutTabHandler.getLayout(patientPrintImageData.getId());

            layoutTab.setPatientPrintImageData(null);
            PrintService.getInstance().reloadDisableButtons();

            PrintService.getInstance().setLayoutForPage(true,false);

            success = true;
        }

        PrintService.DragAndDropContext.clearDraggedItem();

        event.setDropCompleted(success);
        event.consume();
    }


    public String layoutCode ="";
    public DicomPrintEntity defaultPrint;
    public void loadData() {
        layoutBox.getChildren().clear();
        aeTitleComboBox.getItems().clear();
        layoutList.clear();
        layoutsButtonMap.clear();
        printList.clear();


        try {
            layoutList = printLayoutService.findAll();
            printList = dicomPrintService.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (PrintLayoutEntity p : layoutList){
            layoutCode = p.getLayoutName();
            addClick();
        }

        for(DicomPrintEntity p : printList){
            if(p.getIsSelected()){
                defaultPrint = p;
            }
            aeTitleComboBox.getItems().add(p.getAeTitle());
        }

        if(defaultPrint!=null){
            aeTitleComboBox.getSelectionModel().select(defaultPrint.getAeTitle());
        }


        setupImageToolsBtn();
    }


    public void loadPrintSizeAndOrientation(){
        sizeComboBox.getSelectionModel().selectFirst();
        sizeComboBoxSelection();
        portraitClick();



    }

    public void addClick() {
        JFXButton layoutButton = createLayoutButtons();

        if (!layoutsButtonMap.containsKey(layoutCode)) {
            layoutsButtonMap.put(layoutCode, layoutButton);

            int buttonsPerRow = 4;

            // Clear layoutBox and rebuild all rows
            layoutBox.getChildren().clear();
            layoutBox.setSpacing(15);

            HBox currentRow = new HBox(15);
            currentRow.setAlignment(Pos.CENTER_LEFT);
            int count = 0;

            for (JFXButton button : layoutsButtonMap.values()) {
                currentRow.getChildren().add(button);
                count++;

                if (count % buttonsPerRow == 0) {
                    layoutBox.getChildren().add(currentRow);
                    currentRow = new HBox(15);
                    currentRow.setAlignment(Pos.CENTER_LEFT);
                }
            }

            // Add last row if it is not full
            if (!currentRow.getChildren().isEmpty()) {
                layoutBox.getChildren().add(currentRow);
            }

        }
    }

    private JFXButton createLayoutButtons() {
        JFXButton layoutButton = new JFXButton();
        layoutButton.setPrefSize(100, 100);
        layoutButton.setMaxSize(100, 100);
        layoutButton.setMinSize(100, 100);

        layoutButton.setId("unSelectedLayoutButton");

        layoutButton.setText(layoutCode);

        double width = (layoutButton.getPrefWidth()/100)*85;
        double height = (layoutButton.getPrefHeight()/100)*85;

        Node layout = LayoutIconCreator.getInstance().createLayout(layoutCode,width,height);

        layoutButton.setGraphic(layout);
        layoutButton.setContentDisplay(ContentDisplay.CENTER);
        layoutButton.setAlignment(Pos.CENTER);

        layoutButton.setOnAction(event-> {
            handleLayoutClick(layoutButton);
            printService.setLayoutForPage(false,true);
        });
        return layoutButton;
    }

    public static String findKeyByValue(LinkedHashMap<String, JFXButton> map, JFXButton value) {
        for (Map.Entry<String, JFXButton> entry : map.entrySet()) {
            if (entry.getValue().getText().equals(value.getText())) {
                return entry.getKey();
            }
        }
        return null; // Return null if the value is not found
    }

    public void handleLayoutClick(JFXButton layoutButton){
        layoutCode = findKeyByValue(layoutsButtonMap,layoutButton);

        for (JFXButton button : layoutsButtonMap.values()) {
            if (button != layoutButton) {
                button.setId("unSelectedLayoutButton");
            }else{
                button.setId("selectedLayoutButton");
            }
        }

    }

    public void resetOrientation(){
        portraitButton.getStyleClass().removeAll("SelectedToolsButton2", "unSelectedToolsButton2");
        portraitButton.getStyleClass().add("unSelectedToolsButton2");

        landScapeButton.getStyleClass().removeAll("SelectedToolsButton2", "unSelectedToolsButton2");
        landScapeButton.getStyleClass().add("unSelectedToolsButton2");
    }

    public void portraitClick() {
        resetOrientation();
        portraitButton.getStyleClass().remove("unSelectedToolsButton2");
        portraitButton.getStyleClass().add("SelectedToolsButton2");

        orientation= "PORTRAIT";
        printService.portraitClick();
    }

    public void landScapeClick() {
        resetOrientation();
        landScapeButton.getStyleClass().remove("unSelectedToolsButton2");
        landScapeButton.getStyleClass().add("SelectedToolsButton2");

        orientation = "LANDSCAPE";
        printService.landScapeClick();
    }

    private void setupImageToolsBtn() {
        printImageTools.selectButton = selectButton;
        printImageTools.zoomButton = zoomButton;
        printImageTools.panButton = panButton;
        printImageTools.brightnessButton = brightnessButton;
        printImageTools.contrastButton = contrastButton;
        printImageTools.cropButton = cropButton;
        printImageTools.hFlipButton = hFlipButton;
        printImageTools.inversionButton = inversionButton;
        printImageTools.clockwiseRotationButton = cwButton;
        printImageTools.counterClockWiseRotationButton = ccwButton;
        printImageTools.fitButton = fitButton;
        printImageTools.distanceButton = distanceButton;
        printImageTools.angleButton = angleButton;
        printImageTools.rightLabelButton = rightLabelButton;
        printImageTools.leftLabelButton = leftLabelButton;
        printImageTools.hideButton = hideButton;
        printImageTools.deleteButton = deleteButton;
        printImageTools.resetButton=resetButton;
        printImageTools.textButton = textButton;

        printImageTools.toolsBox=toolsBox;
    }

    public void distanceClick() {
        printImageTools.distanceClick();
    }

    public void angleClick() {
        printImageTools.angleClick();
    }

    public void textClick() {
        printImageTools.textClick();
    }

    public void deleteClick() {
        printImageTools.deleteClick();
    }

    public void selectClick() {
        printImageTools.selectClick();
    }

    public void zoomClick() {
        printImageTools.zoomClick();
    }

    public void panClick() {
        printImageTools.panClick();
    }

    public void inversionClick() {
        printImageTools.inversionClick();
    }

    public void fitClick() {
        printImageTools.fitClick();
    }

    public void cropClick() {
        printImageTools.cropClick();
    }

    public void brightnessClick() {
        printImageTools.brightnessClick();
    }

    public void cwClick() {
        printImageTools.cwClick();
    }

    public void ccwClick() {
        printImageTools.ccwClick();
    }

    public void contrastClick() {
        printImageTools.contrastClick();
    }

    public void hFlipClick() {
        printImageTools.hFlipClick();
    }

    public void rightLabelClick() {
        printImageTools.rightLabelClick();
    }

    public void leftLabelClick() {
        printImageTools.leftLabelClick();
    }

    public void hideClick() {
        printImageTools.hideClick();
    }

    public void resetClick() {
        printImageTools.resetClick();
    }

    public void sizeComboBoxSelection() {

        portraitClick();

        int selectedIndex = sizeComboBox.getSelectionModel().getSelectedIndex();

        double width = imageContainerViewBox.getPrefWidth();
        double height = imageContainerViewBox.getPrefHeight();

        //logger.info("oldWidth : {} oldHeight : {} " ,width,height);

        double heightRatio;
        double widthRatio;


        if(selectedIndex==0){
             widthRatio = 8;
             heightRatio = 10;
        }else{
            widthRatio = 14;
            heightRatio = 17;
        }

        double mul;
        if(height>width){
            mul = height/ heightRatio;
        }else{
            mul = width/ heightRatio;
        }

        double newWidth = widthRatio *mul;
        double newHeight = heightRatio *mul;

        //logger.info("mul : {} newWidth : {} newHeight : {} " ,mul,newWidth,newHeight);

        imageContainerViewBox.setPrefSize(newWidth,newHeight);
        imageContainerViewBox.setMaxSize(newWidth,newHeight);
        imageContainerViewBox.setMinSize(newWidth,newHeight);


        printService.setLayoutForPage(true,false);

    }

    public void newPageClick() {
        printService.createPrintPage();
    }

    public void deletePageClick() {
        printService.deletePageClick();
    }



    public void printClick() throws SQLException {

       /* String selectedAeTitle = (String) aeTitleComboBox.getSelectionModel().getSelectedItem();
        DicomPrintEntity dicomPrintEntity = dicomPrintService.findByAeTitle(selectedAeTitle);


        DicomCommunication dicomComm = new DicomCommunication("READY", dicomPrintEntity.getIpAddress(), String.valueOf(dicomPrintEntity.getPort()), selectedAeTitle, "RxView", "STANDARD\\1,1", "PORTRAIT", "8INX10IN", "D:\\Download\\Chest_AP_1_1.dcm");
        DicomPrintManager.getInstance().printDicom(dicomComm);*/

        printService.printClick();

    }

    public void cancelClick() {
        printService.cancelClick();
    }

}
