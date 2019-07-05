package com.scarawooo.util;

import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeSet;

public class ShortWord extends Word {
    ShortWord(String word) {
        super(word);
    }

    private int getMaxMistakeSize(int phoneticRepresentationLength) {
        return phoneticRepresentationLength > 1 ? Math.max(Math.round((float) phoneticRepresentationLength * MAX_LENGTH_DIFFERENCE), 1) : 0;
    }

    // формирование фонетических отпечатков
    @Override
    public TreeSet<String> getSamples() {
        ArrayList<ArrayList<Character>> phoneticRepresentation = Transformer.deepTransform(word);
        Stack<Integer> indexes = new Stack<>();
        TreeSet<String> samples = new TreeSet<>(buildSamples(phoneticRepresentation, indexes));
        int maxMistakeSize = getMaxMistakeSize(phoneticRepresentation.size());
        for (int mistakeSize = 1; mistakeSize <= maxMistakeSize; ++mistakeSize) {
            indexes.push(0);
            while (!indexes.isEmpty()) {
                if (indexes.peek() >= phoneticRepresentation.size()) {
                    indexes.pop();
                    if (!indexes.isEmpty())
                        indexes.push(indexes.pop() + 1);
                } else if (indexes.size() < mistakeSize)
                    indexes.push(indexes.peek() + 1);
                else {
                    samples.addAll(buildSamples(phoneticRepresentation, indexes));
                    indexes.push(indexes.pop() + 1);
                }
            }
        }
        return samples;
    }
}
