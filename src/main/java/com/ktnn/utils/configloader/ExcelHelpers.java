package com.ktnn.utils.configloader;


import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.testng.util.Strings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ktnn.consts.FrameConst.DataConfig.CONFIG_COL;
import static com.ktnn.consts.FrameConst.DataConfig.DATA_ID_COL;


/**
 * ExcelHelpers class provides the ability to read data from Excel file
 */
@Slf4j
public class ExcelHelpers {
    private static final DecimalFormat df = new DecimalFormat("0.000");

    @Getter
    private static final ExcelHelpers instance = new ExcelHelpers();
    private FileInputStream fis;
    private FileOutputStream fileOut;
    private Workbook actualWB;
    private Sheet sh;
    private Cell cell;
    private Row row;
    private String excelFilePath;
    private Map<String, Integer> columnMapper = new HashMap<>();

    private ExcelHelpers() {
    }

    //    Set Excel file
    public void setExcelFile(String excelPath, String sheetName) {
        try {
            File f = new File(excelPath);
            fileValidation(f, sheetName);

            fis = new FileInputStream(excelPath);
            actualWB = WorkbookFactory.create(fis);
            sh = actualWB.getSheet(sheetName);

            if (sh == null) {
                log.info("setExcelFile: Sheet name not found.");
                throw new RuntimeException("Sheet name not found.");
            }

            excelFilePath = excelPath;

            //adding all the column header names to the map 'columns'
            sh.getRow(0).forEach(cell -> columnMapper.put(cell.getStringCellValue(), cell.getColumnIndex()));
        } catch (Exception e) {
            log.info("setExcelFile: failed {}", e.getMessage());
        }
    }

    /**
     * Get the data from the Excel file
     *
     * @param excelPath : The Excel file path
     * @param sheetName : The sheet name
     * @return The data object
     */
    public Object[][] getDataArray(String excelPath, String sheetName, int startCol, int totalCols) {
        Object[][] data = null;
        try {
            File f = new File(excelPath);
            fileValidation(f, sheetName);

            fis = new FileInputStream(excelPath);
            actualWB = new XSSFWorkbook(fis);
            sh = actualWB.getSheet(sheetName);

            if (sh == null) {
                log.error("setExcelFile: Sheet name not found.");
                throw new RuntimeException("Sheet name not found.");
            }

            int noOfRows = sh.getLastRowNum();
            int noOfCols = totalCols + 1;

            data = new String[noOfRows - 1][noOfCols - startCol];
            for (int i = 1; i < noOfRows; i++) {
                for (int j = 0; j < noOfCols - startCol; j++) {
                    data[i - 1][j] = getCellData(i, j + startCol);
                    System.out.println(data[i - 1][j]);
                }
            }
        } catch (Exception e) {
            log.error("getDataArray: failed {}", e.getMessage());
        }
        return data;
    }

    /**
     * Get the data from the Excel file
     *
     * @param filePath  : The Excel file path
     * @param sheetName : The sheet name
     * @return The data object
     */
    public Object[][] getTableArray(String filePath, String sheetName, int iTestCaseRow) {
        String[][] tabArray = null;

        try {
            FileInputStream ExcelFile = new FileInputStream(filePath);

            // Access the required test data sheet
            actualWB = new XSSFWorkbook(ExcelFile);
            sh = actualWB.getSheet(sheetName);

            int startCol = 1;
            int ci = 0;
            int cj = 0;
            int totalRows = 1;
            int totalCols = 2;

            tabArray = new String[totalRows][totalCols];

            for (int j = startCol; j <= totalCols; j++, cj++) {
                tabArray[ci][cj] = getCellData(iTestCaseRow, j);
            }

        } catch (IOException e) {
            log.error("Could not read the Excel sheet: " + e.getMessage());
        }
        return (tabArray);
    }


