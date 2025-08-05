package com.raymedis.rxviewui.controller.patientSize;

import com.raymedis.rxviewui.service.registration.ManController;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class Man {


    @FXML public StackPane anatomyBox;
    @FXML public ImageView imageBox;

    private final ManController manController = ManController.getInstance();

    @FXML
    public void initialize() throws IOException {
        manController.anatomyBox = anatomyBox;
        manController.imageBox = imageBox;
    }




}
