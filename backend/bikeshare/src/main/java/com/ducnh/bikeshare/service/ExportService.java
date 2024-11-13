package com.ducnh.bikeshare.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.apache.poi.xssf.streaming.SXSSFWorkbook.DEFAULT_WINDOW_SIZE;

@Service
public class ExportService implements IExportService{
    private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Override
    public void exportQueryResult(String json, HttpServletResponse response) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_WINDOW_SIZE)) {
            Sheet sheet = workbook.createSheet();

            setResponseHeader(response, "Stations");
            JsonNode jsonArray = readJsonFile(json);

            if (jsonArray == null) {
                throw new IOException("Failed");
            }

            try (ServletOutputStream outStream = response.getOutputStream()) {
                createHeaderRow(sheet, jsonArray);
                addDataRows(sheet, jsonArray);
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

    private void createHeaderRow(Sheet sheet, JsonNode jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) {
            return;
        }

        JsonNode stations = jsonArray.get("stations");

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

    private void addDataRows(Sheet sheet, JsonNode jsonArray) throws IOException {
        if (jsonArray == null) {
            throw new IOException("Error");
        }

        int count = jsonArray.get("count").asInt();
        JsonNode stations = jsonArray.get("stations");

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

    @Override
    public void setResponseHeader(HttpServletResponse response, String table) {
        String fileName = table + ".xlsx";
        response.setContentType(CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setHeader("Expires", "0");
    }

    public String editJSONInput(String json) {
        json = json.replace("\\", "");
        json = json.replace("\"{", "{");
        return json.replace("]}\"", "]}");
    }
}