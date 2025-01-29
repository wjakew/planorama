/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all right reserved
 */
package com.jakubwawak.planorama.backend.database_service;

import com.jakubwawak.planorama.maintanance.ConsoleColors;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * Object for connecting to MongoDB database
 */
public class Database {
    public String database_url;
    public boolean connected;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    ArrayList<String> error_collection;

    /**
     * Constructor
     */
    public Database() {
        this.database_url = "";
        connected = false;
        error_collection = new ArrayList<>();
    }

    /**
     * Function for setting database URL
     * 
     * @param database_url
     */
    public void setDatabase_url(String database_url) {
        this.database_url = database_url;
    }

    /**
     * Function for connecting to database
     * 
     * @return boolean
     */
    public void connect() {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(database_url))
                .serverApi(serverApi)
                .build();
        try {
            mongoClient = MongoClients.create(settings);
            MongoDatabase database = mongoClient.getDatabase("admin");
            // Send a ping to confirm a successful connection
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            Document commandResult = database.runCommand(command);
            connected = true;
            mongoDatabase = mongoClient.getDatabase("db_planorama");
            log("DB-CONNECTION", "Connected succesffully with database - running application");
        } catch (MongoException ex) {
            // catch error
            log("DB-CONNECTION-ERROR", "Failed to connect to database (" + ex.toString() + ")");
            connected = false;
        }
    }

    /**
     * Function for getting collection from database
     * 
     * @param collectionName
     * @return MongoCollection<Document>
     */
    public MongoCollection<Document> getCollection(String collectionName) {
        try {
            return mongoDatabase.getCollection(collectionName);
        } catch (MongoException ex) {
            log("DB-GET-COLLECTION-ERROR", "Failed to get collection (" + ex.toString() + ")");
            return null;
        }
    }

    /**
     * Function for inserting document to collection
     * 
     * @param collectionName
     * @param document
     * @return int
     */
    public int insert(String collectionName, Document document) {
        try {
            InsertOneResult result = mongoDatabase.getCollection(collectionName).insertOne(document);
            if (result.getInsertedId() != null) {
                log("DB-INSERT", "Inserted document to collection (" + result.getInsertedId() + ")");
                return 1;
            } else {
                log("DB-INSERT-ERROR", "Failed to insert document to collection");
                return 0;
            }
        } catch (MongoException ex) {
            log("DB-INSERT-ERROR", "Failed to insert document to collection (" + ex.toString() + ")");
            return -1;
        }
    }

    /**
     * Function for story log data
     * 
     * @param log_category
     * @param log_text
     */
    public void log(String log_category, String log_text) {
        error_collection
                .add(log_category + "(" + LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString() + ") - " + log_text);
        if (log_category.contains("FAILED") || log_category.contains("ERROR")) {
            System.out.println(ConsoleColors.RED_BRIGHT + log_category + "["
                    + LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString() + ") - " + log_text + "]"
                    + ConsoleColors.RESET);
            try {
                Notification noti = Notification.show(log_text);
                noti.addThemeVariants(NotificationVariant.LUMO_ERROR);

            } catch (Exception ex) {
            }
        } else {
            System.out.println(ConsoleColors.GREEN_BRIGHT + log_category + "["
                    + LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString() + ") - " + log_text + "]"
                    + ConsoleColors.RESET);
        }
        // TODO inserting log to database
    }
}