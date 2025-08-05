package com.raymedis.rxviewui.service.adminSettings.procedureManager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_category_table.CategoryBodyPartsEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_category_table.CategoryBodyPartsService;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.sql.SQLException;
import java.util.*;

public class ProcedureManagerCategoryController {

    private static ProcedureManagerCategoryController instance = new ProcedureManagerCategoryController();
    public VBox outerBodyPartsBox;

    public static ProcedureManagerCategoryController getInstance(){
        return instance;
    }

    public Label anatomyNameLabel;
    public ImageView anatomyImageBox;
    public StackPane anatomyBox;
    private String currentAnatomy;
    public VBox bodyPartsBox;

    private final CategoryBodyPartsService categoryBodyPartsService = CategoryBodyPartsService.getInstance();
    private final ArrayList<String> anatomyList;
    private final ArrayList<Image> anatomyImageList = new ArrayList<>();
    private ArrayList<CategoryBodyPartsEntity> bodyPartsList;
    private Map<String,JFXButton> allBodyPartsButtonsMap;


    public ProcedureManagerCategoryController(){
        bodyPartsList =new ArrayList<>();
        allBodyPartsButtonsMap = new HashMap<>();
        anatomyList = new ArrayList<>(Arrays.asList("Male", "Female", "Infant"));

        anatomyImageList.add(new Image(getClass().getResource("/Styles/Images/Assets/Anatomy-Man.png").toExternalForm()));
        anatomyImageList.add(new Image(getClass().getResource("/Styles/Images/Assets/Anatomy-Woman.png").toExternalForm()));
        anatomyImageList.add(new Image(getClass().getResource("/Styles/Images/Assets/Anatomy-Infant.png").toExternalForm()));
    }

