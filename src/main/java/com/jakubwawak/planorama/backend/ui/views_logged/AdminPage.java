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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * Dashboard page - main view for logged in users
 */
@PageTitle("planorama. - admin")
@Route("admin")
public class AdminPage extends VerticalLayout {

    HeaderComponent headerComponent;


    LoginService loginService;
    User user;

    Button back_button;
    
    /**
     * Constructor
     */
    public AdminPage() {
        addClassName("admin-page");
        String session_id = VaadinSession.getCurrent().getAttribute("planorama_session_cookie").toString();
        if (session_id == null) {
            add(new H1("Session expired, please login again."));
            add(back_button);
        }
        else{
            loginService = new LoginService();
            user = loginService.getUserBySessionId(session_id);
    
            back_button = new Button("Back", VaadinIcon.ARROW_LEFT.create());
            back_button.addClickListener(e -> {
                UI.getCurrent().navigate("/dashboard");
            });
            
            if (user != null) {
                if (user.isAdmin()) {
                    prepareLayout();
                } else {
                    add(new H1("You are not authorized to access this page."));
                    add(back_button);
                }
    
            } else {
                PlanoramaApplication.database.log("USER-GET-BY-SESSION-ID", "User not found (session_id: " + session_id + ")");
                VaadinSession.getCurrent().setAttribute("planorama_session_cookie", null);
                PlanoramaApplication.showNotification("Session expired, please login again.");
                UI.getCurrent().navigate("/welcome");
            }
        }
    }

    /**
     * Function for preparing layout
     * @param user
     */
    void prepareLayout() {
        // create layout for logged user
        headerComponent = new HeaderComponent(user);
        add(headerComponent);
    }
}
