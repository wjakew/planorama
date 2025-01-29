/**
 * @author Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.planorama.backend.ui;

import com.jakubwawak.planorama.PlanoramaApplication;
import com.jakubwawak.planorama.backend.entity.User;
import com.jakubwawak.planorama.backend.services.LoginService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

/**
 * Dashboard page - main view for logged in users
 */
@PageTitle("planorama. - Template")
@Route("template-logged-view")
public class TemplateLoggedView extends VerticalLayout {


    LoginService loginService;
    User user;
    
    /**
     * Constructor
     */
    public TemplateLoggedView() {
        addClassName("template-logged-view");
        String session_id = VaadinSession.getCurrent().getAttribute("planorama_session_cookie").toString();
        loginService = new LoginService();
        user = loginService.getUserBySessionId(session_id);
        
        if (user != null) {
            prepareLayout();

        } else {
            PlanoramaApplication.database.log("USER-GET-BY-SESSION-ID", "User not found (session_id: " + session_id + ")");
            VaadinSession.getCurrent().setAttribute("planorama_session_cookie", null);
            PlanoramaApplication.showNotification("Session expired, please login again.");
            UI.getCurrent().navigate("/welcome");
        }
    }

    /**
     * Function for preparing layout
     * @param user
     */
    void prepareLayout() {
        // create layout for logged user
    }
}
