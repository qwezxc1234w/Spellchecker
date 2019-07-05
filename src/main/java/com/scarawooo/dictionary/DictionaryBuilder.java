package com.scarawooo.dictionary;

import com.scarawooo.util.*;

import java.io.*;
import java.util.*;

public class DictionaryBuilder {
    private static final String DICTIONARY_FILENAME = "dictionary.txt";
    private static ArrayList<String> request = new ArrayList<>();

    public static void main(String[] args) {
        long wordsCounter = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(DICTIONARY_FILENAME)))) {
            do {
                request.clear();
                String input;
                for (int i = 0; i < 10000 && (input = reader.readLine()) != null; ++i)
                    request.add(input);
                System.out.println(wordsCounter += 10000);
            } while (!request.isEmpty() && MongoHandler.insert(request));
        }
        catch (IOException exception) {
            //
        }
        finally {
            MongoHandler.shutdown();
        }
    }
}