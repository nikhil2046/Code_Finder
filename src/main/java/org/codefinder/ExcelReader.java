package org.codefinder;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExcelReader {
    public List<String> readFilePaths(String filePaths) {
        List<String> fieldsPaths = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(filePaths);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet("EFX Object Mapping");
            boolean skipFirstRow = true;
            if (Objects.nonNull(sheet)) {
                for (Row row : sheet) {
                    if (skipFirstRow) {
                        skipFirstRow = false;
                        continue;
                    }
                    if (row.getZeroHeight() || row.getCell(1) == null || row.getCell(1).getStringCellValue().isEmpty()) {
                        continue;
                    }
                    String fullPath = row.getCell(1).getStringCellValue();
                    //Extract last name after '/'
                    fieldsPaths.add(fullPath);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (Exception exception){
            throw exception;
        }
        return fieldsPaths;
    }
}






            /*Workbook workbook = new XSSFWorkbook(fis) {
                Sheet sheet = workbook.getSheet("EFX Object Mapping");

                boolean skipFirstRow = true;


            }
        } {
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
