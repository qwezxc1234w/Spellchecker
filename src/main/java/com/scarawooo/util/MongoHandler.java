package com.scarawooo.util;

import com.mongodb.BasicDBList;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class MongoHandler {
    private static final String MONGO_DOCUMENT_KEY = "code";
    private static final String MONGO_DOCUMENT_VALUE = "words";
    private static final String MONGO_DB_NAME_1 = "dictionary3";
    private static final String MONGO_DB_NAME_2 = "dictionary4";
    private static final String MONGO_DB_NAME_3 = "dictionary5";
    private static final String MONGO_COLLECTION_NAME = "dictionary";
    private static MongoClient mongoClient1 = MongoClients.create();
    private static MongoClient mongoClient2 = MongoClients.create();
    private static MongoClient mongoClient3 = MongoClients.create();
    private static MongoDatabase database1 = mongoClient1.getDatabase(MONGO_DB_NAME_1);
    private static MongoDatabase database2 = mongoClient2.getDatabase(MONGO_DB_NAME_2);
    private static MongoDatabase database3 = mongoClient3.getDatabase(MONGO_DB_NAME_3);
    private static MongoCollection<Document> collection1 = database1.getCollection(MONGO_COLLECTION_NAME);
    private static MongoCollection<Document> collection2 = database2.getCollection(MONGO_COLLECTION_NAME);
    private static MongoCollection<Document> collection3 = database3.getCollection(MONGO_COLLECTION_NAME);

    public static boolean insert(ArrayList<String> request) {
        List<Document> list = new ArrayList<>();
        for (String word : request)
            list.add(new Document(MONGO_DOCUMENT_KEY, word));
        collection3.insertMany(list);
        return true;
    }

    public static boolean search(String request) {
        return collection3.find(new Document(MONGO_DOCUMENT_KEY, request)).first() != null;
    }

    public static TreeSet<Correction> fuzzySearch(final BasicDBList request) {
        final TreeSet<Correction> result1 = new TreeSet<>();
        final TreeSet<Correction> result2 = new TreeSet<>();
        Thread thread1 = new Thread(() -> {
            try (MongoCursor<Document> cursor = collection1.find(new Document(MongoHandler.MONGO_DOCUMENT_KEY, new Document("$in", request))).iterator()) {
                while (cursor.hasNext()) {
                    Document next = cursor.next();
                    result1.addAll(next.getList(MongoHandler.MONGO_DOCUMENT_VALUE, String.class).stream().
                            map(word -> new Correction(word, next.getString(MongoHandler.MONGO_DOCUMENT_KEY))).collect(Collectors.toList()));
                }
            }
        });
        thread1.setDaemon(true);
        Thread thread2 = new Thread(() -> {
            try (MongoCursor<Document> cursor = collection2.find(new Document(MongoHandler.MONGO_DOCUMENT_KEY, new Document("$in", request))).iterator()) {
                while (cursor.hasNext()) {
                    Document next = cursor.next();
                    result2.addAll(next.getList(MongoHandler.MONGO_DOCUMENT_VALUE, String.class).stream().
                            map(word -> new Correction(word, next.getString(MongoHandler.MONGO_DOCUMENT_KEY))).collect(Collectors.toList()));
                }
            }
        });
        thread2.setDaemon(true);
        long time = System.nanoTime();
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        }
        catch (InterruptedException exception) {
            System.out.println("Процесс поиска прерван");
        }
        result1.addAll(result2);
        return result1;
    }

    public static void shutdown() {
        mongoClient1.close();
        mongoClient2.close();
        mongoClient3.close();
    }
}
