package com.scarawooo.util;

import java.util.TreeSet;

public class Correction implements Comparable<Correction> {
    private String word;
    private int ngramDistance = 1;
    private int phoneticDistance = Integer.MAX_VALUE;
    private TreeSet<String> phoneticCodes = new TreeSet<>();

    private String normalizePhoneticCode(String code) {
        int index = 0;
        for (; index < code.length() && !Character.isDigit(code.charAt(index)); ++index);
        return code.substring(0, index);
    }

    Correction(String word, String code) {
        this.word = word;
        phoneticCodes.add(normalizePhoneticCode(code));
    }

    @Override
    public int compareTo(Correction o) {
        if (word.compareTo(o.word) == 0 && this != o) {
            TreeSet<String> tmp = new TreeSet<>(this.phoneticCodes);
            tmp.removeAll(o.phoneticCodes);
            o.phoneticCodes.addAll(tmp);
            o.ngramDistance += tmp.size();
        }
        return word.compareTo(o.word);
    }

    public void setPhoneticDistance(int distance) {
        this.phoneticDistance = distance;
    }

    public int getPhoneticDistance() {
        return phoneticDistance;
    }

    public int getNgramDistance() {
        return ngramDistance;
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return word + "\t" + ngramDistance + "\t" + phoneticDistance;
    }
}
