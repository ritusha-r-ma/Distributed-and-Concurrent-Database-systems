package org.example;

public class Main {
    public static void main(String[] args) {
        String filePath = "";
        for (String arg : args) {
            if (arg.contains(".txt")) {
                filePath = arg;
            }
        }

        FileParser fileParser = new FileParser();
        fileParser.readInputFile(filePath);

        SiteManager siteManager = new SiteManager();
        new TransactionManager(fileParser.getAllTransactions(), siteManager);
    }
}