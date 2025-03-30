package org.codefinder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class FileChooserExample {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Windows-like UI
        } catch (Exception ignored) {}

        JFrame frame = new JFrame("Missing Code Utility Application");


        JPanel mainPanel = new JPanel();

        //panel color
        mainPanel.setBackground(new Color(0xCCC9C9));
        mainPanel.setLayout(null);

        JLabel titleLabel = new JLabel("", JLabel.CENTER);
        JLabel bottomLabel = new JLabel("", JLabel.CENTER);

        //color for title label and bottom label
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.black); // Set text color to white
        titleLabel.setOpaque(true); // Make the background color visible
        titleLabel.setBackground(new Color(0xEFAA49));

        bottomLabel.setOpaque(true);
        bottomLabel.setBackground(new Color(0xEFAA49));

        //buttons
        JButton chooseFileButton = new JButton("Select Excel Files");
        JButton chooseProjectButton = new JButton("Select Project");
        JButton generateReportButton = new JButton("Generate Reports");
        generateReportButton.setEnabled(false);
        JButton resetButton = new JButton("Reset");

        chooseFileButton.setBounds(50, 50, 150, 30);
        chooseProjectButton.setBounds(220, 50, 150, 30);
        generateReportButton.setBounds(390, 50, 150, 30);
        resetButton.setBounds(560, 50, 150, 30);

        //labels
        JLabel successMsgLabel = new JLabel();
        JLabel fileMsgLabel = new JLabel();
        JLabel projectMsgLabel = new JLabel();
        JLabel errorMsgLabel = new JLabel();

        titleLabel.setBounds(0, 0, 800, 30);
        bottomLabel.setBounds(0, 550, 800, 30);
        successMsgLabel.setBounds(50, 220, 500, 200);
        fileMsgLabel.setBounds(50, 60, 400, 150);
        projectMsgLabel.setBounds(320, 60, 400, 60);
        errorMsgLabel.setBounds(50, 500, 400, 30);

        //set color
        successMsgLabel.setForeground(new Color(0x4987E3));
        successMsgLabel.setFont(new Font("Arial", Font.BOLD, 15));

        final String[] projectPath = {""};
        Set<String> mappingSheetPaths = new HashSet<>();
        StringBuilder filePaths = new StringBuilder();
        StringBuilder targetPaths = new StringBuilder("<html>");

        ActionListener enableSubmitButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isAvailProjectAndFile(projectMsgLabel.getText(), fileMsgLabel.getText())) {
                    generateReportButton.setEnabled(true); // Enable the submit button
                } else {
                    generateReportButton.setEnabled(false); // Disable the submit button
                }
            }
        };

        // Multi-selection enabled for JFileChooser
        // Multi-selection enabled for JFileChooser
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileMsgLabel.setText("");
                filePaths.setLength(0);
                mappingSheetPaths.clear();
                successMsgLabel.setText("");
                errorMsgLabel.setText("");

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true); // Enable multi-selection

                FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Excel Files", "xlsx", "xls");
                fileChooser.setFileFilter(fileNameExtensionFilter);
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File[] selectedFiles = fileChooser.getSelectedFiles();
                    filePaths.append("<html>");
                    int i = 1;
                    for (File file : selectedFiles) {
                        String modifiedFilePath = file.toString().replace("\\", "/");
                        String[] fname = modifiedFilePath.split("/");
                        mappingSheetPaths.add(modifiedFilePath);
                        filePaths.append(i++ + ". " + fname[fname.length - 1] + "<br>");

                    }
                    filePaths.append("</html>");
                    fileMsgLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    fileMsgLabel.setForeground(Color.BLACK); // Set text color to white

                    fileMsgLabel.setText(filePaths.toString());

                    //filePathLabel.setText(reportFilePath);
                }
                enableSubmitButtonListener.actionPerformed(e);
            }
        });

        chooseProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                projectMsgLabel.setText("");
                errorMsgLabel.setText("");
                successMsgLabel.setText("");

                JFileChooser dirChooser = new JFileChooser();
                dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = dirChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedDir = dirChooser.getSelectedFile();
                    projectPath[0] = selectedDir.getAbsolutePath();
                    targetPaths.append("<html><br/>");
                    targetPaths.append("Target Path : ").append(selectedDir.getAbsolutePath()).append("</html>");


                    projectMsgLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    projectMsgLabel.setForeground(Color.BLACK);
                    projectMsgLabel.setText(targetPaths.toString());

                }
                enableSubmitButtonListener.actionPerformed(e);
            }
        });

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String reportFilePath = "";
                StringBuilder successfulMsg = new StringBuilder("<html>");
                for (String xPath : mappingSheetPaths) {
                    if (xPath.contains("/")) {
                        String[] fullPathExtract = xPath.split("/");
                        String lastFileName = fullPathExtract[fullPathExtract.length - 1];
                        if (lastFileName.contains("_")) {
                            String[] extract2 = lastFileName.split("_");
                            if (extract2.length > 0) {
                                reportFilePath = extract2[0] + "_" + extract2[1];
                            }
                        }
                    }

                    FieldPathChecker fieldPathChecker = new FieldPathChecker();
                    Map<String, List<String>> map = null;
                    try {
                        map = fieldPathChecker.fieldChecker(xPath, projectPath[0]);
                        HtmlGenerator.generateReport2(map, reportFilePath + "_Report" + ".html");
                        successfulMsg.append(reportFilePath + " Generated Successfully..!<br>");
                        //Thread.sleep(2000);
                        SwingUtilities.invokeLater(() -> successMsgLabel.setText(String.valueOf(successfulMsg)));
                    } catch (Exception ex) {
                        String error = extractFilename(xPath);
                        errorMsgLabel.setForeground(Color.RED);
                        errorMsgLabel.setFont(new Font("Arial", Font.BOLD, 14));
                        SwingUtilities.invokeLater(() -> errorMsgLabel.setText(error + " file is invalid.! please retry."));
                        //throw new RuntimeException(ex);
                    }
                   // System.out.println("Report successfully completed.!");
                }
                System.out.println("Reports completed successfully..!");
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                successMsgLabel.setText("");
                errorMsgLabel.setText("");
                fileMsgLabel.setText("");
                projectMsgLabel.setText("");

                mappingSheetPaths.clear();
                //projectPaths.clear();
                filePaths.setLength(0);

                enableSubmitButtonListener.actionPerformed(e);
            }
        });

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                frame.revalidate();
                frame.repaint();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                frame.revalidate();
                frame.repaint();
            }
        });


        mainPanel.add(titleLabel);
        mainPanel.add(chooseFileButton);
        mainPanel.add(generateReportButton);
        mainPanel.add(resetButton);
        mainPanel.add(chooseProjectButton);
        mainPanel.add(successMsgLabel);
        mainPanel.add(projectMsgLabel);
        mainPanel.add(fileMsgLabel);
        mainPanel.add(bottomLabel);
        mainPanel.add(errorMsgLabel);

        frame.add(mainPanel);

        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    private static List<String> readFile(File file) throws IOException {
        List<String> fieldValues = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet("EFX Object Mapping");
            if (sheet != null) {
                for (Row row : sheet) {
                    Cell cell = row.getCell(1); // B column index is 1 (0-based index)
                    if (cell != null) {
                        fieldValues.add(cell.getStringCellValue());
                    }
                }
            }
        }
        return fieldValues;
    }

    private static boolean isAvailProjectAndFile(String projectMessage, String fileMessage) {
        if (fileMessage.isEmpty() || fileMessage.contentEquals("Please select files") ||
                projectMessage.isEmpty() || projectMessage.contentEquals("Please select target path")) {
            return false;
        }
        return true;
    }

    private static String extractFilename(String fname) {
        if (fname.contains("/")) {
            String[] fullPathExtract = fname.split("/");
            String lastFileName = fullPathExtract[fullPathExtract.length - 1];
            if (lastFileName.contains("_")) {
                String[] extract2 = lastFileName.split("_");
                if (extract2.length > 0) {
                    fname =  extract2[0] + "_" + extract2[1];
                }
            }
            fname = lastFileName;
        }
        return fname;
    }
}