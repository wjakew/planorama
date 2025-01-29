/**
 * @author Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.planorama.backend.services;

import com.jakubwawak.planorama.backend.database_service.DatabaseUser;

/**
 * Service for maintaining login and session
 */
public class LoginService {

    DatabaseUser databaseUser;

    /**
     * Constructor
     */
    public LoginService() {
        databaseUser = new DatabaseUser();
    }

}
