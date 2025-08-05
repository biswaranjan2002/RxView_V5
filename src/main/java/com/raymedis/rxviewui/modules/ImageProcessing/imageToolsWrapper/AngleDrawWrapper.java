package com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper;

import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;

public class AngleDrawWrapper implements Cloneable{

    private List<EllipseWrapper> ellipses = FXCollections.observableArrayList();
    private List<LineWrapper> lines = FXCollections.observableArrayList();
    private LabelWrapper labelWrapper;

    public AngleDrawWrapper() {
    }

    public AngleDrawWrapper(List<EllipseWrapper> ellipses, List<LineWrapper> lines, LabelWrapper labelWrapper) {
        this.ellipses = ellipses;
        this.lines = lines;
        this.labelWrapper = labelWrapper;
    }

    public List<EllipseWrapper> getEllipses() {
        return ellipses;
    }

    public void setEllipses(List<EllipseWrapper> ellipses) {
        this.ellipses = ellipses;
    }

    public List<LineWrapper> getLines() {
        return lines;
    }

    public void setLines(List<LineWrapper> lines) {
        this.lines = lines;
    }

    public LabelWrapper getDegreeLabel() {
        return labelWrapper;
    }

    public void setDegreeLabel(LabelWrapper labelWrapper) {
        this.labelWrapper = labelWrapper;
    }

    // In AngleDrawWrapper.clone()
    @Override
    public AngleDrawWrapper clone() {
        try {
            AngleDrawWrapper cloned = (AngleDrawWrapper) super.clone();
            // Deep clone lists
            cloned.ellipses = new ArrayList<>();
            for (EllipseWrapper e : this.ellipses) {
                cloned.ellipses.add(e.clone());
            }
            cloned.lines = new ArrayList<>();
            for (LineWrapper l : this.lines) {
                cloned.lines.add(l.clone());
            }
            cloned.labelWrapper = (this.labelWrapper != null) ? this.labelWrapper.clone() : null;
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("AngleDrawWrapper cloning failed", e);
        }
    }

    @Override
    public String toString() {
        return "AngleDrawWrapper{" +
                "ellipses=" + ellipses +
                ", lines=" + lines +
                ", labelWrapper=" + labelWrapper +
                '}';
    }
}
