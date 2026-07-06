package org.example;

import java.util.*;

public class SerializationGraph {
    Map<String, Set<Pair>> graph;
    Map<String, Transaction> allTransactions;
    Map<String, List<ReadWriteInstr>> instructions;

    public SerializationGraph(Map<String, List<ReadWriteInstr>> instructions, Map<String, Transaction> allTransactions) {
        this.graph = new HashMap<>();
        this.instructions = instructions;
        this.allTransactions = allTransactions;
    }

    public boolean addToGraph(String transactionId, List<ReadWriteInstr> instructions) {
        if (graph.isEmpty()) {
            graph.put(transactionId, new HashSet<>());
            return true;
        }

        List<Pair> additionalRwEdge = new ArrayList<>();
        for (ReadWriteInstr instr : instructions) {
            String variableName = instr.getVariableName();
            ReadWriteInstr.IOType type = instr.getType();

            for (Map.Entry<String, Set<Pair>> vertex : graph.entrySet()) {
                Transaction graphNode = this.allTransactions.get(vertex.getKey());
                List<ReadWriteInstr> nodeInstructions = this.instructions.get(graphNode.getTransactionId());

                 for (ReadWriteInstr nodeInstr : nodeInstructions) {
                     if (nodeInstr.getVariableName().equals(variableName)) {

                         if (nodeInstr.getType() == ReadWriteInstr.IOType.WRITE && type == ReadWriteInstr.IOType.WRITE) {
                             vertex.getValue().add(new Pair(transactionId, "ww"));
                         }

                         if (nodeInstr.getType() == ReadWriteInstr.IOType.WRITE && type == ReadWriteInstr.IOType.READ) {
                             if (graphNode.getEndTime() < this.allTransactions.get(transactionId).getBeginTime()) {
                                 vertex.getValue().add(new Pair(transactionId, "wr"));
                             } else {
                                 additionalRwEdge.add(new Pair(vertex.getKey(), "rw"));
                             }
                         }

                         if (nodeInstr.getType() == ReadWriteInstr.IOType.READ && type == ReadWriteInstr.IOType.WRITE) {
                             vertex.getValue().add(new Pair(transactionId, "rw"));
                         }
                     }
                 }
            }
        }

        if (!additionalRwEdge.isEmpty()) {
            graph.put(transactionId, new HashSet<>());
            for (Pair addEdge : additionalRwEdge) {
                graph.get(transactionId).add(addEdge);
            }
        } else {
            graph.put(transactionId, new HashSet<>());
        }

        return !(hasWWEdge(transactionId) || hasCycleWithConsecutiveRW(transactionId));
    }

    public boolean hasWWEdge(String transactionId) {
        boolean containsWWEdge = false;
        for (Map.Entry<String, Set<Pair>> vertex : graph.entrySet()) {
            for (Pair edge : vertex.getValue()) {
                if (edge.getEdgeType().equals("ww")) {// && allTransactions.get(vertex.getKey()).getEndTime() != -1) {
                    if (allTransactions.get(transactionId).getBeginTime() < allTransactions.get(vertex.getKey()).getEndTime()) {
                        containsWWEdge = true;
                    }
                }
            }
        }

        if (containsWWEdge) {
            removeTransaction(transactionId);
        }

        return containsWWEdge;
    }

    public boolean hasCycleWithConsecutiveRW(String transactionId) {
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();
        List<String> path = new ArrayList<>();

        for (String node : graph.keySet()) {
            if (hasCycleUtil(node, visited, recStack, path)) {
                removeTransaction(transactionId);
                return true;
            }
        }

        return false;
    }

    private boolean hasCycleUtil(String node, Set<String> visited, Set<String> recStack, List<String> path) {
        if (recStack.contains(node)) {
            for (int i = 1; i < path.size(); i++) {
                if ("rw".equals(path.get(i)) && "rw".equals(path.get(i - 1))) {
                    return true;
                }
            }
            return false;
        }

        if (visited.contains(node)) {
            return false;
        }

        visited.add(node);
        recStack.add(node);

        if (graph.containsKey(node)) {
            for (Pair neighbor : graph.get(node)) {
                path.add(neighbor.getEdgeType()); // Add edge type before recursive call
                if (hasCycleUtil(neighbor.getTransactionId(), visited, recStack, path)) {
                    return true;
                }
                path.remove(path.size() - 1); // Remove edge type after recursive call
            }
        }

        recStack.remove(node);
        return false;
    }

    public void removeTransaction(String transactionId) {
        Set<Pair> removalNeighbour = new HashSet<>();
        for (Map.Entry<String, Set<Pair>> vertex : graph.entrySet()) {
            for (Pair neighbour : vertex.getValue()) {
                if (neighbour.getTransactionId().equals(transactionId)) {
                    removalNeighbour.add(neighbour);
                }
            }
        }

        for (Map.Entry<String, Set<Pair>> vertex : graph.entrySet()) {
            for (Pair remove : removalNeighbour) {
                vertex.getValue().remove(remove);
            }
        }

        graph.remove(transactionId);
    }
}
