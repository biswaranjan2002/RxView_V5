package com.raymedis.rxviewui.modules.print.Layout;

import com.raymedis.rxviewui.modules.print.imageProcessing.drawToolsResource.PrintMainDrawTool;
import com.raymedis.rxviewui.modules.print.printInput.PatientPrintImageData;
import com.raymedis.rxviewui.modules.print.imageProcessing.PrintImageTools;

import java.util.*;

public class LayoutTabHandler {

    public LinkedHashMap<String, LayoutTab> layoutTabMap = new LinkedHashMap<>();
    private LayoutTab selectedLayout;
    private PatientPrintImageData currentPatientPrintImageData=null;

    public void addLayout(String id, LayoutTab tab) {
        layoutTabMap.put(id,tab);
    }

    public void deleteLayout(String id){
        layoutTabMap.remove(id);
    }



    public void setSelectLayout(LayoutTab layoutTab){

        this.selectedLayout = layoutTab;

        layoutTabMap.values().forEach(tab -> {
            if (tab != layoutTab) {
                tab.getOverlayGridPane().setStyle("-fx-border-color: transparent; -fx-border-width: 0px");
            }
        });




        if(layoutTab!=null){
            layoutTab.getOverlayGridPane().setStyle("-fx-border-color: #ec641c; -fx-border-width: 4px");

            if(layoutTab.getHandleLayoutImageProcessing()!=null){
                PrintMainDrawTool printMainDrawTool = layoutTab.getHandleLayoutImageProcessing().getPrintMainDrawTool();
                PrintImageTools.getInstance().loadToolsForSelectedLayout(printMainDrawTool);
            }else if(layoutTab.getHandleLayoutImageProcessing()==null){
                PrintImageTools.getInstance().loadToolsForSelectedLayout(null);
            }
        }else{
            PrintImageTools.getInstance().loadToolsForSelectedLayout(null);
        }

    }

    public LayoutTab getSelectedLayout(){
        return selectedLayout;
    }

    public PatientPrintImageData getCurrentPatientPrintImageData() {
        return currentPatientPrintImageData;
    }

    public void setCurrentPatientPrintImageData(PatientPrintImageData currentPatientPrintImageData) {
        this.currentPatientPrintImageData = currentPatientPrintImageData;
    }

    public List<LayoutTab> getAllLayoutTabs() {
        return new ArrayList<>(layoutTabMap.values());
    }




    public void reloadLayout(LayoutTab layoutTab) {
        PatientPrintImageData patientPrintImageData = layoutTab.getPatientPrintImageData();
        for (LayoutTab l : layoutTabMap.values()) {
            if (!l.equals(layoutTab) && Objects.equals(l.getPatientPrintImageData(), patientPrintImageData)) {
                l.setPatientPrintImageData(currentPatientPrintImageData);
                currentPatientPrintImageData=null;
            }
        }
    }

    public void clear() {
        layoutTabMap.clear();
    }

    public LayoutTab getLayout(String id) {
        return layoutTabMap.get(id);
    }

    public void saveAllLayouts() {
        for (LayoutTab layoutTab : layoutTabMap.values()) {
            if(layoutTab.getPatientPrintImageData()!=null){
                layoutTab.getHandleLayoutImageProcessing().getPrintMainDrawTool().saveImageParams();
                layoutTab.getHandleLayoutImageProcessing().getPrintMainDrawTool().saveImageAnnotations();
            }
        }

    }
}
