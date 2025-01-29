/**
 * @author Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.planorama.backend.services;

import com.jakubwawak.planorama.PlanoramaApplication;
import com.jakubwawak.planorama.backend.database_service.DatabaseUser;

/**
 * Service for registering user to the app
 */
public class RegistrationService {

    DatabaseUser databaseUser;

    /**
     * Constructor
     */
    public RegistrationService() {
        databaseUser = new DatabaseUser();
    }

    /**
     * Register user service function
     * @param email
     * @param password
     * @return int
     * 1 - success
     * 0 - user already registered
     * -1 - failed to register user
     */
    public int registerUser(String email, String password) {
        if ( databaseUser.isUserRegistered(email) ) {
            return 0;
        }
        try{
            int id = databaseUser.registerUser(email, password);
            if ( id != -1 ) {
                PlanoramaApplication.database.log("USER-REGISTER-SUCCESS", "User registered with email: " + email);
                return 1;
            } else {
                PlanoramaApplication.database.log("USER-REGISTER-FAILED", "Failed to register user");
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }
}
