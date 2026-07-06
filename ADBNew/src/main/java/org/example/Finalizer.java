package org.example;


import java.util.List;
import java.util.StringJoiner;

public class Finalizer {
    SiteManager siteManager;

    public Finalizer(SiteManager siteManager) {
        this.siteManager = siteManager;
    }

    public void commitWriteValues(List<ReadWriteInstr> instructions, Transaction transaction, int timeStamp) {
        for (ReadWriteInstr instr : instructions) {
            if (instr.getType() == ReadWriteInstr.IOType.READ) {
                System.out.println(transaction.getTransactionId() + " reads " + instr.getVariableName() + ":" + transaction.getSnapshotValues().get(instr.getVariableName()));
                continue;
            }

            transaction.getSnapshotValues().put(instr.getVariableName(), instr.getValue());

            StringJoiner logs = new StringJoiner(", ");
            for (Site site : this.siteManager.getSites()) {
                if (instr.getDownSites().contains(site.getSiteNumber())) {
                    continue;
                }

                if (instr.getType() == ReadWriteInstr.IOType.WRITE && site.getData().containsKey(instr.getVariableName())) {
                    if (site.isRecoveredSite()) {
                        site.setRecoveredSite(false);
                        site.setTimeStamp(timeStamp);
                    }

                    site.getData().put(instr.getVariableName(), instr.getValue());
                    logs.add(String.valueOf(site.getSiteNumber()));
                }
            }
            System.out.println(transaction.getTransactionId() + " writes " + instr.getVariableName() + " at sites - " + logs);
        }
    }
}
