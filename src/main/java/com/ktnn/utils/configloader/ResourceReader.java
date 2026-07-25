package com.ktnn.utils.configloader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

public class ResourceReader {

    /**
     * Read data from a resource file
     *
     * @param filePath : file path in the resources directory
     * @return content of the file as a String
     * @throws IOException if an I/O error occurs
     */
    public static String readDataFromResource(String filePath) throws IOException {
        ClassLoader classLoader = ResourceReader.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + filePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Load all configuration files using ClassLoader
     *
     * @param filePaths : List of file paths in the resources directory
     * @return Properties object containing all the loaded properties
     * @throws IOException if an I/O error occurs
     */
    public static Properties loadAllConfigFiles(List<String> filePaths) throws IOException {
        Properties properties = new Properties();
        ClassLoader classLoader = ResourceReader.class.getClassLoader();

        for (String filePath : filePaths) {
            try (InputStream inputStream = classLoader.getResourceAsStream(filePath)) {
                if (inputStream == null) {
                    throw new IOException("Resource not found: " + filePath);
                }
                properties.load(inputStream);
            }
        }

        return properties;
    }
}
