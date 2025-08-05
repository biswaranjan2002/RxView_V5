package com.raymedis.rxviewui.controller.patientSize;

import com.raymedis.rxviewui.service.registration.WomenController;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class Women {

    @FXML public StackPane anatomyBox;
    @FXML public ImageView imageBox;

    private final WomenController womenController = WomenController.getInstance();

    @FXML
    public void initialize() throws IOException {
        womenController.anatomyBox = anatomyBox;
        womenController.imageBox = imageBox;
    }


}
