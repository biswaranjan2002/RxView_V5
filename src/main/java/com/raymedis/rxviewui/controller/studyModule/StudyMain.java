package com.raymedis.rxviewui.controller.studyModule;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import com.raymedis.rxviewui.ViewBox;
import com.raymedis.rxviewui.service.study.StudyMainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.List;

public class StudyMain {

    @FXML public JFXButton bodyButton;
    @FXML public JFXButton toolsButton;

    @FXML public VBox BodyToolsContainer;

    @FXML public JFXButton selectButton;
    @FXML public JFXButton zoomButton;
    @FXML public JFXButton panButton;
    @FXML public JFXButton contrastButton;
    @FXML public JFXButton brightnessButton;
    @FXML public JFXButton cropButton;
    @FXML public JFXButton flipButton;
    @FXML public JFXButton inversionButton;
    @FXML public JFXButton cwButton;
    @FXML public JFXButton ccwButton;
    @FXML public JFXButton fitButton;
    @FXML public JFXButton distanceButton;
    @FXML public JFXButton angleButton;
    @FXML public JFXButton rightLabelButton;
    @FXML public JFXButton leftLabelButton;
    @FXML public JFXButton hideButton;
    @FXML public JFXButton deleteButton;
    @FXML public JFXButton textButton;
    @FXML public JFXButton aec1Button;
    @FXML public JFXButton aec2Button;
    @FXML public JFXButton aec3Button;
    @FXML public JFXButton exposeButton;

    @FXML public VBox consoleToolBox;
    @FXML public JFXButton consoleOnButton;
    @FXML public JFXButton consoleOffButton;

    @FXML public Label kvValueLabel;
    @FXML public Label secValueLabel;
    @FXML public Label masValueLabel;
    @FXML public Label maValueLabel;

    @FXML public VBox bodyPartTabPane;
    @FXML public HBox tabPane;


    public JFXButton aedReadyButton;
    public FontIcon aedReadyLabel;
    public StackPane canvasStackPane;
    public Canvas drawingCanvas;
    public Pane textPane;
    public VBox toolsBox;
    public ViewBox canvasViewBox;

    public VBox topLeftOverlayBox;
    public VBox topRightOverlayBox;
    public VBox bottomLeftOverlayBox;
    public VBox bottomRightOverlayBox;
    public GridPane overlayGridPane;
    public JFXButton pacsServerButton;
    public JFXButton resetButton;
    public JFXButton exportButton;
    public JFXButton printButton;
    public JFXButton editButton;
    public JFXButton emailButton;
    public JFXButton rejectStudyButton;
    public JFXButton editProjection;
    public JFXButton bodyPartDeleteButton;
    public ViewBox overlayViewBox;
    public Pane canvasPane;

    public JFXToggleButton toggleLbd;
    public JFXToggleButton toggleMas;
    public FontIcon faultIcon;
    public JFXButton kvIncButton;
    public JFXButton kvDecButton;
    public JFXButton maDecButton;
    public JFXButton maIncButton;
    public JFXButton masDecButton;
    public JFXButton masIncButton;
    public JFXButton msDecButton;
    public JFXButton msIncButton;


    Parent bodyParent;
    Parent toolsParent;


    private final StudyMainController studyMainController = StudyMainController.getInstance();

    public void initialize() throws IOException {

        BodyToolsContainer.requestFocus();
        FXMLLoader tools = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/Tools.fxml"));
        toolsParent = tools.load();

        FXMLLoader body = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/StudyBodyParts.fxml"));
        bodyParent = body.load();


        overlayGridPane.setMouseTransparent(true);

        List<JFXButton> buttonList = List.of(
                selectButton, zoomButton, panButton, brightnessButton, cropButton,
                flipButton, inversionButton, cwButton, ccwButton, fitButton,
                distanceButton, angleButton, rightLabelButton, leftLabelButton,
                hideButton, deleteButton, textButton,contrastButton
        );


        studyMainController.bodyButton = bodyButton;
        studyMainController.toolsButton = toolsButton;

        studyMainController.BodyToolsContainer = BodyToolsContainer;

        studyMainController.selectButton = selectButton;
        studyMainController.zoomButton = zoomButton;
        studyMainController.panButton = panButton;
        studyMainController.brightnessButton = brightnessButton;
        studyMainController.cropButton = cropButton;
        studyMainController.flipButton = flipButton;
        studyMainController.inversionButton = inversionButton;
        studyMainController.cwButton = cwButton;
        studyMainController.ccwButton = ccwButton;
        studyMainController.fitButton = fitButton;
        studyMainController.distanceButton = distanceButton;
        studyMainController.angleButton = angleButton;
        studyMainController.rightLabelButton = rightLabelButton;
        studyMainController.leftLabelButton = leftLabelButton;
        studyMainController.hideButton = hideButton;
        studyMainController.deleteButton = deleteButton;
        studyMainController.textButton = textButton;
        studyMainController.aec1Button = aec1Button;
        studyMainController.aec2Button = aec2Button;
        studyMainController.aec3Button = aec3Button;
        studyMainController.exposeButton = exposeButton;
        studyMainController.resetButton = resetButton;

        studyMainController.consoleToolBox = consoleToolBox;
        studyMainController.consoleOnButton = consoleOnButton;
        studyMainController.consoleOffButton = consoleOffButton;
        studyMainController.contrastButton=contrastButton;

        studyMainController.kvValueLabel = kvValueLabel;
        studyMainController.msValueLabel = secValueLabel;
        studyMainController.masValueLabel = masValueLabel;
        studyMainController.maValueLabel = maValueLabel;

        studyMainController.bodyPartTabPane = bodyPartTabPane;
        studyMainController.tabPane = tabPane;

        studyMainController.aedReadyButton = aedReadyButton;
        studyMainController.aedReadyLabel = aedReadyLabel;

        studyMainController.canvasStackPane=canvasStackPane;
        studyMainController.drawingCanvas=drawingCanvas;
        studyMainController.textPane=textPane;
        studyMainController.toolsBox = toolsBox;
        studyMainController.canvasViewBox=canvasViewBox;
        studyMainController.bodyParent = bodyParent;
        studyMainController.toolsParent = toolsParent;
        studyMainController.buttonList= buttonList;

        studyMainController.topLeftOverlayBox=topLeftOverlayBox;
        studyMainController.topRightOverlayBox=topRightOverlayBox;
        studyMainController.bottomLeftOverlayBox=bottomLeftOverlayBox;
        studyMainController.bottomRightOverlayBox=bottomRightOverlayBox;
        studyMainController.overlayGridPane=overlayGridPane;
        studyMainController.overlayViewBox=overlayViewBox;

        studyMainController.exportButton=exportButton;
        studyMainController.printButton=printButton;
        studyMainController.editButton=editButton;
        studyMainController.emailButton=emailButton;
        studyMainController.rejectStudyButton=rejectStudyButton;
        studyMainController.editProjection=editProjection;
        studyMainController.bodyPartDeleteButton=bodyPartDeleteButton;

        studyMainController.canvasPane = canvasPane;

        studyMainController.lbdButton =toggleLbd;
        studyMainController.masButton =toggleMas;
        studyMainController.faultIcon=faultIcon;
        studyMainController.kvIncButton=kvIncButton;
        studyMainController.kvDecButton=kvDecButton;
        studyMainController.maDecButton=maDecButton;
        studyMainController.maIncButton=maIncButton;
        studyMainController.masDecButton=masDecButton;
        studyMainController.masIncButton=masIncButton;
        studyMainController.msDecButton=msDecButton;
        studyMainController.msIncButton=msIncButton;

        studyMainController.studyLoad();
        studyMainController.loadEvents();
    }



