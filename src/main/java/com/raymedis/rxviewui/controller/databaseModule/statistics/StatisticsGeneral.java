package com.raymedis.rxviewui.controller.databaseModule.statistics;

import eu.hansolo.medusa.Gauge;
import javafx.scene.paint.Color;

public class StatisticsGeneral {

    public Gauge gauge;

    public void initialize(){
        gauge.setNeedleColor(Color.ORANGERED);
        gauge.setValue(60);
    }



}
