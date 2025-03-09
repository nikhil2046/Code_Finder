package org.codefinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FieldPathChecker {
    public Map<String, List<String>> fieldChecker(String excelFilePath, String projectPath) throws IOException {
        ExcelReader reader = new ExcelReader();
        Map<String, List<String>> map = new HashMap<>();

        try {
            List<String> excelFieldNames = reader.readFilePaths(excelFilePath);
            //Path to the directory containing the other project's source files
            Path projectDir = Paths.get(projectPath);
            for (String exFieldName : excelFieldNames) {
                String fieldName = exFieldName.substring(exFieldName.lastIndexOf('/') + 1);
                String getMethodName = "get" + capitalize(fieldName) + "()";
                String setMethodName = "set" + capitalize(fieldName) + "()";
                String isMethodName = "is" + capitalize(fieldName) + "()";

                List<String> fieldNamesList = new ArrayList<>();
                //search for method name in all java files

                try (Stream<Path> paths = Files.walk(projectDir)) {
                    boolean found = paths.filter(Files::isRegularFile)
                            .filter(p -> p.toString().endsWith(".java"))
                            .anyMatch(p -> {
                                try {
                                    return searchMethodInFile(p, getMethodName, setMethodName, isMethodName, fieldNamesList);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                    if (!found) {
                        fieldNamesList.add("Not Implemented");
                    }
                }
                map.put(exFieldName, fieldNamesList);
            }
        } catch (IOException ignored) {
            throw ignored;
        }
        return map;
    }

    private boolean searchMethodInFile(Path file, String getMethodName, String setMethodName, String isMethodName, List<String> list) throws IOException {
        try {
            List<String> lines = Files.readAllLines(file);
            for (String line : lines) {
                if (line.toLowerCase().contains(getMethodName.toLowerCase())
                        || line.toLowerCase().contains(isMethodName)
                        || line.toLowerCase().contains(setMethodName)) {
                    list.add(file.getFileName().toString());
                    break;
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
