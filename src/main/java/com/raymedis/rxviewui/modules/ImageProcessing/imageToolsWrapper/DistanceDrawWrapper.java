package com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper;

import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;


public class DistanceDrawWrapper implements Cloneable{

    private List<EllipseWrapper> ellipses = FXCollections.observableArrayList();
    private LineWrapper lines;
    private LabelWrapper labelWrapper;

    public DistanceDrawWrapper() {

    }

    public DistanceDrawWrapper(List<EllipseWrapper> ellipses, LineWrapper lines, LabelWrapper labelWrapper) {
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

    public LineWrapper getLines() {
        return lines;
    }

    public void setLines(LineWrapper lines) {
        this.lines = lines;
    }

    public LabelWrapper getLabelWrapper() {
        return labelWrapper;
    }

    public void setLabelWrapper(LabelWrapper labelWrapper) {
        this.labelWrapper = labelWrapper;
    }

    @Override
    public String toString() {
        return "DistanceDrawWrapper{" +
                "ellipses=" + ellipses +
                ", lines=" + lines +
                ", labelWrapper=" + labelWrapper +
                '}';
    }

    // In DistanceDrawWrapper.clone()
    @Override
    public DistanceDrawWrapper clone() {
        try {
            DistanceDrawWrapper cloned = (DistanceDrawWrapper) super.clone();
            // Deep clone lists
            cloned.ellipses = new ArrayList<>();
            for (EllipseWrapper e : this.ellipses) {
                cloned.ellipses.add(e.clone());
            }
            cloned.lines = (this.lines != null) ? this.lines.clone() : null;
            cloned.labelWrapper = (this.labelWrapper != null) ? this.labelWrapper.clone() : null;
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("DistanceDrawWrapper cloning failed", e);
        }
    }

}
