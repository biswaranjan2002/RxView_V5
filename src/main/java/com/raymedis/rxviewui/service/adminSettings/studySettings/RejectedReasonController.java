package com.raymedis.rxviewui.service.adminSettings.studySettings;

import com.jfoenix.controls.JFXButton;
import com.raymedis.rxviewui.database.tables.adminSettings.study.study_rejectedReason_table.RejectedReasonEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.study.study_rejectedReason_table.RejectedReasonService;
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

public class RejectedReasonController {

    private final static RejectedReasonController instance = new RejectedReasonController();
    public VBox rejectedReasonOuterBox;
    public TableView rejectedReasonsDataGrid;
    public TableColumn reasonColumn;
    public TextField reasonLabel;
    public JFXButton addButton;

    private final RejectedReasonService rejectedReasonService = RejectedReasonService.getInstance();

    public static RejectedReasonController getInstance(){
        return instance;
    }

    private ArrayList<RejectedReasonEntity> rejectedList = new ArrayList<>();

    public void loadEvents() {
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("RejectedReason"));

        rejectedReasonsDataGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    RejectedReasonEntity rejectedReason = (RejectedReasonEntity) rejectedReasonsDataGrid.getSelectionModel().getSelectedItem();
                    if(rejectedReason!=null){
                        handleDoubleClick(rejectedReason);
                        addButton.setText("Edit");
                    }
                }
            }
        });

        rejectedReasonOuterBox.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            clearData();
        });
    }

    public void loadData() throws SQLException {
        rejectedList = rejectedReasonService.findAll();
        rejectedReasonsDataGrid.getItems().clear();
        rejectedReasonsDataGrid.getItems().addAll(rejectedList);

        id = rejectedList.size();
    }


    private void clearData() {
        rejectedReasonsDataGrid.getSelectionModel().clearSelection();
        reasonLabel.setText("");
        addButton.setText("Add");
    }

    private void handleDoubleClick(RejectedReasonEntity rejectedReason) {
        reasonLabel.setText(rejectedReason.getRejectedReason());

        addButton.setText("Edit");
        reasonLabel.setDisable(true);

    }




    public void deleteClick() {
        RejectedReasonEntity rejectedReason = (RejectedReasonEntity) rejectedReasonsDataGrid.getSelectionModel().getSelectedItem();

        if(rejectedReason==null){
            return;
        }

        try {
            rejectedReasonService.delete(rejectedReason.getRejectedReason());
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


    private static int id;
    public void addDataClick() {
        if(addButton.getText().equals("Add") || addButton.getText().equals("Save")){

            if(reasonLabel.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Fields");
                alert.setHeaderText(null);
                alert.setContentText("Please Fill the Details to Add...");
                alert.showAndWait();

                return;
            }

            RejectedReasonEntity rejectedReason = new RejectedReasonEntity();

            rejectedReason.setRejectedReason(reasonLabel.getText());
            rejectedReason.setId(id);

            if (addButton.getText().equals("Add")) {
                try {
                    rejectedReasonService.save(rejectedReason);
                } catch (SQLException e) {
                    alertMessage("Error", "Adding Reason Failed", e.getMessage());
                }
            } else if (addButton.getText().equals("Save")) {
                try {
                    rejectedReasonService.update(reasonLabel.getText(), rejectedReason);
                } catch (SQLException e) {
                    alertMessage("Error", "Save Reason Failed", "Error saving Reason: " + e.getMessage());
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
            reasonLabel.setDisable(false);
        }
    }


    private void alertMessage(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
