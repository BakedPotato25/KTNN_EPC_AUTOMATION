package com.ktnn.datadriven;



import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

@Slf4j
public class BaseProvider {

    /**
     * Merge 2 object to one object
     *
     */
    public static <T> T mergeObjects(T first, T second) {
        Class<?> clas = first.getClass();
        Field[] fields = clas.getDeclaredFields();
        Object result = null;
        try {
            result = clas.getDeclaredConstructor().newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value1 = field.get(first);
                Object value2 = field.get(second);
                Object value = (value1 != null) ? value1 : value2;
                field.set(result, value);
            }
        } catch (Exception e) {
            log.error("Unable to merge 2 objects {}", e.getMessage());
        }
        return (T) result;
    }

    protected <T> void updateDataModel(T className, List<Hashtable<String, Object>> dataList, boolean isFirst) {
        List<Object> finalResult = new ArrayList<>();
        dataList.forEach(data -> {
            Field[] fields = className.getClass().getFields();
            Arrays.stream(fields).forEach(f -> {
                try {
                    Object obj = f.get(className);
                    if (f.getType().equals(List.class)) {
                        List shData = (List) data.get(f.getName());
                        if (Objects.isNull(shData) || shData.isEmpty()) return;
                        String ownerClassName = ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0].getTypeName();
                        Object itemClass = Class.forName(ownerClassName).getDeclaredConstructor().newInstance();

                        var dataDrivenList = (List) data.get(f.getName());
                        updateDataModel(itemClass, dataDrivenList, false);
                        data.put(f.getName(), dataDrivenList);
                        f.set(className, dataDrivenList);
                    } else {
                        DataModel model = (DataModel) obj;
                        Object newModel = mergeObjects(data.get(model.getDevName()), model);
                        f.set(className, newModel);
                        data.put(model.getDevName(), newModel);
                    }
                } catch (Exception e) {
                    log.error("Unable to read data from DataDriven - convert XLS to Object {}", e.getMessage());
                }
            });
            if (isFirst) {
                finalResult.add(className);
            }
        });

        // Return Form
        Object[][] data;
        if (isFirst) {
            data = new Object[finalResult.size()][1];
            for (int i = 0; i < finalResult.size(); i++) {
                data[i][0] = finalResult.get(i);
            }
        }
    }

    /**
     * Return data test with full Object model
     */

    protected <T> Object[][] updateDataModel(T className, List<Hashtable<String, Object>> dataList) {
        List<Object> results = updateModel(className, dataList);
        Object[][] data = new Object[results.size()][1];
        for (int i = 0; i < results.size(); i++) {
            data[i][0] = results.get(i);
        }
        return data;
    }

    /**
     * Update model with data driven
     *
     * @param className : Class name
     * @param dataList  : Data driven
     * @param <T>       : Generic
     * @return : List of object
     */
    private <T> List<Object> updateModel(T className, List<Hashtable<String, Object>> dataList) {
        List<Object> finalResult = new ArrayList<>();
        dataList.forEach(data -> {

            try {
                Object item = className.getClass().getDeclaredConstructor().newInstance();
                Field[] fields = item.getClass().getFields();
                Arrays.stream(fields).forEach(f -> {
                    try {
                        var obj = f.get(item);
                        if (f.getType().equals(List.class)) {
                            var shData = (List) data.get(f.getName()); // sheet name must match field name
                            if (Objects.isNull(shData) || shData.isEmpty()) return;
                            String ownerClassName = ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0].getTypeName();
                            Object itemClass = Class.forName(ownerClassName).getDeclaredConstructor().newInstance();

                            var dataDrivenList = (List) data.get(f.getName());
                            f.set(item, updateModel(itemClass, dataDrivenList));
                            data.put(f.getName(), dataDrivenList);
                        } else {
                            var model = (DataModel) obj;
                            DataModel newModel = (DataModel) mergeObjects(data.get(model.getDevName()), model);
                            if (Objects.nonNull(newModel.getValue())) {
                                f.set(item, newModel);
                                data.put(model.getDevName(), newModel);
                            }
                        }
                    } catch (Exception e) {
                         //log.error("Unable to read data from DataDriven - Convert XLS to Object " + e.getMessage());
                    }
                });
                finalResult.add(item);
            } catch (Exception e) {
                //log.info("updateModel : Unable to read data from DataDriven - Convert XLS to Object " + e.getMessage());
            }
        });

        return finalResult;
    }
}