    public Object[][] getDataHashTable(String excelPath, String sheetName, int startRow) {
        Object[][] data = null;
        try {
            File f = new File(excelPath);
            fileValidation(f, sheetName);

            fis = new FileInputStream(excelPath);
            actualWB = new XSSFWorkbook(fis);
            sh = actualWB.getSheet(sheetName);


            int endRow = getRowCount();
            int columns = getColumnCount();
            data = new Object[endRow - startRow][1];
            Hashtable<String, String> rowData;
            for (int rowNums = startRow; rowNums < endRow; rowNums++) {
                rowData = new Hashtable<>();
                // Validate and don't accept the testing data with an empty testcaseId
                if (getCellData(rowNums, 0).isEmpty()) continue;
                for (int colNum = 0; colNum < columns; colNum++) {
                    rowData.put(getCellData(0, colNum).toLowerCase(), getCellData(rowNums, colNum));
                }
                data[rowNums - startRow][0] = rowData;
            }

        } catch (Exception e) {
            log.error("getDataHashTable: failed " + e.getMessage());
        }
        return data;
    }

    public Object[][] vinGetMasterData(String excelPath, String sheetName, String tcName, int startRow) {
        Object[][] data = null;
        try {
            File f = new File(excelPath);
            fileValidation(f, sheetName);

            fis = new FileInputStream(excelPath);
            actualWB = new XSSFWorkbook(fis);
            sh = actualWB.getSheet(sheetName);

            int endRow = getRowCount();
            int columns = getColumnCount();

            List<Hashtable<String, String>> rowDataList = new ArrayList<>();
            for (int rowNums = startRow; rowNums < endRow; rowNums++) {
                // Validate and don't accept the testing data with an empty testcaseId
                String value = getCellData(rowNums, 0);
                if (Objects.nonNull(value) && value.equalsIgnoreCase(tcName)) {
                    Hashtable<String, String> rowData = new Hashtable<>();
                    for (int colNum = 0; colNum < columns; colNum++) {
                        rowData.put(getCellData(0, colNum).trim().toLowerCase(), getCellData(rowNums, colNum).trim());
                    }
                    rowDataList.add(rowData);
                }
            }

            data = new Object[rowDataList.size()][1];
            for (int i = 0; i < rowDataList.size(); i++) {
                data[i][0] = rowDataList.get(i);
            }

        } catch (Exception e) {
            log.error("vinGetMasterData: failed " + e.getMessage());
        }
        return data;
    }

    /**
     * Read data from XLS file then return Data Object
     *
     * @param excelPath       : The XLS file path
     * @param masterSheetName : The master sheet name
     * @param tcName          : TC Name
     * @param startRow        : Start from row
     * @return Data Provider Object
     */
    public List<Hashtable<String, Object>> vinGetDataDrivenFromXLS(String excelPath, String masterSheetName, String tcName,
                                                                   Integer startRow) {
        List<Hashtable<String, Object>> dataListMap = null;
        try {
            File f = new File(excelPath);
            fileValidation(f, masterSheetName);

            fis = new FileInputStream(excelPath);
            actualWB = new XSSFWorkbook(fis);
            sh = actualWB.getSheet(masterSheetName);

            int endRow = getRowCount();
            int columns = getColumnCount();
            if (Objects.isNull(startRow)) startRow = 2;

            // Read header
            String[] headers = new String[columns];
            int configIndex = -1;
            for (int i = 0; i < columns; i++) {
                headers[i] = getCellData(0, i).trim();
                if (headers[i].equalsIgnoreCase(CONFIG_COL)) {
                    configIndex = i;
                }
            }

            List<List<DataModel>> dataList = new ArrayList<>();
            dataListMap = new ArrayList<>();
            for (int row = startRow; row < endRow; row++) {
                // Validate tcName = Cell (row, 0)
                String value = getCellData(row, 0);
                if (Objects.nonNull(value) && value.equalsIgnoreCase(tcName)) {
                    List<DataModel> rowData = new ArrayList<>();
                    Hashtable<String, Object> rowDataMap = new Hashtable<>();

                    // Get Config Value
                    DataModel configModel = new DataModel();
                    if (configIndex >= 0) {
                        String config = getCellData(row, configIndex);
                        configModel.setValue(config);
                        configModel.setDevName(CONFIG_COL);
                    }
                    rowData.add(configModel);
                    rowDataMap.put(configModel.getDevName(), configModel);

                    for (int col = 0; col < columns; col++) {
                        String val = Objects.nonNull(getCellData(row, col)) ? String.valueOf(getCellData(row, col)).trim() :
                                String.valueOf(getCellData(row, col));
                        DataModel dataModel = DataModel.builder().devName(headers[col]).description(getCellData(1, col))
                                .value(val).build();
                        rowData.add(dataModel);
                        rowDataMap.put(dataModel.getDevName(), dataModel);
                        // skip config
                    }
                    dataList.add(rowData);
                    dataListMap.add(rowDataMap);
                }
            }

            dataListMap = vinGetDetailDataDrivenFromXLS(actualWB, dataListMap);
        } catch (IOException e) {
            log.error("vinGetDataDrivenFromXLS: failed " + e.getMessage());
        }
        return dataListMap;
    }

