/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all right reserved
 */
package com.jakubwawak.planorama.backend.database_service;

import com.jakubwawak.planorama.backend.entity.User;
import com.jakubwawak.planorama.maintanance.ConsoleColors;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.InsertOneResult;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
     * Function for creating session
     * 
     * @param user
     * @return String
     */
    public String createSession(User user){
        String session_id = UUID.randomUUID().toString();
        while (getCollection("sessions").find(eq("session_id", session_id)).first() != null) {
            session_id = UUID.randomUUID().toString();
        }
        Document document = new Document();
        document.put("session_id", session_id);
        document.put("user_id", user.getId());
        document.put("created_at", LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString());
        document.put("active", true);
        document.put("last_activity", LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString());

        if ( insert("sessions", document) == 1 ) {
            manageUserSessions(user.getId());
            return session_id;
        } else {
            return null;
        }
    }

    /**
     * Function to manage user sessions
     * Checks if a user has 3 sessions and removes the oldest if so.
     * 
     * @param userId the ID of the user
     */
    void manageUserSessions(ObjectId userId) {
        List<Document> sessions = getCollection("sessions").find(eq("user_id", userId)).sort(Sorts.ascending("created_at")).into(new ArrayList<>());
        if (sessions.size() >= 3) {
            // Remove the oldest session
            Document oldestSession = sessions.get(0);
            getCollection("sessions").deleteOne(eq("session_id", oldestSession.getString("session_id")));
            log("SESSION-MANAGEMENT", "Removed oldest session for user (" + userId + ")");
        }
        else{
            log("SESSION-MANAGEMENT", "No sessions to remove for user (" + userId + "), amount of sessions: " + sessions.size());
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
        // insert log to database
        try{
            if ( connected ) {
                Document document = new Document();
                document.put("log_category", log_category);
                document.put("log_text", log_text);
                document.put("log_date", LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString());
                getCollection("logs").insertOne(document);
            }
        } catch (Exception e) {
            log("DB-LOG-ERROR", "Failed to insert log to database (" + e.getMessage() + ")");
        }
    }
}