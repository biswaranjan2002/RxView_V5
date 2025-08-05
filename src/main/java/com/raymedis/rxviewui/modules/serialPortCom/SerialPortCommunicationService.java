package com.raymedis.rxviewui.modules.serialPortCom;


import com.raymedis.rxviewui.service.study.StudyMainController;
import com.raymedis.rxviewui.service.study.StudyService;
import com.raymedis.serialPortCommunication.rxview_consolecom_old.InterlockFaults;
import com.raymedis.serialPortCommunication.rxview_consolecom_old.ReceiveCommandRxOld;
import com.raymedis.serialPortCommunication.rxview_consolecom_old.ReceiveEnumRxOld;
import com.raymedis.serialPortCommunication.rxview_consolecom_old.RxPanelInitializationOld;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SerialPortCommunicationService {

    private static SerialPortCommunicationService instance = new SerialPortCommunicationService();
    private static final Logger logger = LoggerFactory.getLogger(SerialPortCommunicationService.class);
    private final RxPanelInitializationOld rxPanelInitializationOld =  RxPanelInitializationOld.getInstance();
    public static SerialPortCommunicationService getInstance() {
        if(instance == null) {
            logger.error("SerialPortCommunicationService instance is null!");
           instance = new SerialPortCommunicationService();
        }
        return instance;
    }

    private SerialPortCommunicationService() {
        ReceiveCommandRxOld.getInstance().setResponseHandler((Object response) -> {

            logger.info("Received response: {}", response);

            if (response instanceof InterlockFaults faults) {
                // Show alert with detailed faults
                StringBuilder alertText = new StringBuilder("Detected Interlock Faults:\n");

                if (faults.overvoltage) alertText.append("- Overvoltage\n");
                if (faults.filament)    alertText.append("- Filament\n");
                if (faults.thermal)     alertText.append("- Thermal\n");
                if (faults.rotor)       alertText.append("- Rotor\n");
                if (faults.overcurrent) alertText.append("- Overcurrent\n");
                if (faults.gos)         alertText.append("- GOS\n");

                StudyMainController.getInstance().faultIcon.setId("faultIcon");

                alertMessage(alertText.toString());

            }
            else if (response instanceof ReceiveEnumRxOld receiveEnumRxOld) {

                logger.info("Received response: {}", receiveEnumRxOld);

                switch (receiveEnumRxOld) {
                    case DO_NOTHING:
                        StudyMainController.getInstance().consoleOnUi();
                        initialized=true;
                        StudyMainController.getInstance().setConsoleXrayValues(StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getCurrentBodyPart().getBodyPart());
                        break;
                    case READY:
                        logger.info("Exposure is ready.");
                        break;
                    case EXPOSE:
                        logger.info("Exposure in progress.");
                        break;
                    case EXPOSE_RELEASE:
                        logger.info("End of exposure.");
                        StudyMainController.getInstance().bodyPartTabPane.setDisable(false);
                        StudyMainController.getInstance().tabPane.setDisable(false);
                        break;
                    case INTERLOCK_INFO:
                        logger.info("Interlock information received.");
                        break;
                    case EXPOSE_SUCCESS:
                        logger.info("Exposure successful.");
                        StudyMainController.getInstance().bodyPartTabPane.setDisable(false);
                        StudyMainController.getInstance().tabPane.setDisable(false);
                        break;
                    case EXPOSE_UNSUCCESS:
                        logger.info("Exposure failed.");
                        break;
                    case INTERLOCK:
                        logger.info("Interlock detected.");
                        break;
                    case LONG_EXPOSURE:
                        logger.info("Long exposure detected.");
                        break;
                    case INTERLOCK_FAILED:
                        logger.info("Interlock failed.");
                        break;
                    case GENERATOR_OFF:
                        logger.info("Generator is off.");
                        alertMessage("Generator is off. Please check the generator.");
                        break;
                    case LBD_OFF:
                        StudyMainController.getInstance().lbdButton.setSelected(false);
                        alertMessage("Generator is off. Please check the generator.");
                        break;
                    case LBD_ON:
                        StudyMainController.getInstance().lbdButton.setSelected(true);
                        break;

                    default:
                        logger.info("Unknown response: {}", receiveEnumRxOld);
                }
            }

        });
    }

    private void alertMessage(String message) {
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });

    }


    public List<Integer> getAvailablePorts() {
        return rxPanelInitializationOld.getAvailablePorts();
    }

    public boolean initialized = false;

    public void initializeSerialPort(int comPortNumber) {
        rxPanelInitializationOld.initialize(comPortNumber);
    }

    public void disconnectSerialPort() {
        if (rxPanelInitializationOld != null ) {
            if(rxPanelInitializationOld.comPort!=null){
                rxPanelInitializationOld.disconnect();
            }
        }else{
            System.out.println("Serial port is not initialized.");
        }


    }


    public void expose() {

        if (SerialPortManager.getInstance().mas.getValue() > 200) {
            logger.info("Exposure time exceeds maximum allowed mAs: {} > {}", SerialPortManager.getInstance().mas.getValue(), SerialPortManager.getInstance().maxmAs);

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Exposure Time Exceeded");
                alert.setContentText("The exposure time exceeds the maximum allowed mAs: " + SerialPortManager.getInstance().maxmAs);
                alert.showAndWait();
            });
        }
        else {
            if (rxPanelInitializationOld != null) {
                rxPanelInitializationOld.handleExpose();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if(!StudyService.imageStatus){
                    StudyService.imageStatus=true;
                }

            } else {
                logger.error("rxPanelInitializationOld is null. Cannot perform exposure.");
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Initialization Error");
                    alert.setContentText("Exposure system is not properly initialized.");
                    alert.showAndWait();
                });
            }
        }



    }


    public void setLbd(boolean enable) {

        //run in async task

        CompletableFuture.runAsync(() -> {
            if (rxPanelInitializationOld != null) {
                logger.info("Setting LBD to: {}", enable);
                rxPanelInitializationOld.handleLbd(enable);
            }
            else {
                logger.error("rxPanelInitializationOld is null. Cannot set LBD.");
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Initialization Error");
                    alert.setContentText("Exposure system is not properly initialized.");
                    alert.showAndWait();
                });
            }
        });


    }



}
