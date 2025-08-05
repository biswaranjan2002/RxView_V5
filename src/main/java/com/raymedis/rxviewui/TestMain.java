package com.raymedis.rxviewui;


import com.ramedis.zioxa.utils.RawImageLoader;
import com.raymedis.rxviewui.service.preProcessing.PreProcess;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class TestMain {
    public static void main(String[] args) {

        try {

            PreProcess preProcess  = PreProcess.getInstance();
            Mat input8Bit1 = RawImageLoader.loadRawImage("D:\\Downloads\\input\\test\\abdomen\\2.raw", 2500, 3052);
            Mat output = preProcess.process(input8Bit1,"abdomen");
            Imgcodecs.imwrite("D:\\Downloads\\output\\abdomen\\2.png", output);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

}
