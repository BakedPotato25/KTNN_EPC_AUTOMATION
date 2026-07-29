package com.ktnn.utils.configloader;

import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Đọc Json Data
 */

@Slf4j
public class JsonUtils {
    @Getter
    private static final JsonUtils instance = new JsonUtils();

    private JsonUtils() {
    }

    /**
     * Đọc data từ Json file
     *
     * @param filePath : đường dẫn Json file
     */
    public static String readDataFromJsonFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error(String.format("Exception occurred - Reading json file:%s\n%s ", filePath, e.getMessage()));
            throw new RuntimeException(e);
        }
        return "";
    }

    /**
     * Đọc data từ resource
     *
     * @param filePath : đường dẫn file
     */
    public static String readFileDataFromResource(String filePath) {
        try {
            log.info("Read configuration data from: {}", filePath);
            return ResourceReader.readDataFromResource(filePath);
        } catch (IOException e) {
            log.error("Error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Đọc data từ XLS file rồi trả về Data Object
     *
     * @param tcName : tên TC
     * @return Data Provider Object
     */
    public List<Hashtable<String, Object>> readDataDrivenFromJSON(String jsonFilePath, String tcName) {
        List<Hashtable<String, Object>> dataListMap = new ArrayList<>();

        try {
            String content = readFileDataFromResource(jsonFilePath);
            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(content));
            var maps = (List<HashMap<String, Object>>) jsonObject.toMap().get(tcName);
            maps.forEach(data -> {
                Hashtable<String, Object> rowDataMap = new Hashtable<>();
                data.forEach((key, value) -> rowDataMap.put(key, DataModel.builder().devName(key).value(String.valueOf(value)).build()));
                dataListMap.add(rowDataMap);
            });
        } catch (Exception e) {
            log.error("VException: " + e.getMessage());
        }
        return dataListMap;
    }

    /**
     * Đọc data từ XLS file rồi trả về Data Object
     *
     * @param tcName : tên TC
     * @return Data Provider Object
     */
    public List<Hashtable<String, Object>> readDataTestFromJSON(String jsonFilePath, String tcName) {
        List<Hashtable<String, Object>> dataListMap = new ArrayList<>();

        HashMap<String, List<String>> configMaps = new HashMap<>();
        try {
            String content = readFileDataFromResource(jsonFilePath);
            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(content));
            var maps = (List<HashMap<String, Object>>) jsonObject.toMap().get(tcName);
            maps.forEach(data -> {
                Hashtable<String, Object> rowDataMap = new Hashtable<>();

                // Xử lý Master Data
                var masterData = (HashMap<String, Object>) data.get("master");
                var masterConfig = (HashMap<String, Object>) masterData.get("config");
                masterConfig.keySet().forEach(k -> configMaps.put(String.valueOf(k), (List<String>) masterConfig.get(k)));

                masterData.keySet().forEach(key -> {
                    if (key.equals("config")) return;
                    String value = String.valueOf(masterData.get(key));
                    rowDataMap.put(key, buildDataModel(value, configMaps, key));
                });

                // Xử lý Detail Data
                HashMap<String, Object> detailData = (HashMap<String, Object>) data.get("detail");
                if (Objects.nonNull(detailData)) {
                    detailData.keySet().forEach(key -> {
                        var details = (List<HashMap<String, Object>>) detailData.get(key);
                        List<Hashtable<String, Object>> rowDetailData = new ArrayList<>();
                        for (var item : details) {
                            Hashtable<String, Object> rowDetailDataMap = new Hashtable<>();
                            configMaps.clear();
                            var detailConfig = (HashMap<String, Object>) item.get("config");
                            detailConfig.keySet().forEach(k -> configMaps.put(String.valueOf(k), (List<String>) detailConfig.get(k)));

                            item.keySet().forEach(keyDetail -> {
                                var modelDataJson = (HashMap<String, Object>) item.get(keyDetail);
                                rowDetailDataMap.put(String.valueOf(keyDetail), buildDataModel(modelDataJson, configMaps, String.valueOf(keyDetail)));
                            });
                            // Thêm vào list cho từng detail
                            if (!rowDetailDataMap.isEmpty()) rowDetailData.add(rowDetailDataMap);
                        }
                        rowDataMap.put(String.valueOf(key), rowDetailData);
                    });
                }
                dataListMap.add(rowDataMap);
            });
        } catch (Exception e) {
            log.error("VException: " + e.getMessage());
        }
        return dataListMap;
    }

    /**
     * Build data model
     *
     * @param data       : HashMap data cần đổi
     * @param configMaps : Config chung
     * @param keyName    : Key name / Dev Name
     */
    private DataModel buildDataModel(HashMap<String, Object> data, HashMap<String, List<String>> configMaps, String keyName) {
        return DataModel.builder()
                .devName(String.valueOf(keyName))
                .value(Objects.nonNull(data.get("value")) ? String.valueOf(data.get("value")) : null)
                .description(Objects.nonNull(data.get("des")) ? String.valueOf(data.get("des")) : null)
                .fill(checkIsExist(configMaps.get("isFill"), String.valueOf(keyName)))
                .verify(checkIsExist(configMaps.get("isVerify"), String.valueOf(keyName)))
                .build();
    }

    /**
     * Build data model
     *
     * @param value      : HashMap data cần đổi
     * @param configMaps : Config chung
     * @param keyName    : Key name / Dev Name
     */
    private DataModel buildDataModel(String value, HashMap<String, List<String>> configMaps, String keyName) {
        return DataModel.builder()
                .devName(String.valueOf(keyName))
                .value(value)
                .fill(checkIsExist(configMaps.get("isFill"), String.valueOf(keyName)))
                .verify(checkIsExist(configMaps.get("isVerify"), String.valueOf(keyName)))
                .build();
    }

    /**
     * Đọc data từ json file
     *
     * @param filePath : đường dẫn file
     * @return : một JSONObject
     */
    public JSONObject readContentFromJsonFile(String filePath) {
        try {
            File file = new File(filePath);
            String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            return new JSONObject(content);
        } catch (Exception e) {
            System.out.println("Error reading : " + e.getMessage());
        }
        return null;
    }

    private boolean checkIsExist(List<String> colList, String colName) {
        boolean isResult = colList.stream().noneMatch(v -> v.equalsIgnoreCase(String.format("^%s", "ALL")));
        // Chứa ^ALL -> Not Fill ALL

        // Chứa ALL  -> Fill ALL
        if (colList.stream().anyMatch(v -> v.equalsIgnoreCase("ALL"))) isResult = true;

        // Chứa ^Col -> Not fill this col
        if (colList.stream().anyMatch(v -> v.equalsIgnoreCase(String.format("^%s", colName)))) {
            isResult = false;
        }
        // Chứa Col -> Fill this column
        if (colList.stream().anyMatch(v -> v.equalsIgnoreCase(colName))) isResult = true;
        return isResult;
    }
}
