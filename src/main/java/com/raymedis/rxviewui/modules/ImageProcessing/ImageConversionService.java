package com.raymedis.rxviewui.modules.ImageProcessing;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.*;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;


public class ImageConversionService {

    private static final Logger logger = LoggerFactory.getLogger(ImageConversionService.class);
    private final static ImageConversionService instance = new ImageConversionService();
    private static final Logger log = LoggerFactory.getLogger(ImageConversionService.class);

    public static ImageConversionService getInstance(){
        return instance;
    }

    public int expectedLength;
    private final int[] argbValues = new int[256];
    private IntBuffer intBuffer;

    public MatOfByte buffer = new MatOfByte();
    public WritableImage writableImage = null;

    public void initConversion(){
        for (int i = 0; i < 256; i++) {
            argbValues[i] = (0xFF << 24) | (i << 16) | (i << 8) | i;
        }
        intBuffer = IntBuffer.allocate(1);
    }


    public Image matToImage0(Mat mat) {
        if (mat == null || mat.empty() || mat.rows() == 0 || mat.cols() == 0) {
            return null;
        }

        boolean result = Imgcodecs.imencode(".bmp", mat, buffer);

        if (!result) {
            return null;
        }

        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }

    public Image matToImage(Mat mat) {
        if (mat == null || mat.empty() || mat.rows() == 0 || mat.cols() == 0) {
            return null;
        }

        boolean result = Imgcodecs.imencode(".png", mat, buffer);

        if (!result) {
            return null;
        }

        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }


