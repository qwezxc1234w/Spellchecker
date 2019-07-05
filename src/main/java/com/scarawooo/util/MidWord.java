package com.scarawooo.util;

import java.util.TreeSet;

public class MidWord extends Word {
    private LongWord longWord;
    private ShortWord shortWord;

    MidWord(String word) {
        super(word);
        longWord = new LongWord(word);
        shortWord = new ShortWord(word);
    }

    @Override
    public TreeSet<String> getSamples() {
        TreeSet<String> samples = new TreeSet<>();
        samples.addAll(shortWord.getSamples());
        samples.addAll(longWord.getSamples());
        return samples;
    }
}
