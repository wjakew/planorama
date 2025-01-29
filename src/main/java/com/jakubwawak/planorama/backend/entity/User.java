/**
 * @author Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.planorama.backend.entity;

import java.security.SecureRandom;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.jakubwawak.planorama.PlanoramaApplication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * User entity - user of the application
 */
public class User {

    ObjectId id;
    String email;
    String password;
    String passwordSalt;
    boolean admin;
    boolean active;

    /**
     * Constructor
     */
    public User() {
        id = null;
        email = "";
        password = "";
        passwordSalt = "";
        admin = false;
        active = false;
    }

    /**
     * Constructor
     * 
     * @param document
     */
    public User(Document document) {
        id = document.getObjectId("_id");
        email = document.getString("email");
        password = document.getString("password");
        passwordSalt = document.getString("passwordSalt");
        admin = document.getBoolean("admin");
        active = document.getBoolean("active");
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setAdmin() {
        this.admin = true;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setNormalUser() {
        this.admin = false;
    }

    public void setActive() {
        this.active = true;
    }

    public void setInactive() {
        this.active = false;
    }

    /**
     * Function for converting user to document
     * 
     * @return Document
     */
    public Document toDocument() {
        Document document = new Document();
        document.append("email", email);
        document.append("password", password);
        document.append("passwordSalt", passwordSalt);
        document.append("admin", admin);
        document.append("active", active);
        return document;
    }

    /**
     * Function for setting password
     * 
     * @param password
     */
    public void setPassword(String password) {
        // Generate salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        this.passwordSalt = Base64.getEncoder().encodeToString(salt);
        // Hash password
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            this.password = Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function for checking if password is correct
     * 
     * @param password
     * @return boolean
     */
    public boolean checkPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            // Use the salt stored in the user object
            md.update(Base64.getDecoder().decode(passwordSalt)); // Decode the stored salt
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Arrays.equals(hashedPassword, Base64.getDecoder().decode(this.password));
        } catch (NoSuchAlgorithmException e) {
            PlanoramaApplication.database.log("USER-CHECK-PASSWORD-FAILED", e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}
