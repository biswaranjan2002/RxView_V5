package com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource;

import com.raymedis.rxviewui.modules.ImageProcessing.DynamicCanvasElementsResize;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TextLabel {

    private String text;
    private double x;
    private double y;
    private Color color;

    public TextLabel(String text, double x, double y, Color color) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String angle) {
        this.text = angle;
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

    public boolean contains(double mouseX, double mouseY) {
        Text tempText = new Text(text);
        tempText.setFont(new Font(DynamicCanvasElementsResize.getLabelFontSize()*4)); // Ensure the same font size
        double textWidth = tempText.getLayoutBounds().getWidth();
        double textHeight = tempText.getLayoutBounds().getHeight();

        boolean withinX = mouseX >= x && mouseX <= x + textWidth;
        boolean withinY = mouseY >= y && mouseY <= y + textHeight;


        return withinX && withinY;
    }
}
