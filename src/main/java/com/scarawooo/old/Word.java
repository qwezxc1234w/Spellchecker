/*
package com.scarawooo.old;

import com.mongodb.BasicDBList;
import com.mongodb.client.MongoCursor;
import com.scarawooo.util.*;
import javafx.util.Pair;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.TreeSet;
import java.util.stream.Collectors;

public abstract class Word {
    static final float MAX_LENGTH_DIFFERENCE = 0.333f;
    private static final int MAX_WORDS_DISTANCE = 99999;
    static final int LONG_WORD_BARRIER = 10; // зависят
    private static final int SHORT_WORD_BARRIER = 6; // друг от друга
    private static HashMap<Character, Character> simplifiedPhoneticClasses;
    private static HashMap<Character, ArrayList<Character>> phoneticClasses;
    private static ArrayList<Character> unknownCharacter;
    private static ArrayList<Pair<String, String>> replacements;

    private String word;
    ArrayList<ArrayList<Character>> phoneticRepresentation;
    private TreeSet<Correction> corrections = new TreeSet<>();
    private int correctionDistance = MAX_WORDS_DISTANCE;

    static  {
        phoneticClasses = new HashMap<>();
        phoneticClasses.put('а', new ArrayList<>());
        phoneticClasses.put('f', new ArrayList<>());
        phoneticClasses.get('а').add('A');
        phoneticClasses.get('а').add('O');
        phoneticClasses.get('f').add('A');
        phoneticClasses.get('f').add('O');
        phoneticClasses.put('о', new ArrayList<>());
        phoneticClasses.put('j', new ArrayList<>());
        phoneticClasses.get('о').add('O');
        phoneticClasses.get('j').add('O');
        phoneticClasses.put('я', new ArrayList<>());
        phoneticClasses.put('z', new ArrayList<>());
        phoneticClasses.get('я').add('A');
        phoneticClasses.get('я').add('E');
        phoneticClasses.get('z').add('A');
        phoneticClasses.get('z').add('E');
        phoneticClasses.put('е', new ArrayList<>());
        phoneticClasses.put('t', new ArrayList<>());
        phoneticClasses.get('е').add('E');
        phoneticClasses.get('t').add('E');
        phoneticClasses.put('и', new ArrayList<>());
        phoneticClasses.put('b', new ArrayList<>());
        phoneticClasses.get('и').add('E');
        phoneticClasses.get('и').add('I');
        phoneticClasses.get('b').add('E');
        phoneticClasses.get('b').add('I');
        phoneticClasses.put('ы', new ArrayList<>());
        phoneticClasses.put('s', new ArrayList<>());
        phoneticClasses.get('ы').add('I');
        phoneticClasses.get('s').add('I');
        phoneticClasses.put('э', new ArrayList<>());
        phoneticClasses.get('э').add('E');
        phoneticClasses.put('у', new ArrayList<>());
        phoneticClasses.put('e', new ArrayList<>());
        phoneticClasses.get('у').add('Y');
        phoneticClasses.get('e').add('Y');
        phoneticClasses.put('ю', new ArrayList<>());
        phoneticClasses.get('ю').add('Y');
        phoneticClasses.put('х', new ArrayList<>());
        phoneticClasses.get('х').add('G');
        phoneticClasses.put('к', new ArrayList<>());
        phoneticClasses.put('r', new ArrayList<>());
        phoneticClasses.get('к').add('G');
        phoneticClasses.get('r').add('G');
        phoneticClasses.put('г', new ArrayList<>());
        phoneticClasses.put('u', new ArrayList<>());
        phoneticClasses.get('г').add('G');
        phoneticClasses.get('u').add('G');
        phoneticClasses.put('ш', new ArrayList<>());
        phoneticClasses.put('i', new ArrayList<>());
        phoneticClasses.get('ш').add('W');
        phoneticClasses.get('ш').add('Z');
        phoneticClasses.get('i').add('W');
        phoneticClasses.get('i').add('Z');
        phoneticClasses.put('ж', new ArrayList<>());
        phoneticClasses.get('ж').add('Z');
        phoneticClasses.put('щ', new ArrayList<>());
        phoneticClasses.put('o', new ArrayList<>());
        phoneticClasses.get('щ').add('W');
        phoneticClasses.get('o').add('W');
        phoneticClasses.put('ч', new ArrayList<>());
        phoneticClasses.put('x', new ArrayList<>());
        phoneticClasses.get('ч').add('W');
        phoneticClasses.get('x').add('W');
        phoneticClasses.put('р', new ArrayList<>());
        phoneticClasses.put('h', new ArrayList<>());
        phoneticClasses.get('р').add('P');
        phoneticClasses.get('h').add('P');
        phoneticClasses.put('л', new ArrayList<>());
        phoneticClasses.put('k', new ArrayList<>());
        phoneticClasses.get('л').add('L');
        phoneticClasses.get('k').add('L');
        phoneticClasses.put('ц', new ArrayList<>());
        phoneticClasses.put('w', new ArrayList<>());
        phoneticClasses.get('ц').add('C');
        phoneticClasses.get('w').add('C');
        phoneticClasses.put('с', new ArrayList<>());
        phoneticClasses.put('c', new ArrayList<>());
        phoneticClasses.get('с').add('C');
        phoneticClasses.get('c').add('C');
        phoneticClasses.put('з', new ArrayList<>());
        phoneticClasses.put('p', new ArrayList<>());
        phoneticClasses.get('з').add('C');
        phoneticClasses.get('p').add('C');
        phoneticClasses.put('м', new ArrayList<>());
        phoneticClasses.put('v', new ArrayList<>());
        phoneticClasses.get('м').add('M');
        phoneticClasses.get('v').add('M');
        phoneticClasses.put('н', new ArrayList<>());
        phoneticClasses.put('y', new ArrayList<>());
        phoneticClasses.get('н').add('N');
        phoneticClasses.get('y').add('N');
        phoneticClasses.put('п', new ArrayList<>());
        phoneticClasses.put('g', new ArrayList<>());
        phoneticClasses.get('п').add('B');
        phoneticClasses.get('g').add('B');
        phoneticClasses.put('б', new ArrayList<>());
        phoneticClasses.get('б').add('B');
        phoneticClasses.put('ф', new ArrayList<>());
        phoneticClasses.put('a', new ArrayList<>());
        phoneticClasses.get('ф').add('V');
        phoneticClasses.get('a').add('V');
        phoneticClasses.put('в', new ArrayList<>());
        phoneticClasses.put('d', new ArrayList<>());
        phoneticClasses.get('в').add('V');
        phoneticClasses.get('d').add('V');
        phoneticClasses.put('т', new ArrayList<>());
        phoneticClasses.put('n', new ArrayList<>());
        phoneticClasses.get('т').add('D');
        phoneticClasses.get('n').add('D');
        phoneticClasses.put('д', new ArrayList<>());
        phoneticClasses.put('l', new ArrayList<>());
        phoneticClasses.get('д').add('D');
        phoneticClasses.get('l').add('D');
        phoneticClasses.put('й', new ArrayList<>());
        phoneticClasses.put('q', new ArrayList<>());
        phoneticClasses.get('й').add('I');
        phoneticClasses.get('q').add('I');
        phoneticClasses.put('ё', new ArrayList<>());
        phoneticClasses.get('ё').add('O');
        phoneticClasses.get('ё').add('E');
        //
        simplifiedPhoneticClasses = new HashMap<>();
        simplifiedPhoneticClasses.put('а', 'A');
        simplifiedPhoneticClasses.put('f', 'A');
        simplifiedPhoneticClasses.put('о', 'A');
        simplifiedPhoneticClasses.put('j', 'A');
        simplifiedPhoneticClasses.put('я', 'Q');
        simplifiedPhoneticClasses.put('z', 'Q');
        simplifiedPhoneticClasses.put('е', 'E');
        simplifiedPhoneticClasses.put('t', 'E');
        simplifiedPhoneticClasses.put('и', 'I');
        simplifiedPhoneticClasses.put('b', 'I');
        simplifiedPhoneticClasses.put('ы', 'I');
        simplifiedPhoneticClasses.put('s', 'I');
        simplifiedPhoneticClasses.put('э', 'E');
        simplifiedPhoneticClasses.put('у', 'Y');
        simplifiedPhoneticClasses.put('e', 'Y');
        simplifiedPhoneticClasses.put('ю', 'Y');
        simplifiedPhoneticClasses.put('х', 'G');
        simplifiedPhoneticClasses.put('к', 'G');
        simplifiedPhoneticClasses.put('r', 'G');
        simplifiedPhoneticClasses.put('г', 'G');
        simplifiedPhoneticClasses.put('u', 'G');
        simplifiedPhoneticClasses.put('ш', 'W');
        simplifiedPhoneticClasses.put('i', 'W');
        simplifiedPhoneticClasses.put('ж', 'Z');
        simplifiedPhoneticClasses.put('щ', 'R');
        simplifiedPhoneticClasses.put('o', 'R');
        simplifiedPhoneticClasses.put('ч', 'R');
        simplifiedPhoneticClasses.put('x', 'R');
        simplifiedPhoneticClasses.put('р', 'P');
        simplifiedPhoneticClasses.put('h', 'P');
        simplifiedPhoneticClasses.put('л', 'L');
        simplifiedPhoneticClasses.put('k', 'L');
        simplifiedPhoneticClasses.put('ц', 'C');
        simplifiedPhoneticClasses.put('w', 'C');
        simplifiedPhoneticClasses.put('с', 'C');
        simplifiedPhoneticClasses.put('c', 'C');
        simplifiedPhoneticClasses.put('з', 'C');
        simplifiedPhoneticClasses.put('p', 'C');
        simplifiedPhoneticClasses.put('м', 'M');
        simplifiedPhoneticClasses.put('v', 'M');
        simplifiedPhoneticClasses.put('н', 'N');
        simplifiedPhoneticClasses.put('y', 'N');
        simplifiedPhoneticClasses.put('п', 'B');
        simplifiedPhoneticClasses.put('g', 'B');
        simplifiedPhoneticClasses.put('б', 'B');
        simplifiedPhoneticClasses.put('ф', 'V');
        simplifiedPhoneticClasses.put('a', 'V');
        simplifiedPhoneticClasses.put('в', 'V');
        simplifiedPhoneticClasses.put('d', 'V');
        simplifiedPhoneticClasses.put('т', 'D');
        simplifiedPhoneticClasses.put('n', 'D');
        simplifiedPhoneticClasses.put('д', 'D');
        simplifiedPhoneticClasses.put('l', 'D');
        simplifiedPhoneticClasses.put('й', 'I');
        simplifiedPhoneticClasses.put('q', 'I');
        simplifiedPhoneticClasses.put('ё', 'X');
        //
        unknownCharacter = new ArrayList<>();
        unknownCharacter.add('U');
        // замены
        replacements = new ArrayList<>();
        replacements.add(new Pair<>("ь", ""));
        replacements.add(new Pair<>("ъ", ""));
        replacements.add(new Pair<>(".", ""));
        replacements.add(new Pair<>(" ", ""));
        replacements.add(new Pair<>("-", ""));
        replacements.add(new Pair<>("_", ""));
        replacements.add(new Pair<>("+", ""));
        replacements.add(new Pair<>("=", ""));
        replacements.add(new Pair<>(">", ""));
        replacements.add(new Pair<>("<", ""));
        replacements.add(new Pair<>("?", ""));
        replacements.add(new Pair<>("/", ""));
        replacements.add(new Pair<>("\\", ""));
        replacements.add(new Pair<>("]", ""));
        replacements.add(new Pair<>("}", ""));
        replacements.add(new Pair<>("[", ""));
        replacements.add(new Pair<>("{", ""));
        replacements.add(new Pair<>(",", ""));
        replacements.add(new Pair<>(")", ""));
        replacements.add(new Pair<>("(", ""));
        replacements.add(new Pair<>("*", ""));
        replacements.add(new Pair<>("&", ""));
        replacements.add(new Pair<>("^", ""));
        replacements.add(new Pair<>(":", ""));
        replacements.add(new Pair<>(";", ""));
        replacements.add(new Pair<>("%", ""));
        replacements.add(new Pair<>("#", ""));
        replacements.add(new Pair<>("№", ""));
        replacements.add(new Pair<>("\"", ""));
        replacements.add(new Pair<>("'", ""));
        replacements.add(new Pair<>("@", ""));
        replacements.add(new Pair<>("!", ""));
        replacements.add(new Pair<>("~", ""));
        replacements.add(new Pair<>("`", ""));
        replacements.add(new Pair<>("1", ""));
        replacements.add(new Pair<>("2", ""));
        replacements.add(new Pair<>("3", ""));
        replacements.add(new Pair<>("4", ""));
        replacements.add(new Pair<>("5", ""));
        replacements.add(new Pair<>("6", ""));
        replacements.add(new Pair<>("7", ""));
        replacements.add(new Pair<>("8", ""));
        replacements.add(new Pair<>("9", ""));
        replacements.add(new Pair<>("0", ""));
        replacements.add(new Pair<>("нтств", "нств"));
        replacements.add(new Pair<>("yncnd", "ycnd"));
        replacements.add(new Pair<>("стск", "ск"));
        replacements.add(new Pair<>("cncr", "cr"));
        replacements.add(new Pair<>("нтск", "нск"));
        replacements.add(new Pair<>("yncr", "ycr"));
        replacements.add(new Pair<>("вств", "ств"));
        replacements.add(new Pair<>("dcnd", "cnd"));
        replacements.add(new Pair<>("ндш", "нш"));
        replacements.add(new Pair<>("yli", "yi"));
        replacements.add(new Pair<>("ндц", "нц"));
        replacements.add(new Pair<>("ylw", "yw"));
        replacements.add(new Pair<>("нтг", "нг"));
        replacements.add(new Pair<>("ynu", "yu"));
        replacements.add(new Pair<>("здц", "зц"));
        replacements.add(new Pair<>("plw", "pw"));
        replacements.add(new Pair<>("здк", "зк"));
        replacements.add(new Pair<>("plr", "pr"));
        replacements.add(new Pair<>("здч", "зч"));
        replacements.add(new Pair<>("plx", "px"));
        replacements.add(new Pair<>("стл", "сл"));
        replacements.add(new Pair<>("cnk", "ck"));
        replacements.add(new Pair<>("стб", "сб"));
        replacements.add(new Pair<>("стн", "сн"));
        replacements.add(new Pair<>("cny", "cy"));
        replacements.add(new Pair<>("стг", "сг"));
        replacements.add(new Pair<>("cnu", "cu"));
        replacements.add(new Pair<>("лнц", "нц"));
        replacements.add(new Pair<>("kyw", "yw"));
        replacements.add(new Pair<>("рдц", "рц"));
        replacements.add(new Pair<>("hlw", "hw"));
        replacements.add(new Pair<>("рдч", "рч"));
        replacements.add(new Pair<>("hlx", "hx"));
        replacements.add(new Pair<>("хг", "г"));
        replacements.add(new Pair<>("тс", "ц"));
        replacements.add(new Pair<>("tc", "w"));
        replacements.add(new Pair<>("дц", "ц"));
        replacements.add(new Pair<>("lw", "w"));
        replacements.add(new Pair<>("сч", "щ"));
        replacements.add(new Pair<>("cx", "o"));
        replacements.add(new Pair<>("жч", "щ"));
    }

    Word(String word, ArrayList<ArrayList<Character>> phoneticRepresentation) {
        this.word = word;
        this.phoneticRepresentation = phoneticRepresentation;
    }

    public static Word getWord(String word) {
        ArrayList<ArrayList<Character>> phoneticRepresentation = deepTransform(word);
        if (phoneticRepresentation.size() <= SHORT_WORD_BARRIER)
            return new LilWord(word, phoneticRepresentation);
        else if (phoneticRepresentation.size() >= LONG_WORD_BARRIER)
            return new LongWord(word, phoneticRepresentation);
        else
            return new MidWord(word, phoneticRepresentation);
    }

    // преобразование слова (по пересекающимся фонетическим классам)
    private static ArrayList<ArrayList<Character>> deepTransform(String word) {
        StringBuilder codeBuilder = new StringBuilder();
        String transformed = word.toLowerCase(); // в нижний регистр
        ArrayList<ArrayList<Character>> phoneCode = new ArrayList<>();
        // упрощение последовательности одинаковых гласных и согласных
        for (int i = 0; i < transformed.length(); ++i) {
            while (i < transformed.length() - 1 && transformed.charAt(i) == transformed.charAt(i + 1))
                ++i;
            codeBuilder.append(transformed.charAt(i));
        }
        // замены
        for (Pair<String, String> i: replacements)
            while (codeBuilder.indexOf(i.getKey()) != -1)
                codeBuilder.replace(codeBuilder.indexOf(i.getKey()), codeBuilder.indexOf(i.getKey()) + i.getKey().length(), i.getValue());
        // фонетический код
        for (int i = 0; i < codeBuilder.length(); ++i)
            phoneCode.add(phoneticClasses.getOrDefault(codeBuilder.charAt(i), unknownCharacter));
        return phoneCode;
    }

    private String simplifiedTransform(String word) {
        StringBuilder codeBuilder = new StringBuilder();
        String transformed = word.toLowerCase(); // в нижний регистр
        // упрощение последовательности одинаковых гласных и согласных
        for (int i = 0; i < transformed.length(); ++i) {
            while (i < transformed.length() - 1 && transformed.charAt(i) == transformed.charAt(i + 1))
                ++i;
            codeBuilder.append(transformed.charAt(i));
        }
        // замены
        for (Pair<String, String> i: replacements)
            while (codeBuilder.indexOf(i.getKey()) != -1)
                codeBuilder.replace(codeBuilder.indexOf(i.getKey()), codeBuilder.indexOf(i.getKey()) + i.getKey().length(), i.getValue());
        // фонетический код
        for (int i = 0; i < codeBuilder.length(); ++i)
            codeBuilder.setCharAt(i, simplifiedPhoneticClasses.getOrDefault(codeBuilder.charAt(i), unknownCharacter.get(0)));
        return codeBuilder.toString();
    }

    // поиск ближайщих исправлений
    public TreeSet<Correction> findNearestCorrections() {
        BasicDBList query = new BasicDBList();
        //long time = System.nanoTime();
        query.addAll(getSamples());
        //System.out.println("Query size  " + query.size());
        try (MongoCursor<Document> cursor = MongoHandler.getCollection().find(new Document(MongoHandler.MONGO_DOCUMENT_KEY, new Document("$in", query))).iterator()) {
            //System.out.println("Searching in database: " + (double) (System.nanoTime() - time) / Math.pow(10., 9.));
            //time = System.nanoTime();
            while (cursor.hasNext())
                corrections.addAll(cursor.next().getList(MongoHandler.MONGO_DOCUMENT_VALUE, String.class).stream().
                        map(Correction::new).collect(Collectors.toList()));
            //System.out.println("Add to separation: " + (double) (System.nanoTime() - time) / Math.pow(10., 9.));
        }
        //System.out.println("Размер первичной выборки: " + corrections.size());
        //System.out.println("Первичная выборка");
        //System.out.println(corrections);
        //time = System.nanoTime();
        if (!corrections.isEmpty()) {
            TreeSet<Correction> nearestCorrections = new TreeSet<>();
            int intersections = corrections.first().getSampleIntersectionsCounter();
            for (Correction correction : corrections)
                if (correction.getSampleIntersectionsCounter() > intersections)
                    intersections = correction.getSampleIntersectionsCounter();
            intersections /= 2;
            for (Correction correction : corrections)
                if (correction.getSampleIntersectionsCounter() >= intersections)
                    nearestCorrections.add(correction);
            corrections.clear();
            corrections.addAll(nearestCorrections);
            nearestCorrections.clear();
            String inputWordSimplifiedTransformation = simplifiedTransform(word);
            for (Correction correction : corrections) {
                int distance = getDistance(
                        inputWordSimplifiedTransformation,
                        simplifiedTransform(correction.getWord())
                );
                if (distance < correctionDistance) {
                    nearestCorrections.clear();
                    nearestCorrections.add(correction);
                    correctionDistance = distance;
                } else if (distance == correctionDistance)
                    nearestCorrections.add(correction);
            }
            corrections.clear();
            corrections.addAll(nearestCorrections);
            nearestCorrections.clear();
            correctionDistance = MAX_WORDS_DISTANCE;
            for (Correction correction : corrections) {
                int distance = getDistance(
                        word,
                        correction.getWord()
                );
                if (distance < correctionDistance) {
                    nearestCorrections.clear();
                    nearestCorrections.add(correction);
                    correctionDistance = distance;
                } else if (distance == correctionDistance)
                    nearestCorrections.add(correction);
            }
            corrections.clear();
            corrections.addAll(nearestCorrections);
        }
        //System.out.println("Distance calculating: " + (double) (System.nanoTime() - time) / Math.pow(10., 9.));
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
}*/
