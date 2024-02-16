package com.rays.bean;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;

import org.bson.Document;

public class UserModel {

    public static void main(String[] args) {
    	String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("jdbc");
            MongoCollection<Document> collection = database.getCollection("student");

            // Insert a document with an auto-incrementing userId
            insertUser(collection, "John Doe", "john.doe@example.com");
           
        }
    }

    private static void insertUser(MongoCollection<Document> collection, String name, String email) {
        int userId = getNextSequenceValue(collection, "userId");
        Document document = new Document("userId", userId)
                .append("name", name)
                .append("email", email);
        collection.insertOne(document);
        System.out.println("User inserted: " + document.toJson());
    }

    private static int getNextSequenceValue(MongoCollection<Document> collection, String sequenceName) {
        Document sequenceDocument = collection.findOneAndUpdate(
                new Document("_id", sequenceName),
                new Document("$inc", new Document("sequence_value", 1)),
                new FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.AFTER)
        );

        return sequenceDocument.getInteger("sequence_value");
    }
}