    //delay issues due to JPEGLosslessImageReaderSpi.canDecodeInput() = false
    public WritableImage matToWritableImage(Mat mat) {
        MatOfByte buffer = new MatOfByte();

        // Ensure correct BMP encoding
        boolean success = Imgcodecs.imencode(".png", mat, buffer);
        if (!success) {
            logger.error("Failed to encode Mat to BMP format");
            return null;
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer.toArray());

        try {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            if (bufferedImage == null) {
                logger.error("ImageIO failed to decode the BMP image");
                return null;
            }

            WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, null);
            return writableImage;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            buffer.release();  // Release buffer resources
        }
    }





    public Mat imageToMat(Image image) {
        // Convert JavaFX Image to BufferedImage
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

        // Ensure the BufferedImage is in the correct format (grayscale)
        if (bufferedImage.getType() != BufferedImage.TYPE_BYTE_GRAY) {
            BufferedImage convertedImg = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            convertedImg.getGraphics().drawImage(bufferedImage, 0, 0, null);
            bufferedImage = convertedImg;
        }

        // Get the data from BufferedImage
        byte[] data = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();

        // Create a Mat and put the data into it
        Mat mat = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), CvType.CV_8UC1);
        mat.put(0, 0, data);

        return mat;
    }


    public Mat imageToMat24Bit(Image image) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

        // Extract image dimensions
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        // Prepare byte array for pixel data
        byte[] pixelData = new byte[width * height * 3];

        // Extract pixel data from BufferedImage and convert RGB to BGR
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = bufferedImage.getRGB(x, y);
                pixelData[(y * width + x) * 3] = (byte) (rgb & 0xFF);        // Blue
                pixelData[(y * width + x) * 3 + 1] = (byte) ((rgb >> 8) & 0xFF); // Green
                pixelData[(y * width + x) * 3 + 2] = (byte) ((rgb >> 16) & 0xFF); // Red
            }
        }

        // Create OpenCV Mat with 8-bit 3-channel format (BGR)
        Mat mat = new Mat(height, width, CvType.CV_8UC3);
        mat.put(0, 0, pixelData);

        return mat;
    }

    public static void saveAsPng(WritableImage writableImage, String filePath) {
        // Convert JavaFX WritableImage to BufferedImage
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);

        // Save the BufferedImage as PNG
        try {
            File outputFile = new File(filePath);
            ImageIO.write(bufferedImage, "png", outputFile);
            logger.info("Image saved to: {}", outputFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Error saving image: {}", e.getMessage());
        }
    }

    public static Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat;
        //logger.info(String.valueOf(bi.getType()));
        switch (bi.getType()) {
            case BufferedImage.TYPE_3BYTE_BGR:
                // Standard 3-byte BGR (most common format for color images)
                mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
                byte[] dataBGR = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
                mat.put(0, 0, dataBGR);
                break;

            case BufferedImage.TYPE_INT_RGB:
                // INT_RGB format (using DataBufferInt), we need to convert int[] to byte[]
                mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
                int[] dataRGB = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
                byte[] rgbBytes = new byte[bi.getWidth() * bi.getHeight() * 3];

                for (int i = 0; i < dataRGB.length; i++) {
                    int argb = dataRGB[i];
                    rgbBytes[i * 3] = (byte) ((argb >> 16) & 0xFF); // Red
                    rgbBytes[i * 3 + 1] = (byte) ((argb >> 8) & 0xFF); // Green
                    rgbBytes[i * 3 + 2] = (byte) (argb & 0xFF); // Blue
                }
                mat.put(0, 0, rgbBytes);
                break;

            case BufferedImage.TYPE_BYTE_GRAY:
                // Grayscale image
                mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC1);
                byte[] dataGray = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
                mat.put(0, 0, dataGray);
                break;

            case BufferedImage.TYPE_INT_ARGB:
                // 4-channel ARGB image (using DataBufferInt)
                mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC4);
                int[] dataARGB = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
                byte[] argbBytes = new byte[bi.getWidth() * bi.getHeight() * 4];

                for (int i = 0; i < dataARGB.length; i++) {
                    int argb = dataARGB[i];
                    argbBytes[i * 4] = (byte) ((argb >> 16) & 0xFF); // Red
                    argbBytes[i * 4 + 1] = (byte) ((argb >> 8) & 0xFF); // Green
                    argbBytes[i * 4 + 2] = (byte) (argb & 0xFF); // Blue
                    argbBytes[i * 4 + 3] = (byte) ((argb >> 24) & 0xFF); // Alpha
                }
                mat.put(0, 0, argbBytes);
                break;

            default:
                // Fallback: Convert unsupported image type to TYPE_3BYTE_BGR
                BufferedImage convertedImg = convertTo3ByteBGR(bi);
                mat = bufferedImageToMat(convertedImg);  // Recursively process converted image
        }

        return mat;
    }


    public static Mat writableImageToMat(WritableImage fxImage) {
        int width = (int) fxImage.getWidth();
        int height = (int) fxImage.getHeight();
        Mat mat = new Mat(height, width, CvType.CV_8UC4); // BGRA format
        byte[] buffer = new byte[width * height * 4];

        PixelReader reader = fxImage.getPixelReader();
        reader.getPixels(0, 0, width, height, WritablePixelFormat.getByteBgraInstance(), buffer, 0, width * 4);
        mat.put(0, 0, buffer);
        return mat;
    }

    // Fallback conversion method for unsupported BufferedImage types
    private static BufferedImage convertTo3ByteBGR(BufferedImage bi) {
        BufferedImage newImage = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(bi, 0, 0, null);
        g.dispose();
        return newImage;
    }


    public byte[] imageToByteArray(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        byte[] imageBytes = new byte[width * height];

        PixelReader pixelReader = image.getPixelReader();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = pixelReader.getArgb(x, y);
                int grayValue = (int) ((((argb >> 16) & 0xFF) + ((argb >> 8) & 0xFF) + (argb & 0xFF)) / 3.0);
                imageBytes[(y * width) + x] = (byte) grayValue;
            }
        }

        return imageBytes;
    }



    public Image byteArrayToImage(byte[] imageData, int width, int height) {

        expectedLength = width*height;
        if(intBuffer.capacity() != expectedLength){
            intBuffer = IntBuffer.allocate(expectedLength);
        }

        WritableImage writableImage = null;
        try {
            if (imageData.length != expectedLength) {
                throw new IllegalArgumentException("Invalid imageData length");
            }

            // Clear the IntBuffer for reuse
            intBuffer.clear();

            // Use a ByteBuffer for faster access
            ByteBuffer buffer = ByteBuffer.wrap(imageData);

            // Convert MONO8 pixel data to ARGB
            for (int i = 0; i < expectedLength; i++) {
                int intensity = buffer.get() & 0xFF;
                intBuffer.put(argbValues[intensity]);
            }

            // Reset the buffer position to the start
            intBuffer.flip();

            // Create a writable image
            writableImage = new WritableImage(width, height);
            PixelWriter pixelWriter = writableImage.getPixelWriter();

            // Use WritablePixelFormat to write all pixels at once
            pixelWriter.setPixels(0, 0, width, height, WritablePixelFormat.getIntArgbInstance(), intBuffer.array(), 0, width);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return writableImage;
    }

    public Mat byteArrayToMat(byte[] imageData, int width, int height, int type) {
        // Validate input size against expected size
        int expectedSize = width * height * (type == CvType.CV_8UC1 ? 1 : 3); // Adjust for channel count
        if (imageData.length != expectedSize) {
            throw new IllegalArgumentException("Invalid imageData length. Expected: " + expectedSize + ", but got: " + imageData.length);
        }

        // Create a Mat with the appropriate size and type
        Mat mat = new Mat(height, width, type);
        if (mat.empty()) {
            throw new IllegalStateException("Matrix is not properly initialized");
        }

        // Use put() method efficiently
        mat.put(0, 0, imageData);
        return mat;
    }

}
