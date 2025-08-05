package com.raymedis.rxviewui.controller.patientSize;

import com.raymedis.rxviewui.service.registration.InfantController;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class Infant {


    @FXML public StackPane anatomyBox;
    @FXML public ImageView imageBox;

    private final InfantController infantController =InfantController.getInstance();

    @FXML
    public void initialize() throws IOException {
        infantController.anatomyBox = anatomyBox;
    }




}
