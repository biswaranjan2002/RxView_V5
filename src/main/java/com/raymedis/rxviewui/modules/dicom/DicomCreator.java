package com.raymedis.rxviewui.modules.dicom;

import com.pixelmed.convert.CommonConvertedAttributeGeneration;
import com.pixelmed.dicom.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DicomCreator {

    private static final Logger logger = LoggerFactory.getLogger(DicomCreator.class);
    private static UIDGenerator uidGenerator = new UIDGenerator();

    private String sOPInstanceUID;
    private String seriesInstanceUID;
    private String studyInstanceUID;

    public String getsOPInstanceUID() {
        return sOPInstanceUID;
    }

    public String getSeriesInstanceUID() {
        return seriesInstanceUID;
    }

    public String getStudyInstanceUID() {
        return studyInstanceUID;
    }


    public DicomCreator(List<Mat> imageMatList, String outputDicomPath, DicomMetadata dicomMetadata) throws IOException, DicomException {
        AttributeList myAttributeList = DicomCreator.generateDICOMPixelModuleFromMatList(imageMatList, null);

        // Helper method to add attributes
        sOPInstanceUID = uidGenerator.getNewSOPInstanceUID(dicomMetadata.getStudyId(), dicomMetadata.getSeriesNumber(), dicomMetadata.getInstanceNumber());
        addUniqueIdentifierAttribute(myAttributeList, TagFromName.SOPInstanceUID, sOPInstanceUID);

        seriesInstanceUID = uidGenerator.getNewSeriesInstanceUID(dicomMetadata.getStudyId(), dicomMetadata.getSeriesNumber());
        addUniqueIdentifierAttribute(myAttributeList, TagFromName.SeriesInstanceUID, seriesInstanceUID);

        studyInstanceUID = uidGenerator.getNewStudyInstanceUID(dicomMetadata.getStudyId());
        addUniqueIdentifierAttribute(myAttributeList, TagFromName.StudyInstanceUID, studyInstanceUID);

        addPersonNameAttribute(myAttributeList, TagFromName.PatientName, dicomMetadata.getPatientName());
        addLongStringAttribute(myAttributeList, TagFromName.PatientID, dicomMetadata.getPatientId());
        addDateAttribute(myAttributeList, TagFromName.PatientBirthDate, dicomMetadata.getPatientDOB());
        addCodeStringAttribute(myAttributeList, TagFromName.PatientSex, dicomMetadata.getPatientSex());
        addShortStringAttribute(myAttributeList, TagFromName.StudyID, dicomMetadata.getStudyId());
        addPersonNameAttribute(myAttributeList, TagFromName.ReferringPhysicianName, dicomMetadata.getReferringPhysician());
        addShortStringAttribute(myAttributeList, TagFromName.AccessionNumber, dicomMetadata.getAccessionNumber());
        addIntegerStringAttribute(myAttributeList, TagFromName.SeriesNumber, dicomMetadata.getSeriesNumber());
        addIntegerStringAttribute(myAttributeList, TagFromName.InstanceNumber, dicomMetadata.getInstanceNumber());
        addLongStringAttribute(myAttributeList, TagFromName.Manufacturer, dicomMetadata.getManufacturer());
        addPersonNameAttribute(myAttributeList, TagFromName.StudyDescription, dicomMetadata.getStudyDescription());
        addPersonNameAttribute(myAttributeList, TagFromName.ProcedureCodeSequence, dicomMetadata.getProcedureCode());
        addPersonNameAttribute(myAttributeList, TagFromName.BodyPartExamined, dicomMetadata.getBodyPart());

        // Add patient orientation and laterality with empty values
        addCodeStringAttribute(myAttributeList, TagFromName.ViewPosition, dicomMetadata.getViewPosition());
        addCodeStringAttribute(myAttributeList, TagFromName.Laterality, "");
        addCodeStringAttribute(myAttributeList, TagFromName.BurnedInAnnotation, String.valueOf(dicomMetadata.isBurnedInAnnotation()));
        addImageTypeAttributes(myAttributeList);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss.SSS");
        addDateAttribute(myAttributeList, TagFromName.StudyDate, dateFormat.format(dicomMetadata.getStudyDateTime()));
        addTimeAttribute(myAttributeList, TagFromName.StudyTime, timeFormat.format(dicomMetadata.getStudyDateTime()));

        addDateAttribute(myAttributeList, TagFromName.AcquisitionDate, dateFormat.format(dicomMetadata.getAcquisitionDateTime()));
        addTimeAttribute(myAttributeList, TagFromName.AcquisitionTime, timeFormat.format(dicomMetadata.getAcquisitionDateTime()));

        // Instance Creator UID
        addUniqueIdentifierAttribute(myAttributeList, TagFromName.InstanceCreatorUID, "1.3.6.1.4.1.5962.99.1");

        int numberOfFrames = Attribute.getSingleIntegerValueOrDefault(myAttributeList, TagFromName.NumberOfFrames, 1);
        int samplesPerPixel = Attribute.getSingleIntegerValueOrDefault(myAttributeList, TagFromName.SamplesPerPixel, 1); // update with image channels

        String sopClass = dicomMetadata.getSopClass();
        if (sopClass == null) {
            int bitsAllocated = Attribute.getSingleIntegerValueOrDefault(myAttributeList, TagFromName.BitsAllocated, 1);
            sopClass = CommonConvertedAttributeGeneration.selectSOPClassForModalityNumberOfFramesAndPixelCharacteristics(
                    dicomMetadata.getModality(), numberOfFrames, samplesPerPixel, bitsAllocated, false);
        }

        if (numberOfFrames > 1) {
            addMultiFrameAttributes(myAttributeList, numberOfFrames);
        }

        if (SOPClass.isMultiframeSecondaryCaptureImageStorage(sopClass) && samplesPerPixel == 1) {
            addMultiframeSecondaryCaptureAttributes(myAttributeList);
        }

        addUniqueIdentifierAttribute(myAttributeList, TagFromName.SOPClassUID, sopClass);

        if (SOPClass.isSecondaryCaptureImageStorage(sopClass)) {
            addCodeStringAttribute(myAttributeList, TagFromName.ConversionType, "WSD");
        }

        String modality = dicomMetadata.getModality();
        if (modality == null) {
            modality = "OT";
        }

        addCodeStringAttribute(myAttributeList, TagFromName.Modality, modality);

        // X-Ray and dose attributes
        addIntegerStringAttribute(myAttributeList, new AttributeTag(0x0018, 0x1151), dicomMetadata.getXrayTubeCurrent());
        addDecimalStringAttribute(myAttributeList, new AttributeTag(0x0018, 0x0060), dicomMetadata.getkVp());
        addDecimalStringAttribute(myAttributeList, new AttributeTag(0x0018, 0x115E), dicomMetadata.getAreaDoseProduct());

        // Hospital information
        addLongStringAttribute(myAttributeList, TagFromName.InstitutionName, dicomMetadata.getHospitalName());
        addLongStringAttribute(myAttributeList, TagFromName.InstitutionAddress, dicomMetadata.getHospitalAddress());

        // Finalize and save the DICOM file
        CodingSchemeIdentification.replaceCodingSchemeIdentificationSequenceWithCodingSchemesUsedInAttributeList(myAttributeList);
        myAttributeList.insertSuitableSpecificCharacterSetForAllStringValues();
        FileMetaInformation.addFileMetaInformation(myAttributeList, "1.2.840.10008.1.2.1", "OURAETITLE");
        //logger.info("file path : {} " , outputDicomPath);
        myAttributeList.write(outputDicomPath, "1.2.840.10008.1.2.1", true, true);
    }


    public static AttributeList generateDICOMPixelModuleFromImageFiles(List<File> fileList, AttributeList attributeList) throws IOException, DicomException {
        if (fileList == null || fileList.isEmpty()) {
            throw new IllegalArgumentException("File list is empty");
        }

        int numberOfFrames = fileList.size();
        int totalWidth = 0;
        int totalHeight = 0;
        String photometricInterpretation = null;
        int samplesPerPixel = 0;
        int bitsAllocated = 0;
        int bitsStored = 0;
        int highBit = 0;
        int pixelRepresentation = 0;

        // Collect pixel data from all files
        List<byte[]> pixelDataList = new ArrayList<>();

        for (File file : fileList) {
            BufferedImage bufferedImage = null;
            FileImageInputStream imageInputStream = new FileImageInputStream(file);
            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);
            ImageReader imageReader = null;
            if (imageReaders.hasNext()) {
                imageReader = imageReaders.next();
            }

            if (imageReader != null) {
                imageReader.setInput(imageInputStream);

                try {
                    bufferedImage = imageReader.read(0);
                } finally {
                    imageReader.dispose();
                }
            }

            if (bufferedImage == null) {
                throw new DicomException("Unrecognized image file type");
            }

            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            totalWidth = width;
            totalHeight = height;

            SampleModel sampleModel = bufferedImage.getSampleModel();
            DataBuffer dataBuffer = bufferedImage.getRaster().getDataBuffer();
            int numBands = sampleModel.getNumBands();

            if (photometricInterpretation == null) {
                photometricInterpretation = numBands == 3 ? "RGB" : (numBands == 1 ? "MONOCHROME2" : "");
                samplesPerPixel = numBands;
            }

            // Handle byte and short data buffer types
            if (dataBuffer instanceof DataBufferByte) {
                int dataSize = width * height * numBands;
                byte[] pixelData = new byte[dataSize];
                int[] pixels = sampleModel.getPixels(0, 0, width, height, (int[]) null, dataBuffer);
                for (int i = 0; i < pixels.length; i++) {
                    pixelData[i] = (byte) pixels[i];
                }
                pixelDataList.add(pixelData);
                bitsAllocated = 8;
                bitsStored = 8;
                highBit = 7;
                pixelRepresentation = 0;
            } else if (dataBuffer instanceof DataBufferShort || dataBuffer instanceof DataBufferUShort) {
                int dataSize = width * height * numBands;
                short[] pixelData = new short[dataSize];
                int[] pixels = sampleModel.getPixels(0, 0, width, height, (int[]) null, dataBuffer);
                for (int i = 0; i < pixels.length; i++) {
                    pixelData[i] = (short) pixels[i];
                }
                byte[] bytePixelData = toByteArray(pixelData);
                pixelDataList.add(bytePixelData);
                bitsAllocated = 16;
                bitsStored = 16;
                highBit = 15;
                pixelRepresentation = (dataBuffer instanceof DataBufferShort) ? 1 : 0;
            } else {
                throw new DicomException("Unsupported pixel data format");
            }
        }

        // Combine all pixel data
        int totalPixelDataSize = pixelDataList.stream().mapToInt(byteArray -> byteArray.length).sum();
        byte[] allPixelData = new byte[totalPixelDataSize];
        int offset = 0;
        for (byte[] pixelData : pixelDataList) {
            System.arraycopy(pixelData, 0, allPixelData, offset, pixelData.length);
            offset += pixelData.length;
        }

        // Create the PixelData attribute
        OtherByteAttribute pixelDataAttribute = new OtherByteAttribute(TagFromName.PixelData);
        pixelDataAttribute.setValues(allPixelData);

        // Initialize or update the attribute list
        if (attributeList == null) {
            attributeList = new AttributeList();
        }

        // Add or update DICOM attributes
        attributeList.put(pixelDataAttribute);

        UnsignedShortAttribute bitsAllocatedAttribute = new UnsignedShortAttribute(TagFromName.BitsAllocated);
        bitsAllocatedAttribute.addValue(bitsAllocated);
        attributeList.put(bitsAllocatedAttribute);

        UnsignedShortAttribute bitsStoredAttribute = new UnsignedShortAttribute(TagFromName.BitsStored);
        bitsStoredAttribute.addValue(bitsStored);
        attributeList.put(bitsStoredAttribute);

        UnsignedShortAttribute highBitAttribute = new UnsignedShortAttribute(TagFromName.HighBit);
        highBitAttribute.addValue(highBit);
        attributeList.put(highBitAttribute);

        UnsignedShortAttribute pixelRepresentationAttribute = new UnsignedShortAttribute(TagFromName.PixelRepresentation);
        pixelRepresentationAttribute.addValue(pixelRepresentation);
        attributeList.put(pixelRepresentationAttribute);

        CodeStringAttribute photometricInterpretationAttribute = new CodeStringAttribute(TagFromName.PhotometricInterpretation);
        photometricInterpretationAttribute.addValue(photometricInterpretation);
        attributeList.put(photometricInterpretationAttribute);

        UnsignedShortAttribute rowsAttribute = new UnsignedShortAttribute(TagFromName.Rows);
        rowsAttribute.addValue(totalHeight);
        attributeList.put(rowsAttribute);

        UnsignedShortAttribute columnsAttribute = new UnsignedShortAttribute(TagFromName.Columns);
        columnsAttribute.addValue(totalWidth);
        attributeList.put(columnsAttribute);

        UnsignedShortAttribute samplesPerPixelAttribute = new UnsignedShortAttribute(TagFromName.SamplesPerPixel);
        samplesPerPixelAttribute.addValue(samplesPerPixel);
        attributeList.put(samplesPerPixelAttribute);

        // Add the NumberOfFrames attribute
        if (numberOfFrames > 1) {
            IntegerStringAttribute numberOfFramesAttribute = new IntegerStringAttribute(TagFromName.NumberOfFrames);
            numberOfFramesAttribute.addValue(numberOfFrames);
            attributeList.put(numberOfFramesAttribute);
        }

        return attributeList;
    }




    public static AttributeList generateDICOMPixelModuleFromMatList(List<Mat> matList, AttributeList attributeList) throws DicomException {

        if (matList == null || matList.isEmpty()) {
            throw new IllegalArgumentException("Mat list is empty");
        }

        int numberOfFrames = matList.size();
        int totalWidth = 0;
        int totalHeight = 0;
        String photometricInterpretation = null;
        int samplesPerPixel = 0;
        int bitsAllocated = 0;
        int bitsStored = 0;
        int highBit = 0;
        int pixelRepresentation = 0;
        int planarConfiguration = -1;

        // Collect pixel data from all mats
        List<byte[]> pixelDataList = new ArrayList<>();

        for (Mat mat : matList) {
            if (mat.empty()) {
                throw new DicomException("Unrecognized image data in Mat");
            }

            int width = mat.cols();
            int height = mat.rows();
            totalWidth = width;
            totalHeight = height;

            int type = mat.type();
            int numChannels = mat.channels();

            // Set photometric interpretation based on the number of channels
            if (photometricInterpretation == null) {

                photometricInterpretation = (numChannels == 3) ? "RGB" : (numChannels == 1) ? "MONOCHROME2" : "";
                samplesPerPixel = numChannels;

            }

            if (planarConfiguration == -1) {
                if (numChannels == 1) {
                    planarConfiguration = 0; // Not applicable for single-channel, but DICOM expects a value
                }
                else if (numChannels >= 3) {
                    planarConfiguration = 0; // 0 means color-by-pixel (RGB RGB RGB...)
                }
            }

            byte[] pixelData;


            //logger.info("mat type: {}, width: {}, height: {}, channels: {}", CvType.typeToString(type), width, height, numChannels);


            // Handle different types of Mats
            if (type == CvType.CV_8UC1 || type == CvType.CV_8UC3) { // 8-bit unsigned images
                pixelData = new byte[(int) (mat.total() * mat.channels())];
                mat.get(0, 0, pixelData);
                pixelDataList.add(pixelData);
                bitsAllocated = 8;
                bitsStored = 8;
                highBit = 7;
                pixelRepresentation = 0;
            }
            else if (type == CvType.CV_16UC1) { // 16-bit unsigned single-channel

                //logger.info("Processing 16-bit unsigned single-channel Mat with size: {}x{}", width, height);


                short[] shortPixelData = new short[(int) mat.total()];
                mat.get(0, 0, shortPixelData);

                // Convert shorts to byte array (little-endian)
                pixelData = new byte[shortPixelData.length * 2];
                ByteBuffer byteBuffer = ByteBuffer.wrap(pixelData);
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

                for (short value : shortPixelData) {
                    byteBuffer.putShort(value);
                }

                pixelDataList.add(pixelData);
                bitsAllocated = 16;
                bitsStored = 16;
                highBit = 15;
                pixelRepresentation = 0; // Unsigned 16-bit
            }
            else if (type == CvType.CV_16UC3) { // 16-bit unsigned 3-channel
                short[] shortPixelData = new short[(int) (mat.total() * 3)];
                mat.get(0, 0, shortPixelData);

                // Convert shorts to byte array (little-endian)
                pixelData = new byte[shortPixelData.length * 2];
                ByteBuffer byteBuffer = ByteBuffer.wrap(pixelData);
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

                for (short value : shortPixelData) {
                    byteBuffer.putShort(value);
                }

                pixelDataList.add(pixelData);
                bitsAllocated = 16;
                bitsStored = 16;
                highBit = 15;
                pixelRepresentation = 0; // Unsigned 16-bit
            }
            else if (type == CvType.CV_16SC1) { // 16-bit signed single-channel
                short[] shortPixelData = new short[(int) mat.total()];
                mat.get(0, 0, shortPixelData);

                // Convert shorts to byte array (little-endian)
                pixelData = new byte[shortPixelData.length * 2];
                ByteBuffer byteBuffer = ByteBuffer.wrap(pixelData);
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

                for (short value : shortPixelData) {
                    byteBuffer.putShort(value);
                }

                pixelDataList.add(pixelData);
                bitsAllocated = 16;
                bitsStored = 16;
                highBit = 15;
                pixelRepresentation = 1; // Signed 16-bit
            }
            else if (type == CvType.CV_16SC3) { // 16-bit signed 3-channel
                short[] shortPixelData = new short[(int) (mat.total() * 3)];
                mat.get(0, 0, shortPixelData);

                // Convert shorts to byte array (little-endian)
                pixelData = new byte[shortPixelData.length * 2];
                ByteBuffer byteBuffer = ByteBuffer.wrap(pixelData);
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

                for (short value : shortPixelData) {
                    byteBuffer.putShort(value);
                }

                pixelDataList.add(pixelData);
                bitsAllocated = 16;
                bitsStored = 16;
                highBit = 15;
                pixelRepresentation = 1; // Signed 16-bit
            }
            else {
                // Log the unsupported Mat type
                logger.info("Unsupported Mat type: {}", CvType.typeToString(type));
                throw new DicomException("Unsupported Mat pixel data format");
            }
        }

        // Combine all pixel data
        int totalPixelDataSize = pixelDataList.stream().mapToInt(byteArray -> byteArray.length).sum();
        byte[] allPixelData = new byte[totalPixelDataSize];
        int offset = 0;
        for (byte[] pixelData : pixelDataList) {
            System.arraycopy(pixelData, 0, allPixelData, offset, pixelData.length);
            offset += pixelData.length;
        }

        // Create the PixelData attribute
        OtherByteAttribute pixelDataAttribute = new OtherByteAttribute(TagFromName.PixelData);
        pixelDataAttribute.setValues(allPixelData);

        // Initialize or update the attribute list
        if (attributeList == null) {
            attributeList = new AttributeList();
        }

        // Add or update DICOM attributes
        attributeList.put(pixelDataAttribute);

        UnsignedShortAttribute bitsAllocatedAttribute = new UnsignedShortAttribute(TagFromName.BitsAllocated);
        bitsAllocatedAttribute.addValue(bitsAllocated);
        attributeList.put(bitsAllocatedAttribute);

        UnsignedShortAttribute bitsStoredAttribute = new UnsignedShortAttribute(TagFromName.BitsStored);
        bitsStoredAttribute.addValue(bitsStored);
        attributeList.put(bitsStoredAttribute);

        UnsignedShortAttribute highBitAttribute = new UnsignedShortAttribute(TagFromName.HighBit);
        highBitAttribute.addValue(highBit);
        attributeList.put(highBitAttribute);

        UnsignedShortAttribute pixelRepresentationAttribute = new UnsignedShortAttribute(TagFromName.PixelRepresentation);
        pixelRepresentationAttribute.addValue(pixelRepresentation);
        attributeList.put(pixelRepresentationAttribute);

        UnsignedShortAttribute planarConfigurationAttribute = new UnsignedShortAttribute(TagFromName.PlanarConfiguration);
        planarConfigurationAttribute.addValue(planarConfiguration);
        attributeList.put(planarConfigurationAttribute);

        CodeStringAttribute photometricInterpretationAttribute = new CodeStringAttribute(TagFromName.PhotometricInterpretation);
        photometricInterpretationAttribute.addValue(photometricInterpretation);
        attributeList.put(photometricInterpretationAttribute);

        UnsignedShortAttribute rowsAttribute = new UnsignedShortAttribute(TagFromName.Rows);
        rowsAttribute.addValue(totalHeight);
        attributeList.put(rowsAttribute);

        UnsignedShortAttribute columnsAttribute = new UnsignedShortAttribute(TagFromName.Columns);
        columnsAttribute.addValue(totalWidth);
        attributeList.put(columnsAttribute);

        UnsignedShortAttribute samplesPerPixelAttribute = new UnsignedShortAttribute(TagFromName.SamplesPerPixel);
        samplesPerPixelAttribute.addValue(samplesPerPixel);
        attributeList.put(samplesPerPixelAttribute);

        // Add the NumberOfFrames attribute
        if (numberOfFrames > 1) {
            IntegerStringAttribute numberOfFramesAttribute = new IntegerStringAttribute(TagFromName.NumberOfFrames);
            numberOfFramesAttribute.addValue(numberOfFrames);
            attributeList.put(numberOfFramesAttribute);
        }

        return attributeList;
    }

    // Helper method to convert short array to byte array with proper byte ordering
    private static byte[] toByteArray(short[] shortArray) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(shortArray.length * 2);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN); // DICOM uses little-endian for pixel data

        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
        shortBuffer.put(shortArray);

        return byteBuffer.array();
    }

   /* // Helper method to convert short[] to byte[]
    private static byte[] toByteArray(short[] shortArray) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(shortArray.length * 2);
        byteBuffer.asShortBuffer().put(shortArray);
        return byteBuffer.array();
    }*/



    // Helper methods to avoid repetition
    private void addUniqueIdentifierAttribute(AttributeList list, AttributeTag tag, String value) throws DicomException {
        UniqueIdentifierAttribute attr = new UniqueIdentifierAttribute(tag);
        attr.addValue(value);
        list.put(attr);
    }

    private void addPersonNameAttribute(AttributeList list, AttributeTag tag, String value) throws DicomException {
        PersonNameAttribute attr = new PersonNameAttribute(tag);
        attr.addValue(value);
        list.put(attr);
    }

    private void addLongStringAttribute(AttributeList list, AttributeTag tag, String value) throws DicomException {
        LongStringAttribute attr = new LongStringAttribute(tag);
        attr.addValue(value);
        list.put(attr);
    }

    private void addDateAttribute(AttributeList list, AttributeTag tag, String value) throws DicomException {
        DateAttribute attr = new DateAttribute(tag);
        attr.addValue(value);
        list.put(attr);
    }

    private void addCodeStringAttribute(AttributeList list, AttributeTag tag, String value) throws DicomException {
        CodeStringAttribute attr = new CodeStringAttribute(tag);
        attr.addValue(value);
        list.put(attr);
    }

    private void addShortStringAttribute(AttributeList list, AttributeTag tag, String value) throws DicomException {
        ShortStringAttribute attr = new ShortStringAttribute(tag);
        attr.addValue(value);
        list.put(attr);
    }

    private void addIntegerStringAttribute(AttributeList list, AttributeTag tag, String value) throws DicomException {
        IntegerStringAttribute attr = new IntegerStringAttribute(tag);
        attr.addValue(value);
        list.put(attr);
    }

    private void addDecimalStringAttribute(AttributeList list, AttributeTag tag, String value) throws DicomException {
        DecimalStringAttribute attr = new DecimalStringAttribute(tag);
        attr.addValue(value);
        list.put(attr);
    }

    private void addTimeAttribute(AttributeList list, AttributeTag tag, String value) throws DicomException {
        TimeAttribute attr = new TimeAttribute(tag);
        attr.addValue(value);
        list.put(attr);
    }

    private void addImageTypeAttributes(AttributeList list) throws DicomException {
        CodeStringAttribute imageType = new CodeStringAttribute(TagFromName.ImageType);
        imageType.addValue("DERIVED");
        imageType.addValue("PRIMARY");
        list.put(imageType);
    }

    private void addMultiframeSecondaryCaptureAttributes(AttributeList list) throws DicomException {
        addCodeStringAttribute(list, TagFromName.PresentationLUTShape, "IDENTITY");
        addDecimalStringAttribute(list, TagFromName.RescaleSlope, "1");
        addDecimalStringAttribute(list, TagFromName.RescaleIntercept, "0");
        addLongStringAttribute(list, TagFromName.RescaleType, "US");
    }

    private void addMultiFrameAttributes(AttributeList list, int numberOfFrames) throws DicomException {
        AttributeTagAttribute framePointer = new AttributeTagAttribute(TagFromName.FrameIncrementPointer);
        framePointer.addValue(TagFromName.PageNumberVector);
        list.put(framePointer);

        IntegerStringAttribute pageNumberVector = new IntegerStringAttribute(TagFromName.PageNumberVector);
        for (int i = 1; i <= numberOfFrames; i++) {
            pageNumberVector.addValue(i);
        }
        list.put(pageNumberVector);
    }

}
