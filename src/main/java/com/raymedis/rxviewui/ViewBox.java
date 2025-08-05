package com.raymedis.rxviewui;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class ViewBox extends GridPane {
    private double parentWidth = -1;
    private double parentHeight = -1;


    private double mainScaleX;
    private double mainScaleY;

    public ViewBox(){
        super();
        updateScale();


    }


    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        if (getParent() != null && getParent() instanceof Region) {
            Region parent = (Region) getParent();
            parentWidth = parent.getWidth();
            parentHeight = parent.getHeight();
            updateScale();
            parent.widthProperty().addListener((observable, oldValue, newValue) -> {
                onParentWidthChanged(oldValue.doubleValue(), newValue.doubleValue());
            });

            parent.heightProperty().addListener((observable, oldValue, newValue) -> {
                onParentHeightChanged(oldValue.doubleValue(), newValue.doubleValue());
            });
        }
    }


    private void onParentWidthChanged(double oldValue, double newValue) {
        parentWidth = newValue;
        updateScale();
    }

    private void onParentHeightChanged(double oldValue, double newValue) {
        parentHeight = newValue;
        updateScale();
    }


    private void updateScale() {

        double scaleX = 1;
        double scaleY = 1;


        if(parentHeight != -1 && parentWidth != -1){

            double ratio = parentWidth / parentHeight;

            double targetRatio = getMaxWidth()/getMaxHeight();

            if (targetRatio < ratio) {
                scaleX = parentHeight / getHeight();
                scaleY = parentHeight / getHeight();
            } else if (targetRatio > ratio) {
                scaleX = parentWidth / getWidth();
                scaleY = parentWidth / getWidth();
            }
            else if (targetRatio == ratio) {
                scaleX = parentWidth / getWidth();
                scaleY = parentWidth / getWidth();
            }
        }

        setScaleX(scaleX);
        setScaleY(scaleY);

        mainScaleX = scaleX;
        mainScaleY = scaleY;
    }

    public double getMainScaleX() {
        return mainScaleX;
    }

    public void setMainScaleX(double mainScaleX) {
        this.mainScaleX = mainScaleX;
    }

    public double getParentHeight() {
        return parentHeight;
    }

    public void setParentHeight(double parentHeight) {
        this.parentHeight = parentHeight;
    }

    public double getParentWidth() {
        return parentWidth;
    }

    public void setParentWidth(double parentWidth) {
        this.parentWidth = parentWidth;
    }

    public double getMainScaleY() {
        return mainScaleY;
    }

    public void setMainScaleY(double mainScaleY) {
        this.mainScaleY = mainScaleY;
    }
}
