package com.raymedis.rxviewui.modules.print.Layout;

import com.raymedis.rxviewui.modules.print.imageProcessing.PrintDynamicCanvasElementsResize;
import com.raymedis.rxviewui.modules.print.imageProcessing.drawToolsResource.PrintCanvasMainChange;
import com.raymedis.rxviewui.modules.print.imageProcessing.drawToolsResource.PrintMainDrawTool;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class HandleLayoutImageProcessing {

    private PrintCanvasMainChange printCanvasMainChange;
    private PrintMainDrawTool printMainDrawTool;
    private PrintDynamicCanvasElementsResize printDynamicCanvasElementsResize;


    public HandleLayoutImageProcessing(LayoutTab layoutTab) {
        printCanvasMainChange = new PrintCanvasMainChange();
        printMainDrawTool = new PrintMainDrawTool(layoutTab,printCanvasMainChange);
        printDynamicCanvasElementsResize = new PrintDynamicCanvasElementsResize();

        printCanvasMainChange.printMainDrawTool = this.printMainDrawTool;

        printCanvasMainChange.printDynamicCanvasElementsResize=printDynamicCanvasElementsResize;
        printCanvasMainChange.setLayoutTab(layoutTab);
    }


    public PrintCanvasMainChange getPrintCanvasMainChange() {
        return printCanvasMainChange;
    }

    public void setPrintCanvasMainChange(PrintCanvasMainChange printCanvasMainChange) {
        this.printCanvasMainChange = printCanvasMainChange;
    }

    public PrintMainDrawTool getPrintMainDrawTool() {
        return printMainDrawTool;
    }

    public void setPrintMainDrawTool(PrintMainDrawTool printMainDrawTool) {
        this.printMainDrawTool = printMainDrawTool;
    }

    public PrintDynamicCanvasElementsResize getPrintDynamicCanvasElementsResize() {
        return printDynamicCanvasElementsResize;
    }

    public void setPrintDynamicCanvasElementsResize(PrintDynamicCanvasElementsResize printDynamicCanvasElementsResize) {
        this.printDynamicCanvasElementsResize = printDynamicCanvasElementsResize;
    }
}
