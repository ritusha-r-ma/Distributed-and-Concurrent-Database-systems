package org.example;

import java.util.List;
import java.util.Map;

public class AvailableCopies {
    Map<String, List<ReadWriteInstr>> commandList;

    Map<String, Transaction> allTransactions;

    SiteManager siteManager;

    public AvailableCopies(Map<String, List<ReadWriteInstr>> commandList, SiteManager siteManager, Map<String, Transaction> allTransactions) {
        this.commandList = commandList;
        this.siteManager = siteManager;
        this.allTransactions = allTransactions;
    }

    public boolean isAvailableCopiesApplies(String transactionId) {
        Transaction transaction = this.allTransactions.get(transactionId);
        List<ReadWriteInstr> currentTransInstructions = this.commandList.get(transactionId);

        boolean isAvailableCopiesGood = true;

        for (ReadWriteInstr currTransaction : currentTransInstructions) {
            if (!isAvailableCopiesGood) {
                break;
            }

            if (currTransaction.getType() == ReadWriteInstr.IOType.READ && !transaction.getQueueItems().isEmpty()) {
                isAvailableCopiesGood = checkReadInstructions(transaction);
            }

            if (currTransaction.getType() == ReadWriteInstr.IOType.WRITE) {
                isAvailableCopiesGood = isAvailableCopiesGood && checkWriteInstructions(transaction);
            }
        }

        return isAvailableCopiesGood;
    }

    public boolean checkReadInstructions(Transaction transaction) {
        if (!transaction.getQueueItems().isEmpty()) {
            for (Site site : transaction.getProperlyUpSites()) {
                if (site.getSiteStatus() == Site.SiteStatus.UP && site.isRecoveredSite()) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean checkWriteInstructions(Transaction transaction) {
        for (Site site : transaction.getProperlyUpSites()) {
            if (site.getSiteStatus() == Site.SiteStatus.UP) {
                return true;
            }
        }

        return false;
    }
}
