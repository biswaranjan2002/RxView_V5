package com.raymedis.rxviewui.service.registration;

import com.jfoenix.controls.JFXButton;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BodyPartSelectionController {

    private static BodyPartSelectionController instance = new BodyPartSelectionController();
    public static BodyPartSelectionController getInstance(){
        return instance;
    }

    public HashMap<JFXButton,JFXButton> selectedBodyPartPositionMapForDelete;
    public Map<JFXButton,String> projectionsButonMap;

    public BodyPartSelectionController(){
        selectedBodyPartPositionMapForDelete = new HashMap<>();
        projectionsButonMap = new LinkedHashMap<>();
    }

}
