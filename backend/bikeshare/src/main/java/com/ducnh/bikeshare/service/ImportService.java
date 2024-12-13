package com.ducnh.bikeshare.service;

import com.ducnh.bikeshare.model.Trip;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class ImportService {
    public static final int TRIP_ID_COLUMN = 0;
    public static final int SUBSCRIBER_TYPE_COLUMN = 1;
    public static final int BIKE_ID_COLUMN = 2;
    public static final int BIKE_TYPE_COLUMN = 3;

    public static void main(String[] args) throws IOException {
        String filePath = "C:\\Users\\DucNH\\Downloads\\Trips_2024_12_12_8_20_24.xlsx";
        List<Trip> trips = readExcel(filePath);

        for (Trip trip : trips) log.info(String.valueOf(trip));
    }

    public static List<Trip> readExcel(String filePath) throws IOException {
        List<Trip> trips = new ArrayList<>();

        InputStream inputStream = new FileInputStream(new File(filePath));

        Workbook workbook = getWorkbook(inputStream, filePath);

        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> iterator = sheet.iterator();

        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            if (nextRow.getRowNum() < 2) continue;

            Iterator<Cell> cellIterator = nextRow.cellIterator();

            Trip trip = new Trip();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                Object cellValue = getCellValue(cell);
                if (cellValue == null || cellValue.toString().isEmpty()) continue;

                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case TRIP_ID_COLUMN:
                        double tmp = (double) getCellValue(cell);
                        long tmp2 = (long) tmp;
                        trip.setTrip_id(String.valueOf(tmp2));
                        break;
                    case SUBSCRIBER_TYPE_COLUMN:
                        trip.setSubscriber_type(String.valueOf(getCellValue(cell)));
                        break;
                    case BIKE_ID_COLUMN:
                        trip.setBike_id(String.valueOf(getCellValue(cell)));
                        break;
                    case BIKE_TYPE_COLUMN:
                        trip.setBike_type(String.valueOf(getCellValue(cell)));
                        break;
                }
            }
            trips.add(trip);
        }

        workbook.close();
        inputStream.close();

        return trips;
    }

    private static Workbook getWorkbook(InputStream inputStream, String filePath) throws IOException {
        Workbook workbook;
        if (filePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else throw new IllegalArgumentException("Invalid file");

        return workbook;
    }

    private static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellTypeEnum();
        Object cellValue = null;
        switch (cellType) {
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            default:
                break;
        }

        return cellValue;
    }
}
