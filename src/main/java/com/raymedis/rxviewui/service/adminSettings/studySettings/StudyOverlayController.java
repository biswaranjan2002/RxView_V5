package com.raymedis.rxviewui.service.adminSettings.studySettings;

import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table.PositionType;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlayItems_table.PrintOverlayItemsEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlayItems_table.PrintOverlayItemsService;
import com.raymedis.rxviewui.database.tables.adminSettings.study.study_overlay_table.PrintOverlayEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.study.study_overlay_table.StudyOverlayService;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudyOverlayController {

    private static final StudyOverlayController instance = new StudyOverlayController();
    private static final Logger LOGGER = Logger.getLogger(StudyOverlayController.class.getName());

    public static StudyOverlayController getInstance() {
        return instance;
    }


    public TableView<PrintOverlayItemsEntity> topLeftTable;
    public TableColumn<PrintOverlayItemsEntity, String> topLeftColumn;


    public TableView<PrintOverlayItemsEntity> bottomLeftTable;
    public TableColumn<PrintOverlayItemsEntity, String> bottomLeftColumn;

    public TableView<PrintOverlayItemsEntity> overlayItemsTable;
    public TableColumn<PrintOverlayItemsEntity, String> itemsColumn;

    public TableView<PrintOverlayItemsEntity> topRightTable;
    public TableColumn<PrintOverlayItemsEntity, String> topRightColumn;

    public TableView<PrintOverlayItemsEntity> bottomRightTable;
    public TableColumn<PrintOverlayItemsEntity, String> bottomRightColumn;

    private final PrintOverlayItemsService printOverlayItemsService = PrintOverlayItemsService.getInstance();
    private final StudyOverlayService studyOverlayService = new StudyOverlayService();

    private final Set<PrintOverlayItemsEntity> topLeftList = new LinkedHashSet<>();
    private final Set<PrintOverlayItemsEntity> topRightList = new LinkedHashSet<>();
    private final Set<PrintOverlayItemsEntity> bottomLeftList = new LinkedHashSet<>();
    private final Set<PrintOverlayItemsEntity> bottomRightList = new LinkedHashSet<>();

    private PrintOverlayItemsService printOverLayItemsService = PrintOverlayItemsService.getInstance();
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(StudyOverlayController.class);

    private StudyOverlayController() {}

    public void loadEvents() {
        itemsColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        topLeftColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        topRightColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        bottomLeftColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        bottomRightColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
    }

    private ArrayList<PrintOverlayEntity> studyOverlayList;

    public void loadData() {
        try {
            List<PrintOverlayItemsEntity> itemsList = printOverlayItemsService.findAll();
            overlayItemsTable.getItems().setAll(itemsList);


            topLeftList.clear();
            topRightList.clear();
            bottomLeftList.clear();
            bottomRightList.clear();

            topRightTable.getItems().clear();
            topLeftTable.getItems().clear();
            bottomLeftTable.getItems().clear();
            bottomRightTable.getItems().clear();


            studyOverlayList = studyOverlayService.findAll();
            if(studyOverlayList!=null){
                for(PrintOverlayEntity overlay : studyOverlayList){
                    String item = overlay.getItemContent();

                    switch (overlay.getPositionType()){
                        case PositionType.TOP_LEFT:
                            topLeftList.add(printOverLayItemsService.findByItem(item));
                            break;
                        case PositionType.TOP_RIGHT:
                            topRightList.add(printOverLayItemsService.findByItem(item));
                            break;
                        case PositionType.BOTTOM_LEFT:
                            bottomLeftList.add(printOverLayItemsService.findByItem(item));
                            break;
                        case PositionType.BOTTOM_RIGHT:
                            bottomRightList.add(printOverLayItemsService.findByItem(item));
                            break;
                        case HEADER:
                            break;
                        default:
                            logger.info("Invalid Position Type {} ",overlay.getPositionType());
                    }
                }

                topRightTable.getItems().addAll(topRightList);
                topLeftTable.getItems().addAll(topLeftList);
                bottomLeftTable.getItems().addAll(bottomLeftList);
                bottomRightTable.getItems().addAll(bottomRightList);
            }


        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading data", e);
        }
    }

    public void addItemToTable(PrintOverlayItemsEntity selectedItem, Set<PrintOverlayItemsEntity> itemList, TableView<PrintOverlayItemsEntity> table) {
        if (selectedItem != null) {
            itemList.add(selectedItem);
            table.getItems().setAll(itemList);
            saveOverlay();
        }
    }

    public void removeItemFromTable(PrintOverlayItemsEntity selectedItem, Set<PrintOverlayItemsEntity> itemList, TableView<PrintOverlayItemsEntity> table) {
        if (selectedItem != null) {
            itemList.remove(selectedItem);
            table.getItems().setAll(itemList);
            saveOverlay();
        }
    }

    public void addTopLeftClick() {
        addItemToTable(getSelectedItem(overlayItemsTable), topLeftList, topLeftTable);
    }

    public void addTopRightClick() {
        addItemToTable(getSelectedItem(overlayItemsTable), topRightList, topRightTable);
    }

    public void addBottomLeftClick() {
        addItemToTable(getSelectedItem(overlayItemsTable), bottomLeftList, bottomLeftTable);
    }

    public void addBottomRightClick() {
        addItemToTable(getSelectedItem(overlayItemsTable), bottomRightList, bottomRightTable);
    }



    public void deleteTopLeftClick() {
        removeItemFromTable(getSelectedItem(topLeftTable), topLeftList, topLeftTable);
    }

    public void deleteTopRightClick() {
        removeItemFromTable(getSelectedItem(topRightTable), topRightList, topRightTable);
    }

    public void deleteBottomLeftClick() {
        removeItemFromTable(getSelectedItem(bottomLeftTable), bottomLeftList, bottomLeftTable);
    }

    public void deleteBottomRightClick() {
        removeItemFromTable(getSelectedItem(bottomRightTable), bottomRightList, bottomRightTable);
    }

    private PrintOverlayItemsEntity getSelectedItem(TableView<PrintOverlayItemsEntity> table) {
        return table.getSelectionModel().getSelectedItem();
    }

    public void saveOverlay() {
        try {
            ArrayList<PrintOverlayEntity> allRecords = studyOverlayService.findAll();
            for (PrintOverlayEntity printOverlayEntity : allRecords){
                studyOverlayService.delete(printOverlayEntity.getId());
            }

            saveOverlayItems(topLeftList, PositionType.TOP_LEFT);
            saveOverlayItems(topRightList, PositionType.TOP_RIGHT);
            saveOverlayItems(bottomLeftList, PositionType.BOTTOM_LEFT);
            saveOverlayItems(bottomRightList, PositionType.BOTTOM_RIGHT);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving overlay", e);
        }
    }

    private void saveOverlayItems(Set<PrintOverlayItemsEntity> itemList, PositionType positionType) {
        for (PrintOverlayItemsEntity item : itemList) {
            PrintOverlayEntity printOverlayEntity = new PrintOverlayEntity();
            printOverlayEntity.setPositionType(positionType);
            printOverlayEntity.setItemContent(item.getItem());

            try {
                studyOverlayService.save(printOverlayEntity);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error saving overlay item", e);
            }
        }
    }
}
