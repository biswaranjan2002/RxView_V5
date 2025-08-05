module com.raymedis.rxviewui {
    requires transitive javafx.graphics;
    requires transitive javafx.fxml;
    requires com.jfoenix;
    requires org.kordamp.ikonli.javafx;
    requires transitive javafx.controls;
    requires Medusa;
    requires java.sql;
    requires com.esotericsoftware.kryo;
    requires opencv;
    requires jbcrypt;
    requires org.objenesis;
    requires transitive javafx.swing;
    requires org.slf4j;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires logback.core;
    requires rxSdk;

    requires pixelmed;
    requires logback.classic;
    requires DicomPrintModuleJAR;

    requires ImageProcessingLib;
    requires SerialPortComLib;
    requires org.jetbrains.annotations;


    opens com.raymedis.rxviewui.service.adminSettings.dicom to javafx.base;
    exports com.raymedis.rxviewui.service.adminSettings.dicom;

    opens com.raymedis.rxviewui to javafx.fxml;
    exports com.raymedis.rxviewui;

    opens com.raymedis.rxviewui.controller.patientSize to javafx.fxml;
    exports com.raymedis.rxviewui.controller.patientSize;

    opens com.raymedis.rxviewui.controller.adminSettings to javafx.fxml;
    exports com.raymedis.rxviewui.controller.adminSettings;

    opens com.raymedis.rxviewui.controller to javafx.fxml;
    exports com.raymedis.rxviewui.controller;

    opens com.raymedis.rxviewui.database to javafx.base;
    exports com.raymedis.rxviewui.database;

    opens com.raymedis.rxviewui.modules.dicom to javafx.base;
    exports com.raymedis.rxviewui.modules.dicom;

    opens com.raymedis.rxviewui.database.tables.adminSettings.register.register_manual_table to javafx.base;
    opens com.raymedis.rxviewui.controller.adminSettings.registration.manual to javafx.fxml;

    exports com.raymedis.rxviewui.controller.adminSettings.registration.general;
    exports com.raymedis.rxviewui.controller.adminSettings.registration.manual;

    opens com.raymedis.rxviewui.controller.adminSettings.registration.general to javafx.fxml;
    exports com.raymedis.rxviewui.controller.adminSettings.registration.physician;

    opens com.raymedis.rxviewui.controller.adminSettings.registration.physician to javafx.fxml;
    exports com.raymedis.rxviewui.controller.adminSettings.system;

    exports com.raymedis.rxviewui.controller.registrationModule;
    opens com.raymedis.rxviewui.controller.registrationModule to javafx.fxml;

    exports com.raymedis.rxviewui.controller.studyModule;
    opens com.raymedis.rxviewui.controller.studyModule to javafx.fxml;

    exports com.raymedis.rxviewui.controller.databaseModule.statistics;
    opens com.raymedis.rxviewui.controller.databaseModule.statistics to javafx.fxml;

    exports com.raymedis.rxviewui.controller.printModule;
    opens com.raymedis.rxviewui.controller.printModule to javafx.fxml;

    exports com.raymedis.rxviewui.controller.databaseModule;
    opens com.raymedis.rxviewui.controller.databaseModule to javafx.fxml;

    exports com.raymedis.rxviewui.service.registration;
    opens com.raymedis.rxviewui.service.registration to javafx.fxml;

    opens com.raymedis.rxviewui.controller.studyModule.patientSize to javafx.fxml;
    exports com.raymedis.rxviewui.modules.study.study;

    exports com.raymedis.rxviewui.modules.study.patient;
    exports com.raymedis.rxviewui.modules.study.bodypart;
    exports com.raymedis.rxviewui.modules.study.params;
    exports com.raymedis.rxviewui.modules.study.tabmanagement;



    opens com.raymedis.rxviewui.modules.study.study to com.esotericsoftware.kryo;
    opens com.raymedis.rxviewui.modules.study.patient to com.esotericsoftware.kryo;
    opens com.raymedis.rxviewui.modules.study.bodypart to com.esotericsoftware.kryo;
    opens com.raymedis.rxviewui.modules.study.params to com.esotericsoftware.kryo;
    opens com.raymedis.rxviewui.modules.study.tabmanagement to com.esotericsoftware.kryo;
    opens com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource to com.esotericsoftware.kryo;
    opens com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper to com.esotericsoftware.kryo;

    exports com.raymedis.rxviewui.database.tables.patientStudy_table;
    opens com.raymedis.rxviewui.database.tables.patientStudy_table to javafx.base;

    exports com.raymedis.rxviewui.controller.adminSettings.backup;
    opens com.raymedis.rxviewui.controller.adminSettings.backup to javafx.fxml;

    exports com.raymedis.rxviewui.controller.adminSettings.dicom;
    opens com.raymedis.rxviewui.controller.adminSettings.dicom to javafx.fxml;

    exports com.raymedis.rxviewui.controller.adminSettings.procedureManager;
    opens com.raymedis.rxviewui.controller.adminSettings.procedureManager to javafx.fxml;

    exports com.raymedis.rxviewui.controller.adminSettings.study;
    opens com.raymedis.rxviewui.controller.adminSettings.study to javafx.fxml;

    exports com.raymedis.rxviewui.database.tables.adminSettings.system.user_accounts_table;
    opens com.raymedis.rxviewui.database.tables.adminSettings.system.user_accounts_table to javafx.base;

    exports com.raymedis.rxviewui.service.adminSettings;
    opens com.raymedis.rxviewui.service.adminSettings to javafx.fxml;

    exports com.raymedis.rxviewui.service.adminSettings.registration;
    opens com.raymedis.rxviewui.service.adminSettings.registration to javafx.fxml;

    exports com.raymedis.rxviewui.database.tables.adminSettings.register.register_physician_table;
    opens com.raymedis.rxviewui.database.tables.adminSettings.register.register_physician_table to javafx.base;

    exports com.raymedis.rxviewui.database.tables.adminSettings.study.study_rejectedReason_table;
    opens com.raymedis.rxviewui.database.tables.adminSettings.study.study_rejectedReason_table to javafx.base;

    exports com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_bodypart_table;
    opens com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_bodypart_table to javafx.base;

    exports com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_projection_table;
    opens com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_projection_table to javafx.base;

    exports com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table;
    opens com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table to javafx.base;

    exports com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_procedure_table;
    opens com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_procedure_table to javafx.base;

    exports com.raymedis.rxviewui.service.adminSettings.procedureManager;
    opens com.raymedis.rxviewui.service.adminSettings.procedureManager to javafx.base;

    exports com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlayItems_table;
    opens com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlayItems_table to javafx.base;

    exports com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table;
    opens com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table to javafx.base;

    exports com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_mwl_table;
    opens com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_mwl_table to javafx.base;

    exports com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_mpps_table;
    opens com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_mpps_table to javafx.base;

    exports com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storage_table;
    opens com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storage_table to javafx.base;


    exports com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storageCommitment_table;
    opens com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storageCommitment_table to javafx.base;

    exports com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_print_table;
    opens com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_print_table to javafx.base;

    exports com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_TagMapping_table;
    opens com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_TagMapping_table to javafx.base;

    exports com.raymedis.rxviewui.database.tables.register.patientLocalList_table;
    opens com.raymedis.rxviewui.database.tables.register.patientLocalList_table to javafx.base;


    exports com.raymedis.rxviewui.service.json;
    exports com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper;

}
