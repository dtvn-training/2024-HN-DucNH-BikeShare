package com.ducnh.bikeshare.service;

import com.ducnh.bikeshare.constant.Constant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static org.apache.poi.xssf.streaming.SXSSFWorkbook.DEFAULT_WINDOW_SIZE;

@Service
public class ExportService implements IExportService{

    // Functions for exporting excel directly from BigQuery

    @Override
    public <P> void exportToExcel(List<P> data, HttpServletResponse response) throws IOException{
        if (data == null || data.isEmpty()) {
            throw new IOException("Empty");
        }

        int number_of_sheet = data.size() / Constant.ROWS_PER_SHEET + 1;
        System.out.println(number_of_sheet);
        int start, end;

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_WINDOW_SIZE)) {
            for (int sheetNo = 0; sheetNo < number_of_sheet; sheetNo++) {
                Sheet sheet = workbook.createSheet("Sheet " + sheetNo);
                start = Constant.ROWS_PER_SHEET * sheetNo;
                end = Math.min(start + Constant.ROWS_PER_SHEET, data.size());

                Class<?> aClass = data.get(0).getClass();
                Field[] fields = aClass.getDeclaredFields();

                createHeaderRow(sheet, fields);
                addDataRows(data, sheet, fields, start, end);
            }

            try (ServletOutputStream outStream = response.getOutputStream()) {
                workbook.write(outStream);
                workbook.dispose();
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    private void createHeaderRow(Sheet sheet, Field[] fields) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < fields.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(fields[i].getName());
        }
    }

    private <T> void addDataRows(List<T> data, Sheet sheet, Field[] fields, int start, int end) {
        System.out.println(start + " " + end);
        int rowNum = 1;
        for (int j = start; j < end; j++) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < fields.length; i++) {
                Cell cell = row.createCell(i);
                Field field = fields[i];
                field.setAccessible(true);
                try {
//                    System.out.println(data.get(j));
                    Object value = field.get(data.get(j));
                    setCellValue(cell, value);
                } catch (IllegalAccessException e) {
                    cell.setCellValue("");
                }
            }
        }
    }


    // Functions for exporting excel from frontend response in JSON type

    @Override
    public void exportQueryResult(String json, HttpServletResponse response, String table) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_WINDOW_SIZE)) {
            Sheet sheet = workbook.createSheet();

            JsonNode jsonArray = readJsonFile(json);

            if (jsonArray == null) {
                throw new IOException("Failed");
            }

            try (ServletOutputStream outStream = response.getOutputStream()) {
                createHeaderRow(sheet, jsonArray, table);
                addDataRows(sheet, jsonArray, table);
                workbook.write(outStream);
                workbook.dispose();
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    private JsonNode readJsonFile(String json) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json);
            return jsonNode.get("json");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    private void createHeaderRow(Sheet sheet, JsonNode jsonArray, String table) {
        if (jsonArray == null || jsonArray.isEmpty()) {
            return;
        }

        JsonNode stations = jsonArray.get(table);

        if (stations == null || stations.isEmpty()) {
            return;
        }

        JsonNode firstObject = stations.get(0);
        Row headerRow = sheet.createRow(0);
        int cellIndex = 0;
        Iterator<String> fieldNames = firstObject.fieldNames();

        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            Cell cell = headerRow.createCell(cellIndex++);
            cell.setCellValue(fieldName);
        }
    }

    private void addDataRows(Sheet sheet, JsonNode jsonArray, String table) throws IOException {
        if (jsonArray == null) {
            throw new IOException("Error");
        }

        int count = jsonArray.get("count").asInt();
        JsonNode stations = jsonArray.get(table);

        for (int i = 0; i < count; i++) {
            JsonNode jsonObject = stations.get(i);
            Row dataRow = sheet.createRow(i + 1);
            Map<String, Object> rowData = new LinkedHashMap<>();
            Iterator<String> fieldNames = jsonObject.fieldNames();

            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode fieldValue = jsonObject.get(fieldName);
                rowData.put(fieldName, formatCellValue(fieldValue));
            }

            int cellIndex = 0;
            for (Map.Entry<String, Object> entry : rowData.entrySet()) {
                Cell cell = dataRow.createCell(cellIndex++);
                setCellValue(cell, entry.getValue());
            }
        }
    }

    private String formatCellValue(JsonNode node) {
        if (node.isNull()) {
            return "";
        }

        return node.asText();
    }

    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }

        String stringValue = value.toString();

        try {
            double numValue = Double.parseDouble(stringValue);
            cell.setCellValue(numValue);
        } catch (NumberFormatException e) {
            cell.setCellValue(stringValue);
        }
    }

    public String editJSONInput(String json) {
        json = json.replace("\\", "");
        json = json.replace("\"{", "{");
        return json.replace("]}\"", "]}");
    }
}