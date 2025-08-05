package com.raymedis.rxviewui;


import com.dicomprintmodule.DicomCommunication;
import com.dicomprintmodule.DicomPrintManager;

public class DicomTest {

    public static void main(String[] args) {

        DicomCommunication dicomCommunication = new DicomCommunication(
                "READY",
                "172.16.1.30",
                "17238",
                "DRYPIX",
                "RxView",
                "STANDARD\\1,1",
                "PORTRAIT",
                "8INX10IN",
                "D:\\Work\\RxView_Java\\RxView\\43_RxView_Study_Restructure\\dcmOutput\\111.dcm");

        //System.out.println("Java return : " + DicomPrintManager.getInstance().printDicom(dicomCommunication));
    }

}
