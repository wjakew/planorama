/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all right reserved
 */
package com.jakubwawak.planorama.backend.database_service;

import com.jakubwawak.planorama.PlanoramaApplication;
import com.jakubwawak.planorama.backend.entity.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

/**
 * DatabaseUser class - class for managing users in database
 */
public class DatabaseUser {

    Database database;

    /**
     * Constructor
     */
    public DatabaseUser() {
        this.database = PlanoramaApplication.database;
    }

    /**
     * Function for checking if user is registered
     * 
     * @param email
     * @return boolean
     */
    public boolean isUserRegistered(String email) {
        try {
            Document document = database.getCollection("application_users").find(eq("email", email)).first();
            if (document != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Function for registering default admin
     * 
     * @return int
     */
    public int registerDefaultAdmin() {
        User user = new User();
        user.setEmail("admin@planorama.com");
        user.setPassword("admin");
        user.setAdmin();
        user.setActive();
        return registerUser(user);
    }

    /**
     * Function for registering user
     * 
     * @param email
     * @param password
     * @return int
     */
    public int registerUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setNormalUser();
        user.setActive();
        try {
            int ans = database.insert("application_users", user.toDocument());
            if (ans == 1) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            database.log("USER-REGISTER-FAILED", e.getMessage());
            return -1;
        }
    }

    /**
     * Function for registering user
     * 
     * @param user
     * @return int
     */
    int registerUser(User user) {
        int ans = database.insert("application_users", user.toDocument());
        if (ans == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Function for logging in user
     * 
     * @param email
     * @param password
     * @return User
     */
    public User loginUser(String email, String password) {
        try{
            Document document = database.getCollection("application_users").find(eq("email", email)).first();
            if (document != null) {
                User user = new User(document);
                if (user.checkPassword(password)) {
                    database.log("USER-LOGIN-SUCCESS", "User logged in");
                    return user;
                } else {
                    database.log("USER-LOGIN-FAILED", "Wrong password");
                    return null;
                }
            } else {
                database.log("USER-LOGIN-FAILED", "User not found");
                return null;
            }
        } catch (Exception e) {
            database.log("USER-LOGIN-FAILED", e.getMessage());
            return null;
        }
    }

    /**
     * Function for updating user
     * @param user
     * @return
     */
    public User updateUser(User user){
        try{
            int ans = database.update("application_users", user.getId(), user.toDocument());
            if (ans == 1){
                database.log("USER-UPDATE-SUCCESS", "User updated (" + user.getId() + ")");
                return user;
            }
            else{
                database.log("USER-UPDATE-FAILED", "Failed to update user (" + user.getId() + ")");
                return null;
            }
        } catch (Exception e) {
            database.log("USER-UPDATE-FAILED", e.getMessage());
            return null;
        }
    }

    /**
     * Function for getting user by id
     * @param id
     * @return
     */
    public User getUserById(ObjectId id) {
        Document document = database.getCollection("application_users").find(eq("_id", id)).first();
        if (document != null) {
            return new User(document);
        } else {
            database.log("USER-GET-BY-ID", "User not found (id: " + id + ")");
            return null;
        }
    }

    /**
     * Function for getting user by session id
     * @param session_id
     * @return
     */
    public User getUserBySessionId(String session_id) {
        Document document = database.getCollection("sessions").find(eq("session_id", session_id)).first();
        if (document != null) {
            if (document.getBoolean("active")) {
                database.log("USER-GET-BY-SESSION-ID", "Session used by user (" + document.getObjectId("user_id") + ") (session_id: " + session_id + ")");
                return getUserById(document.getObjectId("user_id"));
            } else {
                database.log("USER-GET-BY-SESSION-ID", "Session not active (session_id: " + session_id + ")");
                return null;
            }
        } else {
            database.log("USER-GET-BY-SESSION-ID", "Session not found (session_id: " + session_id + ")");
            return null;
        }
    }
}
