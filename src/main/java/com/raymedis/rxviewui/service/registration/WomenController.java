package com.raymedis.rxviewui.service.registration;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_category_table.CategoryBodyPartsEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_category_table.CategoryBodyPartsService;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table.StepEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table.StepService;
import com.raymedis.rxviewui.service.study.StudyBodyPartsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WomenController {

    private static WomenController instance = new WomenController();

    public StackPane anatomyBox;
    public ImageView imageBox;
    public ToggleGroup bodyPartRadioGroup;
    public ToggleGroup studyBodyPartRadioGroup;

    public StackPane anatomyBoxStudy;
    public Parent womenPageStudy;


    public static WomenController getInstance(){
        return instance;
    }

    private final ManualRegisterController  manualRegisterController = ManualRegisterController.getInstance();
    private final CategoryBodyPartsService categoryBodyPartsService = CategoryBodyPartsService.getInstance();
    private final BodyPartSelectionController bodyPartSelectionController = BodyPartSelectionController.getInstance();
    private final StudyBodyPartsController studyBodyPartsController =StudyBodyPartsController.getInstance();



    public void loadAllRadioButtons() {
        bodyPartRadioGroup = new ToggleGroup();
        studyBodyPartRadioGroup = new ToggleGroup();

        ArrayList<CategoryBodyPartsEntity> selectedBodyParts = categoryBodyPartsService.getAllSelectedBodyParts("female");

        removeAllRadioButtons();
        for (CategoryBodyPartsEntity bodyPart : selectedBodyParts){
            createRadioButton(bodyPart.getBodyPartName(),bodyPart.getFemaleXPos(),bodyPart.getFemaleYPos());
        }
    }

    public void removeAllRadioButtons(){
        List<Node> radioButtonsToRemove1 = new ArrayList<>();
        List<Node> radioButtonsToRemove2 = new ArrayList<>();

        for (Node node : anatomyBox.getChildren()) {
            if (node instanceof JFXRadioButton) {
                radioButtonsToRemove1.add(node);
            }
        }

        for (Node node : anatomyBoxStudy.getChildren()) {
            if (node instanceof JFXRadioButton) {
                radioButtonsToRemove2.add(node);
            }
        }

        // Remove all the collected RadioButtons
        anatomyBox.getChildren().removeAll(radioButtonsToRemove1);
        anatomyBoxStudy.getChildren().removeAll(radioButtonsToRemove2);
    }


    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input; // Return as-is if null or empty
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }




    private void createRadioButton(String buttonName, double xPos, double yPos) {
        // Create and configure the first radio button
        JFXRadioButton radioButton1 = new JFXRadioButton(capitalizeFirstLetter(buttonName));
        configureRadioButton(radioButton1, xPos, yPos,"register");
        anatomyBox.getChildren().add(radioButton1);
        radioButton1.setToggleGroup(bodyPartRadioGroup);

        // Create and configure the second radio button
        JFXRadioButton radioButton2 = new JFXRadioButton(capitalizeFirstLetter(buttonName));
        configureRadioButton(radioButton2, xPos, yPos,"study");
        anatomyBoxStudy.getChildren().add(radioButton2);
        radioButton2.setToggleGroup(studyBodyPartRadioGroup);
    }


    private final Logger logger = LoggerFactory.getLogger(WomenController.class);
    public void getSelectedBodyPartProjections(String bodyPartName,String page) {
        String projectionPath = Paths.get(System.getProperty("user.dir"),
                "src", "main", "resources", "Styles", "Images", "Projection").toString();

        File bodyPartFolder = new File(projectionPath, bodyPartName);

        // Ensure the folder exists
        if (!bodyPartFolder.exists() && !bodyPartFolder.mkdirs()) {
            logger.info("Failed to create folder for body part: {}", bodyPartName);
            return;
        }

        ArrayList<StepEntity> selectedBodyPartStepsList = StepService.getInstance().findByStepName(bodyPartName);
        Set<String> displayedImages = new HashSet<>();
        VBox imageContainer = new VBox(10);
        HBox currentRow = createNewRow();
        int imagesPerRow = 2;
        int imageCount = 0;

        for (StepEntity step : selectedBodyPartStepsList) {
            String stepName = step.getStepName();

            // Skip duplicate steps
            if (!displayedImages.add(stepName)) {
                continue;
            }

            File[] matchingFiles = findMatchingFiles(bodyPartFolder, stepName);

            // If no images found, create a button without an image
            if (matchingFiles == null || matchingFiles.length == 0) {
                File imageFile = new File(projectionPath+"/blankBodyPart.png");
                currentRow.getChildren().add(createProjectionButton(stepName, imageFile,page));
            } else {
                // Create buttons for each matching image
                for (File imageFile : matchingFiles) {
                    currentRow.getChildren().add(createProjectionButton(stepName, imageFile,page));
                    imageCount++;

                    // Start a new row if needed
                    if (imageCount % imagesPerRow == 0) {
                        imageContainer.getChildren().add(currentRow);
                        currentRow = createNewRow();
                    }
                }
            }
        }

        // Add any remaining buttons
        if (!currentRow.getChildren().isEmpty()) {
            imageContainer.getChildren().add(currentRow);
        }

        if(page.equals("study")){
            studyBodyPartsController.studyBodyPositionContainer.getChildren().add(imageContainer);
            studyBodyPartsController.studyBodyPartsPositionScrollBar.setVvalue(0.5);
        }else if(page.equals("register")){
            manualRegisterController.bodyPositionContainer.getChildren().add(imageContainer);
        }

    }

    private File[] findMatchingFiles(File folder, String stepName) {
        return folder.listFiles((dir, name) ->
                name.startsWith(stepName) && name.endsWith(".png") ||
                        name.replaceAll("\\s", "").toLowerCase().startsWith(stepName.replaceAll("\\s", "").toLowerCase())
                                && name.toLowerCase().endsWith(".png"));
    }

    private HBox createNewRow() {
        HBox row = new HBox(10);
        row.setAlignment(Pos.TOP_LEFT);
        return row;
    }


    private JFXButton createProjectionButton(String stepName, File imageFile,String page) {
        JFXButton button = new JFXButton(stepName);
        button.setPrefSize(290, 295);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(250);
        imageView.setFitWidth(250);

        if (imageFile != null) {
            imageView.setImage(new Image(imageFile.toURI().toString()));
        }

        button.setGraphic(imageView);
        button.setContentDisplay(ContentDisplay.TOP);
        button.setAlignment(Pos.TOP_CENTER);

        if(page.equals("register")){
            if (bodyPartSelectionController.projectionsButonMap.containsValue(stepName)) {
                button.setDisable(true);
            } else {
                button.setOnAction(event -> handleProjectionButtonClick(stepName, imageFile, button,page));
            }
        }else if (page.equals("study")){
            button.setOnAction(event -> handleProjectionButtonClick(stepName, imageFile, button,page));
        }

        return button;
    }

    private void handleProjectionButtonClick(String stepName, File imageFile, JFXButton button,String page) {
        if(page.equals("register")){
            button.setDisable(true);
            bodyPartSelectionController.projectionsButonMap.put(button,stepName);

            JFXButton selectedButton = createSelectedProjectionButton(stepName, imageFile,button);
            manualRegisterController.selectedPositionsContainer.getChildren().add(selectedButton);
        } else if (page.equals("study")) {
            bodyPartSelectionController.projectionsButonMap.clear();
            bodyPartSelectionController.projectionsButonMap.put(button,stepName);
            ManualRegisterService.getInstance().addBodyParts();
        }

    }

    private JFXButton createSelectedProjectionButton(String stepName, File imageFile,JFXButton projectionButton) {
        JFXButton button = new JFXButton(stepName);
        button.setPrefSize(215.0, 215.0);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(185.0);
        imageView.setFitWidth(185.0);

        if (imageFile != null) {
            imageView.setImage(new Image(imageFile.toURI().toString()));
        }

        button.setGraphic(imageView);
        button.setContentDisplay(ContentDisplay.TOP);
        button.setAlignment(Pos.TOP_CENTER);
        button.setId("unSelectedBodyPartButton");

        button.setOnAction(event -> toggleSelectedButtonState(button,projectionButton));
        return button;
    }

    private void toggleSelectedButtonState(JFXButton button,JFXButton projectionButton) {
        if ("unSelectedBodyPartButton".equals(button.getId())) {
            button.setId("selectedBodyPartButton");
            bodyPartSelectionController.selectedBodyPartPositionMapForDelete.put(projectionButton, button);
        } else {
            button.setId("unSelectedBodyPartButton");
            bodyPartSelectionController.selectedBodyPartPositionMapForDelete.remove(button);
        }
    }


    private void configureRadioButton(JFXRadioButton radioButton, double xPos, double yPos,String page) {
        radioButton.setOnAction(actionEvent -> {
            studyBodyPartsController.studyBodyPositionContainer.getChildren().clear();
            manualRegisterController.bodyPositionContainer.getChildren().clear();
            getSelectedBodyPartProjections(radioButton.getText(),page);
        });

        // General configuration
        radioButton.setMaxSize(225, 50);
        StackPane.setMargin(radioButton, new Insets(yPos, 0, 0, xPos));
    }
}
