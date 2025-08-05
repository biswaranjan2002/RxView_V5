package com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource;

import com.raymedis.rxviewui.modules.ImageProcessing.DynamicCanvasElementsResize;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextDraw implements IDrawTools{

    private Canvas canvas;
    private GraphicsContext gc;

    private TextLabel textLabel;

    private TextField currentTextField;

    private Pane root;

    private double offsetX;
    private double offsetY;

    private Boolean isDragging;

    private int clickCount;

    public static boolean isComplete = true;

    public TextDraw(Canvas theCanvas, Point2D point, Pane theRoot){
        this.canvas = theCanvas;
        this.gc = canvas.getGraphicsContext2D();
        this.root = theRoot;
        clickCount = 0;
        isComplete = false;
        handleClick(point);
    }

    public void reset(){
        clickCount = 0;
        isComplete = true;
    }

    public TextDraw() {

    }

    private void handleClick(Point2D clickPoint) {
        if(clickCount < 1){
            if(clickCount == 0){
                //isComplete = true;
                addTextField(clickPoint.getX(), clickPoint.getY());
            }
            clickCount++;
        }
    }

    private void addTextField(double x, double y) {
        if (currentTextField != null) {
            root.getChildren().remove(currentTextField);
            currentTextField = null;
        }

        CanvasMainChange.removeTextMouse();
        CanvasMainChange.removeTextLabel(textLabel);
        drawTexts();
        currentTextField = new TextField();
        if(textLabel != null){
            currentTextField.setText(textLabel.getText());
        }
        currentTextField.setLayoutX(x);
        currentTextField.setLayoutY(y);
        currentTextField.setFont(new Font(2*DynamicCanvasElementsResize.getLabelFontSize()));
        currentTextField.setId("imageToolsText");
        currentTextField.setPrefWidth(1000);
        root.getChildren().add(currentTextField);
        currentTextField.requestFocus();



        currentTextField.positionCaret(currentTextField.getText().length());

        currentTextField.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                openOnScreenKeyboard();

            }
        });

        currentTextField.setOnAction(e -> {
            String newText = currentTextField.getText();
            if (!newText.isEmpty()) {
                if(textLabel == null){
                    textLabel = new TextLabel(newText, currentTextField.getLayoutX(),
                            currentTextField.getLayoutY(), Color.valueOf("#66FF00"));
                    CanvasMainChange.addTextLabel(textLabel);
                }
                else{
                    textLabel.setText(newText);
                    textLabel.setX(currentTextField.getLayoutX());
                    textLabel.setY(currentTextField.getLayoutY());
                    textLabel.setColor(Color.valueOf("#66FF00"));
                    CanvasMainChange.addTextLabel(textLabel);
                }
            }
            root.getChildren().remove(currentTextField);
            currentTextField = null;

            //canvas.requestFocus();
            CanvasMainChange.setTextMouse();
            isComplete = true;
            drawTexts();
        });
    }

    private Logger logger = LoggerFactory.getLogger(TextDraw.class);

    private void openOnScreenKeyboard() {
        try {
            logger.info("Attempting to open the on-screen keyboard...");

            // Print the command that's about to be executed
            String command = "cmd /c start osk.exe";
            logger.info("Executing command: {}", command);

            ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "start", "osk.exe");

            // Start the process and capture the process handle
            Process process = processBuilder.start();
            logger.info("Process started successfully. Waiting for it to complete...");

            // Wait for the process to finish
            int exitCode = process.waitFor();
            logger.info("Process completed with exit code: {}", exitCode);

        } catch (Exception e) {
            logger.info("An error occurred while trying to open the on-screen keyboard.");
            e.printStackTrace();
        }
    }


    @Override
    public void handleMousePressed(MouseEvent event) {
        if (textLabel != null && textLabel.contains(event.getX(), event.getY())) {

            boolean isDoubleClick = event.getClickCount() == 2;

            if (isDoubleClick && event.getButton() == MouseButton.PRIMARY){
                clickCount = 0;
                isComplete = false;
                handleClick(new Point2D(textLabel.getX(), textLabel.getY()));
            }

            if(isComplete){
                offsetX = event.getX() - textLabel.getX();
                offsetY = event.getY() - textLabel.getY();
                isDragging = true;
            }
        }
    }

    @Override
    public void handleMouseDragged(MouseEvent event) {
        if (textLabel != null && isDragging) {
            textLabel.setX(event.getX() - offsetX);
            textLabel.setY(event.getY() - offsetY);
            drawTexts();
        }
    }

    @Override
    public void handleCanvasReleased(MouseEvent mouseEvent) {
        isDragging = false;
    }


    public void drawTexts(){

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        CanvasMainChange.redraw();

        if(isComplete){
            if(textLabel!=null){
                String text = textLabel.getText();
                double px = textLabel.getX();
                double py = textLabel.getY();
                Color color = textLabel.getColor();

                gc.setStroke(Color.BLACK);
                gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * DynamicCanvasElementsResize.getLabelFontSize()));
                gc.strokeText(text, px, py+ 2*DynamicCanvasElementsResize.getLabelFontSize());


                gc.setFill(color);
                gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * DynamicCanvasElementsResize.getLabelFontSize()));
                gc.fillText(text, px, py+ 2*DynamicCanvasElementsResize.getLabelFontSize());

            }

        }

    }

    public void selected(){
        String text = textLabel.getText();
        double px = textLabel.getX();
        double py = textLabel.getY();
        Color color = textLabel.getColor();

        gc.setStroke(Color.BLACK);
        gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * DynamicCanvasElementsResize.getLabelFontSize()));
        gc.strokeText(text, px, py+ 2*DynamicCanvasElementsResize.getLabelFontSize());


        gc.setFill(color);
        gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * DynamicCanvasElementsResize.getLabelFontSize()));
        gc.fillText(text, px, py+ 2*DynamicCanvasElementsResize.getLabelFontSize());
    }

    @Override
    public void removeAll() {
        CanvasMainChange.removeTextLabel(textLabel);
        root.getChildren().remove(currentTextField);
        currentTextField = null;
        CanvasMainChange.setTextMouse();
        textLabel = null;
        isComplete = true;
    }

    @Override
    public Boolean containsElement(Node element) {
        return null;
    }

    public Boolean containsElement(TextLabel element) {
        return textLabel == element;
    }

    public void hideAll(){
        CanvasMainChange.removeTextLabel(textLabel);
        root.getChildren().remove(currentTextField);
        CanvasMainChange.setTextMouse();
    }


    public void unHideAll(){
        CanvasMainChange.addTextLabel(textLabel);
        CanvasMainChange.setTextMouse();
    }


    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public TextLabel getTextLabel() {
        return textLabel;
    }

    public void setTextLabel(TextLabel textLabel) {
        this.textLabel = textLabel;
    }

    public TextField getCurrentTextField() {
        return currentTextField;
    }

    public void setCurrentTextField(TextField currentTextField) {
        this.currentTextField = currentTextField;
    }

    public Pane getRoot() {
        return root;
    }

    public void setRoot(Pane root) {
        this.root = root;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public Boolean getDragging() {
        return isDragging;
    }

    public void setDragging(Boolean dragging) {
        isDragging = dragging;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public static boolean isIsComplete() {
        return isComplete;
    }

    public static void setIsComplete(boolean isComplete) {
        TextDraw.isComplete = isComplete;
    }
}
