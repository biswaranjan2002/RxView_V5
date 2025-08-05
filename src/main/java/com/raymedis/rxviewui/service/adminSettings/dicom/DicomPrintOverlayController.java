package com.raymedis.rxviewui.service.adminSettings.dicom;

import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table.PositionType;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table.PrintOverlayEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table.PrintOverlayService;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlayItems_table.PrintOverlayItemsEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlayItems_table.PrintOverlayItemsService;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DicomPrintOverlayController {

    private final static DicomPrintOverlayController instance = new DicomPrintOverlayController();
    public VBox overlayStylesBox;

    public static DicomPrintOverlayController getInstance(){
        return instance;
    }

    public TableView overlayTable;
    public TableColumn overlayNameColumn;

    public TextField overlayNameInput;

    public JFXComboBox logoComboBox;
    public TextField logoPathInput;

    public TableView headerTable;
    public TableColumn headerColumn;

    public TableView topLeftTable;
    public TableColumn topLeftColumn;

    public TableView bottomLeftTable;
    public TableColumn bottomLeftColumn;

    public TableView overlayItemsTable;
    public TableColumn itemsColumn;

    public TableView topRightTable;
    public TableColumn topRightColumn;

    public TableView bottomRightTable;
    public TableColumn bottomRightColumn;

    public Label appliedLabel;

    private PrintOverlayService printOverlayService = PrintOverlayService.getInstance();
    private PrintOverlayItemsService printOverLayItemsService = PrintOverlayItemsService.getInstance();

    private ArrayList<PrintOverlayItemsEntity> itemsList = new ArrayList<>();
    private Map<String,PrintOverlayEntity> overlayEntityMap = new LinkedHashMap<>();

    private String currentSelectedOverlayId = "" ;
;
    public void loadEvents() {
        logoPathInput.setEditable(false);
        itemsColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
         headerColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
         topLeftColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
         topRightColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
         bottomLeftColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
         bottomRightColumn.setCellValueFactory(new PropertyValueFactory<>("item"));

        overlayNameColumn.setCellValueFactory(new PropertyValueFactory<>("overlayName"));


        overlayTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadSelectedLayout();
            }
        });

    }

    private void loadSelectedLayout() {
        addOverlayClick();

        PrintOverlayEntity selectedOverlay = (PrintOverlayEntity) overlayTable.getSelectionModel().getSelectedItem();
        currentSelectedOverlayId = selectedOverlay.getOverlayId();
        overlayNameInput.setText(selectedOverlay.getOverlayName());
        logoPathInput.setText(selectedOverlay.getLogoPath());

        ArrayList<PrintOverlayEntity> overlayEntitiesList = printOverlayService.findAllByOverlayId(selectedOverlay.getOverlayId());
        for (PrintOverlayEntity printOverlayEntity:overlayEntitiesList){
            PositionType positionType = printOverlayEntity.getPositionType();
            String item = printOverlayEntity.getItemContent();
            switch (positionType){
                case HEADER -> headerList.add(printOverLayItemsService.findByItem(item));
                case TOP_LEFT -> topLeftList.add(printOverLayItemsService.findByItem(item));
                case TOP_RIGHT -> topRightList.add(printOverLayItemsService.findByItem(item));
                case BOTTOM_LEFT -> bottomLeftList.add(printOverLayItemsService.findByItem(item));
                case BOTTOM_RIGHT -> bottomRightList.add(printOverLayItemsService.findByItem(item));
                case null -> System.out.println("position type is null");
            }
        }

        headerTable.getItems().addAll(headerList);
        topRightTable.getItems().addAll(topRightList);
        topLeftTable.getItems().addAll(topLeftList);
        bottomLeftTable.getItems().addAll(bottomLeftList);
        bottomRightTable.getItems().addAll(bottomRightList);
    }

    private Set<PrintOverlayItemsEntity> headerList = new LinkedHashSet<>();
    private Set<PrintOverlayItemsEntity> topLeftList = new LinkedHashSet<>();
    private Set<PrintOverlayItemsEntity> topRightList = new LinkedHashSet<>();
    private Set<PrintOverlayItemsEntity> bottomLeftList = new LinkedHashSet<>();
    private Set<PrintOverlayItemsEntity> bottomRightList = new LinkedHashSet<>();

    public void  loadData() {

        overlayStylesBox.setManaged(false);

        try {
            itemsList = printOverLayItemsService.findAll();

            for (PrintOverlayEntity overlay : printOverlayService.findAll()){
                if(!overlayEntityMap.containsKey(overlay.getOverlayId())){
                    overlayEntityMap.put(overlay.getOverlayId(), overlay);
                }
                if(overlay.getIsSelected()){
                    appliedLabel.setText(overlay.getOverlayName());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        overlayItemsTable.getItems().clear();
        overlayItemsTable.getItems().addAll(itemsList);

        overlayTable.getItems().clear();
        overlayTable.getItems().addAll(overlayEntityMap.values());
        addOverlayClick();

        currentSelectedOverlayId = "";
    }

    public void logoSelectionClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Logo Image");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) logoPathInput.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            logoPathInput.setText(file.getAbsolutePath());
        }
    }

    public void addOverlayClick() {
        overlayNameInput.setText("");
        logoPathInput.setText("");

        headerTable.getItems().clear();
        topLeftTable.getItems().clear();
        topRightTable.getItems().clear();
        bottomLeftTable.getItems().clear();
        bottomRightTable.getItems().clear();

        headerList.clear();
        topRightList.clear();
        topLeftList.clear();
        bottomRightList.clear();
        bottomLeftList.clear();

        currentSelectedOverlayId="";
    }

    public void addHeaderItemClick() {
        PrintOverlayItemsEntity selectedItem = (PrintOverlayItemsEntity) overlayItemsTable.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            headerTable.getItems().clear();
            headerList.add(selectedItem);
            headerTable.getItems().addAll(headerList);
        }
    }

    public void addTopLeftClick() {
        PrintOverlayItemsEntity selectedItem = (PrintOverlayItemsEntity) overlayItemsTable.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            topLeftTable.getItems().clear();
            topLeftList.add(selectedItem);
            topLeftTable.getItems().addAll(topLeftList);
        }
    }

    public void addBottomLeftClick() {
        PrintOverlayItemsEntity selectedItem = (PrintOverlayItemsEntity) overlayItemsTable.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            bottomLeftTable.getItems().clear();
            bottomLeftList.add(selectedItem);
            bottomLeftTable.getItems().addAll(bottomLeftList);
        }
    }

    public void addTopRightClick() {
        PrintOverlayItemsEntity selectedItem = (PrintOverlayItemsEntity) overlayItemsTable.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            topRightTable.getItems().clear();
            topRightList.add(selectedItem);
            topRightTable.getItems().addAll(topRightList);
        }
    }

    public void addBottomRightClick() {
        PrintOverlayItemsEntity selectedItem = (PrintOverlayItemsEntity) overlayItemsTable.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            bottomRightTable.getItems().clear();
            bottomRightList.add(selectedItem);
            bottomRightTable.getItems().addAll(bottomRightList);
        }
    }

    public void deleteOverlayClick() throws SQLException {
        PrintOverlayEntity selectedOverlay = (PrintOverlayEntity) overlayTable.getSelectionModel().getSelectedItem();
        appliedLabel.setText("");
        printOverlayService.deleteAllByOverlayId(selectedOverlay.getOverlayId());
        overlayEntityMap.remove(selectedOverlay.getOverlayId());

        for (PrintOverlayEntity overlay : printOverlayService.findAll()){
            if(overlay.getIsSelected()){
                appliedLabel.setText(overlay.getOverlayName());
            }
            if(!overlayEntityMap.containsKey(overlay.getOverlayId())){
                overlayEntityMap.put(overlay.getOverlayId(), overlay);
            }
        }

        overlayTable.getItems().clear();
        overlayTable.getItems().addAll(overlayEntityMap.values());
    }

    public void deleteHeaderItemClick() {
        PrintOverlayItemsEntity selectedItem = (PrintOverlayItemsEntity) headerTable.getSelectionModel().getSelectedItem();
        headerList.remove(selectedItem);

        headerTable.getItems().clear();
        headerTable.getItems().addAll(headerList);
    }

    public void deleteTopLeftClick() {
        PrintOverlayItemsEntity selectedItem = (PrintOverlayItemsEntity) topLeftTable.getSelectionModel().getSelectedItem();
        topLeftList.remove(selectedItem);

        topLeftTable.getItems().clear();
        topLeftTable.getItems().addAll(topLeftList);

    }

    public void deleteBottomLeftClick() {
        PrintOverlayItemsEntity selectedItem = (PrintOverlayItemsEntity) bottomLeftTable.getSelectionModel().getSelectedItem();
        bottomLeftList.remove(selectedItem);

        bottomLeftTable.getItems().clear();
        bottomLeftTable.getItems().addAll(bottomLeftList);
    }

    public void deleteTopRightClick() {
        PrintOverlayItemsEntity selectedItem = (PrintOverlayItemsEntity) topRightTable.getSelectionModel().getSelectedItem();
        topRightList.remove(selectedItem);

        topRightTable.getItems().clear();
        topRightTable.getItems().addAll(topRightList);
    }

    public void deleteBottomRightClick() {
        PrintOverlayItemsEntity selectedItem = (PrintOverlayItemsEntity) bottomRightTable.getSelectionModel().getSelectedItem();
        bottomRightList.remove(selectedItem);

        bottomRightTable.getItems().clear();
        bottomRightTable.getItems().addAll(bottomRightList);
    }

    public void applySelectedOverlayClick() throws SQLException {
        PrintOverlayEntity printOverlayEntity = (PrintOverlayEntity) overlayTable.getSelectionModel().getSelectedItem();

        if(printOverlayEntity==null){
            return;
        }
        for (PrintOverlayEntity overlay:printOverlayService.findAll()){
            overlay.setIsSelected(false);
            printOverlayService.update(overlay.getId(),overlay);
        }

        for (PrintOverlayEntity overlay : printOverlayService.findAllByOverlayId(printOverlayEntity.getOverlayId())){
            overlay.setIsSelected(true);
            printOverlayService.update(overlay.getId(),overlay);
            appliedLabel.setText(overlay.getOverlayName());
        }
    }

    public void saveCurrentOverlayClick() throws SQLException {
        String overlayId = generateId();
        String overlayName = overlayNameInput.getText();

        if(overlayName==null){
            System.out.println("Please Set layout Name");
            return;
        }

        if(!currentSelectedOverlayId.isEmpty()){
            printOverlayService.deleteAllByOverlayId(currentSelectedOverlayId);
            overlayEntityMap.remove(currentSelectedOverlayId);
        }

        Boolean isSelected = false;
        String logoPath = logoPathInput.getText();

        for (PrintOverlayItemsEntity item : headerList){
            PrintOverlayEntity printOverlayEntity = new PrintOverlayEntity();

            printOverlayEntity.setOverlayId(overlayId);
            printOverlayEntity.setOverlayName(overlayName);
            printOverlayEntity.setIsSelected(isSelected);
            printOverlayEntity.setLogoPath(logoPath);
            printOverlayEntity.setPositionType(PositionType.HEADER);
            printOverlayEntity.setItemContent(item.getItem());

            printOverlayService.save(printOverlayEntity);
        }

        for (PrintOverlayItemsEntity item : topLeftList){
            PrintOverlayEntity printOverlayEntity = new PrintOverlayEntity();

            printOverlayEntity.setOverlayId(overlayId);
            printOverlayEntity.setOverlayName(overlayName);
            printOverlayEntity.setIsSelected(isSelected);
            printOverlayEntity.setLogoPath(logoPath);
            printOverlayEntity.setPositionType(PositionType.TOP_LEFT);
            printOverlayEntity.setItemContent(item.getItem());

            printOverlayService.save(printOverlayEntity);
        }

        for (PrintOverlayItemsEntity item : topRightList){
            PrintOverlayEntity printOverlayEntity = new PrintOverlayEntity();

            printOverlayEntity.setOverlayId(overlayId);
            printOverlayEntity.setOverlayName(overlayName);
            printOverlayEntity.setIsSelected(isSelected);
            printOverlayEntity.setLogoPath(logoPath);
            printOverlayEntity.setPositionType(PositionType.TOP_RIGHT);
            printOverlayEntity.setItemContent(item.getItem());

            printOverlayService.save(printOverlayEntity);
        }

        for (PrintOverlayItemsEntity item : bottomRightList){
            PrintOverlayEntity printOverlayEntity = new PrintOverlayEntity();

            printOverlayEntity.setOverlayId(overlayId);
            printOverlayEntity.setOverlayName(overlayName);
            printOverlayEntity.setIsSelected(isSelected);
            printOverlayEntity.setLogoPath(logoPath);
            printOverlayEntity.setPositionType(PositionType.BOTTOM_RIGHT);
            printOverlayEntity.setItemContent(item.getItem());

            printOverlayService.save(printOverlayEntity);
        }

        for (PrintOverlayItemsEntity item : bottomLeftList){
            PrintOverlayEntity printOverlayEntity = new PrintOverlayEntity();

            printOverlayEntity.setOverlayId(overlayId);
            printOverlayEntity.setOverlayName(overlayName);
            printOverlayEntity.setIsSelected(isSelected);
            printOverlayEntity.setLogoPath(logoPath);
            printOverlayEntity.setPositionType(PositionType.BOTTOM_LEFT);
            printOverlayEntity.setItemContent(item.getItem());

            printOverlayService.save(printOverlayEntity);
        }

        ArrayList<PrintOverlayEntity> allRecords = printOverlayService.findAll();

        for (PrintOverlayEntity overlay : allRecords){
            if(!overlayEntityMap.containsKey(overlay.getOverlayId())){
                overlayEntityMap.put(overlay.getOverlayId(),overlay);
            }
        }

        overlayTable.getItems().clear();
        overlayTable.getItems().addAll(overlayEntityMap.values());

        addOverlayClick();
    }

    public static String generateId() {
        LocalDateTime now = LocalDateTime.now();
        String dateTimeString = now.format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        return dateTimeString;
    }
}