    private void fileValidation(File f, String sheetName) {
        if (!f.exists()) {
            try {
                log.info("File Excel path not found.");
                throw new InvalidPathException(f.getPath(), "File Excel path not found.");
            } catch (Exception e) {
                log.error("File Validation: failed " + e.getMessage());
            }
        }

        if (Objects.isNull(sheetName) || sheetName.isEmpty()) {
            try {
                log.info("The Sheet Name is empty or null.");
                throw new InvalidPathException(f.getPath(), "The Sheet Name is empty or null.");
            } catch (Exception e) {
                log.error("File Validation: failed " + e.getMessage());
            }
        }
    }

    /**
     * Get detail data from master Sheet
     *
     * @param xlsFile : Data Driven File
     */
    public List<Hashtable<String, Object>> vinGetDetailDataDrivenFromXLS(Workbook xlsFile, List<Hashtable<String, Object>> masterData) {
        masterData.forEach(master -> {
            String dataIds = ((DataModel) master.get(DATA_ID_COL)).getValue();
            Set<String> sheets;
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(dataIds);
                sheets = jsonObject.keySet();
            } catch (Exception e) {
                log.info("Json Parse error " + e.getMessage());
                return;
            }

            sheets.forEach(sheetName -> {
                var jsonValue = jsonObject.getJSONArray(sheetName).toList();

                sh = xlsFile.getSheet(sheetName);
                if (Objects.isNull(sh)) return;

                int currentRow = 2;
                int endRow = getRowCount();
                int columnCount = getColumnCount();

                // collect headers first
                String[] headers = new String[columnCount];
                int dataIdIndex = -1;
                int configIndex = -1;
                for (int i = 0; i < columnCount; i++) {
                    headers[i] = getCellData(0, i);
                    if (headers[i].equalsIgnoreCase(DATA_ID_COL)) dataIdIndex = i;
                    else if (headers[i].equalsIgnoreCase(CONFIG_COL)) {
                        configIndex = i;
                    }
                }

                // Get DataID
                if (Objects.isNull(jsonValue) || jsonObject.isEmpty()) return;

                List<Hashtable<String, Object>> dataListMap = new ArrayList<>();
                do {
                    Hashtable<String, Object> rowDataMap = new Hashtable<>();

                    // Find the matching dataID
                    String dataValue = getCellData(currentRow, dataIdIndex);
                    if (Objects.nonNull(dataValue) && jsonValue.contains(dataValue)) {
                        // Get Config Value
                        DataModel configModel = new DataModel();
                        if (configIndex >= 0) {
                            String config = getCellData(currentRow, configIndex);
                            configModel.setValue(config);
                            configModel.setDevName(CONFIG_COL);
                        }
                        rowDataMap.put(configModel.getDevName(), configModel);

                        for (int col = 2; col < columnCount; col++) {
                            String val = Objects.nonNull(getCellData(currentRow, col)) ? String.valueOf(getCellData(currentRow, col)).trim() :
                                    String.valueOf(getCellData(currentRow, col));
                            var dataModel = DataModel.builder().description(getCellData(1, col)).devName(headers[col])
                                    .build();
                            rowDataMap.put(dataModel.getDevName(), dataModel);
                        }
                        dataListMap.add(rowDataMap);
                    }
                    currentRow++;
                } while (currentRow <= endRow);

                master.put(sheetName, dataListMap);

            });
        });
        return masterData;
    }

    /**
     * Return the status of column, TRUE if: empty or All or correct column names; FAIL: ^[column name] (^TK -> The TK column will return false)
     *
     * @param data      : Json data from config columns
     * @param fieldName : The type of keyword (fill or verify...)
     * @param colName   : Column Name - which had the data
     */
    private boolean isExisted(String data, String fieldName, String colName) {
        var colList = new JSONObject(data).getJSONArray(fieldName).toList();
        boolean isResult = true;
        // "^ALL" -> not fill ALL
        if (colList.stream().anyMatch(v -> ((String) v).equalsIgnoreCase(String.format("^%s", "ALL"))))
            isResult = false;

        // "ALL" -> fill ALL
        if (colList.stream().anyMatch(v -> ((String) v).equalsIgnoreCase("ALL"))) isResult = true;

        // "^Col" -> not fill this col
        if (colList.stream().anyMatch(v -> ((String) v).equalsIgnoreCase(String.format("^%s", colName)))) {
            isResult = false;
        }
        // "Col" -> fill this column
        if (colList.stream().anyMatch(v -> ((String) v).equalsIgnoreCase(colName))) isResult = true;
        return isResult;
    }

    public List<Object> getDetailDataHashTable(String excelPath, String sheetName, String testCase) {
        List<Object> data = null;
        try {
            File f = new File(excelPath);
            fileValidation(f, sheetName);

            fis = new FileInputStream(excelPath);
            actualWB = new XSSFWorkbook(fis);
            sh = actualWB.getSheet(sheetName);

            int currentRow = 1;
            int endRow = getRowCount();
            int columnCount = getColumnCount();
            String[] columns = new String[columnCount];
            // collect headers first
            for (int i = 0; i < columnCount; i++) {
                columns[i] = getCellData(0, i);
            }

            data = new ArrayList<>();
            // find row by testcase, read excel row sequentially until out of target testcase
            do {
                Hashtable<String, String> rowData = new Hashtable<>();
                // Validate and stop looting data if testcase doesn't match the target testcase

                String value = getCellData(currentRow, 0);
                if (Objects.nonNull(value) && value.equalsIgnoreCase(testCase)) {
                    for (int colNum = 1; colNum < columnCount; colNum++) {
                        if (Objects.nonNull(getCellData(currentRow, colNum)))
                            rowData.put(columns[colNum].trim().toLowerCase(), getCellData(currentRow, colNum).trim());
                    }
                    data.add(rowData);
                }

                // read cell in row and build row data
                currentRow++;
            } while (currentRow <= endRow);
        } catch (Exception e) {
            log.error("getDetailDataHashTable: failed " + e.getMessage());
        }
        return data;
    }

    public String getTestCaseName(String sTestCase) {
        String value = sTestCase;
        try {
            int posit = value.indexOf("@");
            value = value.substring(0, posit);
            posit = value.lastIndexOf(".");

            value = value.substring(posit + 1);
            return value;

        } catch (Exception e) {
            log.error("getTestCaseName: failed " + e.getMessage());
            throw (e);
        }
    }

    // Get cell data
    public String getCellData(int rowNum, int colNum) {
        try {
            cell = sh.getRow(rowNum).getCell(colNum);
            return getCellData(cell);
        } catch (Exception e) {
            return "";
        }
    }

    public int getRowCount() {
        return sh.getLastRowNum() + 1;
    }

    public int getColumnCount() {
        row = sh.getRow(0);
        return row.getLastCellNum();
    }

    // Write data to excel sheet
    public void setCellData(String text, int rowNumber, int colNumber) {
        try {
            row = sh.getRow(rowNumber);
            if (row == null) {
                row = sh.createRow(rowNumber);
            }
            cell = row.getCell(colNumber);

            if (cell == null) {
                cell = row.createCell(colNumber);
            }
            cell.setCellValue(text);

            XSSFCellStyle style = (XSSFCellStyle) actualWB.createCellStyle();
            if (Objects.equals(text, "pass") || Objects.equals(text, "passed") || Objects.equals(text, "Pass") ||
                    Objects.equals(text, "Passed")) {
                style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            } else {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
            }
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            cell.setCellStyle(style);

            fileOut = new FileOutputStream(excelFilePath);
            actualWB.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            log.error("setCellData: failed " + e.getMessage());
        }
    }

    public void setCellData(String text, int rowNum, String columnName) {
        try {
            row = sh.getRow(rowNum);
            if (row == null) {
                row = sh.createRow(rowNum);
            }
            cell = row.getCell(columnMapper.get(columnName));

            if (cell == null) {
                cell = row.createCell(columnMapper.get(columnName));
            }
            cell.setCellValue(text);

            XSSFCellStyle style = (XSSFCellStyle) actualWB.createCellStyle();
            if (Objects.equals(text, "pass") || Objects.equals(text, "passed") || Objects.equals(text, "Pass") || Objects.equals(text, "Passed")) {
                style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            } else {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
            }
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            cell.setCellStyle(style);

            fileOut = new FileOutputStream(excelFilePath);
            actualWB.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            log.error("setCellData: failed " + e.getMessage());
        }
    }

    public Hashtable<String, Object> verifyReportData(String excelPath, String reportTitle, String expReportPath, boolean isEmptyFile) {
        boolean isResult = false;
        Hashtable<String, Object> result = new Hashtable<>();
        Hashtable<Integer, List<String>> rowDataList = new Hashtable<>();
        List<String> failureMsg = new ArrayList<>();

        try {
            File f = new File(excelPath);
            if (!f.exists()) {
                log.info("File Excel path not found.");
                throw new RuntimeException("File Excel path not found.");
            }

            fis = new FileInputStream(excelPath);
            actualWB = new XSSFWorkbook(fis);
            sh = actualWB.getSheetAt(0);

            int endRow = getRowCount();
            // Add to handle file empty
            if (endRow == 0) {
                isResult = isEmptyFile;
                failureMsg.add("File is empty");
            } else {
                int columns = getColumnCount();

                boolean isHeader = true;
                for (int rowNums = 0; rowNums < endRow; rowNums++) {
                    List<String> rowData = new ArrayList<>();
                    for (int colNum = 0; colNum < columns; colNum++) {
                        String value = getCellData(rowNums, colNum).trim().toLowerCase();
                        if (isHeader & value.contains(reportTitle.toLowerCase())) {
                            isResult = true;
                        }
                        rowData.add(getCellData(rowNums, colNum).trim());
                    }
                    isHeader = rowNums < 9;
                    //Log.info(String.format("Row %s  - Data: %s", rowNums, rowData));
                    rowDataList.put(rowNums, rowData);
                    if (failureMsg.isEmpty()) failureMsg.add(getCellData(0, 0));
                }
            }

            if (Strings.isNotNullAndNotEmpty(expReportPath)) {
                File expFile = new File(expReportPath);

                FileInputStream expFis = new FileInputStream(expFile);
                Workbook expectedWB = WorkbookFactory.create(expFis);

                // TODO: 26/11/2023 Verify detail
            }
        } catch (Exception e) {
            log.error("verifyReportData: failed " + e.getMessage());
        }

        // Add result
        result.put("status", isResult);
        result.put("message", failureMsg); // Save err msg Structure: Row 1: Wrong value num
        result.put("fileData", rowDataList);
        return result;
    }


    private String getCellData(Cell cell) {
        if (cell == null) return "";
        ((XSSFCell) cell).setCellType(CellType.STRING);
        String CellData = null;
        switch (cell.getCellType()) {
            case STRING:
                CellData = cell.getStringCellValue();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss");
                    Date date = cell.getDateCellValue();
                    String[] tmp = dateFormat.format(date).split("_");
                    if (date.after(new Date(0)) && tmp[1].equals("00:00:00")) {
                        CellData = tmp[0];
                    } else CellData = tmp[1];

                    //CellData = String.valueOf(cell.getDateCellValue());
                } else {
                    double tmp = cell.getNumericCellValue();

                    if (tmp >= Long.MIN_VALUE && tmp <= Long.MAX_VALUE) {
                        double tmp2 = (long) tmp - tmp;
                        if (tmp2 != 0) CellData = df.format(tmp);
                        else CellData = df.format((long) tmp);
                    }
                    //CellData = String.valueOf((long) cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                CellData = Boolean.toString(cell.getBooleanCellValue());
                break;
            case BLANK:
                CellData = "";
                break;
        }
        return CellData;
    }

    private boolean checkIsExist(List<Object> colList, String colName) {
        boolean isResult = colList.stream().noneMatch(v -> ((String) v).equalsIgnoreCase(String.format("^%s", "ALL")));
        // "^ALL" -> not fill ALL

        // "ALL" -> fill ALL
        if (colList.stream().anyMatch(v -> ((String) v).equalsIgnoreCase("ALL"))) isResult = true;

        // "^Col" -> not fill this col
        if (colList.stream().anyMatch(v -> ((String) v).equalsIgnoreCase(String.format("^%s", colName)))) {
            isResult = false;
        }
        // "Col" -> fill this column
        if (colList.stream().anyMatch(v -> ((String) v).equalsIgnoreCase(colName))) isResult = true;
        return isResult;
    }
}
