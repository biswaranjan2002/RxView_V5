package com.raymedis.rxviewui.service.study;

import com.ramedis.zioxa.bilateral.Bilateral;
import com.ramedis.zioxa.clahe.CLAHE;
import com.ramedis.zioxa.utils.RawImageLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;

public class Demo {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws IOException {
        Mat img32f= RawImageLoader.loadRawImage("D:\\Downloads\\input\\test\\chest\\1.raw",2500,3052);
        Bilateral bilateral=new Bilateral();
        CLAHE clahe=new CLAHE();
        Mat result16= clahe.apply16BitImage(img32f,7,7);
        result16=bilateral.apply16BitImage(result16,51,75,75);
        Imgcodecs.imwrite("D:\\Work\\Temp\\saidemo.png", result16);
    }
}
