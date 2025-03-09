package org.codefinder;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        String projectPath = "D:/My_library_management_projects/book-service";
        List<String> excelPaths = List.of("C:/Users/Nikhil/OneDrive/Documents/acctAdd_DDA.xlsx", "C:/Users/Nikhil/OneDrive/Documents/AcctAdd_CDA.xlsx");

        for (String path : excelPaths) {
            String[] fullPathExtract = path.split("/");
            String lastFileName = fullPathExtract[fullPathExtract.length - 1];
            String[] extract2 = lastFileName.split("_");
            String reportFilePath = extract2[0] + "_" + extract2[1] + "_" + "report";

            System.out.println("Please wait...generating report for " + reportFilePath + "....\n");
            FieldPathChecker fieldPathChecker = new FieldPathChecker();
            try {
                Map<String, List<String>> map = fieldPathChecker.fieldChecker(path, projectPath);
                System.out.println(map);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}