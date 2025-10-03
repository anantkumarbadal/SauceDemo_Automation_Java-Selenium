package com.sauceDemo.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // For .xlsx files

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtils {

    /**
     * Reads all data from a specific sheet in an Excel file.
     * Assumes the first row contains headers and skips it.
     * Data is returned as a 2D Object array, suitable for TestNG @DataProvider.
     */
    public static Object[][] getTableArray(String filePath, String sheetName) throws IOException {

        System.out.println("DEBUG: ExcelUtils.getTableArray() called with filePath: " + filePath + ", sheetName: " + sheetName);

        FileInputStream ExcelFile = null;
        Workbook workbook = null;
        Object[][] tabArray = null;

        try {
            ExcelFile = new FileInputStream(filePath);

            System.out.println("DEBUG: File found and opened at: " + filePath);

            try {
                System.out.println("Entering try block for workbook");

                workbook = new XSSFWorkbook(ExcelFile); // For .xlsx files
             
                System.out.println("DEBUG: Workbook successfully initialized. It's a valid XLSX file.");
            }
            catch (Exception e)
            {
                System.err.println("ERROR: Failed to initialize workbook from file: " + filePath);
                System.err.println("This usually means the Excel file is corrupted or not a valid XLSX format.");
                System.err.println("Reason: " + e.getMessage());
                e.printStackTrace(); // Print full stack trace for more details
                throw new IOException("Failed to initialize workbook from Excel file. Check file format and integrity. Original error: " + e.getMessage(), e);
            }
                Sheet sheet = workbook.getSheet(sheetName);

                if (sheet == null) {
                    System.err.println("ERROR: Sheet '" + sheetName + "' not found in workbook: " + filePath);
                    throw new IOException("Sheet '" + sheetName + "' not found in workbook: " + filePath);
                }

                System.out.println("DEBUG: Sheet '" + sheetName + "' found.");

                int startRow = 1; // Assuming first row (index 0) is header, so start from second row (index 1)
                int totalRows = sheet.getLastRowNum(); // Gets last row index (0-based)

                System.out.println("DEBUG: Total rows in sheet (including header): " + (totalRows + 1));
                System.out.println("DEBUG: Data rows to read (excluding header): " + (totalRows - startRow + 1));

                if (totalRows < startRow) {
                    System.out.println("No data rows found in sheet: " + sheetName);
                    return new Object[0][0]; // Return empty array if no data rows
                }

                Row headerRow = sheet.getRow(0);
                if (headerRow == null) {
                    throw new IOException("Header row (row 0) not found in sheet: " + sheetName);
                }
                int totalCols = headerRow.getLastCellNum(); // Gets last column index + 1 (1-based count)

                tabArray = new Object[totalRows - startRow + 1][totalCols];

                for (int i = startRow; i <= totalRows; i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) { // Ensure row is not null (e.g., if there are empty rows in between)
                        for (int j = 0; j < totalCols; j++) {
                            Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK); // Get cell, treat null as blank
                            tabArray[i - startRow][j] = getCellValueAsString(cell);
                        }
                    } else {
                        // If a row is null, fill its corresponding array row with nulls or empty strings
                        for (int j = 0; j < totalCols; j++) {
                            tabArray[i - startRow][j] = ""; // Fill with empty string for consistency
                        }
                    }
                }
            } finally {
                if (workbook != null) {
                    workbook.close();
                }
                if (ExcelFile != null) {
                    ExcelFile.close();
                }
            }
            System.out.println("DEBUG: ExcelUtils returning " + (tabArray != null ? tabArray.length : 0) + " data rows.");
            return tabArray;
        }

        /**
         * Helper method to get cell value as String, handling different cell types.
         * cell The cell to get the value from.
         * @return The cell value as a String.
         */
        private static String getCellValueAsString (Cell cell){
            if (cell == null) {
                return "";
            }
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString(); // Or format as needed
                    } else {
                        // For numeric, prevent .0 from showing up on whole numbers
                        return String.valueOf((long) cell.getNumericCellValue());
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    // Evaluate formula to get its result, then get as string
                    FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                    return getCellValueAsString(evaluator.evaluateInCell(cell));
                case BLANK:
                    return "";
                default:
                    return cell.toString();
            }
        }
    }
