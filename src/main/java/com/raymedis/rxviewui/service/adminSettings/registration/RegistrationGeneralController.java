package com.raymedis.rxviewui.service.adminSettings.registration;

import com.jfoenix.controls.JFXRadioButton;
import com.raymedis.rxviewui.database.tables.adminSettings.register.register_general_table.DefaultTabEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.register.register_general_table.RegistrationGeneralService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import java.sql.SQLException;
import java.util.ArrayList;

public class RegistrationGeneralController {

    private static final RegistrationGeneralController instance = new RegistrationGeneralController();
    public static RegistrationGeneralController getInstance(){
        return instance;
    }

    public JFXRadioButton manualRadioButton;
    public JFXRadioButton workListRadioButton;
    public JFXRadioButton localListRadioButton;

    @FXML ToggleGroup radioGroup = new ToggleGroup();

    private final RegistrationGeneralService registrationGeneralService = RegistrationGeneralService.getInstance();

    public void loadData() throws SQLException {
        manualRadioButton.setToggleGroup(radioGroup);
        workListRadioButton.setToggleGroup(radioGroup);
        localListRadioButton.setToggleGroup(radioGroup);

        ArrayList<DefaultTabEntity> defaultTabEntities =  registrationGeneralService.getDefaultTab();
        if(defaultTabEntities!=null){

            DefaultTabEntity defaultTab=null;

            if(!defaultTabEntities.isEmpty()){
                defaultTab = defaultTabEntities.getFirst();
            }

            if(defaultTab.getManual()){
                manualRadioButton.setSelected(true);
            }else if(defaultTab.getWorkList()){
                workListRadioButton.setSelected(true);
            } else if (defaultTab.getLocalList()) {
                localListRadioButton.setSelected(true);
            }else{
                System.out.println("Nothing....");
            }
        }
    }


    public void defaultTabSaveClick(ActionEvent actionEvent) throws SQLException {
        Toggle selectedToggle = radioGroup.getSelectedToggle();
        if (selectedToggle != null) {
            JFXRadioButton selectedRadioButton = (JFXRadioButton) selectedToggle;
            String defaultTab = selectedRadioButton.getText().toLowerCase();
            DefaultTabEntity defaultTabEntity;
            switch (defaultTab){
                case "manual":
                    defaultTabEntity = new DefaultTabEntity(true,false,false);
                    registrationGeneralService.updateDefaultTab(0,defaultTabEntity);
                    break;
                case "worklist":
                    defaultTabEntity = new DefaultTabEntity(false,true,false);
                    registrationGeneralService.updateDefaultTab(0,defaultTabEntity);
                    break;
                case "locallist":
                    defaultTabEntity = new DefaultTabEntity(false,false,true);
                    registrationGeneralService.updateDefaultTab(0,defaultTabEntity);
                    break;
                default:
                    defaultTabEntity = new DefaultTabEntity();
                    System.out.println("something wrong.....");
            }
        }
    }
}