    public void bodyClick() {
        studyMainController.bodyClick();
    }

    public void toolsClick() {
        studyMainController.toolsClick();
    }

    public void selectClick() {
        studyMainController.selectClick();
    }

    public void zoomClick() {
        studyMainController.zoomClick();
    }

    public void panClick() {
        studyMainController.panClick();
    }

    public void brightnessClick() {
        studyMainController.brightnessClick();
    }

    public void contrastClick( ) {
        studyMainController.contrastClick();
    }

    public void cropClick() {
        studyMainController.cropClick();
    }

    public void flipClick() {
        studyMainController.flipClick();
    }

    public void windowLevelClick() {
        studyMainController.windowLevelClick();
    }

    public void cwClick() {
        studyMainController.cwClick();
    }

    public void ccwClick() {
        studyMainController.ccwClick();
    }

    public void fitClick() {
        studyMainController.fitClick();
    }

    public void distanceClick() {
        studyMainController.distanceClick();
    }

    public void angleClick() {
        studyMainController.angleClick();
    }

    public void rightLabelClick() {
        studyMainController.rightLabelClick();
    }

    public void leftLabelClick() {
        studyMainController.leftLabelClick();
    }

    public void hideClick() {
        studyMainController.hideClick();
    }

    public void deleteClick() {
        studyMainController.deleteClick();
    }

    public void textClick() {
        studyMainController.textClick();
    }

    public void aec1Click() {
        studyMainController.aec1Click();
    }

    public void aec2Click() {
        studyMainController.aec2Click();
    }

    public void aec3Click() {
        studyMainController.aec3Click();
    }

    public void exposeClick() {
        studyMainController.exposeClick();
    }

    public void consoleOnClick() {
        studyMainController.consoleOnClick();
    }

    public void consoleOffClick() {
        studyMainController.consoleOffClick();
    }

    public void kvDecreaseClick() {
        studyMainController.kvDecreaseClick();
    }

    public void kvIncreaseClick() {
        studyMainController.kvIncreaseClick();
    }

    public void maDecreaseClick() {
        studyMainController.maDecreaseClick();
    }

    public void maIncreaseClick() {
        studyMainController.maIncreaseClick();
    }

    public void masDecreaseClick() {
        studyMainController.masDecreaseClick();
    }

    public void masIncreaseClick() {
        studyMainController.masIncreaseClick();
    }

    public void secDecreaseClick() {
        studyMainController.msDecreaseClick();
    }

    public void secIncreaseClick() {
        studyMainController.msIncreaseClick();
    }

    public void deleteSelectedBodyPart() {
        studyMainController.deleteSelectedBodyPart();
    }

    public void saveAllStudies() throws IOException {
        studyMainController.saveAllStudies();
    }

    public void aedReadyClick() {
        studyMainController.aedReadyClick();
    }

    public void saveCurrentStudy() throws IOException {
        studyMainController.saveCurrentStudy();
    }

    public void pacsServerClick() {
        studyMainController.pacsServerClick();
    }

    public void resetClick() {
        studyMainController.resetClick();
    }

    public void exportClick() {
        studyMainController.exportClick();
    }

    public void printClick() {
        studyMainController.printClick();
    }

    public void editClick() {
        studyMainController.editClick();
    }

    public void emailClick() {
        studyMainController.emailClick();
    }

    public void rejectStudyClick() {
        studyMainController.rejectStudyClick();
    }

    public void editSelectedBodyPart() {
        studyMainController.editSelectedBodyPart();
    }

    public void toggleLbdClick() {
        //studyMainController.toggleLbdClick();
    }

}
