package com.raymedis.rxviewui.modules.print.printPage;

import com.raymedis.rxviewui.modules.print.Layout.LayoutTabHandler;

public class PrintPage {

    private String id;
    private String layoutCode;
    private LayoutTabHandler layoutTabHandler;

    public PrintPage() {
    }

    public PrintPage(String id, String layoutCode, LayoutTabHandler layoutTabHandler) {
        this.id = id;
        this.layoutCode = layoutCode;
        this.layoutTabHandler = layoutTabHandler;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLayoutCode() {
        return layoutCode;
    }

    public void setLayoutCode(String layoutCode) {
        this.layoutCode = layoutCode;
    }

    public LayoutTabHandler getLayoutTabHandler() {
        return layoutTabHandler;
    }

    public void setLayoutTabHandler(LayoutTabHandler layoutTabHandler) {
        this.layoutTabHandler = layoutTabHandler;
    }
}