    public void loadAllEvents(){
        // Allow dragging over anatomyBox only
        anatomyBox.setOnDragOver(event -> {
            if (event.getGestureSource() != anatomyBox && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        // Handle the drop inside anatomyBox
        anatomyBox.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasString()) {
                String droppedText = dragboard.getString();
                double x = event.getX();
                double y = event.getY();

                CategoryBodyPartsEntity bodyParts =  getBodyPartByName(droppedText.toLowerCase());
                switch (currentAnatomy){
                    case "male":
                        bodyParts.setMaleIsSelected(true);
                        bodyParts.setMaleXPos(x);
                        bodyParts.setMaleYPos(y);
                        break;
                    case "female":
                        bodyParts.setFemaleIsSelected(true);
                        bodyParts.setFemaleXPos(x);
                        bodyParts.setFemaleYPos(y);
                        break;
                    case "infant":
                        bodyParts.setInfantIsSelected(true);
                        bodyParts.setInfantXPos(x);
                        bodyParts.setInfantYPos(y);
                        break;
                    default:
                        System.out.println("unknown current anatomy : " + currentAnatomy);
                }


                try {
                    categoryBodyPartsService.updateBodyPart(bodyParts.getBodyPartName(),bodyParts);
                    loadAllRadioButtons(currentAnatomy);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                success = true;
                loadAllButtons();
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    public void loadData() {
        bodyPartsList.clear();

        bodyPartsList = categoryBodyPartsService.getAllStepExistsBodyParts();
        currentAnatomy = anatomyNameLabel.getText().toLowerCase();

        loadAllButtons();
        loadAllRadioButtons(currentAnatomy);
    }

    public void loadAllButtons() {
        bodyPartsBox.getChildren().clear();

        int maxButtonsPerRow = 3; // Maximum buttons in a single HBox
        HBox currentRow = null; // To hold the current row of buttons

        for (int i = 0; i < bodyPartsList.size(); i++) {
            if (i % maxButtonsPerRow == 0) {
                // Create a new HBox for every set of 3 buttons
                currentRow = new HBox();
                currentRow.setAlignment(Pos.CENTER);
                currentRow.setSpacing(10);
                currentRow.setPrefHeight(60);
                currentRow.setMaxHeight(60);
                currentRow.setMinHeight(60);
                currentRow.setPrefWidth(725);
                currentRow.setMaxWidth(725);
                currentRow.setMinWidth(725);
                bodyPartsBox.getChildren().add(currentRow); // Add the row to the VBox
            }

            // Create the button and add it to the current row
            CategoryBodyPartsEntity bodyPartsButton = bodyPartsList.get(i);


            JFXButton button = createButton(bodyPartsButton.getBodyPartName());
            currentRow.getChildren().add(button);
        }


        bodyPartsBox.requestFocus();
    }

    public void removeAllRadioButtons(){
        List<Node> radioButtonsToRemove = new ArrayList<>();

        for (Node node : anatomyBox.getChildren()) {
            if (node instanceof JFXRadioButton) {
                radioButtonsToRemove.add(node);
            }
        }

        // Remove all the collected RadioButtons
        anatomyBox.getChildren().removeAll(radioButtonsToRemove);
    }

    private void loadAllRadioButtons(String currentAnatomy) {
        removeAllRadioButtons();

        for (CategoryBodyPartsEntity bodyPart: categoryBodyPartsService.getAllSelectedBodyParts(currentAnatomy)){
            switch (currentAnatomy){
                case "male":
                    createRadioButton(bodyPart.getBodyPartName(),bodyPart.getMaleXPos(),bodyPart.getMaleYPos());
                    break;
                case "female":
                    createRadioButton(bodyPart.getBodyPartName(),bodyPart.getFemaleXPos(),bodyPart.getFemaleYPos());
                    break;
                case "infant":
                    createRadioButton(bodyPart.getBodyPartName(),bodyPart.getInfantXPos(),bodyPart.getInfantYPos());
                    break;
                default:
                    System.out.println("unknown current Anatomy : " + currentAnatomy);
            }
        }
    }

    private void createRadioButton(String buttonName,double xPos,double yPos) {
        JFXRadioButton radioButton = new JFXRadioButton(buttonName);

        radioButton.setMaxSize(225, 50);
        StackPane.setMargin(radioButton, new Insets(yPos, 0, 0, xPos));

        anatomyBox.getChildren().add(radioButton);
        radioButtonDragAndDrop(radioButton,outerBodyPartsBox);

        radioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                radioButton.setSelected(false);
            }
        });

    }

    public CategoryBodyPartsEntity getBodyPartByName(String bodyPartName) {
        for (CategoryBodyPartsEntity bodyPart : bodyPartsList) {
            if (bodyPartName.equalsIgnoreCase(bodyPart.getBodyPartName())) {
                return bodyPart;
            }
        }
        return null;
    }


    public Image createRadioButtonImage(JFXButton button){
        JFXRadioButton droppedRadioButton = new JFXRadioButton(button.getText());
        droppedRadioButton.setTextFill(Paint.valueOf("#ec641c"));

        StackPane pane = new StackPane(droppedRadioButton);
        pane.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(pane, (int)droppedRadioButton.getPrefWidth(), (int)droppedRadioButton.getPrefHeight());
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        Image radioButton = pane.snapshot(parameters, null);




        String bodyPart = button.getText().toLowerCase().replace("-", "").replace(" ", "");
        CategoryBodyPartsEntity bodyPartsEntity = getBodyPartByName(bodyPart);

        switch (currentAnatomy){
            case "male":
                 bodyPartsEntity.setMaleIsSelected(true);
                 bodyPartsEntity.setMaleXPos(0);
                 bodyPartsEntity.setMaleXPos(0);
                 break;
            case "female":
                bodyPartsEntity.setFemaleIsSelected(true);
                bodyPartsEntity.setFemaleXPos(0);
                bodyPartsEntity.setFemaleYPos(0);
                 break;
            case "infant":
                bodyPartsEntity.setInfantIsSelected(true);
                bodyPartsEntity.setInfantXPos(0);
                bodyPartsEntity.setInfantYPos(0);
                break;
            default:
                System.out.println("Unknown Gender : " + currentAnatomy);
        }

        try {
            if(bodyPartsEntity!=null){
                categoryBodyPartsService.updateBodyPart(bodyPart, bodyPartsEntity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }




        return radioButton;
    }

    public void radioButtonDragAndDrop(JFXRadioButton radioButton, Node outerBodyPartsBox) {
        // Define the drag-over handler
        EventHandler<DragEvent> dragOverHandler = event -> {
            Dragboard dragboard = event.getDragboard();
            String droppedText = dragboard.getString();
            deleteRadioButton(droppedText);
            event.consume();
        };

        // Set up drag detection on the RadioButton
        radioButton.setOnDragDetected(event -> {
            // Add the drag-over event to outerBodyPartsBox
            outerBodyPartsBox.setOnDragOver(dragOverHandler);

            // Start the drag-and-drop operation
            Dragboard dragboard = radioButton.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            Image radioButtonImage = createRadioButtonImage2(radioButton);
            dragboard.setDragView(radioButtonImage);
            content.putString(radioButton.getText());
            dragboard.setContent(content);
            event.consume();
        });

        // Remove the drag-over event once the drag is complete
        radioButton.setOnDragDone(event -> {
            outerBodyPartsBox.setOnDragOver(null); // Remove the drag-over handler
            event.consume();
        });
    }

    private void deleteRadioButton(String radioButton) {
        CategoryBodyPartsEntity bodyPartsEntity=getBodyPartByName(radioButton);

        switch (currentAnatomy){
            case "male":
                bodyPartsEntity.setMaleIsSelected(false);
                break;
            case "female":
                bodyPartsEntity.setFemaleIsSelected(false);
                break;
            case "infant":
                bodyPartsEntity.setInfantIsSelected(false);
                break;
            default:
                System.out.println("Unknown Gender : " + currentAnatomy);
        }

        try {
            if(bodyPartsEntity!=null){
                categoryBodyPartsService.updateBodyPart(bodyPartsEntity.getBodyPartName(), bodyPartsEntity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        loadAllButtons();
        loadAllRadioButtons(currentAnatomy);
    }

    public Image createRadioButtonImage2(JFXRadioButton button){

        JFXRadioButton droppedRadioButton = new JFXRadioButton(button.getText());
        droppedRadioButton.setTextFill(Paint.valueOf("#ec641c"));

        StackPane pane = new StackPane(droppedRadioButton);
        pane.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(pane, (int)droppedRadioButton.getPrefWidth(), (int)droppedRadioButton.getPrefHeight());
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        Image radioButton = pane.snapshot(parameters, null);



        String bodyPart = button.getText().toLowerCase().replace("-", "").replace(" ", "");
        CategoryBodyPartsEntity bodyPartsEntity = getBodyPartByName(bodyPart);
        switch (currentAnatomy){
            case "male":
                bodyPartsEntity.setMaleIsSelected(true);
                bodyPartsEntity.setMaleXPos(0);
                bodyPartsEntity.setMaleXPos(0);
                break;
            case "female":
                bodyPartsEntity.setFemaleIsSelected(true);
                bodyPartsEntity.setFemaleXPos(0);
                bodyPartsEntity.setFemaleYPos(0);
                break;
            case "infant":
                bodyPartsEntity.setInfantIsSelected(true);
                bodyPartsEntity.setInfantXPos(0);
                bodyPartsEntity.setInfantYPos(0);
                break;
            default:
                System.out.println("Unknown Gender : " + currentAnatomy);
        }

        try {
            if(bodyPartsEntity!=null){
                categoryBodyPartsService.updateBodyPart(bodyPart, bodyPartsEntity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return radioButton;
    }

    public void setupDragAndDrop(JFXButton button){
        button.setOnDragDetected(event -> {
            Dragboard dragboard = button.startDragAndDrop(TransferMode.MOVE);

            ClipboardContent content = new ClipboardContent();
            content.putString(button.getText());
            Image radioButton  = createRadioButtonImage(button);
            dragboard.setDragView(radioButton);
            dragboard.setContent(content);
            event.consume();
        });
    }

    private JFXButton createButton(String bodyPartName) {
        String formattedName = capitalizeFirstLetter(bodyPartName);
        JFXButton button = new JFXButton(formattedName);
        button.setPrefHeight(50);
        button.setPrefWidth(225);
        button.getStyleClass().add("unSelectedToolsButton");

        switch (currentAnatomy){
            case "male":
                if(getBodyPartByName(bodyPartName).getMaleIsSelected()){
                    button.getStyleClass().remove("unSelectedToolsButton");
                    button.getStyleClass().add("SelectedToolsButton");
                    button.setDisable(true);
                }
                break;
            case "female":
                if(getBodyPartByName(bodyPartName).getFemaleIsSelected()){
                    button.getStyleClass().remove("unSelectedToolsButton");
                    button.getStyleClass().add("SelectedToolsButton");
                    button.setDisable(true);
                }
                break;
            case "infant":
                if(getBodyPartByName(bodyPartName).getInfantIsSelected()){
                    button.getStyleClass().remove("unSelectedToolsButton");
                    button.getStyleClass().add("SelectedToolsButton");
                    button.setDisable(true);
                }
                break;
        }


        setupDragAndDrop(button);

        allBodyPartsButtonsMap.put(bodyPartName,button);
        return button;
    }


    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input; // Return as-is if null or empty
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }


    public void backwardSelection() {
        switch (currentAnatomy) {
            case "male":
                loadInfantAnatomy();
                break;
            case "female":
                loadMaleAnatomy();
                break;
            case "infant":
                loadFemaleAnatomy();
                break;
            default:
                System.out.println("Unknown anatomy type : " + currentAnatomy);
                break;
        }
    }

    public void forwardSelection() {
        switch (currentAnatomy) {
            case "male":
                loadFemaleAnatomy();
                break;
            case "female":
                loadInfantAnatomy();
                break;
            case "infant":
                loadMaleAnatomy();
                break;
            default:
                System.out.println("Unknown anatomy type : " + currentAnatomy);
                break;
        }
    }


    private void loadMaleAnatomy(){
        anatomyImageBox.setImage(anatomyImageList.getFirst());
        anatomyNameLabel.setText(anatomyList.getFirst());
        currentAnatomy = anatomyNameLabel.getText().toLowerCase();

        loadAllButtons();
        loadAllRadioButtons(currentAnatomy);
    }

    private void loadFemaleAnatomy(){
        anatomyImageBox.setImage(anatomyImageList.get(1));
        anatomyNameLabel.setText(anatomyList.get(1));
        currentAnatomy = anatomyNameLabel.getText().toLowerCase();

        loadAllButtons();
        loadAllRadioButtons(currentAnatomy);
    }

    private void loadInfantAnatomy(){
        anatomyImageBox.setImage(anatomyImageList.get(2));
        anatomyNameLabel.setText(anatomyList.get(2));
        currentAnatomy = anatomyNameLabel.getText().toLowerCase();

        loadAllButtons();
        loadAllRadioButtons(currentAnatomy);
    }


}


































