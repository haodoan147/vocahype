package com.vocahype.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SplitCSVRows {
    public static void main(String[] args) {
        String inputFilePath = "C:\\Users\\hao.doanvonhut\\Documents\\Project\\vocahype\\src\\main\\java\\com\\vocahype\\tools\\antonyms.csv";
        String outputFilePath = "C:\\Users\\hao.doanvonhut\\Documents\\Project\\vocahype\\src\\main\\java\\com\\vocahype\\tools\\output_file_antonyms.csv";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            FileWriter writer = new FileWriter(outputFilePath);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length == 3) {
                    String thirdValue = values[2];
                    String[] splitValues = thirdValue.split("[;|]");
                    for (String splitValue : splitValues) {
                        writer.write(values[0] + "," + values[1] + "," + splitValue + "\n");
                    }
                }
            }

            reader.close();
            writer.close();

            System.out.println("CSV file split and saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

