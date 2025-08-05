package com.raymedis.rxviewui.service.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonService {

    public static <T> List<T> readJson(File file, Class<T> elementType) {
        if (file == null || !file.exists() || !file.isFile()) {
            logger.error("Invalid file: {}", file != null ? file.getAbsolutePath() : "null");
            return new ArrayList<>();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Try to read as a List first
            return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, elementType));
        } catch (IOException e) {
            try {
                // If it's a single object, wrap it in a List
                T singleObject = objectMapper.readValue(file, elementType);
                List<T> list = new ArrayList<>();
                list.add(singleObject);
                return list;
            } catch (IOException ex) {
                logger.error("Error reading JSON file: {}", ex.getMessage());
            }
        }
        return new ArrayList<>();
    }

    private static final Logger logger = LoggerFactory.getLogger(JsonService.class);
    public static void writeJson(List<Object> objects, String filePath) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convert List to JSON array and save to file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), objects);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.error("JSON array file created: {}", filePath);
    }



}
