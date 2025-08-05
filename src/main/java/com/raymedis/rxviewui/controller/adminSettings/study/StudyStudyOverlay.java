package com.raymedis.rxviewui.controller.adminSettings.study;

import com.raymedis.rxviewui.service.adminSettings.studySettings.StudyOverlayController;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class StudyStudyOverlay {



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

    private StudyOverlayController studyOverlayController = StudyOverlayController.getInstance();

    public void initialize(){
        studyOverlayController.topLeftTable=topLeftTable;
        studyOverlayController.topLeftColumn=topLeftColumn;
        studyOverlayController.bottomLeftTable=bottomLeftTable;
        studyOverlayController.bottomLeftColumn=bottomLeftColumn;
        studyOverlayController.overlayItemsTable=overlayItemsTable;
        studyOverlayController.itemsColumn=itemsColumn;
        studyOverlayController.topRightTable=topRightTable;
        studyOverlayController.topRightColumn=topRightColumn;
        studyOverlayController.bottomRightTable=bottomRightTable;
        studyOverlayController.bottomRightColumn=bottomRightColumn;

        studyOverlayController.loadEvents();
        studyOverlayController.loadData();
    }



    public void addTopLeftClick() {
        studyOverlayController.addTopLeftClick();
    }

    public void deleteTopLeftClick() {
        studyOverlayController.deleteTopLeftClick();
    }

    public void addBottomLeftClick() {
        studyOverlayController.addBottomLeftClick();
    }

    public void deleteBottomLeftClick() {
        studyOverlayController.deleteBottomLeftClick();
    }

    public void addTopRightClick() {
        studyOverlayController.addTopRightClick();
    }

    public void deleteTopRightClick() {
        studyOverlayController.deleteTopRightClick();
    }

    public void addBottomRightClick() {
        studyOverlayController.addBottomRightClick();
    }

    public void deleteBottomRightClick() {
        studyOverlayController.deleteBottomRightClick();
    }
}
