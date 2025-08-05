package com.raymedis.rxviewui.service.adminSettings.procedureManager;

import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_bodypart_table.ProcedureBodyPartEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_bodypart_table.ProcedureBodyPartService;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_projection_table.ProcedureProjectionEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_projection_table.ProcedureProjectionService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProcedureManagerProjectionController {

    private final static ProcedureManagerProjectionController instance = new ProcedureManagerProjectionController();

    public static ProcedureManagerProjectionController getInstance(){
        return instance;
    }

    public TableView bodyPartDataGrid;
    public TableColumn<ProcedureBodyPartEntity, String> bodyPartMeaningColumn;
    public TableColumn<ProcedureBodyPartEntity, String> bodyPartAliasColumn;
    public TableColumn<ProcedureBodyPartEntity, Boolean> bodyPartHideColumn;
    public TableColumn<ProcedureBodyPartEntity, String> bodyPartCodeValueColumn;
    public TableColumn<ProcedureBodyPartEntity, String> bodyPartDesignatorColumn;
    public TableColumn<ProcedureBodyPartEntity, String> bodyPartVersionColumn;
    public TableColumn<ProcedureBodyPartEntity, String> bodyPartExaminationColumn;


    public TableView projectionDataGrid;
    public TableColumn<ProcedureProjectionEntity,String> projectionMeaningColumn;
    public TableColumn<ProcedureProjectionEntity,String> projectionAliasColumn;
    public TableColumn<ProcedureProjectionEntity,Boolean> projectionHideColumn;
    public TableColumn<ProcedureProjectionEntity,String> projectionCodeValueColumn;
    public TableColumn<ProcedureProjectionEntity,String> projectionDesignatorColumn;
    public TableColumn<ProcedureProjectionEntity,String> projectionVersionColumn;
    public TableColumn<ProcedureProjectionEntity,String> projectionViewPositionColumn;
    public ScrollPane projectionScrollPane;

    private ProcedureBodyPartService procedureBodyPartService = ProcedureBodyPartService.getInstance();
    private ProcedureProjectionService procedureProjectionService = ProcedureProjectionService.getInstance();

    private ArrayList<ProcedureBodyPartEntity> bodyPartList = new ArrayList<>();
    private ArrayList<ProcedureProjectionEntity> projectionList = new ArrayList<>();


    public void loadData() throws SQLException {
        bodyPartList = procedureBodyPartService.findAll();
        projectionList = procedureProjectionService.findAll();

        bodyPartDataGrid.getItems().clear();
        bodyPartDataGrid.getItems().addAll(bodyPartList);

        projectionDataGrid.getItems().clear();
        projectionDataGrid.getItems().addAll(projectionList);
    }

    public void loadEvents() {
        bodyPartMeaningColumn.setCellValueFactory(new PropertyValueFactory<>("meaning"));
        bodyPartAliasColumn.setCellValueFactory(new PropertyValueFactory<>("alias"));
        bodyPartHideColumn.setCellValueFactory(new PropertyValueFactory<>("hide"));
        bodyPartCodeValueColumn.setCellValueFactory(new PropertyValueFactory<>("codeValue"));
        bodyPartDesignatorColumn.setCellValueFactory(new PropertyValueFactory<>("designator"));
        bodyPartVersionColumn.setCellValueFactory(new PropertyValueFactory<>("version"));
        bodyPartExaminationColumn.setCellValueFactory(new PropertyValueFactory<>("bodyPartExamination"));

        // Meaning Column
        bodyPartMeaningColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        bodyPartMeaningColumn.setOnEditCommit(event -> {
            ProcedureBodyPartEntity entity = event.getRowValue();
            entity.setMeaning(event.getNewValue());

            try {
                procedureBodyPartService.update(entity.getId(), entity);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Alias Column
        bodyPartAliasColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        bodyPartAliasColumn.setOnEditCommit(event -> {
            ProcedureBodyPartEntity entity = event.getRowValue();
            entity.setAlias(event.getNewValue());

            try {
                procedureBodyPartService.update(entity.getId(), entity);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Code Value Column
        bodyPartCodeValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        bodyPartCodeValueColumn.setOnEditCommit(event -> {
            ProcedureBodyPartEntity entity = event.getRowValue();
            entity.setCodeValue(event.getNewValue());

            try {
                procedureBodyPartService.update(entity.getId(), entity);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Designator Column
        bodyPartDesignatorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        bodyPartDesignatorColumn.setOnEditCommit(event -> {
            ProcedureBodyPartEntity entity = event.getRowValue();
            entity.setDesignator(event.getNewValue());

            try {
                procedureBodyPartService.update(entity.getId(), entity);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Version Column
        bodyPartVersionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        bodyPartVersionColumn.setOnEditCommit(event -> {
            ProcedureBodyPartEntity entity = event.getRowValue();
            entity.setVersion(event.getNewValue());

            try {
                procedureBodyPartService.update(entity.getId(), entity);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Examination Column
        bodyPartExaminationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        bodyPartExaminationColumn.setOnEditCommit(event -> {
            ProcedureBodyPartEntity entity = event.getRowValue();
            entity.setBodyPartExamination(event.getNewValue());

            try {
                procedureBodyPartService.update(entity.getId(), entity);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        //Hide Column
        bodyPartHideColumn.setCellValueFactory(cellData -> {
            SimpleBooleanProperty property = new SimpleBooleanProperty(cellData.getValue().getHide());
            property.addListener((observable, oldValue, newValue) -> {
                ProcedureBodyPartEntity entity = cellData.getValue();
                entity.setHide(newValue);

                try {
                    procedureBodyPartService.update(entity.getId(), entity);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            return property;
        });
        bodyPartHideColumn.setCellFactory(CheckBoxTableCell.forTableColumn(bodyPartHideColumn));



        projectionMeaningColumn.setCellValueFactory(new PropertyValueFactory<>("meaning"));
        projectionAliasColumn.setCellValueFactory(new PropertyValueFactory<>("alias"));
        projectionHideColumn.setCellValueFactory(new PropertyValueFactory<>("hide"));
        projectionCodeValueColumn.setCellValueFactory(new PropertyValueFactory<>("codeValue"));
        projectionDesignatorColumn.setCellValueFactory(new PropertyValueFactory<>("designator"));
        projectionVersionColumn.setCellValueFactory(new PropertyValueFactory<>("version"));
        projectionViewPositionColumn.setCellValueFactory(new PropertyValueFactory<>("viewPosition"));

        // Meaning Column
        projectionMeaningColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        projectionMeaningColumn.setOnEditCommit(event -> {
            ProcedureProjectionEntity entity = event.getRowValue();
            entity.setMeaning(event.getNewValue());

            try {
                procedureProjectionService.update(entity.getId(), entity);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Alias Column
        projectionAliasColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        projectionAliasColumn.setOnEditCommit(event -> {
            ProcedureProjectionEntity entity = event.getRowValue();
            entity.setAlias(event.getNewValue());

            try {
                procedureProjectionService.update(entity.getId(), entity);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Code Value Column
        projectionCodeValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        projectionCodeValueColumn.setOnEditCommit(event -> {
            ProcedureProjectionEntity entity = event.getRowValue();
            entity.setCodeValue(event.getNewValue());

            try {
                procedureProjectionService.update(entity.getId(), entity);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Designator Column
        projectionDesignatorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        projectionDesignatorColumn.setOnEditCommit(event -> {
            ProcedureProjectionEntity entity = event.getRowValue();
            entity.setDesignator(event.getNewValue());

            try {
                procedureProjectionService.update(entity.getId(), entity);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Version Column
        projectionVersionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        projectionVersionColumn.setOnEditCommit(event -> {
            ProcedureProjectionEntity entity = event.getRowValue();
            entity.setVersion(event.getNewValue());

            try {
                procedureProjectionService.update(entity.getId(), entity);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // View Position Column
        projectionViewPositionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        projectionViewPositionColumn.setOnEditCommit(event -> {
            ProcedureProjectionEntity entity = event.getRowValue();
            entity.setViewPosition(event.getNewValue());

            try {
                procedureProjectionService.update(entity.getId(), entity);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        //Hide Column
        projectionHideColumn.setCellValueFactory(cellData -> {
            SimpleBooleanProperty property = new SimpleBooleanProperty(cellData.getValue().getHide());
            property.addListener((observable, oldValue, newValue) -> {
                ProcedureProjectionEntity entity = cellData.getValue();
                entity.setHide(newValue);

                try {
                    procedureProjectionService.update(entity.getId(), entity);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            return property;
        });
        projectionHideColumn.setCellFactory(CheckBoxTableCell.forTableColumn(projectionHideColumn));

    }


    public void bodyPartAddClick() {
        if (!bodyPartList.isEmpty() && bodyPartList.get(bodyPartList.size() - 1).getMeaning() == null) {
            return;
        }

        System.out.println(bodyPartList.size()+1);
        ProcedureBodyPartEntity procedureBodyPartEntity= new ProcedureBodyPartEntity(bodyPartList.size()+1,"","",false,"","","","");

        try {
            procedureBodyPartService.save(procedureBodyPartEntity);
            bodyPartList=procedureBodyPartService.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        bodyPartDataGrid.getItems().clear();
        bodyPartDataGrid.getItems().addAll(bodyPartList);
        bodyPartDataGrid.scrollTo(bodyPartList.size() - 1);
    }


    public void projectionAddClick() {
        if (!projectionList.isEmpty() && projectionList.get(projectionList.size() - 1).getMeaning() == null) {
            return;
        }
        ProcedureProjectionEntity procedureProjectionEntity = new ProcedureProjectionEntity(projectionList.size()+1,"","",false,"","","","");

        try {
            procedureProjectionService.save(procedureProjectionEntity);
            projectionList=procedureProjectionService.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        projectionDataGrid.getItems().clear();
        projectionDataGrid.getItems().addAll(projectionList);
        projectionDataGrid.scrollTo(projectionList.size() - 1);
    }


    public void projectionDeleteClick() {
        ProcedureProjectionEntity procedureProjectionEntity = (ProcedureProjectionEntity) projectionDataGrid.getSelectionModel().getSelectedItem();
        if(procedureProjectionEntity!=null){
            try {
                procedureProjectionService.delete(procedureProjectionEntity.getId());
                projectionDataGrid.getItems().clear();
                projectionList=procedureProjectionService.findAll();
                projectionDataGrid.getItems().addAll(projectionList);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void bodyPartDeleteClick() {
        ProcedureBodyPartEntity procedureBodyPartEntity = (ProcedureBodyPartEntity) bodyPartDataGrid.getSelectionModel().getSelectedItem();
        if(procedureBodyPartEntity!=null){
            try {
                procedureBodyPartService.delete(procedureBodyPartEntity.getId());
                bodyPartDataGrid.getItems().clear();
                bodyPartList=procedureBodyPartService.findAll();
                bodyPartDataGrid.getItems().addAll(bodyPartList);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
