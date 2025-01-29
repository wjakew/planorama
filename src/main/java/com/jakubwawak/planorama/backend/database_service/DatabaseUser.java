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
}
