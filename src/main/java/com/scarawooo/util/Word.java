package com.scarawooo.util;

import com.mongodb.BasicDBList;

import java.util.*;

public abstract class Word {
    static final float MAX_LENGTH_DIFFERENCE = 0.333f;
    static final int LONG_WORD_BARRIER = 10; // зависят
    private static final int SHORT_WORD_BARRIER = 6; // друг от друга

    protected String word;
    private TreeSet<Correction> corrections = new TreeSet<>();
    private int phoneticCorrectionDistance = Integer.MAX_VALUE;
    private int lexicographicalCorrectionDistance = Integer.MAX_VALUE;

    Word(String word) {
        this.word = word;
    }

    public static Word getWord(String word) {
        String normalized = Transformer.normalize(word);
        if (normalized.length() <= SHORT_WORD_BARRIER)
            return new ShortWord(word);
        else if (normalized.length() >= LONG_WORD_BARRIER)
            return new LongWord(word);
        else
            return new MidWord(word);
    }

    public String getWord() {
        return word;
    }

    public int getPhoneticCorrectionDistance() {
        return phoneticCorrectionDistance;
    }

    public int getLexicographicalCorrectionDistance() {
        return lexicographicalCorrectionDistance;
    }

    // поиск ближайщих исправлений
    public void findNearestCorrections() {
        BasicDBList request = new BasicDBList();
        request.addAll(getSamples());
        corrections = MongoHandler.fuzzySearch(request);
        if (!corrections.isEmpty()) {
            ArrayList<ArrayList<Character>> inputPhoneticRepresentations = Transformer.deepTransform(word);
            TreeSet<Correction> nearestCorrections = new TreeSet<>();
            Correction[] arr = new Correction[corrections.size()];
            corrections.toArray(arr);
            Arrays.sort(arr, (first, second) ->
                -Integer.compare(first.getNgramDistance(),
                            second.getNgramDistance())
            );
            for (int i = 0; i < arr.length && arr[i].getNgramDistance() >= (int)((float) arr[0].getNgramDistance() * MAX_LENGTH_DIFFERENCE); ++i) {
                ArrayList<ArrayList<Character>> correctionSimplifiedTransformation = Transformer.deepTransform(arr[i].getWord());
                for (String inputPhoneticRepresentation : buildSamples(inputPhoneticRepresentations, new Stack<>()))
                    for (String correctionPhoneticRepresentation : buildSamples(correctionSimplifiedTransformation, new Stack<>())) {
                        int distance = getDistance(inputPhoneticRepresentation, correctionPhoneticRepresentation);
                        if (distance < arr[i].getPhoneticDistance())
                            arr[i].setPhoneticDistance(distance);
                    }
                if (arr[i].getPhoneticDistance() <= phoneticCorrectionDistance)
                    if ((inputPhoneticRepresentations.size() > correctionSimplifiedTransformation.size() ?
                            arr[i].getPhoneticDistance() <= Math.round((float) inputPhoneticRepresentations.size() * MAX_LENGTH_DIFFERENCE) :
                            arr[i].getPhoneticDistance() <= Math.round((float) correctionSimplifiedTransformation.size() * MAX_LENGTH_DIFFERENCE))) {
                        if (arr[i].getPhoneticDistance() < phoneticCorrectionDistance) {
                            phoneticCorrectionDistance = arr[i].getPhoneticDistance();
                            nearestCorrections.clear();
                        }
                        nearestCorrections.add(arr[i]);
                    }
            }
            if (nearestCorrections.size() > 5) {
                corrections.clear();
                corrections.addAll(nearestCorrections);
                nearestCorrections.clear();
                for (Correction correction : corrections) {
                    int distance = getDistance(
                            word,
                            correction.getWord()
                    );
                    if (distance < lexicographicalCorrectionDistance) {
                        nearestCorrections.clear();
                        nearestCorrections.add(correction);
                        lexicographicalCorrectionDistance = distance;
                    } else if (distance == lexicographicalCorrectionDistance)
                        nearestCorrections.add(correction);
                }
            }
            corrections = nearestCorrections;
        }
    }

    public TreeSet<Correction> getCorrections() {
        return corrections;
    }

    // формирование фонетических отпечатков
    public abstract TreeSet<String> getSamples();

    TreeSet<String> buildSamples(ArrayList<ArrayList<Character>> phoneCode, Stack<Integer> phoneCodeIndexes) {
        class Iterator {
            private int innerIndex, barrier;

            private Iterator(int barrier) {
                this.barrier = barrier;
                innerIndex = 0;
            }

            private boolean next() {
                boolean snowball = ++innerIndex == barrier;
                innerIndex %= barrier;
                return snowball;
            }

            private int getInnerIndex() {
                return innerIndex;
            }
        }
        ArrayList<ArrayList<Character>> phoneCodePart = new ArrayList<>();
        ArrayList<Iterator> builderIndexes = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        TreeSet<String> samples = new TreeSet<>();
        for (int i = 0; i < phoneCode.size(); ++i)
            if (!phoneCodeIndexes.contains(i)) {
                phoneCodePart.add(phoneCode.get(i));
                builderIndexes.add(new Iterator(phoneCode.get(i).size()));
            }
        builderIndexes.add(new Iterator(2));
        while (builderIndexes.get(builderIndexes.size() - 1).getInnerIndex() == 0) {
            builder.delete(0, builder.length());
            for (int i = 0; i < builderIndexes.size() - 1; ++i)
                builder.append(phoneCodePart.get(i).get(builderIndexes.get(i).getInnerIndex()));
            samples.add(builder.toString());
            for (Iterator i : builderIndexes)
                if (!i.next())
                    break;
        }
        return samples;
    }

    private int getDistance(String first, String second) {
        int maxLength = Math.max(first.length(), second.length());
        int[] currentRow = new int[maxLength + 1];
        int[] previousRow = new int[maxLength + 1];
        int[] transpositionRow = new int[maxLength + 1];
        int firstLength = first.length();
        int secondLength = second.length();
        if (firstLength == 0)
            return secondLength;
        else if (secondLength == 0) return firstLength;
        if (firstLength > secondLength) {
            String tmp = first;
            first = second;
            second = tmp;
            firstLength = secondLength;
            secondLength = second.length();
        }
        int max = secondLength;
        if (secondLength - firstLength > max) return max + 1;
        if (firstLength > currentRow.length) {
            currentRow = new int[firstLength + 1];
            previousRow = new int[firstLength + 1];
            transpositionRow = new int[firstLength + 1];
        }
        for (int i = 0; i <= firstLength; i++)
            previousRow[i] = i;
        char lastSecondCh = 0;
        for (int i = 1; i <= secondLength; i++) {
            char secondCh = second.charAt(i - 1);
            currentRow[0] = i;
            int from = Math.max(i - max - 1, 1);
            int to = Math.min(i + max + 1, firstLength);
            char lastFirstCh = 0;
            for (int j = from; j <= to; j++) {
                char firstCh = first.charAt(j - 1);
                int cost = firstCh == secondCh ? 0 : 1;
                int value = Math.min(Math.min(currentRow[j - 1] + 1, previousRow[j] + 1), previousRow[j - 1] + cost);
                if (firstCh == lastSecondCh && secondCh == lastFirstCh)
                    value = Math.min(value, transpositionRow[j - 2] + cost);
                currentRow[j] = value;
                lastFirstCh = firstCh;
            }
            lastSecondCh = secondCh;

            int tempRow[] = transpositionRow;
            transpositionRow = previousRow;
            previousRow = currentRow;
            currentRow = tempRow;
        }
        return previousRow[firstLength];
    }
}