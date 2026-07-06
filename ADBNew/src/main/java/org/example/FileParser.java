package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FileParser {
    List<String> transactions;
    public void readInputFile(String ioPath) {
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(ioPath));
            transactions = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("//") || line.isEmpty()) {
                    continue;
                }

                if (line.contains("//")) {
                    String[] parts = line.split("//");
                    transactions.add(parts[0].trim());
                } else {
                    transactions.add(line.trim());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public List<String> getAllTransactions() {
        return transactions;
    }
}

