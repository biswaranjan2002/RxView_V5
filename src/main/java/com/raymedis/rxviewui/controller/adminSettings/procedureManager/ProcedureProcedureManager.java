package com.raymedis.rxviewui.controller.adminSettings.procedureManager;

import com.jfoenix.controls.JFXButton;
import com.raymedis.rxviewui.service.adminSettings.procedureManager.ProcedureManagerCategoryController;
import com.raymedis.rxviewui.service.adminSettings.procedureManager.ProcedureManagerProcedureController;
import com.raymedis.rxviewui.service.adminSettings.procedureManager.ProcedureManagerProjectionController;
import com.raymedis.rxviewui.service.adminSettings.procedureManager.ProcedureManagerStepController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.sql.SQLException;

public class ProcedureProcedureManager {


    public VBox contentNode;

    public JFXButton categoryButton;
    public JFXButton procedureButton;
    public JFXButton stepButton;
    public JFXButton bodyPartProjectionButton;


    Parent categoryParent;
    Parent ProcedureParent;
    Parent stepParent;
    Parent bodyProjectionParent;

    public void initialize() throws IOException {
        FXMLLoader category = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/ProcedureManagerCategory.fxml"));
        categoryParent = category.load();

        FXMLLoader procedure = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/ProcedureManagerProcedure.fxml"));
        ProcedureParent = procedure.load();

        FXMLLoader step = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/ProcedureManagerStep.fxml"));
        stepParent = step.load();

        FXMLLoader bodyPartProjection = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/ProcedureManagerProjection.fxml"));
        bodyProjectionParent = bodyPartProjection.load();


        categoryClick();
    }

    public void resetButtons() {
        categoryButton.getStyleClass().removeAll("unSelectedButton","selectedButton");
        procedureButton.getStyleClass().removeAll("unSelectedButton","selectedButton");
        stepButton.getStyleClass().removeAll("unSelectedButton","selectedButton");
        bodyPartProjectionButton.getStyleClass().removeAll("unSelectedButton","selectedButton");


        categoryButton.getStyleClass().add("unSelectedButton");
        procedureButton.getStyleClass().add("unSelectedButton");
        stepButton.getStyleClass().add("unSelectedButton");
        bodyPartProjectionButton.getStyleClass().add("unSelectedButton");

        try {
            ProcedureManagerCategoryController.getInstance().loadData();
            ProcedureManagerProjectionController.getInstance().loadData();
            ProcedureManagerStepController.getInstance().loadData();
            ProcedureManagerProcedureController.getInstance().loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void categoryClick() {
        resetButtons();
        categoryButton.getStyleClass().remove("unSelectedButton");
        categoryButton.getStyleClass().add("selectedButton");
        contentNode.getChildren().setAll(categoryParent);
    }

    public void procedureClick() {
        resetButtons();
        procedureButton.getStyleClass().remove("unSelectedButton");
        procedureButton.getStyleClass().add("selectedButton");
        contentNode.getChildren().setAll(ProcedureParent);
    }

    public void stepClick() {
        resetButtons();
        stepButton.getStyleClass().remove("unSelectedButton");
        stepButton.getStyleClass().add("selectedButton");
        contentNode.getChildren().setAll(stepParent);
    }

    public void bodyPartProjectionClick() {
        resetButtons();
        bodyPartProjectionButton.getStyleClass().remove("unSelectedButton");
        bodyPartProjectionButton.getStyleClass().add("selectedButton");
        contentNode.getChildren().setAll(bodyProjectionParent);
    }
}
