package com.raymedis.rxviewui.modules.print.printPage;

import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table.PrintOverlayEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table.PrintOverlayService;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table.SystemInfoEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table.SystemInfoService;
import com.raymedis.rxviewui.modules.print.Layout.LayoutTabHandler;
import com.raymedis.rxviewui.modules.print.printInput.PatientPrintData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class PrintPageHandler {

    private final static PrintPageHandler instance = new PrintPageHandler();
    private static final Logger log = LoggerFactory.getLogger(PrintPageHandler.class);

    public static PrintPageHandler getInstance(){
        return instance;
    }


    private final LinkedHashMap<String, PrintPage> printPageHashMap = new LinkedHashMap<>();
    private ArrayList<PrintOverlayEntity> printOverlayEntityList = null;
    private SystemInfoEntity systemInfo =null;
    private PrintOverlayService printOverlayService = PrintOverlayService.getInstance();

    public PrintPageHandler(){
        try {
            printOverlayEntityList = printOverlayService.findAllSelected();
            if(!SystemInfoService.getInstance().getSystemInfo().isEmpty()){
                systemInfo = SystemInfoService.getInstance().getSystemInfo().getFirst();
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private PrintPage currentPrintPage;

    private PatientPrintData patientPrintData;

    public PrintPage createPage(){
        PrintPage printPage = new PrintPage();

        printPage.setId(UUID.randomUUID().toString());
        printPage.setLayoutCode("STANDARD\\1,1");
        LayoutTabHandler layoutTabHandler = new LayoutTabHandler();

        printPage.setLayoutTabHandler(layoutTabHandler);
        printPageHashMap.put(printPage.getId(), printPage);

        return printPage;
    }

    public void deletePage(String id){
        printPageHashMap.remove(id);
    }

    public void setCurrentPage(String id){
        this.currentPrintPage = printPageHashMap.get(id);
    }

    public PrintPage getCurrentPrintPage() {
        return currentPrintPage;
    }

    public void setCurrentPrintPage(PrintPage currentPrintPage) {
        this.currentPrintPage = currentPrintPage;
    }

    public List<PrintPage> getAllTabs(){
        return printPageHashMap.values().stream().toList();
    }

    public PatientPrintData getPatientPrintData() {
        return patientPrintData;
    }

    public void setPatientPrintData(PatientPrintData patientPrintData) {
        this.patientPrintData = patientPrintData;
    }

    public LinkedHashMap<String, PrintPage> getPrintPageHashMap() {
        return printPageHashMap;
    }

    public ArrayList<PrintOverlayEntity> getPrintOverlayEntityList() {
        return printOverlayEntityList;
    }

    public void setPrintOverlayEntityList(ArrayList<PrintOverlayEntity> printOverlayEntityList) {
        this.printOverlayEntityList = printOverlayEntityList;
    }

    public SystemInfoEntity getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(SystemInfoEntity systemInfo) {
        this.systemInfo = systemInfo;
    }

}
