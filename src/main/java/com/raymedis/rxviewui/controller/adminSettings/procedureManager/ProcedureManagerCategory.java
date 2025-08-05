package com.raymedis.rxviewui.controller.adminSettings.procedureManager;

import com.raymedis.rxviewui.service.adminSettings.procedureManager.ProcedureManagerCategoryController;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class ProcedureManagerCategory {

    public ImageView anatomyImageBox;
    public StackPane anatomyBox;
    public Label anatomyNameLabel;
    public VBox bodyPartsBox;
    public VBox outerBodyPartsBox;


    private ProcedureManagerCategoryController procedureManagerCategoryController;

    public void initialize(){
        procedureManagerCategoryController = ProcedureManagerCategoryController.getInstance();

        procedureManagerCategoryController.anatomyNameLabel =anatomyNameLabel;
        procedureManagerCategoryController.anatomyImageBox =anatomyImageBox;
        procedureManagerCategoryController.anatomyBox = anatomyBox;
        procedureManagerCategoryController.bodyPartsBox = bodyPartsBox;
        procedureManagerCategoryController.outerBodyPartsBox =outerBodyPartsBox;

        procedureManagerCategoryController.loadAllEvents();
        procedureManagerCategoryController.loadData();
    }


    public void backwardSelection(ActionEvent actionEvent) {
        procedureManagerCategoryController.backwardSelection();
    }

    public void forwardSelection(ActionEvent actionEvent) {
        procedureManagerCategoryController.forwardSelection();
    }

}
