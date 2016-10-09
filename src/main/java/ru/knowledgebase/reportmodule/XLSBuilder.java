package ru.knowledgebase.reportmodule;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.knowledgebase.configmodule.Config;
import ru.knowledgebase.configmodule.Configurations;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Мария on 01.10.2016.
 */
public class XLSBuilder {

    private String fileName;
    private XSSFWorkbook workbook;
    private String excelFileName;

    public XLSBuilder(String fileName) throws Exception {

        this.fileName = fileName;
        workbook = new XSSFWorkbook();
    }

    public void saveXLS() throws Exception {
        String pathToFolder = Configurations.getReportFolder();
        excelFileName = pathToFolder + "/" + fileName;
        FileOutputStream fos = new FileOutputStream(excelFileName);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }

    public String addSheetToXLS(String sheetName)throws Exception{
        Sheet sheet = workbook.createSheet(sheetName);
        return sheetName;
    }

    public void printToSheet(String sheetName, List<Object[]> newData) throws Exception{
        Sheet sheet = workbook.getSheet(sheetName);
        int roeNum = 0;
        for (Object[] objArr : newData) {
            Row row = sheet.createRow(roeNum++);
            int cellNum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellNum++);
                if (obj instanceof String) { cell.setCellValue((String) obj);
                } else if (obj instanceof Integer) {
                    cell.setCellValue((Integer) obj);
                } else if (obj instanceof Timestamp) {
                    cell.setCellValue((Timestamp) obj);
                }
            }
        }
    }

    public String getPath() {
        return excelFileName;
    }
}
