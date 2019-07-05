/*
package com.scarawooo.util;
import java.util.ArrayList;
import java.util.Stack;

public class Tree {
    static final int MAX_WEIGHT = 999;
    private Stack<TreeUnit> rootPool = new Stack<>();
    private ArrayList<TreeUnit> result1 = new ArrayList<>();
    private ArrayList<TreeUnit> result2 = new ArrayList<>();

    public Tree(String word) {
        TreeUnit unit = new TreeUnit(0);
        WordEnvironment wordEnvironment = new WordEnvironment(word);
        wordEnvironment.findNearestCorrections();
        unit.getParts().add(wordEnvironment);
        unit.increaseDistance(wordEnvironment.getCorrectionDistance());
        rootPool.push(unit);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < result2.size(); ++i) {
            builder.append("Вариант ".concat(String.valueOf(i)).concat("\n"));
            builder.append(result2.get(i).toString());
        }
        return builder.toString();
    }

    public ArrayList<TreeUnit> getResult2() {
        return result2;
    }

    public void split() {
        boolean rootIsChanged;
        TreeUnit unit;
        int minDistance;
        while (!rootPool.isEmpty()) {
            do {
                unit = rootPool.pop();
                rootIsChanged = false;
                for (int i = 0; i < unit.getParts().size(); ++i)
                    for (int j = 1; j < unit.getParts().get(i).getWord().length(); ++j) {
                        TreeUnit neighbor = new TreeUnit(unit.getPunishment() + 1);
                        for (int k = 0; k < unit.getParts().size(); ++k)
                            if (k != i) {
                                neighbor.getParts().add(unit.getParts().get(k));
                                neighbor.increaseDistance(unit.getParts().get(k).getCorrectionDistance());
                            }
                            else {
                                WordEnvironment wordEnvironment = new WordEnvironment(unit.getParts().get(i).getWord().substring(0, j));
                                long time = System.nanoTime();
                                wordEnvironment.findNearestCorrections();
                                System.out.println("Searching nearest correction: " + (double) (System.nanoTime() - time) / Math.pow(10., 9.));
                                neighbor.increaseDistance(wordEnvironment.getCorrectionDistance());
                                neighbor.getParts().add(wordEnvironment);
                                wordEnvironment = new WordEnvironment(unit.getParts().get(i).getWord().substring(j, unit.getParts().get(i).getWord().length()));
                                time = System.nanoTime();
                                wordEnvironment.findNearestCorrections();
                                System.out.println("Searching nearest correction: " + (double) (System.nanoTime() - time) / Math.pow(10., 9.));
                                neighbor.increaseDistance(wordEnvironment.getCorrectionDistance());
                                neighbor.getParts().add(wordEnvironment);
                            }
                        unit.getNeighbors().add(neighbor);
                    }
                long time = System.nanoTime();
                minDistance = unit.getTotalDistance();
                ArrayList<TreeUnit> rootPoolCandidates = new ArrayList<>();
                for (TreeUnit neighbor : unit.getNeighbors()) {
                    if (neighbor.getTotalDistance() < minDistance) {
                        rootIsChanged = true;
                        minDistance = neighbor.getTotalDistance();
                        rootPoolCandidates.clear();
                        rootPoolCandidates.add(neighbor);
                    }
                    else if (neighbor.getTotalDistance() == minDistance) {
                        rootIsChanged = true;
                        rootPoolCandidates.add(neighbor);
                    }
                }
                if (minDistance == unit.getTotalDistance() && rootIsChanged)
                    result1.add(unit);
                for (TreeUnit i : rootPoolCandidates)
                    rootPool.push(i);
                System.out.println("Choosing a new root: " + (double) (System.nanoTime() - time) / Math.pow(10., 9.));
            } while (rootIsChanged);
            result1.add(unit);
        }
        long time = System.nanoTime();
        minDistance = MAX_WEIGHT;
        for (TreeUnit unit1 : result1)
            if (unit1.getTotalDistance() < minDistance)
                minDistance = unit1.getTotalDistance();
        for (TreeUnit unit1 : result1)
            if (unit1.getTotalDistance() == minDistance)
                result2.add(unit1);
        System.out.println("Building a result: " + (double) (System.nanoTime() - time) / Math.pow(10., 9.));
    }

    public static class TreeUnit {
        private ArrayList<WordEnvironment> parts = new ArrayList<>();
        private ArrayList<TreeUnit> neighbors = new ArrayList<>();
        private int totalDistance, punishment;

        TreeUnit(int punishment) {
            this.punishment = totalDistance = punishment;
        }

        public ArrayList<WordEnvironment> getParts() {
            return parts;
        }

        ArrayList<TreeUnit> getNeighbors() {
            return neighbors;
        }

        int getTotalDistance() {
            return totalDistance;
        }

        int getPunishment() {
            return punishment;
        }

        void increaseDistance(int distance) {
            totalDistance += distance;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Corrections\n");
            for (WordEnvironment part : parts) {
                builder.append("\t");
                for (String correction : part.getCorrections())
                    builder.append(correction.concat(" "));
                builder.append("\n");
            }
            return builder.toString();
        }
    }
}*/
