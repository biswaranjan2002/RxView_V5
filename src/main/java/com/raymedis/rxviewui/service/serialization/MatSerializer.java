package com.raymedis.rxviewui.service.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MatSerializer extends com.esotericsoftware.kryo.Serializer<Mat> {

    @Override
    public void write(Kryo kryo, Output output, Mat mat) {
        int rows = mat.rows();
        int cols = mat.cols();
        int type = mat.type();
        int depth = mat.depth();
        int channels = mat.channels();
        int total = rows * cols * channels;

        output.writeInt(rows);
        output.writeInt(cols);
        output.writeInt(type); // Save the full type

        switch (depth) {
            case CvType.CV_8U -> {
                byte[] data = new byte[total];
                mat.get(0, 0, data);
                output.writeBytes(data);
            }
            case CvType.CV_16U, CvType.CV_16S -> {
                short[] data = new short[total];
                mat.get(0, 0, data);
                for (short val : data) output.writeShort(val);
            }
            case CvType.CV_32S -> {
                int[] data = new int[total];
                mat.get(0, 0, data);
                for (int val : data) output.writeInt(val);
            }
            case CvType.CV_32F -> {
                float[] data = new float[total];
                mat.get(0, 0, data);
                for (float val : data) output.writeFloat(val);
            }
            case CvType.CV_64F -> {
                double[] data = new double[total];
                mat.get(0, 0, data);
                for (double val : data) output.writeDouble(val);
            }
            default -> throw new UnsupportedOperationException("Mat data type is not compatible: " + type);
        }
    }


    @Override
    public Mat read(Kryo kryo, Input input, Class<? extends Mat> typeClass) {
        int rows = input.readInt();
        int cols = input.readInt();
        int type = input.readInt();

        int depth = type % 8;
        int channels = (type / 8) + 1;
        int total = rows * cols * channels;

        Mat mat = new Mat(rows, cols, type);

        switch (depth) {
            case CvType.CV_8U -> {
                byte[] data = input.readBytes(total);
                mat.put(0, 0, data);
            }
            case CvType.CV_16U, CvType.CV_16S -> {
                short[] data = new short[total];
                for (int i = 0; i < total; i++) {
                    data[i] = input.readShort();
                }
                mat.put(0, 0, data);
            }
            case CvType.CV_32S -> {
                int[] data = new int[total];
                for (int i = 0; i < total; i++) {
                    data[i] = input.readInt();
                }
                mat.put(0, 0, data);
            }
            case CvType.CV_32F -> {
                float[] data = new float[total];
                for (int i = 0; i < total; i++) {
                    data[i] = input.readFloat();
                }
                mat.put(0, 0, data);
            }
            case CvType.CV_64F -> {
                double[] data = new double[total];
                for (int i = 0; i < total; i++) {
                    data[i] = input.readDouble();
                }
                mat.put(0, 0, data);
            }
            default -> throw new UnsupportedOperationException("Mat data type is not compatible: " + type);
        }

        return mat;
    }
}


