package com.raymedis.rxviewui.service.adminSettings.registration;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.database.tables.adminSettings.register.register_physician_table.PhysicianEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.register.register_physician_table.PhysicianService;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;

public class RegistrationPhysicianController {

    private final static RegistrationPhysicianController instance = new RegistrationPhysicianController();
    private final PhysicianService physicianService = PhysicianService.getInstance();


    public static RegistrationPhysicianController getInstance(){
        return instance;
    }

    public JFXButton addButton;

    public TextField physicianIdLabel;
    public TextField physicianNameLabel;
    public JFXComboBox physicianGroupComboBox;

    public VBox physicianOuterBox;

    public TableView physicianDataGrid;

    public TableColumn physicianNameColumn;
    public TableColumn physicianGroupColumn;

    private ArrayList<PhysicianEntity> physicianEntities = new ArrayList<>();

    private void alphabetsValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s]*")) {  //Only allows letters and spaces
                textField.setText(newValue.replaceAll("[^a-zA-Z\\s]", ""));
            } else if (newValue.length() > 50) {
                textField.setText(oldValue);
            }
        });
    }

    private void idValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9]*")) {
                textField.setText(newValue.replaceAll("[^a-zA-Z0-9]", ""));
            } else if (newValue.length() > 20) {
                textField.setText(oldValue);
            }
        });
    }

    public void loadData() throws SQLException {
        physicianEntities=physicianService.findAll();
        physicianDataGrid.getItems().clear();
        physicianDataGrid.getItems().addAll(physicianEntities);
    }

    public void loadEvents(){
        physicianGroupColumn.setCellValueFactory(new PropertyValueFactory<>("physicianGroup"));
        physicianNameColumn.setCellValueFactory(new PropertyValueFactory<>("physicianName"));


        physicianDataGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    PhysicianEntity physician = (PhysicianEntity) physicianDataGrid.getSelectionModel().getSelectedItem();
                    if(physician!=null){
                        handleDoubleClick(physician);
                        addButton.setText("Edit");
                    }
                }
            }
        });

        physicianOuterBox.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            clearData();
        });

        alphabetsValidation(physicianNameLabel);
        idValidation(physicianIdLabel);
    }

    private void clearData(){
        physicianDataGrid.getSelectionModel().clearSelection();
        physicianNameLabel.setText("");
        physicianIdLabel.setText("");

        physicianNameLabel.setDisable(false);
        physicianIdLabel.setDisable(false);


        physicianGroupComboBox.setDisable(false);
        addButton.setText("Add");
    }

    private void handleDoubleClick(PhysicianEntity physician) {
        physicianNameLabel.setText(physician.getPhysicianName());
        physicianIdLabel.setText(physician.getPhysicianId());

        String group = physician.getPhysicianGroup().toLowerCase();
        switch (group){
            case "referring physician":
                physicianGroupComboBox.getSelectionModel().select(0);
                break;
            case "reading physician":
                physicianGroupComboBox.getSelectionModel().select(1);
                break;
            case "performing physician":
                physicianGroupComboBox.getSelectionModel().select(2);
                break;
            default:
                System.out.println("unknown user group....");
        }


        addButton.setText("Edit");
        physicianIdLabel.setDisable(true);
        physicianNameLabel.setDisable(true);
        physicianGroupComboBox.setDisable(true);
    }

    public void addDataClick() {
        if(addButton.getText().equals("Add") || addButton.getText().equals("Save")){

            if(physicianIdLabel.getText().isEmpty() || physicianNameLabel.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Fields");
                alert.setHeaderText(null);
                alert.setContentText("Please Fill the Details to Add...");
                alert.showAndWait();
                return;
            }

            PhysicianEntity physician = new PhysicianEntity();

            physician.setPhysicianId(physicianIdLabel.getText());
            physician.setPhysicianName(physicianNameLabel.getText());
            physician.setPhysicianGroup(physicianGroupComboBox.getSelectionModel().getSelectedItem().toString());





            if (addButton.getText().equals("Add")) {
                try {
                    physicianService.save(physician);
                } catch (SQLException e) {
                    alertMessage("Error", "Adding Physician Failed", "PhysicianId Already in Use... ");
                }
            } else if (addButton.getText().equals("Save")) {
                try {
                    physicianService.update(physicianIdLabel.getText(), physician);
                } catch (SQLException e) {
                    alertMessage("Error", "Save Physician Failed", "Error saving physician: " + e.getMessage());
                }
            }



            try {
                loadData();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            clearData();
        }
        else if (addButton.getText().equals("Edit")) {
            addButton.setText("Save");
            physicianNameLabel.setDisable(false);
            physicianGroupComboBox.setDisable(false);
        }
    }


    private void alertMessage(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }




    public void deleteClick() {
        PhysicianEntity physician = (PhysicianEntity) physicianDataGrid.getSelectionModel().getSelectedItem();

        if(physician==null){
            return;
        }

        try {
            physicianService.delete(physician.getPhysicianId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        clearData();
    }


}
