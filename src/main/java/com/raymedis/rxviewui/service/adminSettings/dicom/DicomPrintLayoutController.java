package com.raymedis.rxviewui.service.adminSettings.dicom;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printLayout_table.PrintLayoutEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printLayout_table.PrintLayoutService;
import com.raymedis.rxviewui.modules.print.Layout.LayoutIconCreator;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DicomPrintLayoutController {

    private final static DicomPrintLayoutController instance = new DicomPrintLayoutController();

    public static DicomPrintLayoutController getInstance(){
        return instance;
    }
    private static PrintLayoutService printLayoutService = PrintLayoutService.getInstance();

    public TextField inputText;
    public TextField outputText;

    public JFXComboBox layoutTypeComboBox;

    public GridPane layoutBox;
    public VBox selectedLayoutContainer;
    public JFXButton addButton;

    private LinkedHashMap<String, JFXButton> selectedLayoutButtonsMap = new LinkedHashMap<>();
    private String output="";
    private ArrayList<PrintLayoutEntity> layoutList = new ArrayList<>();

    public void loadEvents(){
        numericValidation(inputText);
    }

    private void numericValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9,]*")) {
                textField.setText(newValue.replaceAll("[^0-9,]", ""));
            } else if (newValue.length() > 15) {
                textField.setText(oldValue);
            }
        });
    }

    public void loadData() throws SQLException {
        addButton.setDisable(true);
        layoutTypeComboBox.getSelectionModel().select(0);
        inputText.setPromptText("Input Like: 3,4");
        selectedLayoutContainer.getChildren().clear();
        layoutBox.getChildren().clear();
        layoutBox.getColumnConstraints().clear();
        layoutBox.getRowConstraints().clear();
        selectedLayoutButtonsMap.clear();


        layoutList = printLayoutService.findAll();
        for (PrintLayoutEntity p : layoutList){
            output = p.getLayoutName();
            addClick();
        }
        output = "";

        layoutBox.getChildren().clear();
        layoutBox.getColumnConstraints().clear();
        layoutBox.getRowConstraints().clear();
    }

    public void checkClick() {
        output = layoutTypeComboBox.getSelectionModel().getSelectedItem().toString() + "\\" + inputText.getText();
        String input = inputText.getText();

        boolean isInvalid;

        if (layoutTypeComboBox.getSelectionModel().isSelected(0)) {
            isInvalid = !isTwoNumberFormat(input);
        } else {
            isInvalid = !isValidFormat(input);
        }

        if (isInvalid) {
            outputText.setText(output + " is Invalid");
            outputText.setId("inValidOutputTextField");
        } else {
            outputText.setText(output + " is Valid");
            outputText.setId("validOutputTextField");

            double width = (layoutBox.getPrefWidth()/100)*98;
            double height = (layoutBox.getPrefHeight()/100)*98;

            layoutBox.getChildren().clear();
            layoutBox.setAlignment(Pos.CENTER);

            layoutBox.getChildren().add(LayoutIconCreator.getInstance().createLayout(output,width,height));
            addButton.setDisable(false);
        }
    }


    private boolean isTwoNumberFormat(String input) {
        if (!input.matches("\\d+,\\d+")) {
            return false;
        }

        String[] numbers = input.split(",");
        for (String number : numbers) {
            try {
                int value = Integer.parseInt(number.trim());
                if (value == 0 || value >= 5) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidFormat(String input) {
        if (!input.matches("(\\d+)(,\\d+)*")) {
            return false;
        }

        String[] numbers = input.split(",");
        if (numbers.length - 1 != input.chars().filter(ch -> ch == ',').count()) {
            return false;
        }

        for (String number : numbers) {
            try {
                int value = Integer.parseInt(number.trim());
                if (value == 0 || value >= 6) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public void addClick() {
        JFXButton layoutButton = createLayoutButtons();
        if(selectedLayoutButtonsMap.get(output)==null){
            selectedLayoutContainer.getChildren().add(layoutButton);
            selectedLayoutButtonsMap.put(output, layoutButton);
        }
        addButton.setDisable(true);
    }

    private JFXButton createLayoutButtons() {
        JFXButton layoutButton = new JFXButton();
        layoutButton.setPrefSize(200, 200);
        layoutButton.setMaxSize(200, 200);
        layoutButton.setMinSize(200, 200);

        layoutButton.setId("unSelectedLayoutButton");

        double width = (layoutButton.getPrefWidth()/100)*85;
        double height = (layoutButton.getPrefHeight()/100)*85;

        Node layout = LayoutIconCreator.getInstance().createLayout(output,width,height);

        layoutButton.setGraphic(layout);
        layoutButton.setContentDisplay(ContentDisplay.CENTER);
        layoutButton.setAlignment(Pos.CENTER);

        layoutButton.setOnAction(event -> {
            layoutButton.setId("selectedLayoutButton");

            for (JFXButton button : selectedLayoutButtonsMap.values()) {
                if (button != layoutButton) {
                    button.setId("unSelectedLayoutButton");
                }
            }
        });
        return layoutButton;
    }

    public void saveClick() throws SQLException {
        layoutList = printLayoutService.findAll();

        for (Map.Entry<String, JFXButton> entry : selectedLayoutButtonsMap.entrySet()) {
            boolean layoutExists = false;

            for (PrintLayoutEntity p : layoutList) {
                if (p.getLayoutName().equals(entry.getKey())) {
                    layoutExists = true;
                    break;
                }
            }


            if (!layoutExists) {
                PrintLayoutEntity printLayoutEntity = new PrintLayoutEntity();
                printLayoutEntity.setLayoutName(entry.getKey());
                printLayoutService.save(printLayoutEntity);
            }
        }

        for (PrintLayoutEntity p : layoutList) {
            if (!selectedLayoutButtonsMap.containsKey(p.getLayoutName())) {
                printLayoutService.delete(p.getId());
            }
        }
    }

    public void deleteLayoutClick() {
        JFXButton buttonToDelete = null;

        for (Map.Entry<String, JFXButton> entry : selectedLayoutButtonsMap.entrySet()) {
            JFXButton button = entry.getValue();
            if ("selectedLayoutButton".equals(button.getId())) {
                buttonToDelete = button;
                break;
            }
        }

        if (buttonToDelete != null) {
            selectedLayoutButtonsMap.values().remove(buttonToDelete);
            if (buttonToDelete.getParent() != null) {
                ((Pane) buttonToDelete.getParent()).getChildren().remove(buttonToDelete);
            }
        }
    }

}



