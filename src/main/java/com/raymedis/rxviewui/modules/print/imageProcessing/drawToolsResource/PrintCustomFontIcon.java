package com.raymedis.rxviewui.modules.print.imageProcessing.drawToolsResource;

import com.raymedis.rxviewui.modules.print.imageProcessing.PrintDynamicCanvasElementsResize;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintCustomFontIcon {

    private FontIcon leftIcon = new FontIcon("mdi2a-alpha-l");

    private FontIcon rightIcon = new FontIcon("mdi2a-alpha-r");

    private double iconSize; // Size of the icon

    private WritableImage leftImage;

    private WritableImage rightImage;

    private WritableImage currentImage;

    private Canvas canvas;

    private Logger logger = LoggerFactory.getLogger(PrintCustomFontIcon.class);



    private double x;
    private double y;
    private Color color;

    public PrintCustomFontIcon(double x, double y, Color color, Canvas canvas, PrintDynamicCanvasElementsResize printDynamicCanvasElementsResize) {

        iconSize = printDynamicCanvasElementsResize.getCustomFontIconSize();

        //left
        leftIcon.setIconSize((int)iconSize);
        leftIcon.setIconColor(Color.WHITE);
        leftIcon.setStroke(Color.BLACK);

        StackPane pane = new StackPane(leftIcon);
        pane.setStyle("-fx-background-color: transparent;"); // Ensure the background is transparent

        Scene scene = new Scene(pane, (int)iconSize, (int)iconSize);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        leftImage = pane.snapshot(parameters, null);

        //right
        rightIcon.setIconSize((int)iconSize);
        rightIcon.setIconColor(Color.WHITE);
        rightIcon.setStroke(Color.BLACK);

        StackPane pane1 = new StackPane(rightIcon);
        pane1.setStyle("-fx-background-color: transparent;"); // Ensure the background is transparent

        Scene scene1 = new Scene(pane1, (int)iconSize, (int)iconSize);

        SnapshotParameters parameters1 = new SnapshotParameters();
        parameters1.setFill(Color.TRANSPARENT);
        rightImage = pane1.snapshot(parameters1, null);


        this.x = x;
        this.y = y;
        this.color = color;
        this.canvas = canvas;
    }

    public WritableImage setIconGetImage(String text) {
        if(text.equalsIgnoreCase("LEFT")){
            currentImage = leftImage;
            return leftImage;
        }
        else if(text.equalsIgnoreCase("RIGHT")){
            if(currentImage == null){
                double width = canvas.getWidth();
                double topRightX = width;
                double topRightY = 0;
                x = topRightX - x - iconSize;
            }

            currentImage = rightImage;
            return rightImage;
        }
        else {
            logger.info("Please enter a valid icon text");
            return null;
        }
    }

    public WritableImage getCurrentImage(){
        return currentImage;
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean contains(double clickX, double clickY) {
        // Check if the click is within the icon's bounds
        return clickX >= x && clickX <= x + iconSize &&
                clickY >= y && clickY <= y + iconSize;
    }

}
