package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class CSVReportUtil {

    private static Workbook workbook = new XSSFWorkbook();
    private static Sheet sheet = workbook.createSheet("Register Report");
    private static int rowCount = 1;

    static {
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("Email");
        header.createCell(1).setCellValue("Expected Status");
        header.createCell(2).setCellValue("Actual Status");
        header.createCell(3).setCellValue("Result");
        header.createCell(4).setCellValue("Description");
    }

    public static void writeResult(String email,
                                   int expected,
                                   int actual,
                                   String result,
                                   String description) {

        Row row = sheet.createRow(rowCount++);

        row.createCell(0).setCellValue(email);
        row.createCell(1).setCellValue(expected);
        row.createCell(2).setCellValue(actual);
        row.createCell(3).setCellValue(result);
        row.createCell(4).setCellValue(description);
    }

    public static void saveReport() {
        try (FileOutputStream fileOut =
                     new FileOutputStream("reports/TeacherRegisterReport.xlsx")) {

            workbook.write(fileOut);
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}