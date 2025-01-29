/**
 * @author Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.planorama.backend.ui.views_logged;

import com.jakubwawak.planorama.PlanoramaApplication;
import com.jakubwawak.planorama.backend.entity.User;
import com.jakubwawak.planorama.backend.services.LoginService;
import com.jakubwawak.planorama.backend.ui.components.HeaderComponent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

/**
 * Dashboard page - main view for logged in users
 */
@PageTitle("planorama. - Dashboard")
@Route("dashboard")
public class DashboardPage extends VerticalLayout {


    LoginService loginService;
    User user;

    HeaderComponent header;

    Button return_button;
    
    /**
     * Constructor
     */
    public DashboardPage() {
        addClassName("dashboard-page");

        if (VaadinSession.getCurrent().getAttribute("planorama_session_cookie") == null) {
            return_button = new Button("Return to Login page");
            return_button.addClassName("default-button");
            return_button.addClickListener(e -> {
                UI.getCurrent().navigate("/welcome");
            });
            add(new H1("planorama."), new H6("You are not logged in, please login to continue."), return_button);
        }
        else{
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

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }

    /**
     * Function for preparing layout
     * @param user
     */
    void prepareLayout() {
        header = new HeaderComponent(user);

        add(header);
    }
}
