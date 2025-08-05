package com.raymedis.rxviewui.service.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper.*;
import com.raymedis.rxviewui.modules.study.bodypart.BodyPartHandler;
import com.raymedis.rxviewui.modules.study.bodypart.BodyPartNode;
import com.raymedis.rxviewui.modules.study.bodypart.PatientBodyPart;
import com.raymedis.rxviewui.modules.study.params.ImagingParams;
import com.raymedis.rxviewui.modules.study.params.XrayParams;
import com.raymedis.rxviewui.modules.study.patient.PatientInfo;
import com.raymedis.rxviewui.modules.study.study.PatientStudy;
import com.raymedis.rxviewui.modules.study.study.StudyThumbNails;
import com.raymedis.rxviewui.modules.study.tabmanagement.TabManager;
import com.raymedis.rxviewui.modules.study.tabmanagement.TabNode;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KryoSerializer {



   private Kryo kryo;

    private static KryoSerializer instance = new KryoSerializer();
    public static KryoSerializer getInstance(){
        return instance;
    }

    public KryoSerializer() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        kryo = new Kryo();

        kryo.setReferences(true);

        kryo.register(PatientStudy.class);
        kryo.register(PatientBodyPart.class);
        kryo.register(PatientInfo.class);
        kryo.register(XrayParams.class);
        kryo.register(ImagingParams.class);
        kryo.register(HashMap.class);
        kryo.register(ArrayList.class);
        kryo.register(Mat.class, new MatSerializer());
        kryo.register(TabNode.class);
        kryo.register(TabManager.class);
        kryo.register(BodyPartHandler.class);
        kryo.register(LocalDateTime.class);
        kryo.register(BodyPartNode.class);
        kryo.register(EllipseWrapper.class);
        kryo.register(LineWrapper.class);
        kryo.register(LabelWrapper.class);
        kryo.register(AngleDrawWrapper.class);
        kryo.register(DistanceDrawWrapper.class);
        kryo.register(CropLayoutWrapper.class);
        kryo.register(LeftRightDrawWrapper.class);
        kryo.register(TextDrawWrapper.class);
        kryo.register(StudyThumbNails.class);
        kryo.register(ch.qos.logback.classic.Logger.class);
    }



    public void serializeToFile(PatientStudy study,File file) throws IOException {
        try (OutputStream fileStream = new FileOutputStream(file);
             Output output = new Output(fileStream)) {
            kryo.writeObject(output, study);
        }
    }


    public PatientStudy deserializeFromFile(File file) throws IOException {
        try (InputStream fileStream = new FileInputStream(file);
             Input input = new Input(fileStream)) {
            return kryo.readObject(input, PatientStudy.class);
        }
    }


    public void serializeMatToFile(List<StudyThumbNails> studyThumbNailsList, File file) throws IOException {
        try (OutputStream fileStream = new FileOutputStream(file);
             Output output = new Output(fileStream)) {
            kryo.writeObject(output, studyThumbNailsList);
        }
    }

    public List<StudyThumbNails> deserializeMatListFromFile(File file) throws IOException {
        try (InputStream fileStream = new FileInputStream(file);
             Input input = new Input(fileStream)) {
            return kryo.readObject(input, ArrayList.class);
        }
    }



}
