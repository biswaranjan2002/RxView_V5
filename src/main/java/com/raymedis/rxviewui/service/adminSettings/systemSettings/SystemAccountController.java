package com.raymedis.rxviewui.service.adminSettings.systemSettings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.database.tables.adminSettings.system.user_accounts_table.SystemAccountsEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.system.user_accounts_table.SystemAccountsService;
import com.raymedis.rxviewui.service.login.Hashing;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SystemAccountController {

    private static final SystemAccountController instance = new SystemAccountController();
    public TableView dataGrid;
    public TextField userIdLabel;
    public TextField userNameLabel;
    public PasswordField passwordLabel;
    public TextField confirmPasswordLabel;
    public JFXComboBox groupComboBox;
    public JFXButton addButton;

    public TableColumn userIdColumn;
    public TableColumn userNameColumn;
    public TableColumn groupColumn;
    public VBox tableRootBox;

    private List<SystemAccountsEntity> totalAccounts = new ArrayList<>();
    private final SystemAccountsService systemAccountsService = SystemAccountsService.getInstance();
    public static SystemAccountController getInstance(){
        return instance;
    }

    public void loadEvents(){
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("userGroup"));

        dataGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    SystemAccountsEntity account = (SystemAccountsEntity) dataGrid.getSelectionModel().getSelectedItem();
                    if(account!=null){
                        handleDoubleClick(account);
                        addButton.setText("Edit");
                    }
                }
            }
        });

        tableRootBox.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            clearData();
        });


        idValidation(userIdLabel);
        alphabetsValidation(userNameLabel);
        idValidation(passwordLabel);
        idValidation(confirmPasswordLabel);
    }

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
            if (!newValue.matches("[a-zA-Z0-9]*")) {  //Only allows letters and numbers
                textField.setText(newValue.replaceAll("[^a-zA-Z0-9]", ""));
            } else if (newValue.length() > 20) {
                textField.setText(oldValue);
            }
        });
    }

    private void handleDoubleClick(SystemAccountsEntity account) {
        userIdLabel.setText(account.getUserId());
        userNameLabel.setText(account.getUserName());
//        passwordLabel.setText(account.getPassword());
//        confirmPasswordLabel.setText(account.getPassword());

        String group = account.getUserGroup().toLowerCase();
        switch (group){
            case "admin":
                groupComboBox.getSelectionModel().select(0);
                break;
            case "technician":
                groupComboBox.getSelectionModel().select(1);
                break;
            case "service":
                groupComboBox.getSelectionModel().select(2);
                break;
            default:
                System.out.println("unknown user group....");
        }

        addButton.setText("Edit");
        userIdLabel.setDisable(true);
        userNameLabel.setDisable(true);
        passwordLabel.setDisable(true);
        confirmPasswordLabel.setDisable(true);
        groupComboBox.setDisable(true);
    }


    public void loadData() {
        dataGrid.getItems().clear();
        clearData();

        try {
            totalAccounts = systemAccountsService.findAllAccounts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dataGrid.getItems().addAll(totalAccounts);
    }

    public void deleteClick() {
        SystemAccountsEntity account = (SystemAccountsEntity) dataGrid.getSelectionModel().getSelectedItem();

        if(account==null){
            return;
        }

        try {
            systemAccountsService.deleteSystemAccount(account.getUserId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        loadData();
        clearData();

    }

    private void clearData(){
        dataGrid.getSelectionModel().clearSelection();
        userIdLabel.setText("");
        userNameLabel.setText("");
        passwordLabel.setText("");
        confirmPasswordLabel.setText("");

        userIdLabel.setDisable(false);
        userNameLabel.setDisable(false);
        passwordLabel.setDisable(false);
        confirmPasswordLabel.setDisable(false);

        groupComboBox.setDisable(false);
        addButton.setText("Add");
    }




    public void addDataClick() {
        if(addButton.getText().equals("Add") || addButton.getText().equals("Save")){

            if(userIdLabel.getText().isEmpty() || userNameLabel.getText().isEmpty() ||
                    passwordLabel.getText().isEmpty() || confirmPasswordLabel.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Fields");
                alert.setHeaderText(null);
                alert.setContentText("Please Fill the Details to Add...");
                alert.showAndWait();

                return;
            }

            SystemAccountsEntity systemAccount = new SystemAccountsEntity();

            if(addButton.getText().equals("Save")){
                userIdLabel.setDisable(true);
            }else if(addButton.getText().equals("Add")){
                userIdLabel.setDisable(false);
            }

            systemAccount.setUserId(userIdLabel.getText());
            systemAccount.setUserName(userNameLabel.getText());
            systemAccount.setPassword(Hashing.hashPassword(passwordLabel.getText()));
            systemAccount.setUserGroup(groupComboBox.getSelectionModel().getSelectedItem().toString());

            if (!passwordLabel.getText().equals(confirmPasswordLabel.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Password Mismatch");
                alert.setHeaderText(null);
                alert.setContentText("Please check the password. The passwords do not match.");
                alert.showAndWait();

                return;
            }




            try {
                systemAccountsService.updateSystemAccount(userIdLabel.getText(), systemAccount);
            } catch (SQLException e) {
                alertMessage("Error", "Saving Account Failed", "UserId Already in Use..");
            }


            loadData();
            clearData();
        }
        else if (addButton.getText().equals("Edit")) {
            addButton.setText("Save");
            userNameLabel.setDisable(false);
            passwordLabel.setDisable(false);
            confirmPasswordLabel.setDisable(false);
            groupComboBox.setDisable(false);
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
