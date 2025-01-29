/**
 * @author Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.planorama.backend.services;

import com.jakubwawak.planorama.PlanoramaApplication;
import com.jakubwawak.planorama.backend.database_service.DatabaseUser;
import com.jakubwawak.planorama.backend.entity.User;
import com.vaadin.flow.server.VaadinSession;

import oshi.util.tuples.Pair;

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

    /**
     * Login user service function
     * @param email
     * @param password
     * @return int
     * 1 - success
     * 0 - user not found
     * -1 - wrong password
     * -2 - failed to create session
     */
    public Pair<Integer, String> loginUser(String email, String password) {
        // verify if user exists with email
        if ( databaseUser.isUserRegistered(email) ) {
            // verify if password is correct
            User user = databaseUser.loginUser(email, password);
            if (user != null) {
                // create session
                String session_id = PlanoramaApplication.database.createSession(user);
                if ( session_id != null ) {
                    VaadinSession.getCurrent().setAttribute("planorama_session_cookie", session_id);
                    PlanoramaApplication.database.log("USER-LOGIN-SUCCESS", "User logged ()"+user.getEmail()+") in with session id: " + session_id);
                    return new Pair<>(1, session_id);
                } else {
                    PlanoramaApplication.database.log("USER-LOGIN-FAILED", "Failed to create session");
                    return new Pair<>(-2, null);
                }
            } else {
                // wrong password
                return new Pair<>(-1, null);
            }
        }
        // user not found
        return new Pair<>(0, null);
    }

    /**
     * Logout user service function
     * @param user
     * @return int
     * 1 - success
     * 0 - user not found
     */
    public int logoutUser(User user){
        if (user != null){
            String session_id = VaadinSession.getCurrent().getAttribute("planorama_session_cookie").toString();
            PlanoramaApplication.database.removeSession(session_id);
            return 1;
        }
        return 0;
    }

    /**
     * Function for getting user by session id
     * @param session_id
     * @return
     */
    public User getUserBySessionId(String session_id) {
        return databaseUser.getUserBySessionId(session_id);
    }   

    /**
     * Function for updating user password
     * @param user
     * @param password
     * @return int
     * 1 - success
     * 0 - failed
     */
    public int updateUserPassword(User user, String password) {
        user.setPassword(password);
        User updated_user = databaseUser.updateUser(user);
        if (updated_user != null){
            PlanoramaApplication.database.log("USER-PASSWORD-UPDATE-SUCCESS", "User password updated (" + user.getId() + ")");
            return 1;
        }
        PlanoramaApplication.database.log("USER-PASSWORD-UPDATE-FAILED", "Failed to update user password (" + user.getId() + ")");
        return 0;
    }

}
