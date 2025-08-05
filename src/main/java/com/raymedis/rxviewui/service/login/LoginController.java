package com.raymedis.rxviewui.service.login;

import com.raymedis.rxviewui.controller.NavConnect;
import com.raymedis.rxviewui.database.tables.adminSettings.register.register_general_table.DefaultTabEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.register.register_general_table.RegistrationGeneralService;
import com.raymedis.rxviewui.database.tables.adminSettings.study.study_autoDelete_table.AutoDeleteEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.study.study_autoDelete_table.AutoDeleteService;
import com.raymedis.rxviewui.database.tables.adminSettings.study.study_storageAlerts_table.StorageAlertsEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.study.study_storageAlerts_table.StorageAlertsService;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyEntity;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyService;
import com.raymedis.rxviewui.service.fpdSdks.controller.FpdController;
import com.raymedis.rxviewui.service.fpdSdks.service.FpsStatus;
import com.raymedis.rxviewui.service.json.*;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController {

    private final static LoginController instance = new LoginController();
    public Parent mainWindowParent;

    public static LoginController getInstance(){
        return instance;
    }

    public TextField userIdText;
    public TextField passwordText;

    private final LoginService loginService = LoginService.getInstance();
    private final NavConnect navConnect  = NavConnect.getInstance();
    private final RegistrationGeneralService registrationGeneralService = RegistrationGeneralService.getInstance();
    private final StorageAlertsService storageAlertsService = StorageAlertsService.getInstance();
    private final FpdController fpdController = FpdController.getInstance();

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    public static List<RaymedisConfig> applications;

    public void loginButtonClick() {
        String role =  loginService.loginButtonClick(userIdText.getText(),passwordText.getText());
        logger.info("role is {}",role);

        Scene currentScene = passwordText.getScene();

        switch (role.toLowerCase()){
            case "admin":
                resetLoginPage();
                currentScene.setRoot(mainWindowParent);
                navConnect.adminLabel.setVisible(true);
                navConnect.adminIcon.setVisible(true);
                navConnect.adminButton.setOnAction(actionEvent -> {
                    navConnect.navigateToAdminPage();
                });
                loadMainWindowSettings();
                break;
            case "technician", "service":
                resetLoginPage();
                currentScene.setRoot(mainWindowParent);
                navConnect.adminButton.setOnAction(null);
                navConnect.adminLabel.setVisible(false);
                navConnect.adminIcon.setVisible(false);
                loadMainWindowSettings();
                break;
            case "null":
                showAlert(Alert.AlertType.ERROR,"Login Failed", "Username or password not found");
                break;
        }

    }

    public static double formatSize(long bytes) {
        int unit = 1024;
        if (bytes < unit) {
            return bytes;
        }

        int exp = (int) (Math.log(bytes) / Math.log(unit));
        double result = bytes / Math.pow(unit, exp);
        return Double.parseDouble(String.format("%.1f", result));
    }

    public static DetectorDetails defaultDetector;

    public void loadMainWindowSettings(){

        new Thread(this::loadDefaultDetector).start();

        ArrayList<DefaultTabEntity> defaultTabEntities = null;
        try {
            defaultTabEntities = registrationGeneralService.getDefaultTab();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(defaultTabEntities!=null){
            DefaultTabEntity defaultTab = defaultTabEntities.getFirst();

            if(defaultTab.getManual()){
                navConnect.navigateToManual();
            }else if(defaultTab.getWorkList()){
                navConnect.navigateToWorkList();
            } else if (defaultTab.getLocalList()) {
                navConnect.navigateToLocalList();
            }else{
                logger.info("Something Wrong in Default Tab....");
            }
        }


        StorageAlertsEntity storageAlerts = null;

        try {
            storageAlerts = storageAlertsService.findAll().getFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(storageAlerts!=null){
            double criticalValue = storageAlerts.getCriticalValue();
            double warningValue = storageAlerts.getWarningValue();

            File currentDir = new File(".");
            double usedSpace = formatSize(currentDir.getUsableSpace());

            if (criticalValue >= usedSpace) {
                showAlertWithDelay(Alert.AlertType.ERROR, "Critical Alert", "Storage is critically low. Please free up some space.");
            } else if (warningValue >= usedSpace) {
                showAlertWithDelay(Alert.AlertType.WARNING, "Warning", "Storage is getting low. Consider freeing up some space.");
            }
        }

        new Thread(() -> {
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkAutoDelete();
        }).start();


    }

    public static int defaultDetectorIndex;
    public static boolean isInitialized;
    public static boolean isConnected;

    private void loadDefaultDetector() {


        String projectPath = System.getProperty("user.dir");
        String filePath = projectPath + "/src/main/resources/config/Raymedis_Config.json";

        File jsonFile = new File(filePath);
        if (!jsonFile.exists()) {
            try {
                throw new FileNotFoundException("File not found at: " + filePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        applications = JsonService.readJson(jsonFile, RaymedisConfig.class);


        if (applications == null || applications.isEmpty()) {
            handleError("RaymedisConfig list is null or empty.");
            return;
        }

        RaymedisConfig config = applications.getFirst();
        if (config.getApplications() == null || config.getApplications().isEmpty()) {
            handleError("Applications list is null or empty.");
        }

        ApplicationDetails app = config.getApplications().getFirst();
        if (app.getType() == null) {
            handleError("ApplicationType is null.");
        }

        if(app.getType() == ApplicationType.RADIOGRAPHY){

            defaultDetectorIndex = app.getSelectedDetector();

            switch (defaultDetectorIndex){
                case 1:
                    defaultDetector = app.getFirstDetector();
                    break;
                case 2:
                    defaultDetector = app.getSecondDetector();
                    break;
                case 3:
                    defaultDetector = app.getThirdDetector();
                    break;
                default:
                    defaultDetector = null;
            }

            if(defaultDetector==null){
                logger.info("default detector is null");
            }

            String defaultDetectorIpAddress = defaultDetector.getIpAddress();
            if(defaultDetectorIpAddress!=null && !defaultDetectorIpAddress.isEmpty()){
                logger.info(defaultDetectorIpAddress);
            }


            List<String> wifiIps = WifiSettings.getWifiIpAddress();

            wifiIps.forEach(System.out::println);

            if (wifiIps.isEmpty()) {
                logger.info("No Wi-Fi interface found or no IPv4 address assigned.");
            }
            else {
                boolean found = false;
                for(String ip : wifiIps){
                    if(ip.equals(defaultDetectorIpAddress)){
                        logger.info("ip matched {}",ip);
                        found = true;
                        break;
                    }else {
                        logger.info("ip mismatched {} and {}",ip,defaultDetectorIpAddress);
                    }
                }

                if (!found) {
                    logger.info("IP not found");

                    String batFileName = "set_static_ip.bat";

                    WifiSettings.createBatchFileForSettingIp(batFileName, defaultDetectorIpAddress, "255.255.255.0", "");
                    WifiSettings.runAsAdmin(projectPath + "/" + batFileName);
                }

                fpdController.loadDetector(defaultDetector);

                isInitialized = fpdController.init();//innoCareService.init();

                if(isInitialized){
                    logger.info("initialized");

                    int fpdSize = fpdController.findAndGetDevices();
                    if(fpdSize>0){
                        fpdController.connect();
                        isConnected = fpdController.isConnected();
                        if(isConnected){
                            logger.info("connected successfully");


                            FpsStatus status =fpdController.getFpdStatus();

                           /* InxFpdOperStatusJava fpsStatus = InnoCareService.innoCareNative.getFpdStatus();
                            FpdStatusMode mode = FpdStatusMode.fromValue(fpsStatus.getMode() & 0xFF);*/

                            logger.info("fpd status {}",status);
                            navConnect.loadFpdStatus(defaultDetector);
                        }
                    }else {
                        logger.info("No devices found!!!");
                    }
                }else {
                    logger.info("failed to initialized");
                }
            }
        }
        else{
            logger.info("Raymedis Config is not done {}",app.getType());
        }
    }



    private void handleError(String errorMessage) {
        logger.info("Error: {}", errorMessage);
    }


    private void resetLoginPage(){
        userIdText.setText("");
        passwordText.setText("");
    }

    private void showAlertWithDelay(Alert.AlertType alertType, String title, String message) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> showAlert(alertType, title, message));
        }).start();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void checkAutoDelete(){
        AutoDeleteService autoDeleteService = AutoDeleteService.getInstance();
        List<AutoDeleteEntity> autoDeleteEntities = null;
        AutoDeleteEntity autoDelete = null;
        try {
            autoDeleteEntities = autoDeleteService.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching auto delete entities", e);
        }

        if (autoDeleteEntities != null && !autoDeleteEntities.isEmpty()) {
            autoDelete = autoDeleteEntities.get(0);
        }

        if (autoDelete != null && autoDelete.isAutoDelete()) {
            if (autoDelete.isAllStudies() || autoDelete.isSentOrPrintedStudies() || autoDelete.isRejectedStudies()) {
                if (autoDelete.isTimeEnabled() || autoDelete.isStorageEnabled()) {
                    if (autoDelete.isStorageEnabled()) {
                        //handleStorageDeletion(autoDelete);
                    }
                    if (autoDelete.isTimeEnabled()) {
                        handleTimeBasedDeletion(autoDelete);
                    }
                }
            }
        }
    }

    private void handleStorageDeletion(AutoDeleteEntity autoDelete) {
        int storageSize = autoDelete.getStorageSize();
        File currentDir = new File(".");
        double usedSpace = LoginController.formatSize(currentDir.getUsableSpace());

        if (storageSize >= usedSpace) {
            //checkAndDeleteStudies(autoDelete);
        }
    }

    private void handleTimeBasedDeletion(AutoDeleteEntity autoDelete) {
        LocalDate savedDate = LocalDate.from(autoDelete.getSavedDate());
        LocalDate now = LocalDate.now();
        long daysDifference = ChronoUnit.DAYS.between(savedDate, now);


        if ((autoDelete.getWeekDuration() * 7L) <= daysDifference) {
            //checkAndDeleteStudies(autoDelete);
        }

    }

    private void checkAndDeleteStudies(AutoDeleteEntity autoDelete) {
        Platform.runLater(()->{
            boolean userConfirmed = displayConfirmationAlert(
                    "Scheduled Auto-Delete",
                    "Auto-delete has been scheduled. Do you want to continue or cancel?"
            );

            if (!userConfirmed) {
                return;
            }

            if (autoDelete.isAllStudies()) deleteAllStudies();
            if (autoDelete.isSentOrPrintedStudies()) deletePrintedOrSentStudies();
            if (autoDelete.isRejectedStudies()) deleteRejectedStudies();
        });

    }

    private boolean displayConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }


    private final PatientStudyService patientStudyService = PatientStudyService.getInstance();

    private void deleteAllStudies(){
        ArrayList<PatientStudyEntity> totalPatients = null;
        try {
            totalPatients = (ArrayList<PatientStudyEntity>) PatientStudyService.getInstance().findAllPatients();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (PatientStudyEntity patientStudy : totalPatients){
            if(!patientStudy.getIsRejected()){
                try {
                    patientStudyService.deletePatient(patientStudy.getStudyId());
                    String baseDir = System.getProperty("user.dir");
                    String patientId = patientStudy.getPatientId();
                    String directoryPath = String.format("%s/output/%s", baseDir, patientId);

                    File directory = new File(directoryPath);
                    if (directory.exists()) {
                        String fileName = patientStudy.getStudyId() + ".rxv";
                        File outputFile = new File(directory, fileName);

                        outputFile.delete();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void deleteRejectedStudies(){
        ArrayList<PatientStudyEntity> totalPatients = null;
        try {
            totalPatients = (ArrayList<PatientStudyEntity>) PatientStudyService.getInstance().findAllPatients();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (PatientStudyEntity patientStudy : totalPatients){
            if(patientStudy.getIsRejected()){
                try {
                    patientStudyService.deletePatient(patientStudy.getStudyId());
                    String baseDir = System.getProperty("user.dir");
                    String patientId = patientStudy.getPatientId();
                    String directoryPath = String.format("%s/output/%s", baseDir, patientId);

                    File directory = new File(directoryPath);
                    if (directory.exists()) {
                        String fileName = patientStudy.getStudyId() + ".rxv";
                        File outputFile = new File(directory, fileName);

                        outputFile.delete();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void deletePrintedOrSentStudies(){
        ArrayList<PatientStudyEntity> totalPatients = null;
        try {
            totalPatients = (ArrayList<PatientStudyEntity>) PatientStudyService.getInstance().findAllPatients();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (PatientStudyEntity patientStudy : totalPatients){
            if(patientStudy.getIsPrinted() || patientStudy.getIsDicomUploaded()){
                try {
                    patientStudyService.deletePatient(patientStudy.getStudyId());
                    String baseDir = System.getProperty("user.dir");
                    String patientId = patientStudy.getPatientId();
                    String directoryPath = String.format("%s/output/%s", baseDir, patientId);

                    File directory = new File(directoryPath);
                    if (directory.exists()) {
                        String fileName = patientStudy.getStudyId() + ".rxv";
                        File outputFile = new File(directory, fileName);

                        outputFile.delete();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }






}
