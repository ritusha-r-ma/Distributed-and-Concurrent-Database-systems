package org.example;

import java.util.*;

public class TransactionManager {
    List<String> transactions;
    Map<String, Transaction> allTransactions;
    Map<String, List<ReadWriteInstr>> commandList;
    SerializationGraph serializationGraph;
    AvailableCopies availableCopies;
    SiteManager siteManager;
    Finalizer finalizer;
    Map<String, Integer> snapshotValues;

    public TransactionManager(List<String> transactions, SiteManager siteManager) {
        this.transactions = transactions;
        this.allTransactions = new HashMap<>();
        this.commandList = new HashMap<>();
        this.siteManager = siteManager;
        this.finalizer = new Finalizer(siteManager);
        this.availableCopies = new AvailableCopies(commandList, siteManager, this.allTransactions);
        this.serializationGraph = new SerializationGraph(commandList, this.allTransactions);
        this.snapshotValues = new HashMap<>();
        readAllInstructions();
    }

    public void readAllInstructions() {
        int timeStamp = 0;
        for (String transaction : this.transactions) {
            int startIndex = transaction.indexOf('(') + 1;
            int endIndex = transaction.indexOf(')');
            String command = transaction.substring(startIndex, endIndex).trim();

            if (transaction.startsWith("begin")) {
                handleBegin(command, ++timeStamp);
            } else if (transaction.startsWith("W")) {
                handleWrite(command, ++timeStamp);
            } else if (transaction.startsWith("R")) {
                handleRead(command, ++timeStamp);
            } else if (transaction.startsWith("fail")) {
                handleSiteFailure(command, ++timeStamp);
            } else if (transaction.startsWith("recover")) {
                handleSiteRecovery(command, ++timeStamp);
            } else if (transaction.startsWith("end")) {
                handleEnd(command, ++timeStamp);
            } else if (transaction.startsWith("dump")) {
                handleDump();
            }
        }
    }

    public void handleBegin(String transactionId, int timeStamp) {
        Transaction transaction = new Transaction(transactionId, timeStamp);
        transaction.setSnapshotValues(new HashMap<>(this.snapshotValues));
        this.allTransactions.put(transactionId, transaction);

        for (Site site : siteManager.getSites()) {
            if (site.getSiteStatus() == Site.SiteStatus.UP && !site.isRecoveredSite() && site.getTimeStamp() < timeStamp) {
                transaction.getProperlyUpSites().add(site);
            }
        }
    }

    public void handleSiteFailure(String site, int timeStamp) {
        int siteNumber = Integer.parseInt(site.trim());
        siteManager.bringSiteDown(siteNumber, timeStamp);
    }

    public void handleSiteRecovery(String site, int timeStamp) {
        int siteNumber = Integer.parseInt(site.trim());
        siteManager.bringSiteUp(siteNumber, timeStamp);
    }

    public List<Integer> getAllDownSites() {
        List<Integer> downSites = new ArrayList<>();
        for (Site site : siteManager.getSites()) {
            if (site.getSiteStatus() == Site.SiteStatus.DOWN) {
                downSites.add(site.getSiteNumber());
            }
        }

        return downSites;
    }

    public void handleRead(String transactionString, int timeStamp) {
        String[] parts = transactionString.split(",");
        String transactionId = parts[0].trim();
        String variable = parts[1].trim();

        if (!this.allTransactions.containsKey(transactionId)) {
            System.out.println("Read command before initiating the transaction : abort here");
            System.exit(1);
        }

        List<Integer> downSites = getAllDownSites();

        if (!this.commandList.containsKey(transactionId)) {
            List<ReadWriteInstr> writeCommands = new ArrayList<>();
            writeCommands.add(new ReadWriteInstr(variable, timeStamp, downSites));
            this.commandList.put(transactionId, writeCommands);
        } else {
            this.commandList.get(transactionId).add(new ReadWriteInstr(variable, timeStamp, downSites));
        }

        Transaction transaction = this.allTransactions.get(transactionId);
        boolean isAnySiteUp = false;
        for (Site site : transaction.getProperlyUpSites()) {
            if (site.getSiteStatus() == Site.SiteStatus.UP && !site.isRecoveredSite && site.getData().containsKey(variable)) {
                transaction.getSnapshotValues().put(variable, site.getData().get(variable));
                isAnySiteUp = true;
            }
        }

        if (!isAnySiteUp && !transaction.getProperlyUpSites().isEmpty()) {
            System.out.println(transactionId + " " + variable + " is waiting for any site to be up till end(" + transactionId + ")");
            transaction.getQueueItems().add(variable);
        }
    }

    public void handleWrite(String transactionString, int timeStamp) {
        String[] parts = transactionString.split(",");
        String transactionId = parts[0].trim();
        String variable = parts[1].trim();
        int value = Integer.parseInt(parts[2].trim());

        if (!this.allTransactions.containsKey(transactionId)) {
            System.out.println("Write command before initiating the transaction : abort here");
            System.exit(1);
        }

        Transaction transaction = this.allTransactions.get(transactionId);
        List<Integer> downSites = getAllDownSites();
        transaction.getSnapshotValues().put(variable, this.siteManager.getVariableValueForAnyUpSite(variable, downSites));

        if (!this.commandList.containsKey(transactionId)) {
            List<ReadWriteInstr> writeCommands = new ArrayList<>();
            writeCommands.add(new ReadWriteInstr(variable, value, timeStamp, downSites));
            this.commandList.put(transactionId, writeCommands);
        } else {
            this.commandList.get(transactionId).add(new ReadWriteInstr(variable, value, timeStamp, downSites));
        }
    }

    public void handleEnd(String transactionId, int timeStamp) {
        boolean isAvailableCopiesGood = this.availableCopies.isAvailableCopiesApplies(transactionId);

        if (!isAvailableCopiesGood) {
            System.out.println(transactionId + " aborted");
            return;
        }

        boolean isTransSerialization = this.serializationGraph.addToGraph(transactionId, this.commandList.get(transactionId));
        if (!isTransSerialization) {
            System.out.println(transactionId + " aborted");
            return;
        }

        this.allTransactions.get(transactionId).setEndTime(timeStamp);
        this.snapshotValues = this.allTransactions.get(transactionId).getSnapshotValues();
        this.finalizer.commitWriteValues(this.commandList.get(transactionId),  this.allTransactions.get(transactionId), timeStamp);
        System.out.println(transactionId + " commits");
    }

    public void handleDump() {
        for (Site site : this.siteManager.getSites()) {
            System.out.print("Site " + site.getSiteNumber() + " - ");

            site.getData().entrySet().stream()
                    .sorted(Comparator.comparingInt(entry -> Integer.parseInt(entry.getKey().substring(1))))
                    .forEach(entry -> System.out.print(entry.getKey() + ":" + entry.getValue() + " "));

            System.out.println();
        }
    }
}
