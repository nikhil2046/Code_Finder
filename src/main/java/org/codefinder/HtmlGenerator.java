package org.codefinder;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HtmlGenerator {
    public static void generateReport(Map<String, List<String>> data, String fileName) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>table { width: 100%; border-collapse: collapse; } th, td { border: 1px solid black; padding: 8px; text-align: left; } th { background-color: orange; } tr:nth-child(even) { background-color: #f2f2f2; } </style></head><body>");
        html.append("<table>");
        html.append("<tr><th>Field Name</th><th>XPath</th><th>Implemented</th></tr>");

        for (Map.Entry<String, List<String>> entry : data.entrySet()) {
            String fieldName = entry.getKey();
            List<String> values = entry.getValue();
            String xpath = values.size() > 0 ? values.get(0) : "";
            String implemented = values.size() > 1 ? values.get(1) : "";

            html.append("<tr>");
            html.append("<td>").append(fieldName).append("</td>");
            html.append("<td>").append(xpath).append("</td>");
            html.append("<td>").append(implemented).append("</td>");
            html.append("</tr>");
        }

        html.append("</table></body></html>");

        // Write the generated HTML to a file
        try (FileWriter fileWriter = new FileWriter(fileName + ".html")) {
            fileWriter.write(html.toString());
            System.out.println("HTML file generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void generateReport2(Map<String, List<String>> data, String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.write("<html><head><style>");
        writer.write("body { font-family: Calibri, sans-serif; margin: 10px; background-color: #f4f4f4; }");
        writer.write("table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }");
        writer.write("th, td { border: 1px solid #dddddd; text-align: left; padding: 12px; }");
        writer.write("th { background-color: #EFAA49FF; color: white; text-align: center;}");
        writer.write("tr:nth-child(even) { background-color: #f2f2f2; }");
        writer.write("tr:hover { background-color: #ddd; }");
        writer.write(".green { color: green; }");
        writer.write(".red { color: red; }");
        writer.write("h1 { color: #333; text-align: center; }");
        //writer.write("footer { text-align: center; padding: 10px; background-color: #4CAF50; color: white; position: fixed; bottom: 0; width: 100%; }");
        writer.write("header { text-align: center; padding: 5px; background-color: orange; color: white; position: fixed; top: 0; width: 100%; }");
        writer.write("</style></head><body>");
        // writer.write("<header>  <h1>Missing Code Utility Report </h1> </header>");
        writer.write("<h1>Missing Code Reports</h1>");
        writer.write("<h4> Date : "  + LocalDate.now()+"</h4>");
        writer.write("<table>");
        writer.write("<tr><th>EFX Fields Path</th><th>Status</th><th> File Name Location</th></tr>");

        if (data.entrySet().isEmpty()) {
            writer.write("<tr><td colspan='3' style='text-align:center; color:red;'>No Record Found</td></tr>");
        } else {
            for (Map.Entry<String, List<String>> entry : data.entrySet()) {
                List<String> filenames = entry.getValue();
                String status = filenames.get(0).equals("Not Implemented") ? "Not Implemented" : "Implemented";
                String colorClass = status.equals("Implemented") ? "green" : "red";

                writer.write("<tr><td>" + entry.getKey() + "</td><td class='" + colorClass + "'>" + status + "</td><td>");
                writer.write("<ol class='" + colorClass + "'>");
                for (String filename : filenames) {
                    if (!filename.equals("Not Implemented")) {
                        writer.write("<li>" + filename + "</li>");
                    }
                }
            }
            writer.write("</ol>");
            writer.write("</td></tr>");
        }

        writer.write("</table>");
        // writer.write("<footer>Report generated on " + java.time.LocalDateTime.now() + "</footer>");
        writer.write("</body></html>");
        writer.close();
    }
}

