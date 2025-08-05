package com.raymedis.rxviewui.controller;

import com.jfoenix.controls.JFXButton;
import com.raymedis.rxviewui.service.fpdSdks.service.InnoCareService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class ExitWindow {

    public JFXButton logoutButton;
    public JFXButton shutdownButton;
    public JFXButton closeButton;

    StageConnect stageConnect = StageConnect.getInstance();

    public void logoutClick(ActionEvent actionEvent) {
        stageConnect.xmainStage.getScene().setRoot(stageConnect.loginParent);
        closeClick(actionEvent);
    }

    public void exitClick() {
        InnoCareService.innoCareNative.cancelOperationMode();
        InnoCareService.innoCareNative.exitOperationMode();
        InnoCareService.innoCareNative.disConnect();
        InnoCareService.innoCareNative.sysReboot();
        InnoCareService.innoCareNative.cleanAll();
        Platform.exit();
        System.exit(0);
    }

    public void closeClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

}
