package com.raymedis.rxviewui.service.study;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProcessingToolsController {

    public JFXButton boneButton;
    public JFXButton softTissueButton;
    public JFXButton balanceButton;

    public JFXSlider denoiseSlider;
    public JFXSlider sharpnessSlider;
    public JFXSlider contrastSlider;
    public JFXSlider brightnessSlider;


    public TextField denoiseValueLabel;
    public TextField sharpnessValueLabel;
    public TextField contrastValueLabel;
    public TextField brightnessValueLabel;
    public Canvas histogramCanvas;

    private List<JFXButton> buttonList = new ArrayList<>();

    private static final ProcessingToolsController instance = new ProcessingToolsController();
    public static ProcessingToolsController getInstance() {
        return instance;
    }

    private final Logger logger = LoggerFactory.getLogger(ProcessingToolsController.class);


    private final ProcessingToolsService processingToolsService = ProcessingToolsService.getInstance();


    public void loadData(){
        processingToolsService.denoiseSlider = denoiseSlider;
        processingToolsService.brightnessSlider= brightnessSlider;
        processingToolsService.contrastSlider=contrastSlider;
        processingToolsService.sharpnessSlider=sharpnessSlider;
        processingToolsService.histogramCanvas=histogramCanvas;




    }

    private void bindSliderAndTextField(JFXSlider slider, TextField textField) {
        // Determine if slider uses fractional steps (double) or integer steps
        boolean isDoubleSlider = slider.getBlockIncrement() < 1.0;

        // Prepare formatter
        DecimalFormat decimalFormat = isDoubleSlider
                ? new DecimalFormat("#0.0")  // one decimal place for double sliders
                : new DecimalFormat("#");     // integer format for integer sliders

        // Listener: Slider → TextField
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double value = newVal.doubleValue();

            // Snap to correct increment (round value to nearest increment)
            double increment = slider.getBlockIncrement();
            double snappedValue;
            if (isDoubleSlider) {
                snappedValue = Math.round(value / increment) * increment;
                // Fix floating point rounding issues (e.g., 1.9999999 to 2.0)
                snappedValue = Math.round(snappedValue * 10) / 10.0;
            } else {
                snappedValue = Math.round(value);
            }

            // If slider's value differs from snapped value, update slider to snapped value
            if (Math.abs(slider.getValue() - snappedValue) > 0.0001) {
                slider.setValue(snappedValue);
                return; // wait for next event to update textField to avoid infinite loop
            }

            // Update text field with formatted string
            textField.setText(decimalFormat.format(snappedValue));

            // Optional: process on every value change
            processingToolsService.process(slider);
        });

        // Listener: slider release triggers final processing
        slider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                processingToolsService.process(slider);
            }
        });

        // Listener: TextField → Slider
        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                return; // allow empty input temporarily
            }

            try {
                double parsedValue = isDoubleSlider ? Double.parseDouble(newVal) : Integer.parseInt(newVal);

                // Clamp value to slider range
                parsedValue = Math.max(slider.getMin(), Math.min(slider.getMax(), parsedValue));

                // Snap parsedValue to nearest increment
                double increment = slider.getBlockIncrement();
                double snappedValue;
                if (isDoubleSlider) {
                    snappedValue = Math.round(parsedValue / increment) * increment;
                    snappedValue = Math.round(snappedValue * 10) / 10.0; // fix floating rounding
                } else {
                    snappedValue = Math.round(parsedValue);
                }

                // Only update slider if value differs
                if (Math.abs(slider.getValue() - snappedValue) > 0.0001) {
                    slider.setValue(snappedValue);
                    processingToolsService.process(slider);
                }
            } catch (NumberFormatException e) {
                // Reset to slider's current value on invalid input
                textField.setText(decimalFormat.format(slider.getValue()));
            }
        });
    }





    public void loadEvents(){
        buttonList.addAll(List.of(boneButton, softTissueButton, balanceButton));
        boneButton.setDisable(true);
        softTissueButton.setDisable(true);



        denoiseSlider.setMin(3);
        denoiseSlider.setMax(51);
        denoiseSlider.setValue(3);
        denoiseSlider.setBlockIncrement(1);
        denoiseSlider.setMajorTickUnit(1);
        denoiseSlider.setMinorTickCount(0);


        sharpnessSlider.setMin(1);
        sharpnessSlider.setMax(31);
        sharpnessSlider.setBlockIncrement(1);
        sharpnessSlider.setMajorTickUnit(2);  // optional, major ticks every 2 (so they fall on odd numbers 1,3,5...)
        sharpnessSlider.setMinorTickCount(0);


        contrastSlider.setMin(0);
        contrastSlider.setMax(3);
        contrastSlider.setValue(1.0);
        contrastSlider.setBlockIncrement(0.1);
        contrastSlider.setMajorTickUnit(0.1);
        contrastSlider.setMinorTickCount(0);



        brightnessSlider.setValue(0.0);
        brightnessSlider.setMin(-100);
        brightnessSlider.setMax(100);
        brightnessSlider.setBlockIncrement(1);
        brightnessSlider.setMajorTickUnit(1);
        brightnessSlider.setMinorTickCount(0);


        denoiseValueLabel.setText(3.0 + "");
        sharpnessValueLabel.setText(1.0 + "");
        contrastValueLabel.setText(1.0 + "");
        brightnessValueLabel.setText(0.0 + "");


        bindSliderAndTextField(denoiseSlider, denoiseValueLabel);
        bindSliderAndTextField(sharpnessSlider, sharpnessValueLabel);
        bindSliderAndTextField(contrastSlider, contrastValueLabel);
        bindSliderAndTextField(brightnessSlider, brightnessValueLabel);
    }


    public void resetTools() {
        for (JFXButton button : buttonList) {
            button.getStyleClass().removeAll("selectedProcessingTools", "unSelectedProcessingTools");
            button.getStyleClass().add("unSelectedProcessingTools");
        }
    }


    public void boneClick() {
     /*   Mat mainMat = Imgcodecs.imread("D:\\Downloads\\output\\chest\\1.png", Imgcodecs.IMREAD_UNCHANGED);
        Mat outputImage = bilateral.apply16BitImage(mainMat,31,75, 75);
        Imgcodecs.imwrite("D:\\Work\\Temp\\outputImage0.png", outputImage);*/

        resetTools();
        boneButton.getStyleClass().remove("unSelectedProcessingTools");
        boneButton.getStyleClass().add("selectedProcessingTools");
    }

    public void softTissueClick() {
        resetTools();
        softTissueButton.getStyleClass().remove("unSelectedProcessingTools");
        softTissueButton.getStyleClass().add("selectedProcessingTools");
    }

    public void balanceClick() {
        resetTools();
        balanceButton.getStyleClass().remove("unSelectedProcessingTools");
        balanceButton.getStyleClass().add("selectedProcessingTools");
    }






}
