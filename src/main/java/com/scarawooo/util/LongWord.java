package com.scarawooo.util;

import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeSet;

public class LongWord extends Word {
    LongWord(String word) {
        super(word);
    }

    private int getRightBound(int length) {
        int end = length;
        while (end - Math.max(Math.round(end * MAX_LENGTH_DIFFERENCE), 1) <= length)
            ++end;
        return --end;
    }

    @Override
    public TreeSet<String> getSamples() {
        ArrayList<ArrayList<Character>> phoneticRepresentation = Transformer.deepTransform(word);
        Stack<Integer> indexes1 = new Stack<>();
        Stack<Integer> indexes2 = new Stack<>();
        Stack<Integer> union = new Stack<>();
        ArrayList<String> bigrams = new ArrayList<>();
        TreeSet<String> samples = new TreeSet<>();
        for (int i = phoneticRepresentation.size() - 1; i >= 0; --i)
            indexes1.push(i);
        while (indexes1.size() != 1) {
            int first = indexes1.pop();
            int second = indexes1.pop();
            union.clear();
            union.addAll(indexes1);
            union.addAll(indexes2);
            bigrams.addAll(buildSamples(phoneticRepresentation, union));
            indexes2.push(first);
            indexes1.push(second);
        }
        int offset = bigrams.size() / 2;
        for (int i = 0; i < bigrams.size(); ++i)
            for (int j = i + 1; j <= i + offset && j < bigrams.size(); ++j) {
                String sample = bigrams.get(i).concat(bigrams.get(j));
                int left = Math.max(phoneticRepresentation.size(), LONG_WORD_BARRIER);
                int right = getRightBound(phoneticRepresentation.size());
                for (int length = left; length <= right; ++length)
                    samples.add(sample.concat(String.valueOf(length)));
            }
        return samples;
    }
}
