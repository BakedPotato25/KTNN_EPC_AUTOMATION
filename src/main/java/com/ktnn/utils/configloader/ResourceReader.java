package com.ktnn.utils.configloader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

public class ResourceReader {

    /**
     * Đọc data từ resource file
     *
     * @param filePath : đường dẫn file trong resources
     * @return nội dung file dạng String
     * @throws IOException nếu lỗi I/O
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
     * Load tất cả config file bằng ClassLoader
     *
     * @param filePaths : danh sách đường dẫn file trong resources
     * @return Properties object chứa toàn bộ property đã load
     * @throws IOException nếu lỗi I/O
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
