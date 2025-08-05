package com.raymedis.rxviewui.controller;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StageConnect {


    public static StageConnect instance = new StageConnect();

    public static StageConnect getInstance(){
        return instance;
    }

    public Stage xmainStage = null;

    public Scene mainScene ;

    public Parent loginParent;
}
