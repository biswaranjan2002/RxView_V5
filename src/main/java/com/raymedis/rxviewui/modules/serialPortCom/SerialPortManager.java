package com.raymedis.rxviewui.modules.serialPortCom;

import com.raymedis.rxviewui.service.study.StudyService;
import com.raymedis.serialPortCommunication.rxview_consolecom_old.TriggerCommandRxOld;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SerialPortManager {

    private static final SerialPortManager instance = new SerialPortManager();

    private static final Logger logger = LoggerFactory.getLogger(SerialPortManager.class);


    public static SerialPortManager getInstance() {
        return instance;
    }

    private SerialPortManager() {

    }

    private final TriggerCommandRxOld triggerCommandRxOld = TriggerCommandRxOld.getInstance();

    public void setKv(){

        double kv1 = filamentVoltage;
        double ma1 = filamentCurrent;
        double y = getGridKv(kv1);

        logger.info("KV Sent : {}", y);

        y=ma1;

        double filamentCurrent = getFilamentVoltage(kv1,y) * 1;  //0.992 is a calibration factor

        short val = (short)Math.round((filamentCurrent*250)/10);
        byte kv = (byte)(val & 0xff);

        logger.info("In short sending KV: {}", val);
        logger.info("In byte sending KV: {}", kv);

        new Thread(()->{
            triggerCommandRxOld.setKv(kv);
        }).start();
    }

    private double getFilamentVoltage(double kv, double ma) {
        double[] maStepsLow = {20, 40, 50, 60, 80, 100, 120, 160};
        double[] maStepsHigh = {200, 250, 300};
        double[] kvSteps = {40, 50, 60, 70, 80, 90, 100, 110}; // add upper bound 110 for extrapolation

        // Choose maSteps based on mA value
        double[] maSteps = ma <= 165 ? maStepsLow : maStepsHigh;

        // Clamp kv within range
        if (kv < kvSteps[0]) kv = kvSteps[0];
        if (kv > kvSteps[kvSteps.length - 1]) kv = kvSteps[kvSteps.length - 1];

        // Find kv interval (kv1, kv2)
        int kvIndex = findInterval(kvSteps, kv);
        double kv1 = kvSteps[kvIndex];
        double kv2 = kvSteps[kvIndex + 1];

        // Find ma interval (ma1, ma2)
        int maIndex = findInterval(maSteps, ma);
        double ma1 = maSteps[maIndex];
        double ma2 = maSteps[Math.min(maIndex + 1, maSteps.length - 1)];

        // Get calibration values at four corners
        double v11 = getFilamentCurrent(kv1, ma1);
        double v12 = getFilamentCurrent(kv1, ma2);
        double v21 = getFilamentCurrent(kv2, ma1);
        double v22 = getFilamentCurrent(kv2, ma2);

        // Interpolate in ma dimension for kv1 and kv2
        double v1 = linearInterpolate(ma1, v11, ma2, v12, ma);
        double v2 = linearInterpolate(ma1, v21, ma2, v22, ma);

        // Interpolate in kv dimension between v1 and v2
        return linearInterpolate(kv1, v1, kv2, v2, kv);
    }

    // Helper method: linear interpolation
    private double linearInterpolate(double x0, double y0, double x1, double y1, double x) {
        if (x1 == x0) return y0;  // avoid division by zero
        return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
    }

    // Helper method: find interval index in array where x is between arr[i] and arr[i+1]
    private int findInterval(double[] arr, double x) {
        if (x <= arr[0]) return 0;
        for (int i = 0; i < arr.length - 1; i++) {
            if (x >= arr[i] && x < arr[i + 1]) return i;
        }
        return arr.length - 2; // If beyond last interval, clamp to last
    }


    /*private double getFilamentVoltage(double kv, double ma) {

        double v1,v2,v3,v4,v5,v6;
        double finalVoltage=0;
        double kv1=0,kv2=0, ma1, ma2;
        double[] maSteps = {20, 40, 50, 60, 80, 100, 120, 160};

        if(ma<=165){
            if (kv < 40) {
                kv1 = 40;

                // Below lowest range
                if (ma < maSteps[0]) {
                    finalVoltage = getCalValue(kv1, maSteps[0]);
                }
                // Above highest range
                else if (ma >= maSteps[maSteps.length - 1]) {
                    finalVoltage = getCalValue(kv1, maSteps[maSteps.length - 1]);
                }
                // Interpolation range
                else {
                    for (int i = 0; i < maSteps.length - 1; i++) {
                         ma1 = maSteps[i];
                         ma2 = maSteps[i + 1];

                        if (ma >= ma1 && ma < ma2) {
                             v1 = getCalValue(kv1, ma1);
                             v2 = getCalValue(kv1, ma2);
                            finalVoltage = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;
                            break;
                        }
                    }
                }
            }
            else if (kv >= 40 && kv < 50) {
                kv1 = 40;
                kv2 = 50;

                // Case: ma below minimum
                if (ma < maSteps[0]) {
                     ma1 = maSteps[0];
                     v5 = getCalValue(kv1, ma1);
                     v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                }
                // Case: ma above or equal to maximum
                else if (ma >= maSteps[maSteps.length - 1]) {
                     ma1 = maSteps[maSteps.length - 1];
                     v5 = getCalValue(kv1, ma1);
                     v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                }
                // Case: within interpolation range
                else {
                    for (int i = 0; i < maSteps.length - 1; i++) {
                         ma1 = maSteps[i];
                         ma2 = maSteps[i + 1];

                        if (ma >= ma1 && ma < ma2) {
                            // Interpolate for kv1
                             v1 = getCalValue(kv1, ma1);
                             v2 = getCalValue(kv1, ma2);
                             v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                            // Interpolate for kv2
                             v3 = getCalValue(kv2, ma1);
                             v4 = getCalValue(kv2, ma2);
                             v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                            // Interpolate between kv1 and kv2
                            finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                            break;
                        }
                    }
                }
            }
            else if (kv >= 50 && kv < 60) {
                kv1 = 50;
                kv2 = 60;

                if (ma < maSteps[0]) {
                    ma1 = maSteps[0];
                    v5 = getCalValue(kv1, ma1);
                    v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                } else if (ma >= maSteps[maSteps.length - 1]) {
                    ma1 = maSteps[maSteps.length - 1];
                    v5 = getCalValue(kv1, ma1);
                    v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                } else {
                    for (int i = 0; i < maSteps.length - 1; i++) {
                        ma1 = maSteps[i];
                        ma2 = maSteps[i + 1];
                        if (ma >= ma1 && ma < ma2) {
                            v1 = getCalValue(kv1, ma1);
                            v2 = getCalValue(kv1, ma2);
                            v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                            v3 = getCalValue(kv2, ma1);
                            v4 = getCalValue(kv2, ma2);
                            v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                            finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                            break;
                        }
                    }
                }
            }
            else if (kv >= 60 && kv < 70) {
                kv1 = 60;
                kv2 = 70;

                if (ma < maSteps[0]) {
                    ma1 = maSteps[0];
                    v5 = getCalValue(kv1, ma1);
                    v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                } else if (ma >= maSteps[maSteps.length - 1]) {
                    ma1 = maSteps[maSteps.length - 1];
                    v5 = getCalValue(kv1, ma1);
                    v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                } else {
                    for (int i = 0; i < maSteps.length - 1; i++) {
                        ma1 = maSteps[i];
                        ma2 = maSteps[i + 1];
                        if (ma >= ma1 && ma < ma2) {
                            v1 = getCalValue(kv1, ma1);
                            v2 = getCalValue(kv1, ma2);
                            v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                            v3 = getCalValue(kv2, ma1);
                            v4 = getCalValue(kv2, ma2);
                            v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                            finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                            break;
                        }
                    }
                }
            }
            else if (kv >= 70 && kv < 80) {
                kv1 = 70;
                kv2 = 80;

                if (ma < maSteps[0]) {
                    ma1 = maSteps[0];
                    v5 = getCalValue(kv1, ma1);
                    v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                } else if (ma >= maSteps[maSteps.length - 1]) {
                    ma1 = maSteps[maSteps.length - 1];
                    v5 = getCalValue(kv1, ma1);
                    v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                } else {
                    for (int i = 0; i < maSteps.length - 1; i++) {
                        ma1 = maSteps[i];
                        ma2 = maSteps[i + 1];
                        if (ma >= ma1 && ma < ma2) {
                            v1 = getCalValue(kv1, ma1);
                            v2 = getCalValue(kv1, ma2);
                            v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                            v3 = getCalValue(kv2, ma1);
                            v4 = getCalValue(kv2, ma2);
                            v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                            finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                            break;
                        }
                    }
                }
            }
            else if (kv >= 80 && kv < 90) {
                kv1 = 80;
                kv2 = 90;

                if (ma < maSteps[0]) {
                    ma1 = maSteps[0];
                    v5 = getCalValue(kv1, ma1);
                    v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                } else if (ma >= maSteps[maSteps.length - 1]) {
                    ma1 = maSteps[maSteps.length - 1];
                    v5 = getCalValue(kv1, ma1);
                    v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                } else {
                    for (int i = 0; i < maSteps.length - 1; i++) {
                        ma1 = maSteps[i];
                        ma2 = maSteps[i + 1];
                        if (ma >= ma1 && ma < ma2) {
                            v1 = getCalValue(kv1, ma1);
                            v2 = getCalValue(kv1, ma2);
                            v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                            v3 = getCalValue(kv2, ma1);
                            v4 = getCalValue(kv2, ma2);
                            v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                            finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                            break;
                        }
                    }
                }
            }
            else if (kv >= 90 && kv < 100) {
                kv1 = 90;
                kv2 = 100;

                if (ma < maSteps[0]) {
                    ma1 = maSteps[0];
                    v5 = getCalValue(kv1, ma1);
                    v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                } else if (ma >= maSteps[maSteps.length - 1]) {
                    ma1 = maSteps[maSteps.length - 1];
                    v5 = getCalValue(kv1, ma1);
                    v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                } else {
                    for (int i = 0; i < maSteps.length - 1; i++) {
                        ma1 = maSteps[i];
                        ma2 = maSteps[i + 1];
                        if (ma >= ma1 && ma < ma2) {
                            v1 = getCalValue(kv1, ma1);
                            v2 = getCalValue(kv1, ma2);
                            v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                            v3 = getCalValue(kv2, ma1);
                            v4 = getCalValue(kv2, ma2);
                            v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                            finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                            break;
                        }
                    }
                }
            }
            else if (kv >= 100) {
                kv1 = 100;
                kv2 = kv1 + 10; // Or set kv2 to a fixed upper limit if applicable

                if (ma < maSteps[0]) {
                    ma1 = maSteps[0];
                    v5 = getCalValue(kv1, ma1);
                    v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                } else if (ma >= maSteps[maSteps.length - 1]) {
                    ma1 = maSteps[maSteps.length - 1];
                    v5 = getCalValue(kv1, ma1);
                    v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                } else {
                    for (int i = 0; i < maSteps.length - 1; i++) {
                        ma1 = maSteps[i];
                        ma2 = maSteps[i + 1];
                        if (ma >= ma1 && ma < ma2) {
                            v1 = getCalValue(kv1, ma1);
                            v2 = getCalValue(kv1, ma2);
                            v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                            v3 = getCalValue(kv2, ma1);
                            v4 = getCalValue(kv2, ma2);
                            v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                            finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                            break;
                        }
                    }
                }
            }
        }
        else if(ma >165){
             if (kv < 40) {

                kv1 = 40;
                if (ma < 200) {
                    ma1 = 200;
                    finalVoltage = getCalValue(kv1, ma1);
                } else if (ma < 300) {
                    if (ma < 250) {
                        ma1 = 200;
                        ma2 = 250;
                    } else {
                        ma1 = 250;
                        ma2 = 300;
                    }
                    v1 = getCalValue(kv1, ma1);
                    v2 = getCalValue(kv1, ma2);
                    finalVoltage = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;
                } else {
                    ma1 = 300;
                    v5 = getCalValue(kv1, ma1);
                    v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                }
            }
             else if (kv >= 40 && kv < 60) {
                 kv1 = 40;
                 kv2 = 60;

                 if (ma < 200) {
                     ma1 = 200;
                      v5 = getCalValue(kv1, ma1);
                      v6 = getCalValue(kv2, ma1);
                     finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;

                 } else if (ma < 250) {
                     ma1 = 200;
                     ma2 = 250;

                     v1 = getCalValue(kv1, ma1);
                     v2 = getCalValue(kv1, ma2);
                     v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                     v3 = getCalValue(kv2, ma1);
                     v4 = getCalValue(kv2, ma2);
                     v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                     finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;

                 } else if (ma < 300) {
                     ma1 = 250;
                     ma2 = 300;

                     v1 = getCalValue(kv1, ma1);
                     v2 = getCalValue(kv1, ma2);
                     v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                     v3 = getCalValue(kv2, ma1);
                     v4 = getCalValue(kv2, ma2);
                     v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                     finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;

                 } else {
                     ma1 = 300;
                     v5 = getCalValue(kv1, ma1);
                     v6 = getCalValue(kv2, ma1);
                     finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                 }
             }
             else if (kv >= 60 && kv < 80) {
                 kv1 = 60;
                 kv2 = 80;

                 if (ma < 200) {
                     ma1 = 200;
                     v5 = getCalValue(kv1, ma1);
                     v6 = getCalValue(kv2, ma1);
                     finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;

                 }
                 else if (ma < 250) {
                     ma1 = 200;
                     ma2 = 250;

                     v1 = getCalValue(kv1, ma1);
                     v2 = getCalValue(kv1, ma2);
                     v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                     v3 = getCalValue(kv2, ma1);
                     v4 = getCalValue(kv2, ma2);
                     v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                     finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;

                 }
                 else if (ma < 300) {
                     ma1 = 250;
                     ma2 = 300;

                     v1 = getCalValue(kv1, ma1);
                     v2 = getCalValue(kv1, ma2);
                     v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                     v3 = getCalValue(kv2, ma1);
                     v4 = getCalValue(kv2, ma2);
                     v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                     finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;

                 }
                 else {
                     ma1 = 300;
                     v5 = getCalValue(kv1, ma1);
                     v6 = getCalValue(kv2, ma1);
                     finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                 }
             }
             else if (kv >= 80 && kv < 100) {
                 kv1 = 80;
                 kv2 = 100;

                 if (ma < 200) {
                     ma1 = 200;
                     v5 = getCalValue(kv1, ma1);
                     v6 = getCalValue(kv2, ma1);
                     finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;

                 } else if (ma < 250) {
                     ma1 = 200;
                     ma2 = 250;
                     v1 = getCalValue(kv1, ma1);
                     v2 = getCalValue(kv1, ma2);
                     v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                     v3 = getCalValue(kv2, ma1);
                     v4 = getCalValue(kv2, ma2);
                     v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                     finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;

                 } else if (ma < 300) {
                     ma1 = 250;
                     ma2 = 300;
                     v1 = getCalValue(kv1, ma1);
                     v2 = getCalValue(kv1, ma2);
                     v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                     v3 = getCalValue(kv2, ma1);
                     v4 = getCalValue(kv2, ma2);
                     v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                     finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;

                 } else {
                     ma1 = 300;
                     v5 = getCalValue(kv1, ma1);
                     v6 = getCalValue(kv2, ma1);
                     finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                 }
             }
             else if (kv >= 100 && kv < 120) {
                kv1 = 100;
                kv2 = 120;

                if (ma < 200) {
                    ma1 = 200;
                    v5 = getCalValue(kv1, ma1);
                    v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;

                } else if (ma < 250) {
                    ma1 = 200;
                    ma2 = 250;
                    v1 = getCalValue(kv1, ma1);
                    v2 = getCalValue(kv1, ma2);
                    v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                    v3 = getCalValue(kv2, ma1);
                    v4 = getCalValue(kv2, ma2);
                    v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;

                } else if (ma < 300) {
                    ma1 = 250;
                    ma2 = 300;
                    v1 = getCalValue(kv1, ma1);
                    v2 = getCalValue(kv1, ma2);
                    v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                    v3 = getCalValue(kv2, ma1);
                    v4 = getCalValue(kv2, ma2);
                    v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;

                } else {
                    ma1 = 300;
                    v5 = getCalValue(kv1, ma1);
                    v6 = getCalValue(kv2, ma1);
                    finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                }
            }
             else if (kv >= 120 && kv < 125) {
                 kv1 = 120;
                 kv2 = 125;
                 if (ma < 200) {
                     ma1 = 200;
                     v5 = getCalValue(kv1, ma1);
                     v6 = getCalValue(kv2, ma1);
                     finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                 }
                 else if (ma < 250) {
                     ma1 = 200;
                     ma2 = 250;
                     v1 = getCalValue(kv1, ma1);
                     v2 = getCalValue(kv1, ma2);
                     v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                     v3 = getCalValue(kv2, ma1);
                     v4 = getCalValue(kv2, ma2);
                     v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                     finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                 }
                 else if (ma < 300) {
                     ma1 = 250;
                     ma2 = 300;
                     v1 = getCalValue(kv1, ma1);
                     v2 = getCalValue(kv1, ma2);
                     v5 = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;

                     v3 = getCalValue(kv2, ma1);
                     v4 = getCalValue(kv2, ma2);
                     v6 = ((v4 - v3) / (ma2 - ma1)) * (ma - ma1) + v3;

                     finalVoltage = ((v6 - v5) / (kv2 - kv1)) * (kv - kv1) + v5;
                 }
             }
             else if (kv >= 125) {
                 kv1 = 125;
                 if (ma < 200) {
                     ma1 = 200;
                     finalVoltage = getCalValue(kv1, ma1);
                 } else if (ma < 250) {
                     ma1 = 200;
                     ma2 = 250;
                     v1 = getCalValue(kv1, ma1);
                     v2 = getCalValue(kv1, ma2);
                     finalVoltage = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;
                 } else if (ma < 300) {
                     ma1 = 250;
                     ma2 = 300;
                     v1 = getCalValue(kv1, ma1);
                     v2 = getCalValue(kv1, ma2);
                     finalVoltage = ((v2 - v1) / (ma2 - ma1)) * (ma - ma1) + v1;
                 } else {
                     ma1 = 300;
                     finalVoltage = getCalValue(kv1, ma1);
                 }
             }
        }

        return finalVoltage;
    }*/


    public double getFilamentCurrent(double kv, double ma) {
        double[][] mACalibTable = {
                // 20      40      60      70      80      100     120     160    // mA/kV
                { 7.50,   7.95,   8.23,   8.35,   8.45,   8.60,   8.80,   9.00 },  // 40 kV
                { 7.47,   7.92,   8.20,   8.32,   8.40,   8.55,   8.75,   9.00 },  // 50 kV
                { 7.44,   7.89,   8.17,   8.29,   8.35,   8.52,   8.70,   7.40 },  // 60 kV
                { 7.41,   7.86,   8.14,   8.26,   8.32,   8.49,   8.70,   7.40 },  // 70 kV
                { 7.38,   7.83,   8.11,   8.23,   8.29,   8.49,   8.70,   7.40 },  // 80 kV
                { 7.35,   7.80,   8.08,   8.20,   8.29,   8.49,   8.70,   7.40 },  // 90 kV
                { 7.35,   7.78,   8.05,   8.17,   8.29,   8.49,   8.70,   7.40 }   // 100 kV
        };

        int kvpos;
        int mapos;

        // Determine kv index
        if (kv < 50) {
            kvpos = 0;  // default to first row for <50
        } else if (kv < 60) {
            kvpos = 1;
        } else if (kv < 70) {
            kvpos = 2;
        } else if (kv < 80) {
            kvpos = 3;
        } else if (kv < 90) {
            kvpos = 4;
        } else if (kv < 100) {
            kvpos = 5;
        } else {
            kvpos = 6;  // >= 100
        }

        // Determine ma index
        if (ma < 40) {
            mapos = 0;
        } else if (ma < 60) {
            mapos = 1;
        } else if (ma < 70) {
            mapos = 2;
        } else if (ma < 80) {
            mapos = 3;
        } else if (ma < 100) {
            mapos = 4;
        } else if (ma < 120) {
            mapos = 5;
        } else if (ma < 160) {
            mapos = 6;
        } else {
            mapos = 7;  // >=160
        }

        double calValue = mACalibTable[kvpos][mapos];
        logger.info("Calibration Val: {}", calValue);
        return calValue;
    }

    private double getGridKv(double kv) {
        double[] kvSteps = {40, 50, 60, 70, 80, 90, 100};

        for (int i = 0; i < kvSteps.length - 1; i++) {
            double kv1 = kvSteps[i];
            double kv2 = kvSteps[i + 1];

            if (kv >= kv1 && kv < kv2) {
                return interpolate(kv1, kv2, kv1, kv2, kv);
            }
        }

        // If kv >= 100
        return Math.min(kv, 100); // Capping at 100 as per original logic
    }


    private double interpolate(double x1, double x2, double y1, double y2, double x) {
        return ((y2 - y1) / (x2 - x1)) * (x - x1) + y1;
    }

    public void setMa(){

        short x;

        double kv1 = filamentVoltage;
        double ma1 = filamentCurrent;

        logger.info("setMa kv1: {}, ma1: {}", kv1, ma1);


        double y = getGridKv(kv1);

        x = (short)Math.round((y * 250) / 100);
        logger.info("x: {}", x);
        if (x >= 250) {
            x = 250;
        }

        byte val1= (byte) (x & 0xFF);

        y = ma1;
        logger.info("Current Sent: {}", y);
        double filamentCurrent = getFilamentVoltage(kv1, y);
        logger.info("filamentCurrent: {}", filamentCurrent);
        x = (short)Math.round((filamentCurrent * 250) / 10);
        byte val2 = (byte) (x & 0xFF);

        new Thread(()->{
            triggerCommandRxOld.setMa(val1,val2);
        }).start();

    }

    public void setExposureTime(){

        logger.info("setExposureTime {}",exposureTime);

        double y=exposureTime*1000;

        short x = (short)y;
        logger.info("Exposure Time Sent - {}", x);

        byte v1 = (byte)(x & 0xff);
        byte v2 = (byte)((x >> 8) & 0xff);

        new Thread(()->{
            triggerCommandRxOld.setExpTime(v1, v2,'G'); //G is default command Variable
        }).start();
    }


    double minkv = 0;
    double maxkv = 400;
    double filamentVoltage = 0;
    double filamentCurrent = 0;
    double exposureTime;
    double maxmAs;

    public void incKv(double currentKv, double currentMa) {
        logger.info("incKv currentKv: {}, currentMa: {}", currentKv, currentMa);

        String maLabel;
        minkv=40;

        if (currentMa == 20 || currentMa == 40 || currentMa == 50 || currentMa == 60) {
            maLabel = "400/320/250";
            maxkv = 100;
        } else if (currentMa == 80) {
            maLabel = "250";
            maxkv = 87;
        } else if (currentMa == 100) {
            maLabel = "250";
            maxkv = 70;
        } else if (currentMa == 120) {
            maLabel = "250";
            maxkv = 58;
        } else if (currentMa == 160) {
            maLabel = "250";
            maxkv = 43;
        } else {
            logger.warn("Unhandled ma value: {}", currentMa);
            return;
        }

        logger.info("In mA {}", maLabel);

        if (currentKv < maxkv) {
            currentKv++;
        }

        filamentVoltage = currentKv;
        filamentCurrent = currentMa;

        this.kv.set(currentKv);
        this.ma.set(currentMa);

        setMa();
        setKv();
    }

    public void incMa(double currentKv, double currentMa){

        logger.info("received currentKv: {}, currentMa: {}", currentKv, currentMa);

        int maxlength;
        List<Integer> mAkW = new ArrayList<>();

        try
        {
            if (currentKv <= 43)
            {
                mAkW.add(20);
                mAkW.add(40);
                mAkW.add(60);
                mAkW.add(70);
                mAkW.add(80);
                mAkW.add(100);
                mAkW.add(120);
                mAkW.add(160);
            }
            if (currentKv > 43 && currentKv <= 50)
            {
                mAkW.add(20);
                mAkW.add(40);
                mAkW.add(60);
                mAkW.add(70);
                mAkW.add(80);
                mAkW.add(100);
                mAkW.add(120);
            }
            if (currentKv > 50 && currentKv <= 58)
            {
                mAkW.add(20);
                mAkW.add(40);
                mAkW.add(60);
                mAkW.add(70);
                mAkW.add(80);
                mAkW.add(100);
                mAkW.add(120);
            }
            if (currentKv > 58 && currentKv <= 60)
            {
                mAkW.add(20);
                mAkW.add(40);
                mAkW.add(60);
                mAkW.add(70);
                mAkW.add(80);
                mAkW.add(100);
            }
            if (currentKv > 60 && currentKv <= 70)
            {
                mAkW.add(20);
                mAkW.add(40);
                mAkW.add(60);
                mAkW.add(70);
                mAkW.add(80);
                mAkW.add(100);
            }
            if (currentKv > 70 && currentKv <= 80)
            {
                mAkW.add(20);
                mAkW.add(40);
                mAkW.add(60);
                mAkW.add(70);
                mAkW.add(80);
            }
            if (currentKv > 80 && currentKv <= 90)
            {
                mAkW.add(20);
                mAkW.add(40);
                mAkW.add(60);
                mAkW.add(70);
            }
            if (currentKv > 90 && currentKv <= 100)
            {
                mAkW.add(20);
                mAkW.add(40);
                mAkW.add(60);
                mAkW.add(70);
            }

            maxlength = mAkW.size() - 1;

            int x;
            int val = (int) currentMa;

            if (mAkW.contains(val))
            {
                x = mAkW.indexOf(val);
            }
            else
            {
                x = mAkW.size() - 1;
            }

            logger.info("x {}, maxlength: {}", x, maxlength);

            if (x < maxlength)
                x++;

            logger.info("x {}, maxlength: {}", x, maxlength);

            currentMa = mAkW.get(x);

            logger.info("currentMa: {}", currentMa);

            filamentVoltage = currentKv;
            filamentCurrent = currentMa;

            this.kv.set(currentKv);
            this.ma.set(currentMa);

            setMa();
            setKv();
            updateTimer();
        }
        catch (Exception ex)
        {
            logger.info("Info - {}", ex.getMessage());
        }


    }

    private void updateTimer() {

        if(ma.getValue()==20){
            maxmAs=100;
        } else if (ma.getValue() == 30) {
            maxmAs=150;
        }else{
            maxmAs=200;
        }

        if(mas.getValue()>maxmAs){
           mas.set(maxmAs);
           exposureTime = mas.getValue()/ma.getValue();
           ms.set(exposureTime);
       }

       if(ma.getValue()!=0){
           exposureTime = mas.getValue()/ma.getValue();
           ms.set(exposureTime);
       }

       setExposureTime();

    }

    public void incmAs(double currentMa,double currentmAs) {
        try {

            if (currentMa == 20) {
                maxmAs = 100;
            } else if (currentMa == 30) {
                maxmAs = 150;
            } else {
                maxmAs = 200;
            }


            if(currentmAs<maxmAs){
                currentmAs++;
                mas.set(currentmAs);
            }

            if(currentmAs>maxmAs){
                currentmAs = maxmAs;
                mas.set(currentmAs);
            }

            if (currentMa != 0.0) {
                exposureTime = currentmAs / currentMa;
                ms.set(exposureTime);
            }

            setExposureTime();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public void decKv(double currentKv, double currentMa) {
        logger.info("decKv currentKv: {}, currentMa: {}", currentKv, currentMa);

        String maLabel = "";

        if (currentMa == 20 || currentMa == 40) {
            maLabel = "In mA 400";
            minkv = 40;
            maxkv = 100;
        } else if (currentMa == 50) {
            maLabel = "In mA 320";
            minkv = 40;
            maxkv = 100;
        } else if (currentMa == 60) {
            maLabel = "In mA 250";
            minkv = 40;
            maxkv = 100;
        } else if (currentMa == 80) {
            maLabel = "In mA 250";
            minkv = 40;
            maxkv = 87;
        } else if (currentMa == 100) {
            maLabel = "In mA 250";
            minkv = 40;
            maxkv = 70;
        } else if (currentMa == 120) {
            maLabel = "In mA 250";
            minkv = 40;
            maxkv = 58;
        } else if (currentMa == 160) {
            maLabel = "In mA 250";
            minkv = 40;
            maxkv = 43;
        } else {
            logger.warn("Unhandled mA value: {}", currentMa);
        }

        logger.info("{}", maLabel);

        if(currentKv>minkv){
            currentKv--;
        }

        filamentVoltage=currentKv;
        filamentCurrent=currentMa;

        this.kv.set(currentKv);
        this.ma.set(currentMa);

        setMa();
        setKv();
    }

    public void decMA(double currentKv, double currentMa) {

        logger.info("decMA currentKv: {}, currentMa: {}", currentKv, currentMa);

        List<Integer> mAkW = new ArrayList<>();

        if (currentKv <= 43) {
            mAkW.addAll(Arrays.asList(20, 40, 60, 70, 80, 100, 120, 160));
        } else if (currentKv > 43 && currentKv <= 58) {
            mAkW.addAll(Arrays.asList(20, 40, 60, 70, 80, 100, 120));
        } else if (currentKv > 58 && currentKv <= 70) {
            mAkW.addAll(Arrays.asList(20, 40, 60, 70, 80, 100));
        } else if (currentKv > 70 && currentKv <= 80) {
            mAkW.addAll(Arrays.asList(20, 40, 60, 70, 80));
        } else if (currentKv > 80 && currentKv <= 100) {
            mAkW.addAll(Arrays.asList(20, 40, 60, 70));
        }

        int val = (int) currentMa;

        logger.info("Current mA value: {}", val);

        int x = mAkW.contains(val) ? mAkW.indexOf(val) : mAkW.size() - 1;

        logger.info("x before decrement: {}, mAkW size: {}", x, mAkW.size());

        if (x > 0) x--;

        logger.info("x after decrement: {}, mAkW size: {}", x, mAkW.size());



        filamentCurrent = mAkW.get(x);
        filamentVoltage = currentKv;

        logger.info("New filamentCurrent: {}, filamentVoltage: {}", filamentCurrent, filamentVoltage);

        this.kv.set(currentKv);
        this.ma.set(mAkW.get(x));

        setMa();
        setKv();
        updateTimer();
    }

    public void decmAs(double currentMa,double currentmAs){

        double minMas = 1;

        //checkMas
        if (currentMa == 20) {
            maxmAs=100;
        } else if (currentMa==30) {
            maxmAs=150;
        }
        else{
            maxmAs=200;
        }

        //decrement
        if(currentmAs>minMas){
            currentmAs--;
            mas.set(currentmAs);
        }


        if(currentmAs>maxmAs){
            currentmAs = maxmAs;
            mas.set(currentmAs);
        }

        if(ma.getValue()!=0){
            exposureTime= (currentmAs/currentMa);
            ms.set(exposureTime);
        }

        setExposureTime();

    }


    public DoubleProperty kv = new SimpleDoubleProperty(0.0);
    public DoubleProperty ma = new SimpleDoubleProperty(0);
    public DoubleProperty ms = new SimpleDoubleProperty(0);
    public DoubleProperty mas = new SimpleDoubleProperty(0);

    public void setXrayConsole(double kv,int ma,double mas){

        String patientSize = StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy().getPatientInfo().getPatientSize();

        switch (patientSize){
            case "Infant":
                kv = kv-10;
                break;
            case "Small":
                kv=kv-5;
            case "Big":
                kv=kv+5;
        }

        logger.info("Setting Xray Console - kv: {}, ma: {}, mas: {}", kv, ma, mas);

        double finalKv = kv;
        Platform.runLater(()->{
            this.kv.set(finalKv);
            this.ma.set(ma);
            this.mas.set(mas);
            this.ms.set(mas/ma);
        });


        filamentVoltage= kv;
        filamentCurrent = ma;
        exposureTime=(mas/ma);

        setData();
    }

    private void setData() {
        setMa();
        setKv();
        setExposureTime();

   /*     executor.submit(() -> {
            try {
                Thread.sleep(300);
                Thread.sleep(0);

                Thread.sleep(50);

                Thread.sleep(50);

                Thread.sleep(50);
                System.out.println("Set Timer Delay Done.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });*/

    }


    public void decMs() {
        double currentMs = ms.getValue();

        logger.info("decMs currentMs: {}", currentMs);

        if (currentMs > 0.01) { //Assuming 10ms in Minimum Limit

            currentMs = currentMs - (currentMs*0.1); //temporarily Decrement by 10 percent
            ms.set(currentMs);
            mas .set( currentMs * ma.getValue()); // Update mas based on new ms value
        }
        else {
            logger.warn("ms is already at minimum value.");
        }

        setExposureTime();
    }


    public void incMs() {
        double currentMs = ms.getValue();

        logger.info("incMs currentMs: {}", currentMs);

        if (currentMs < 5) { // Assuming 5000 ms is the maximum limit

            currentMs = currentMs + (currentMs*0.1); //temporarily Increment by 10 percent
            ms.set(currentMs);
            mas .set( currentMs * ma.getValue()); // Update mas based on new ms value
        }
        else {
            logger.warn("ms is already at minimum value.");
        }

        setExposureTime();
    }
}
