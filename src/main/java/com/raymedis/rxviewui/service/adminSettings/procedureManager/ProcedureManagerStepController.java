package com.raymedis.rxviewui.service.adminSettings.procedureManager;

import com.jfoenix.controls.JFXButton;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_bodypart_table.ProcedureBodyPartEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_bodypart_table.ProcedureBodyPartService;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_category_table.CategoryBodyPartsEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_category_table.CategoryBodyPartsService;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_projection_table.ProcedureProjectionEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_projection_table.ProcedureProjectionService;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table.StepEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table.StepService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class ProcedureManagerStepController {

    private final static ProcedureManagerStepController instance = new ProcedureManagerStepController();

    public static ProcedureManagerStepController getInstance(){
        return instance;
    }

    public TextField inputTextFiled;
    public JFXButton searchButton;

    public TableView bodyPartsTable;
    public TableColumn bodyPartsNameColumn;

    public TableView projectionTable;
    public TableColumn projectionNameColumn;

    public JFXButton insertStepButton;
    public JFXButton deleteStepButton;

    public TableView stepTable;
    public TableColumn<StepEntity, Integer> stepIdColumn;
    public TableColumn<StepEntity, String> stepNameColumn;
    public TableColumn<StepEntity, String> patientSizeColumn;
    public TableColumn<StepEntity, Boolean> autoCropColumn;
    public TableColumn<StepEntity, Double> imageProcessParamKeyColumn;
    public TableColumn<StepEntity, Integer> detectorNoColumn;
    public TableColumn<StepEntity, String> lrLabelColumn;
    public TableColumn<StepEntity, Double> lrPosColumn;
    public TableColumn<StepEntity, String> textColumn;
    public TableColumn<StepEntity, String> textPosColumn;
    public TableColumn<StepEntity, Boolean> flipColumn;
    public TableColumn<StepEntity, Boolean> mirrorColumn;
    public TableColumn<StepEntity, Boolean> rotationColumn;
    public TableColumn<StepEntity, Boolean> imageRotationColumn;
    public TableColumn<StepEntity, Double> consoleKvColumn;
    public TableColumn<StepEntity, Double> consoleMasColumn;
    public TableColumn<StepEntity, Double> consoleMaColumn;
    public TableColumn<StepEntity, Double> consoleMsColumn;
    public TableColumn<StepEntity, Boolean> consoleAecColumn;
    public TableColumn<StepEntity, Boolean> consoleAecLeftColumn;
    public TableColumn<StepEntity, Boolean> consoleAecRightColumn;
    public TableColumn<StepEntity, Boolean> consoleAecCenterColumn;
    public TableColumn<StepEntity, Integer> consoleDenColumn;
    public TableColumn<StepEntity, String> generatorP1Column;
    public TableColumn<StepEntity, String> generatorP2Column;
    public TableColumn<StepEntity, Integer> imageProcTypeColumn;
    public TableColumn<StepEntity, Double> sidColumn;
    public TableColumn<StepEntity, Integer> imageParamTypeColumn;
    public TableColumn<StepEntity, Double> armAngleColumn;
    public TableColumn<StepEntity, Double> detectorAngleColumn;
    public TableColumn<StepEntity, Double> tubeTiltAngleColumn;
    public TableColumn<StepEntity, Double> tubeRotateAngleColumn;
    public TableColumn<StepEntity, Double> ceilingPositionColumn;
    public TableColumn<StepEntity, Double> detectorArmPositionColumn;
    public TableColumn<StepEntity, Double> tubeArmPositionColumn;



    private ArrayList<StepEntity> stepList = new ArrayList<>();
    private StepService stepService = StepService.getInstance();

    private ProcedureBodyPartService procedureBodyPartService = ProcedureBodyPartService.getInstance();
    private ProcedureProjectionService procedureProjectionService = ProcedureProjectionService.getInstance();

    private ArrayList<ProcedureBodyPartEntity> bodyPartList = new ArrayList<>();
    private ArrayList<ProcedureProjectionEntity> projectionList = new ArrayList<>();

    public void loadEvents() {
        bodyPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("meaning"));
        projectionNameColumn.setCellValueFactory(new PropertyValueFactory<>("meaning"));

        // Setting up cell value factories for columns
        stepIdColumn.setCellValueFactory(new PropertyValueFactory<>("stepId"));
        stepNameColumn.setCellValueFactory(new PropertyValueFactory<>("stepName"));
        patientSizeColumn.setCellValueFactory(new PropertyValueFactory<>("patientSize"));
        autoCropColumn.setCellValueFactory(new PropertyValueFactory<>("autoCrop"));
        imageProcessParamKeyColumn.setCellValueFactory(new PropertyValueFactory<>("imageProcessParamKey"));
        detectorNoColumn.setCellValueFactory(new PropertyValueFactory<>("detectorNo"));
        lrLabelColumn.setCellValueFactory(new PropertyValueFactory<>("lrLabel"));
        lrPosColumn.setCellValueFactory(new PropertyValueFactory<>("lrPos"));
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
        textPosColumn.setCellValueFactory(new PropertyValueFactory<>("textPos"));
        flipColumn.setCellValueFactory(new PropertyValueFactory<>("flip"));
        mirrorColumn.setCellValueFactory(new PropertyValueFactory<>("mirror"));
        rotationColumn.setCellValueFactory(new PropertyValueFactory<>("rotation"));
        imageRotationColumn.setCellValueFactory(new PropertyValueFactory<>("imageRotation"));
        consoleKvColumn.setCellValueFactory(new PropertyValueFactory<>("consoleKv"));
        consoleMasColumn.setCellValueFactory(new PropertyValueFactory<>("consoleMas"));
        consoleMaColumn.setCellValueFactory(new PropertyValueFactory<>("consoleMa"));
        consoleMsColumn.setCellValueFactory(new PropertyValueFactory<>("consoleMs"));
        consoleAecColumn.setCellValueFactory(new PropertyValueFactory<>("consoleAec"));
        consoleAecLeftColumn.setCellValueFactory(new PropertyValueFactory<>("consoleAecLeft"));
        consoleAecRightColumn.setCellValueFactory(new PropertyValueFactory<>("consoleAecRight"));
        consoleAecCenterColumn.setCellValueFactory(new PropertyValueFactory<>("consoleAecCenter"));
        consoleDenColumn.setCellValueFactory(new PropertyValueFactory<>("consoleDen"));
        generatorP1Column.setCellValueFactory(new PropertyValueFactory<>("generatorP1"));
        generatorP2Column.setCellValueFactory(new PropertyValueFactory<>("generatorP2"));
        imageProcTypeColumn.setCellValueFactory(new PropertyValueFactory<>("imageProcType"));
        sidColumn.setCellValueFactory(new PropertyValueFactory<>("sid"));
        imageParamTypeColumn.setCellValueFactory(new PropertyValueFactory<>("imageParamType"));
        armAngleColumn.setCellValueFactory(new PropertyValueFactory<>("armAngle"));
        detectorAngleColumn.setCellValueFactory(new PropertyValueFactory<>("detectorAngle"));
        tubeTiltAngleColumn.setCellValueFactory(new PropertyValueFactory<>("tubeTiltAngle"));
        tubeRotateAngleColumn.setCellValueFactory(new PropertyValueFactory<>("tubeRotateAngle"));
        ceilingPositionColumn.setCellValueFactory(new PropertyValueFactory<>("ceilingPosition"));
        detectorArmPositionColumn.setCellValueFactory(new PropertyValueFactory<>("detectorArmPosition"));
        tubeArmPositionColumn.setCellValueFactory(new PropertyValueFactory<>("tubeArmPosition"));



        autoCropColumn.setCellValueFactory(cellData -> {
            SimpleBooleanProperty property = new SimpleBooleanProperty(cellData.getValue().getAutoCrop());
            property.addListener((observable, oldValue, newValue) -> {
                StepEntity entity = cellData.getValue();
                entity.setAutoCrop(newValue);

                try {

                    stepService.update(entity.getStepId(), entity);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            return property;
        });
        autoCropColumn.setCellFactory(CheckBoxTableCell.forTableColumn(autoCropColumn));



        imageProcessParamKeyColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        imageProcessParamKeyColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setImageProcessParamKey(event.getNewValue());
            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        detectorNoColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        detectorNoColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setDetectorNo(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        /*lrLabelColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lrLabelColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setLrLabel(String.valueOf(event.getNewValue()));

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });*/

        lrLabelColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                FXCollections.observableArrayList("Null","L", "R")
        ));

        // Handle selection (edit commit)
        lrLabelColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setLrLabel(event.getNewValue()); // no need for String.valueOf since it's already String

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });







        // Define position options with mappings
        ObservableList<Double> positionValues = FXCollections.observableArrayList(
                0.0,1.0, 2.0, 3.0, 4.0
        );

        // Map doubles to human-readable labels
        Map<Double, String> positionLabels = Map.of(
                0.0, "No Position",
                1.0, "Top Corner",
                2.0, "Right Corner",
                3.0, "Bottom Corner",
                4.0, "Left Corner"
        );

        // Create a StringConverter for mapping
        StringConverter<Double> converter = new StringConverter<>() {
            @Override
            public String toString(Double value) {
                return positionLabels.getOrDefault(value, value != null ? value.toString() : "");
            }

            @Override
            public Double fromString(String label) {
                return positionLabels.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(label))
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElse(null);
            }
        };

        // Setup cell factory and edit commit
        lrPosColumn.setCellValueFactory(new PropertyValueFactory<>("lrPos"));

        lrPosColumn.setCellFactory(ComboBoxTableCell.forTableColumn(converter, positionValues));

        lrPosColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setLrPos(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });



        /*lrPosColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        lrPosColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setLrPos(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });*/



        textColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        textColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setText(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        textPosColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        textPosColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setTextPos(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        flipColumn.setCellValueFactory(cellData -> {
            SimpleBooleanProperty property = new SimpleBooleanProperty(cellData.getValue().getFlip());
            property.addListener((observable, oldValue, newValue) -> {
                StepEntity entity = cellData.getValue();
                entity.setFlip(newValue);

                try {
                    stepService.update(entity.getStepId(), entity);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            return property;
        });
        flipColumn.setCellFactory(CheckBoxTableCell.forTableColumn(flipColumn));


        mirrorColumn.setCellValueFactory(cellData -> {
            SimpleBooleanProperty property = new SimpleBooleanProperty(cellData.getValue().getMirror());
            property.addListener((observable, oldValue, newValue) -> {
                StepEntity entity = cellData.getValue();
                entity.setMirror(newValue);

                try {
                    stepService.update(entity.getStepId(), entity);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            return property;
        });
        mirrorColumn.setCellFactory(CheckBoxTableCell.forTableColumn(mirrorColumn));


        rotationColumn.setCellValueFactory(cellData -> {
            SimpleBooleanProperty property = new SimpleBooleanProperty(cellData.getValue().getRotation());
            property.addListener((observable, oldValue, newValue) -> {
                StepEntity entity = cellData.getValue();
                entity.setRotation(newValue);

                try {
                    stepService.update(entity.getStepId(), entity);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            return property;
        });
        rotationColumn.setCellFactory(CheckBoxTableCell.forTableColumn(rotationColumn));

        imageRotationColumn.setCellValueFactory(cellData -> {
            SimpleBooleanProperty property = new SimpleBooleanProperty(cellData.getValue().getImageRotation());
            property.addListener((observable, oldValue, newValue) -> {
                StepEntity entity = cellData.getValue();
                entity.setImageRotation(newValue);

                try {
                    stepService.update(entity.getStepId(), entity);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            return property;
        });
        imageRotationColumn.setCellFactory(CheckBoxTableCell.forTableColumn(imageRotationColumn));

        consoleKvColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        consoleKvColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setConsoleKv(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        consoleMasColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        consoleMasColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setConsoleMas(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        consoleMaColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        consoleMaColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setConsoleMa(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        consoleMsColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        consoleMsColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setConsoleMs(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        consoleAecColumn.setCellValueFactory(cellData -> {
            SimpleBooleanProperty property = new SimpleBooleanProperty(cellData.getValue().getConsoleAec());
            property.addListener((observable, oldValue, newValue) -> {
                StepEntity entity = cellData.getValue();
                entity.setConsoleAec(newValue);

                try {
                    stepService.update(entity.getStepId(), entity);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            return property;
        });
        consoleAecColumn.setCellFactory(CheckBoxTableCell.forTableColumn(consoleAecColumn));


        consoleAecLeftColumn.setCellValueFactory(cellData -> {
            SimpleBooleanProperty property = new SimpleBooleanProperty(cellData.getValue().getConsoleAecLeft());
            property.addListener((observable, oldValue, newValue) -> {
                StepEntity entity = cellData.getValue();
                entity.setConsoleAecLeft(newValue);

                try {
                    stepService.update(entity.getStepId(), entity);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            return property;
        });
        consoleAecLeftColumn.setCellFactory(CheckBoxTableCell.forTableColumn(consoleAecLeftColumn));

        consoleAecRightColumn.setCellValueFactory(cellData -> {
            SimpleBooleanProperty property = new SimpleBooleanProperty(cellData.getValue().getConsoleAecRight());
            property.addListener((observable, oldValue, newValue) -> {
                StepEntity entity = cellData.getValue();
                entity.setConsoleAecRight(newValue);

                try {
                    stepService.update(entity.getStepId(), entity);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            return property;
        });
        consoleAecRightColumn.setCellFactory(CheckBoxTableCell.forTableColumn(consoleAecRightColumn));


        consoleAecCenterColumn.setCellValueFactory(cellData -> {
            SimpleBooleanProperty property = new SimpleBooleanProperty(cellData.getValue().getConsoleAecCenter());
            property.addListener((observable, oldValue, newValue) -> {
                StepEntity entity = cellData.getValue();
                entity.setConsoleAecCenter(newValue);

                try {
                    stepService.update(entity.getStepId(), entity);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            return property;
        });
        consoleAecCenterColumn.setCellFactory(CheckBoxTableCell.forTableColumn(consoleAecCenterColumn));


        consoleDenColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        consoleDenColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setConsoleDen(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        generatorP1Column.setCellFactory(TextFieldTableCell.forTableColumn());
        generatorP1Column.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setGeneratorP1(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        generatorP2Column.setCellFactory(TextFieldTableCell.forTableColumn());
        generatorP2Column.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setGeneratorP2(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        imageProcTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        imageProcTypeColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setImageProcType(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        sidColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        sidColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setSid(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        imageParamTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        imageParamTypeColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setImageParamType(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        armAngleColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        armAngleColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setArmAngle(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        detectorAngleColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        detectorAngleColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setDetectorAngle(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        tubeTiltAngleColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tubeTiltAngleColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setTubeTiltAngle(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        tubeRotateAngleColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tubeRotateAngleColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setTubeRotateAngle(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        ceilingPositionColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        ceilingPositionColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setCeilingPosition(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        detectorArmPositionColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        detectorArmPositionColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setDetectorArmPosition(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        tubeArmPositionColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tubeArmPositionColumn.setOnEditCommit(event -> {
            StepEntity entity = event.getRowValue();
            entity.setTubeArmPosition(event.getNewValue());

            try {
                stepService.update(entity.getStepId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void loadData() throws SQLException {
        stepList=stepService.findAll();
        bodyPartList = procedureBodyPartService.findAll();
        projectionList = procedureProjectionService.findAll();

        stepTable.getItems().clear();
        stepTable.getItems().addAll(stepList);

        bodyPartsTable.getItems().clear();
        bodyPartsTable.getItems().addAll(bodyPartList);

        projectionTable.getItems().clear();
        projectionTable.getItems().addAll(projectionList);
    }

    public void addStepClick() {
        String[] patientSizes = {"Infant", "Small", "Medium", "Big"};

        ProcedureBodyPartEntity selectedBodyPart = (ProcedureBodyPartEntity) bodyPartsTable.getSelectionModel().getSelectedItem();
        ProcedureProjectionEntity selectedProjection = (ProcedureProjectionEntity) projectionTable.getSelectionModel().getSelectedItem();

        if(selectedBodyPart==null || selectedProjection==null){
            return;
        }

        String stepName = selectedBodyPart.getMeaning() + " " + selectedProjection.getMeaning();

        for (StepEntity step : stepList){
            if(step.getStepName().equals(stepName)){
                return;
            }
        }


        for (String size : patientSizes) {
            StepEntity stepEntity = new StepEntity();
            stepEntity.setStepName(stepName);
            stepEntity.setPatientSize(size);

            stepEntity.setAutoCrop(true);
            stepEntity.setFlip(true);
            stepEntity.setMirror(true);
            stepEntity.setRotation(true);
            stepEntity.setImageRotation(true);
            stepEntity.setConsoleAec(false);
            stepEntity.setConsoleAecLeft(false);
            stepEntity.setConsoleAecRight(false);
            stepEntity.setConsoleAecCenter(false);

            stepEntity.setDetectorNo(1);
            stepEntity.setConsoleDen(0);
            stepEntity.setImageProcType(0);
            stepEntity.setImageParamType(0);


            stepEntity.setImageProcessParamKey(1.0);
            stepEntity.setLrLabel("L");
            stepEntity.setLrPos(175.0);
            stepEntity.setConsoleKv(60.0);
            stepEntity.setConsoleMas(75.0);
            stepEntity.setConsoleMa(85.0);
            stepEntity.setConsoleMs(0.0);
            stepEntity.setSid(0.0);
            stepEntity.setArmAngle(0.0);
            stepEntity.setDetectorAngle(10.0);
            stepEntity.setTubeTiltAngle(50.0);
            stepEntity.setTubeRotateAngle(10.0);
            stepEntity.setCeilingPosition(0.0);
            stepEntity.setDetectorArmPosition(0.0);
            stepEntity.setTubeArmPosition(0.0);


            stepEntity.setText("Some Text");
            stepEntity.setTextPos("145");
            stepEntity.setGeneratorP1("0");
            stepEntity.setGeneratorP2("0");
            stepEntity.setProcedureId(0);


            try {
                stepService.save(stepEntity);
                stepList = stepService.findAll();

                stepTable.getItems().clear();
                stepTable.getItems().addAll(stepList);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if(CategoryBodyPartsService.getInstance().getCategoryBodyPartByName(selectedBodyPart.getMeaning())==null){
            CategoryBodyPartsEntity categoryBodyPartsEntity = new CategoryBodyPartsEntity();
            categoryBodyPartsEntity.setBodyPartName(selectedBodyPart.getMeaning());
            categoryBodyPartsEntity.setMaleIsSelected(false);
            categoryBodyPartsEntity.setMaleXPos(0);
            categoryBodyPartsEntity.setMaleYPos(0);
            categoryBodyPartsEntity.setFemaleIsSelected(false);
            categoryBodyPartsEntity.setFemaleXPos(0);
            categoryBodyPartsEntity.setFemaleYPos(0);
            categoryBodyPartsEntity.setInfantIsSelected(false);
            categoryBodyPartsEntity.setInfantXPos(0);
            categoryBodyPartsEntity.setInfantYPos(0);
            categoryBodyPartsEntity.setIsStepExist(true);

            try {
                System.out.println(categoryBodyPartsEntity.getBodyPartName() + " : saving...");
                CategoryBodyPartsService.getInstance().saveBodyPart(categoryBodyPartsEntity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }



        stepTable.scrollTo(stepTable.getItems().size() - 1);

    }

    public void deleteStepClick() {
        StepEntity selectedStep = (StepEntity) stepTable.getSelectionModel().getSelectedItem();
        if(selectedStep==null){
            return;
        }

        String stepName = selectedStep.getStepName();
        try {
            stepService.deleteByProjection(stepName);
            stepList = stepService.findAll();

            stepTable.getItems().clear();
            stepTable.getItems().addAll(stepList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String bodyPart = selectedStep.getStepName().split(" ")[0];
        ArrayList<StepEntity> existStepsList = stepService.findByStepName(bodyPart);
        if(existStepsList.isEmpty()){
            CategoryBodyPartsEntity categoryBodyPartsEntity = CategoryBodyPartsService.getInstance().getCategoryBodyPartByName(bodyPart);
            if(categoryBodyPartsEntity!=null){
                CategoryBodyPartsService.getInstance().deleteBodyPart(categoryBodyPartsEntity.getBodyPartName());
            }
        }
    }



    public void searchClick() {
        String searchText = inputTextFiled.getText();

        stepList = stepService.findByStepName(searchText);
        stepTable.getItems().clear();
        stepTable.getItems().addAll(stepList);
    }

    public void clearClick() {
        try {
            stepList = stepService.findAll();
            inputTextFiled.setText("");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stepTable.getItems().clear();
        stepTable.getItems().addAll(stepList);
    }
}
