package com.raymedis.rxviewui;

import com.raymedis.rxviewui.controller.StageConnect;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.opencv.core.Core;


import java.io.IOException;

public class Main extends Application {
    StageConnect stageConnect=StageConnect.getInstance();
    Parent loginParent;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Override
    public void start(Stage mainStage) throws IOException {
        FXMLLoader loginPage = new FXMLLoader(getClass().getResource("controllerUi/LoginPage.fxml"));
        loginParent = loginPage.load();
        Scene scene1 = new Scene(loginParent);
        mainStage.setTitle("RxView");
        mainStage.setScene(scene1);

        Screen primaryScreen = Screen.getPrimary();
        Rectangle2D bounds1 = primaryScreen.getVisualBounds();
        mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.setX(bounds1.getMinX());
        mainStage.setY(bounds1.getMinY());
        mainStage.setWidth(bounds1.getWidth());
        mainStage.setHeight(bounds1.getHeight());
        mainStage.setMaximized(true);
        mainStage.setFullScreen(true);
        mainStage.setFullScreenExitHint("");
        mainStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        mainStage.setAlwaysOnTop(true);
        mainStage.show();
        stageConnect.xmainStage = mainStage;
        stageConnect.loginParent = loginParent;
        stageConnect.mainScene = scene1;
    }


    public static void main(String[] args) {
        launch();
    }
}