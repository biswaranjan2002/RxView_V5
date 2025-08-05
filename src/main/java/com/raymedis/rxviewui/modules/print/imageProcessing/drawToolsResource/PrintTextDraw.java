package com.raymedis.rxviewui.modules.print.imageProcessing.drawToolsResource;

import com.raymedis.rxviewui.modules.print.printInput.PrintTextLabel;
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

public class PrintTextDraw implements PrintInterfaceDrawTools{
    private Canvas canvas;
    private GraphicsContext gc;

    private PrintTextLabel textLabel;

    private TextField currentTextField;

    private Pane root;

    private double offsetX;
    private double offsetY;

    private Boolean isDragging;

    private int clickCount;

    private PrintCanvasMainChange printCanvasMainChange;

    private Logger logger = LoggerFactory.getLogger(PrintTextDraw.class);

    public PrintTextDraw(Canvas theCanvas, Point2D point, Pane theRoot, PrintCanvasMainChange printCanvasMainChange){
        this.canvas = theCanvas;
        this.gc = canvas.getGraphicsContext2D();
        this.root = theRoot;
        clickCount = 0;
        this.printCanvasMainChange=printCanvasMainChange;
        printCanvasMainChange.isTextDrawComplete = false;


        handleClick(point);
    }

    public void reset(){
        clickCount = 0;
        printCanvasMainChange.isTextDrawComplete = true;
    }

    public PrintTextDraw() {

    }

    private void handleClick(Point2D clickPoint) {
        if(clickCount < 1){
            if(clickCount == 0){
                //printCanvasMainChange.isTextDrawComplete = true;
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

        printCanvasMainChange.removeTextMouse();
        printCanvasMainChange.removeTextLabel(textLabel);
        drawTexts();
        currentTextField = new TextField();
        if(textLabel != null){
            currentTextField.setText(textLabel.getText());
        }
        currentTextField.setLayoutX(x);
        currentTextField.setLayoutY(y);
        currentTextField.setFont(new Font(2* printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize()));
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
                    textLabel = new PrintTextLabel(newText, currentTextField.getLayoutX(),
                            currentTextField.getLayoutY(), Color.valueOf("#66FF00"),printCanvasMainChange.printDynamicCanvasElementsResize);
                    printCanvasMainChange.addTextLabel(textLabel);
                }
                else{
                    textLabel.setText(newText);
                    textLabel.setX(currentTextField.getLayoutX());
                    textLabel.setY(currentTextField.getLayoutY());
                    textLabel.setColor(Color.valueOf("#66FF00"));
                    printCanvasMainChange.addTextLabel(textLabel);
                }
            }
            root.getChildren().remove(currentTextField);
            currentTextField = null;

            //canvas.requestFocus();
            printCanvasMainChange.setTextMouse();
            printCanvasMainChange.isTextDrawComplete = true;
            drawTexts();
        });
    }



    private void openOnScreenKeyboard() {
        try {
            logger.info("Attempting to open the on-screen keyboard...");

            // Print the command that's about to be executed
            String command = "cmd /c start osk.exe";
            logger.info("Executing command: " + command);

            ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "start", "osk.exe");

            // Start the process and capture the process handle
            Process process = processBuilder.start();
            logger.info("Process started successfully. Waiting for it to complete...");

            // Wait for the process to finish
            int exitCode = process.waitFor();
            logger.info("Process completed with exit code: " + exitCode);

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
                printCanvasMainChange.isTextDrawComplete = false;
                handleClick(new Point2D(textLabel.getX(), textLabel.getY()));
            }

            if(printCanvasMainChange.isTextDrawComplete){
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
        printCanvasMainChange.redraw();

        if(printCanvasMainChange.isTextDrawComplete){
            if(textLabel!=null){

                String text = textLabel.getText();
                double px = textLabel.getX();
                double py = textLabel.getY();
                Color color = textLabel.getColor();

                /*gc.setStroke(Color.BLACK);
                gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize()));
                gc.strokeText(text, px, py+ 2*printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize());*/


                gc.setFill(color);
                gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize()));
                gc.fillText(text, px, py+ 2*printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize());
            }

        }

    }

    public void selected(PrintCanvasMainChange printCanvasMainChange){
        this.printCanvasMainChange = printCanvasMainChange;
        String text = textLabel.getText();
        double px = textLabel.getX();
        double py = textLabel.getY();
        Color color = textLabel.getColor();

     /*   gc.setStroke(Color.BLACK);
        gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize()));
        gc.strokeText(text, px, py+ 2*printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize());*/


        gc.setFill(color);
        gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize()));
        gc.fillText(text, px, py+ 2*printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize());
    }

    @Override
    public void removeAll() {
        printCanvasMainChange.removeTextLabel(textLabel);
        root.getChildren().remove(currentTextField);
        currentTextField = null;
        printCanvasMainChange.setTextMouse();
        textLabel = null;
        printCanvasMainChange.isTextDrawComplete = true;
    }

    @Override
    public Boolean containsElement(Node element) {
        return null;
    }

    public Boolean containsElement(PrintTextLabel element) {
        return textLabel == element;
    }

    public void hideAll(PrintCanvasMainChange printCanvasMainChange){

        this.printCanvasMainChange = printCanvasMainChange;

        printCanvasMainChange.removeTextLabel(textLabel);
        root.getChildren().remove(currentTextField);
        printCanvasMainChange.setTextMouse();
    }


    public void unHideAll(){
        printCanvasMainChange.addTextLabel(textLabel);
        printCanvasMainChange.setTextMouse();
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

    public PrintTextLabel getTextLabel() {
        return textLabel;
    }

    public void setTextLabel(PrintTextLabel textLabel) {
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

    public PrintCanvasMainChange getPrintCanvasMainChange() {
        return printCanvasMainChange;
    }

    public void setPrintCanvasMainChange(PrintCanvasMainChange printCanvasMainChange) {
        this.printCanvasMainChange = printCanvasMainChange;
    }

}
