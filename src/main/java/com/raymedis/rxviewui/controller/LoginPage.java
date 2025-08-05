package com.raymedis.rxviewui.controller;

import com.jfoenix.controls.JFXButton;
import com.raymedis.rxviewui.service.login.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoginPage {

    public JFXButton loginButton;
    public TextField passwordText;
    public TextField userIdText;
    public GridPane rootNode;
    public Label rxViewLabel;
    public Label tagLineLabel;

    Parent mainWindowParent;

    private LoginController loginController;

    public void initialize() throws IOException {
        loginController = LoginController.getInstance();

        loginController.passwordText =passwordText;
        loginController.userIdText = userIdText;

        addDoubleClickEventHandler(userIdText);
        addDoubleClickEventHandler(passwordText);

        FXMLLoader mainWindow = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/MainWindow.fxml"));
        mainWindowParent = mainWindow.load();

        noSpaceValidation(userIdText);

        loginController.mainWindowParent =mainWindowParent;
    }

    private final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    @FXML
    private void loginButtonClick() {

        //uncomment the following line to enable login functionality
          //loginController.loginButtonClick();

        Scene currentScene = loginButton.getScene();
        currentScene.setRoot(mainWindowParent);
        loginButton.getParent().requestFocus();
        loginController.loadMainWindowSettings();

        logger.info("logged In");
    }



    private void addDoubleClickEventHandler(TextField textField) {
        textField.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                openOnScreenKeyboard();
            }
        });
    }

    private void openOnScreenKeyboard() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "start", "osk.exe");
            Process process = processBuilder.start();
        } catch (Exception e) {
            System.out.println("An error occurred while trying to open the on-screen keyboard.");
            e.printStackTrace();
        }
    }


    private void noSpaceValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                textField.setText(newValue.replaceAll(" ", ""));
            }
        });
    }

}