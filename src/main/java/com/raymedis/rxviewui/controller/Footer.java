package com.raymedis.rxviewui.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.*;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Footer {

    public JFXButton minimizeButton;

    public FontIcon registerPageIcon;
    public Label registerPage;

    public FontIcon studyPageIcon;
    public Label studyPage;

    public FontIcon dataBasePageIcon;
    public Label dataBasePage;

    public FontIcon printPageIcon;
    public Label printPage;

    public FontIcon adminIcon;
    public Label adminLabel;

    public Label timeLabel;
    public Label dateLabel;
    public Label rxViewLabel;

    public JFXButton adminPageButton;
    public FontIcon fpd1FontIcon;
    public FontIcon fpd2FontIcon;
    public FontIcon fpd3FontIcon;
    public Label pacsUploadStatusLabel;
    public FontIcon batteryFontIcon;
    public FontIcon wifiSignalFontIcon;
    public Label wifiLabel;
    public Label batteryLabel;
    public JFXButton registerPageButton;
    public JFXButton studyPageButton;
    public JFXButton databasePageButton;
    public JFXButton printPageButton;

    NavConnect navConnect = NavConnect.getInstance();


    public void initialize() throws IOException {
        navConnect.registerPageIcon = registerPageIcon;
        navConnect.studyPageIcon = studyPageIcon;
        navConnect.dataBasePageIcon = dataBasePageIcon;
        navConnect.printPageIcon = printPageIcon;

        navConnect.registerPage = registerPage;
        navConnect.studyPage = studyPage;
        navConnect.dataBasePage = dataBasePage;
        navConnect.printPage = printPage;

        navConnect.adminIcon = adminIcon;
        navConnect.adminLabel = adminLabel;
        navConnect.adminButton = adminPageButton;

        navConnect.fpd1FontIcon=fpd1FontIcon;
        navConnect.fpd2FontIcon=fpd2FontIcon;
        navConnect.fpd3FontIcon=fpd3FontIcon;
        navConnect.batteryFontIcon=batteryFontIcon;
        navConnect.wifiSignalFontIcon = wifiSignalFontIcon;
        navConnect.wifiLabel=wifiLabel;
        navConnect.batteryLabel=batteryLabel;
        navConnect.pacsUploadStatusLabel=pacsUploadStatusLabel;

        navConnect.registerPageButton=registerPageButton;
        navConnect.studyPageButton=studyPageButton;
        navConnect.databasePageButton=databasePageButton;
        navConnect.printPageButton=printPageButton;


        setDateAndTime();



    }

    private void setDateAndTime() {
        LocalDate date = LocalDate.now();
        String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dateLabel.setText(dateString);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeLabel.setText(LocalTime.now().format(formatter));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    public void adminPageClick() {
        navConnect.navigateToAdminPage();
    }


    public void registerPageClick() {
        navConnect.navigateToRegisterMain();
    }

    public void studyPageClick() {
        navConnect.navigateToStudy();
    }

    public void dataBasePageClick() {
        navConnect.navigateToDataBase();
    }

    public void printPageClick() {
        navConnect.navigateToPrint();
    }


    public void minimizeClick() {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void closeClick(javafx.event.ActionEvent actionEvent) {
        {
            try {
                // Get the window from the event's source
                Window window = ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();

                // Find the screen that contains this window
                Screen screen = Screen.getScreensForRectangle(
                        window.getX(), window.getY(), window.getWidth(), window.getHeight()
                ).getFirst();
                Rectangle2D screenBounds = screen.getVisualBounds();

                double scaleX = 1;
                double scaleY = 1;

                double parentHeight = screenBounds.getHeight();
                double parentWidth = screenBounds.getWidth();

                double ratio = parentWidth / parentHeight;
                double targetRatio = (double) 600 / 400;

                if (targetRatio < ratio) {
                    scaleX = parentHeight / 600;
                    scaleY = parentHeight / 400;
                } else if (targetRatio > ratio) {
                    scaleX = parentWidth / 600;
                    scaleY = parentWidth / 400;
                } else {
                    scaleX = parentWidth / 600;
                    scaleY = parentWidth / 400;
                }

                double newWidth = 600 * scaleX * 0.6;
                double newHeight = 400 * scaleY * 0.4;


                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/ExitWindow.fxml"));
                Parent root1 = fxmlLoader.load();


                // Apply a clip to make the stage rounded
                double radius = 20;
                Rectangle clip = new Rectangle(newWidth, newHeight);
                clip.setArcWidth(radius);
                clip.setArcHeight(radius);
                root1.setClip(clip);


                // Create the scene and set its fill to transparent
                Scene scene = new Scene(root1, newWidth, newHeight);
                scene.setFill(Color.TRANSPARENT);

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setTitle("Exit Window");
                stage.setScene(scene);
                stage.show();

                // Center the stage on the secondary screen
                stage.setX(screenBounds.getMinX() + (screenBounds.getWidth() - stage.getWidth()) / 2);
                stage.setY(screenBounds.getMinY() + (screenBounds.getHeight() - stage.getHeight()) / 2);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
