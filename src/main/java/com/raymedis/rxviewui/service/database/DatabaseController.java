package com.raymedis.rxviewui.service.database;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.controller.NavConnect;
import com.raymedis.rxviewui.database.tables.adminSettings.register.register_manual_table.CandidateTagsEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.register.register_manual_table.RegistrationManualService;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyEntity;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyService;
import com.raymedis.rxviewui.database.tables.study_table.StudyStatsEntity;
import com.raymedis.rxviewui.database.tables.study_table.StudyStatsService;
import com.raymedis.rxviewui.modules.dicom.DicomService;
import com.raymedis.rxviewui.modules.study.bodypart.BodyPartNode;
import com.raymedis.rxviewui.modules.study.bodypart.PatientBodyPart;
import com.raymedis.rxviewui.modules.study.patient.PatientInfo;
import com.raymedis.rxviewui.modules.study.study.IdGenerator;
import com.raymedis.rxviewui.modules.study.study.PatientStudy;
import com.raymedis.rxviewui.modules.study.study.PatientStudyNode;
import com.raymedis.rxviewui.modules.study.study.StudyThumbNails;
import com.raymedis.rxviewui.modules.study.tabmanagement.TabNode;
import com.raymedis.rxviewui.service.export.ExportWindowController;
import com.raymedis.rxviewui.service.print.PrintController;
import com.raymedis.rxviewui.service.print.PrintInputCreator;
import com.raymedis.rxviewui.service.print.PrintService;
import com.raymedis.rxviewui.service.registration.ManualRegisterController;
import com.raymedis.rxviewui.service.serialization.KryoSerializer;
import com.raymedis.rxviewui.service.study.StudyMainController;
import com.raymedis.rxviewui.service.study.StudyService;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.*;
import javafx.util.Duration;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class DatabaseController {

    private static final DatabaseController instance = new DatabaseController();
    private final String baseDir = System.getProperty("user.dir");
    public static JFXButton editButton;
    private String directoryPath="";
    private String fileName="";


    public static DatabaseController getInstance(){
        return instance;
    }

    public TableView dataGrid;
    public TableColumn registerDateColumn;
    public TableColumn registerTimeColumn;
    public TableColumn patientIdColumn;
    public TableColumn patientNameColumn;
    public TableColumn sexColumn;
    public TableColumn weightColumn;
    public TableColumn dobColumn;
    public TableColumn accessionNumberColumn;
    public TableColumn physicianNameColumn;
    public TableColumn studyDescriptionColumn;
    public TableColumn exposureDateTimeColumn;
    public TableColumn modalityColumn;
    public TableColumn studyDateTimeColumn;
    public TableColumn rejectedColumn;

    public VBox databaseMediaContainer;

    public TextField detailsText;

    public JFXComboBox searchByComboBox;

    public DatePicker fromDate;
    public DatePicker toDate;

    public JFXButton todayButton;
    public JFXButton weekButton;
    public JFXButton monthButton;
    public JFXButton searchButton;
    public JFXButton clearButton;

    private final NavConnect navConnect = NavConnect.getInstance();
    private final PatientStudyService patientStudyService = PatientStudyService.getInstance();
    private List<PatientStudyEntity> totalPatients = new ArrayList<>();

    private static PatientStudyEntity selectedPatient = null;
    private final AtomicReference<CompletableFuture<Void>> currentLoadTask = new AtomicReference<>();
    private String selectedStudyId = "";

    private final Logger logger = LoggerFactory.getLogger(DatabaseController.class);

    public void loadData() throws SQLException {
        searchByComboBox.getSelectionModel().select(0);
        detailsText.setDisable(true);

        dataGrid.getItems().clear();
        databaseMediaContainer.getChildren().clear();
        totalPatients = patientStudyService.findAllDatabaseStudies();
        dataGrid.getItems().addAll(totalPatients);

        ArrayList<CandidateTagsEntity> selectedTextFields = RegistrationManualService.getInstance().getAllSelectedCandidateTags();
        setAllColumnsInVisible();

        for (CandidateTagsEntity candidateTagsEntity : selectedTextFields) {
            String field = candidateTagsEntity.getTagName().toLowerCase();

            //logger.info("Setting visibility for field: {}", field);

            if (field.equals("registerdate") || field.equals("registertime") ||
                    field.equals("exposuredatetime") || field.equals("studydatetime") ||
                    field.equals("rejected")) {
                continue;
            }

            switch (field) {
                case "patient id":
                    patientIdColumn.setVisible(true);
                    break;
                case "patient name":
                    patientNameColumn.setVisible(true);
                    break;
                case "weight":
                    weightColumn.setVisible(true);
                    break;
                case "dob":
                    dobColumn.setVisible(true);
                    break;
                case "accession number":
                    accessionNumberColumn.setVisible(true);
                    break;
                case "physician name":
                    physicianNameColumn.setVisible(true);
                    break;
                case "study description":
                    studyDescriptionColumn.setVisible(true);
                    break;
                case "modality":
                    modalityColumn.setVisible(true);
                    break;
                case "studyid":
                    studyDateTimeColumn.setVisible(true);
                    break;
                default:
                    break;
            }
        }
    }

    private void setAllColumnsInVisible() {
        // Set all columns to visible initially
        patientIdColumn.setVisible(false);
        patientNameColumn.setVisible(false);
        weightColumn.setVisible(false);
        dobColumn.setVisible(false);
        accessionNumberColumn.setVisible(false);
        physicianNameColumn.setVisible(false);
        studyDescriptionColumn.setVisible(false);
    }


    public void loadEvents(){
        registerDateColumn.setCellValueFactory(new PropertyValueFactory<>("registerDateTime"));
        registerDateColumn.setCellFactory(col -> {
            TableCell<PatientStudyEntity, LocalDateTime> cell = new TableCell<PatientStudyEntity, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Format the LocalDateTime to "dd/MM/yyyy"
                        String formattedDate = item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        setText(formattedDate);
                    }
                }
            };
            return cell;
        });

        registerTimeColumn.setCellValueFactory(new PropertyValueFactory<>("registerDateTime"));
        registerTimeColumn.setCellFactory(col -> {
            TableCell<PatientStudyEntity, LocalDateTime> cell = new TableCell<PatientStudyEntity, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Extract the time part from LocalDateTime
                        LocalTime time = item.toLocalTime();

                        // Format the LocalTime to "HH:mm:ss"
                        String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                        setText(formattedTime);
                    }
                }
            };
            return cell;
        });

        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        sexColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));

        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        dobColumn.setCellFactory(col -> {
            TableCell<PatientStudyEntity, LocalDate> cell = new TableCell<PatientStudyEntity, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Format the LocalDate to "dd/MM/yyyy"
                        String formattedDate = item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        setText(formattedDate);
                    }
                }
            };
            return cell;
        });

        accessionNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accessionNumber"));
        physicianNameColumn.setCellValueFactory(new PropertyValueFactory<>("performingPhysician"));
        studyDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("studyDescription"));


        exposureDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("exposureDateTime"));
        exposureDateTimeColumn.setCellFactory(col -> {
            TableCell<PatientStudyEntity, LocalDateTime> cell = new TableCell<PatientStudyEntity, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Format the LocalDateTime to "dd/MM/yyyy - HH:mm:ss"
                        String formattedDateTime = item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"));
                        setText(formattedDateTime);
                    }
                }
            };
            return cell;
        });


        studyDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("studyDateTime"));
        studyDateTimeColumn.setCellFactory(col -> {
            TableCell<PatientStudyEntity, LocalDateTime> cell = new TableCell<PatientStudyEntity, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Format the LocalDateTime to "dd/MM/yyyy - HH:mm:ss"
                        String formattedDateTime = item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"));
                        setText(formattedDateTime);
                    }
                }
            };
            return cell;
        });


        modalityColumn.setCellValueFactory(new PropertyValueFactory<>("modality"));
        rejectedColumn.setCellValueFactory(new PropertyValueFactory<>("isRejected"));

        rejectedColumn.setCellFactory(tc -> new TableCell<PatientStudyEntity, Boolean>() {
            @Override
            protected void updateItem(Boolean isRejected, boolean empty) {
                super.updateItem(isRejected, empty);
                if (empty || isRejected == null) {
                    setText(null);
                } else {
                    setText(isRejected ? "True" : "False");
                }
            }
        });



        PauseTransition clickPause = new PauseTransition(Duration.millis(250));
        final boolean[] isSingleClick = {true};

        clickPause.setOnFinished(event -> {
            if (isSingleClick[0]) {
                PatientStudyEntity patient = (PatientStudyEntity) dataGrid.getSelectionModel().getSelectedItem();
                handleRowSelection(patient);
            }
            isSingleClick[0] = true;
        });

        dataGrid.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                clickPause.playFromStart();
            }
        });

        dataGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    isSingleClick[0] = false;
                    clickPause.stop();
                    PatientStudyEntity patient = (PatientStudyEntity) dataGrid.getSelectionModel().getSelectedItem();
                    handleDoubleClick(patient);
                }
            }
        });

    }

    private void handleDoubleClick(PatientStudyEntity patient) {
        if (patient == null || patient.equals(selectedPatient)) {
            return;
        }

        cancelCurrentLoad();
        selectedPatient = patient;
        openSelectedStudy(selectedPatient);
        selectedPatient = null;
    }

    private void cancelCurrentLoad() {
        CompletableFuture<Void> currentTask = currentLoadTask.getAndSet(null);
        if (currentTask != null) {
            currentTask.cancel(true);
        }
    }

    private void handleRowSelection(PatientStudyEntity patient) {
        if (patient == null || patient.equals(selectedPatient)) {
            return;
        }

        cancelCurrentLoad();
        loadPatientData(patient);
    }

    private void onRowSelect(PatientStudyEntity selectedPatient) {
        if (Thread.currentThread().isInterrupted()) {
            return;
        }

        selectedStudyId = selectedPatient.getStudyId();
    }

    private final LinkedHashMap<String,VBox> bodyPartMap = new LinkedHashMap<>();

    private PatientStudy patientStudy;

    private void loadPatientData(PatientStudyEntity selectedPatient) {
        String baseDir = System.getProperty("user.dir");
        String patientId = selectedPatient.getPatientId();
        String directoryPath = String.format("%s/output/%s", baseDir, patientId);
        String thumbNailFilePath = selectedPatient.getStudyId() + "_thumbnails.rxv";
        String fileName = selectedPatient.getStudyId() + ".rxv";
        File thumbNailFile = new File(directoryPath, thumbNailFilePath);
        File file = new File(directoryPath, fileName);

        if (file.exists()  && thumbNailFile.exists()) {
            try {

                patientStudy = KryoSerializer.getInstance().deserializeFromFile(file);
                List<StudyThumbNails> studyThumbNailsList = KryoSerializer.getInstance().deserializeMatListFromFile(thumbNailFile);

                databaseMediaContainer.getChildren().clear();
                bodyPartMap.clear();

                for (StudyThumbNails studyThumbNail : studyThumbNailsList) {
                    bodyPartMap.put(
                            studyThumbNail.getId(),
                            createBodyPartTabButton(studyThumbNail.getBodyPartName(), studyThumbNail.getThumbnail())
                    );
                }

                if (!bodyPartMap.isEmpty()) {
                    StudyMainController.getInstance().bodyPartTabPane.setSpacing(10);
                    for (Map.Entry<String,VBox> entry : bodyPartMap.entrySet()){
                        databaseMediaContainer.getChildren().add(entry.getValue());
                    }
                }


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            databaseMediaContainer.getChildren().clear();
            bodyPartMap.clear();
            System.err.println("File not found: " + file.getAbsolutePath());
        }
    }


    private VBox createBodyPartTabButton(String bodyPartName,Mat mat) {
        //create the Vbox
        VBox vbox = new VBox();
        vbox.setId("unSelectedBodyPartsButton");
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefSize(160, 160);
        vbox.setSpacing(5);

        // Create the ImageView
        ImageView imageView = new ImageView();
        imageView.setFitWidth(125);
        imageView.setFitHeight(125);
        imageView.setPreserveRatio(true);
        imageView.setPickOnBounds(true);

        imageView.setImage(StudyService.matToImage(mat));
        Label label = new Label(bodyPartName);
        vbox.getChildren().addAll(imageView,label);

        return vbox;
    }



    public void todayBtnClick() {
        totalPatients = patientStudyService.findLastDay();
        dataGrid.getItems().setAll(totalPatients);
    }

    public void weekBtnClick() {
        totalPatients = patientStudyService.findLastWeek();
        dataGrid.getItems().setAll(totalPatients);
    }

    public void monthBtnClick() {
        totalPatients = patientStudyService.findLastMonth();
        dataGrid.getItems().setAll(totalPatients);
    }


    private void filterByDates(LocalDateTime from, LocalDateTime to) {
        int index = searchByComboBox.getSelectionModel().getSelectedIndex();
        String detail = detailsText.getText();

        if (from == null && to != null || from != null && to == null || from != null && from.isAfter(to)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Dates");
            alert.showAndWait();
            return;
        }

        if (from == null) {
            from = LocalDateTime.MIN;
            to = LocalDateTime.now();
        }

        if (detail != null && !detail.isEmpty()) {
            switch (index) {
                case 1:
                    totalPatients = patientStudyService.findAllPatientsFilterByPatientName(from, to, detail);
                    break;
                case 2:
                    totalPatients = patientStudyService.findAllPatientsFilterByPatientId(from, to, detail);
                    break;
                case 3:
                    totalPatients = patientStudyService.findAllPatientsFilterByAccessionNumber(from, to, detail);
                    break;
                default:
                    totalPatients = patientStudyService.findCustomDateRange(from, to);
                    break;
            }
        } else {
            totalPatients = patientStudyService.findCustomDateRange(from, to);
        }

        dataGrid.getItems().setAll(totalPatients);
    }

    private void resetSearchFields() {
        searchByComboBox.getSelectionModel().select(0);
        toDate.setValue(null);
        fromDate.setValue(null);
        detailsText.clear();
    }

    public void clearClick() {
        clearButton.getParent().requestFocus();
        dataGrid.getItems().clear();
        dataGrid.refresh();
        resetSearchFields();
        searchClick();
    }

    public void searchClick() {
        try {
            LocalDateTime from = fromDate.getValue() != null ? fromDate.getValue().atStartOfDay() : null;
            LocalDateTime to = toDate.getValue() != null ? toDate.getValue().atStartOfDay().plusDays(1).minusNanos(1) : null;
            filterByDates(from, to);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Dates");
            alert.showAndWait();
        }
        searchButton.getParent().requestFocus();
    }

    public void selectionChanged() {
        int selectedIndex = searchByComboBox.getSelectionModel().getSelectedIndex();
        detailsText.setDisable(selectedIndex == 0);
    }

    public void statsPageClick() {
        navConnect.navigateToStatsPage();
    }

    public void comparePageClick() {
        navConnect.navigateToComparePage();
    }

    public void rejectStudy() {
        PatientStudyEntity patientStudy = (PatientStudyEntity) dataGrid.getSelectionModel().getSelectedItem();

        if(patientStudy==null || patientStudy.getIsRejected()){
            return;
        }

        patientStudy.setIsRejected(true);
        try {
            patientStudyService.updatePatient(patientStudy.getStudyId(),patientStudy);

            StudyStatsEntity stats = StudyStatsService.getInstance().findAllThisDayStudies(LocalDate.from(patientStudy.getExposureDateTime()));
            stats.setRejectedStudyCount(stats.getRejectedStudyCount()+1);
            StudyStatsService.getInstance().update(stats.getId(),stats);
            dataGrid.getItems().clear();
            databaseMediaContainer.getChildren().clear();

            totalPatients = patientStudyService.findAllPatients();
            dataGrid.getItems().addAll(totalPatients);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void printClick() {
        PatientStudyEntity selectedPatient = getSelectedPatient();
        if (selectedPatient == null) return;

        PatientStudy patientStudy = loadPatientStudy(selectedPatient);
        if (patientStudy == null) return;

        PrintController.getInstance().loadData();
        PrintService.getInstance().loadStudyData(PrintInputCreator.getInstance().createPrintInput(patientStudy));
        NavConnect.getInstance().navigateToPrint();

    }

    private void openSelectedStudy(PatientStudyEntity selectedPatient) {
        PatientStudy patientStudy = loadPatientStudy(selectedPatient);
        if (patientStudy == null) return;

        String sessionId = StudyService.patientStudyHandler.containsStudyWithStudyId(patientStudy.getStudyId());
        if (sessionId == null) {
            sessionId = IdGenerator.generateSessionId(3, patientStudy.getPatientInfo().getPatientName());
            PatientStudyNode patientStudyNode = new PatientStudyNode(patientStudy, sessionId);
            StudyService.patientStudyHandler.addTab(sessionId, patientStudyNode);
        }
        StudyService.patientStudyHandler.selectPatientStudy(sessionId);
        navConnect.navigateToStudy();
    }

    // Common helper methods
    private PatientStudyEntity getSelectedPatient() {
        return (PatientStudyEntity) dataGrid.getSelectionModel().getSelectedItem();
    }

    private File getPatientStudyFile(PatientStudyEntity patient) {
        directoryPath = String.format("%s/output/%s", baseDir, patient.getPatientId());
        fileName = patient.getStudyId() + ".rxv";
        return new File(directoryPath, fileName);
    }

    private PatientStudy loadPatientStudy(PatientStudyEntity patientEntity) {
        File file = getPatientStudyFile(patientEntity);
        if (!file.exists()) {
            System.err.println("File not found: " + file.getAbsolutePath());
            return null;
        }

        try {
            PatientStudy patientStudy = KryoSerializer.getInstance().deserializeFromFile(file);
            return patientStudy.getStudyId() != null ? patientStudy : null;
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize patient study", e);
        }
    }

    public void exportClick() {
        try {
            Screen secondaryScreen = Screen.getScreens().getFirst();
            Rectangle2D screenBounds = secondaryScreen.getVisualBounds();

            double scaleX;
            double scaleY;

            double parentHeight = screenBounds.getHeight();
            double parentWidth = screenBounds.getWidth();

            double ratio = parentWidth / parentHeight;
            double targetRatio = (double) 800 / 700;

            if (targetRatio < ratio) {
                scaleX = parentHeight / 700;
                scaleY = parentHeight / 700;
            } else if (targetRatio > ratio) {
                scaleX = parentWidth / 800;
                scaleY = parentWidth / 800;
            } else {
                scaleX = parentWidth / 800;
                scaleY = parentWidth / 800;
            }

            double newWidth = 800 * scaleX * 0.7;
            double newHeight = 700 * scaleY * 0.7;

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/ExportWindow.fxml"));
            Parent root1 = fxmlLoader.load();

            ExportWindowController exportWindowController = ExportWindowController.getInstance();

            PatientStudyEntity selectedPatient = getSelectedPatient();
            if (selectedPatient == null) return;

            PatientStudy patientStudy = loadPatientStudy(selectedPatient);
            exportWindowController.loadData(patientStudy,"database");

            // Apply a clip to make the stage rounded
            double radius = 50; // Adjust this value to control curvature
            Rectangle clip = new Rectangle(newWidth, newHeight);
            clip.setArcWidth(radius);
            clip.setArcHeight(radius);
            root1.setClip(clip);

            // Set the root node background to transparent
            root1.setStyle("-fx-background-color: transparent;");

            // Create the scene and set its fill to transparent
            Scene scene = new Scene(root1, newWidth, newHeight);
            scene.setFill(Color.TRANSPARENT);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT); // Set the stage style to transparent
            stage.setTitle("Export Window");
            stage.setScene(scene);
            stage.show();

            // Center the stage on the secondary screen
            stage.setX(screenBounds.getMinX() + (screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY(screenBounds.getMinY() + (screenBounds.getHeight() - stage.getHeight()) / 2);

        }
        catch (IOException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }

    private Stage getCurrentStage() {
        return (Stage) todayButton.getScene().getWindow();
    }


    private final static DicomService dicomService = DicomService.getInstance();

    public void pacsClick() {

        selectedPatient = (PatientStudyEntity) dataGrid.getSelectionModel().getSelectedItem();

        if(selectedPatient==null){
            return;
        }

        LocalDateTime exposedDate = selectedPatient.getExposureDateTime();
        if(exposedDate==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "0 Exposed Studies Found", ButtonType.OK);
            alert.setTitle("PACS Server");
            alert.initOwner(getCurrentStage());
            alert.showAndWait();
            return;
        }


        try {
            dicomService.loadData(patientStudy);
            //dicomService.handleAnnotatedImage(StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getCurrentBodyPart());
            dicomService.uploadToStorageServer();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    public static boolean isEdit = false;
    public void editClick() {
        isEdit = true;
        NavConnect.getInstance().navigateToRegisterMain();
        NavConnect.getInstance().navigateToManual();
        ManualRegisterController.getInstance().loadRegisterForEdit(patientStudy);
    }



    public void importClick() {
        // Get the stage from any UI control (e.g., a button)
        Stage stage = (Stage) todayButton.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import .rx File");

        // Allow only .rx files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("RX files (*.rxv)", "*.rxv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show the open file dialog
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                // Deserialize the selected file
                PatientStudy patientStudy = KryoSerializer.getInstance().deserializeFromFile(selectedFile);
                if (patientStudy != null) {
                    // Check if the study already exists in the database
                    String studyId = patientStudy.getStudyId();
                    if (patientStudyService.findByStudyId(studyId) == null) {
                        loadImportedStudyToDataBase(patientStudy);


                        copyStudyFilesToOutputDirectory(patientStudy,selectedFile);
                        File thumbNailFile = checkForThumbNails(selectedFile);
                        if(thumbNailFile!=null){
                            copyStudyFilesToOutputDirectory(patientStudy,thumbNailFile);
                        }
                        else{
                            logger.info("No thumbnails found for this study.");
                        }


                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Study with this ID already exists.", ButtonType.OK);
                        alert.setTitle("Import Error");
                        alert.initOwner(stage);
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to deserialize the file.", ButtonType.OK);
                    alert.setTitle("Import Error");
                    alert.initOwner(stage);
                    alert.showAndWait();
                }
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error reading the file: " + e.getMessage(), ButtonType.OK);
                alert.setTitle("Import Error");
                alert.initOwner(stage);
                alert.showAndWait();
            }

        } else {
            System.out.println("File selection cancelled.");
        }
    }

    private File checkForThumbNails(File selectedFile) {

        File thumbNailFile = new File(selectedFile.getParent(), selectedFile.getName().replace(".rxv", "_thumbnails.rxv"));
        logger.info("Checking for thumbnails in directory: {}", thumbNailFile.getAbsoluteFile());

        if (thumbNailFile.exists()) {
           return thumbNailFile;
        } else {
            System.out.println("No thumbnails found for this study.");
        }

        return null;

    }

    private void copyStudyFilesToOutputDirectory(PatientStudy patientStudy,File sourceFile) {

        Path sourcePath = Path.of(sourceFile.getPath());

        String baseDir = System.getProperty("user.dir");
        String patientId = patientStudy.getPatientInfo().getPatientId();
        String directoryPath = String.format("%s/output/%s", baseDir, patientId);

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Target directory path
        Path targetPath = Paths.get(directoryPath, sourceFile.getName());

        try {
            // Copy the file, replacing the existing one if it exists
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully.");
        } catch (IOException e) {
            System.out.println("Error copying the file: " + e.getMessage());
        }


    }


    private void loadImportedStudyToDataBase(PatientStudy patientStudy){

        PatientStudyEntity patientStudyEntity = new PatientStudyEntity();

        PatientInfo patientInfo = patientStudy.getPatientInfo();

        patientStudyEntity.setPatientId(patientInfo.getPatientId());
        patientStudyEntity.setPatientName(patientInfo.getPatientName());
        patientStudyEntity.setSex(patientInfo.getSex());
        patientStudyEntity.setAge(patientInfo.getAge());
        patientStudyEntity.setDob(LocalDate.from(patientInfo.getBirthDate()));
        patientStudyEntity.setHeight(patientInfo.getHeight());
        patientStudyEntity.setWeight(patientInfo.getWeight());
        patientStudyEntity.setPatientSize(patientInfo.getPatientSize());
        patientStudyEntity.setPatientInstituteResidence(patientInfo.getPatientInstituteResidence());

        patientStudyEntity.setAccessionNumber(patientStudy.getAccessionNum());
        patientStudyEntity.setModality(patientStudy.getModality());
        patientStudyEntity.setRequestProcedurePriority(patientStudy.getRequestProcedurePriority());
        patientStudyEntity.setAdditionalPatientHistory(patientStudy.getAdditionalPatientHistory());
        patientStudyEntity.setAdmittingDiagnosisDescription(patientStudy.getAdmittingDiagnosisDescription());
        patientStudyEntity.setStudyDescription(patientStudy.getStudyDescription());
        patientStudyEntity.setStudyProcedure(patientStudy.getStudyDescription());
        patientStudyEntity.setStudyDateTime(patientStudy.getStudyDateTime());
        patientStudyEntity.setRegisterDateTime(patientStudy.getRegisterDateTime());
        patientStudyEntity.setScheduledDateTime(patientStudy.getScheduledDateTime());
        patientStudyEntity.setExposureDateTime(patientStudy.getExposedDateTime());
        patientStudyEntity.setPatientComments(patientStudy.getPatientComments());
        patientStudyEntity.setStudyId(patientStudy.getStudyId());
        patientStudyEntity.setReadingPhysician(patientStudy.getReadingPhysician());
        patientStudyEntity.setReferringPhysician(patientStudy.getReferringPhysician());
        patientStudyEntity.setPerformingPhysician(patientStudy.getPerformingPhysician());
        patientStudyEntity.setInstitution(patientStudy.getInstitution());
        patientStudyEntity.setStudyUid(patientStudy.getStudyUid());


        for(TabNode tabNode : patientStudy.getBodyPartHandler().getAllTabs()) {

            BodyPartNode bodyPartNode = (BodyPartNode) tabNode;
            PatientBodyPart bodyPart = bodyPartNode.getBodyPart();

            if(bodyPart.isExposed()){
                patientStudyEntity.setSeriesUid(bodyPart.getSeriesUid());
                patientStudyEntity.setInstanceUid(bodyPart.getInstanceUid());
                patientStudyEntity.setSeriesId(bodyPart.getSeriesId());
                patientStudyEntity.setInstanceId(bodyPart.getInstanceId());
            }
        }



        patientStudyEntity.setIsRejected(patientStudyEntity.getIsRejected());
        patientStudyEntity.setIsPrinted(patientStudyEntity.getIsPrinted());
        patientStudyEntity.setIsDicomUploaded(patientStudyEntity.getIsDicomUploaded());
        patientStudyEntity.setIsBackedUp(patientStudyEntity.getIsBackedUp());
        patientStudyEntity.setIsCleaned(patientStudyEntity.getIsCleaned());

        try {
            patientStudyService.savePatient(patientStudyEntity);
            loadData(); // Reload data to reflect the new study
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error saving the study: " + e.getMessage(), ButtonType.OK);
            alert.setTitle("Import Error");
            alert.initOwner(getCurrentStage());
            alert.showAndWait();
            throw new RuntimeException(e);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Import successful!", ButtonType.OK);
        alert.setTitle("Import");
        alert.initOwner(getCurrentStage());
        alert.showAndWait();
    }


    public void moveClick() {

    }

    public void stitchClick() {

    }

}
