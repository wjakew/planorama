/**
 * @author Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.planorama.backend.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import com.jakubwawak.planorama.PlanoramaApplication;
import com.jakubwawak.planorama.backend.entity.User;
import com.jakubwawak.planorama.backend.services.LoginService;
import com.jakubwawak.planorama.backend.ui.components.windows.windows_logged.SettingsWindow;


/**
 * Header component for the app
 */
public class HeaderComponent extends HorizontalLayout{

    Button logout_button, settings_button;
    User user;
    
    /**
     * Constructor
     */
    public HeaderComponent(User user) {
        addClassName("header-component");
        this.user = user;

        prepare_components();
        prepare_layout();

        setWidthFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setVerticalComponentAlignment(Alignment.CENTER);
    }

    /**
     * Function for preparing components
     */
    void prepare_components() {
        logout_button = new Button("Logout");

        logout_button.addClassName("default-button");

        settings_button = new Button("Settings");
        settings_button.addClassName("secondary-button");

        logout_button.addClickListener(e -> {
            LoginService loginService = new LoginService();
            loginService.logoutUser(user);
            UI.getCurrent().navigate("/welcome");
        });
        
        settings_button.addClickListener(e -> {
            SettingsWindow settingsWindow = new SettingsWindow();
            add(settingsWindow.main_dialog);
            settingsWindow.main_dialog.open();
        });

        logout_button.getStyle().set("margin", "0 10px");
        settings_button.getStyle().set("margin", "0 10px");
    }

    /**
     * Function for preparing layout
     */
    void prepare_layout() {
        FlexLayout left_layout = new FlexLayout();
        left_layout.setSizeFull();
        left_layout.setJustifyContentMode(JustifyContentMode.START);
        left_layout.setAlignItems(Alignment.CENTER);
        left_layout.setWidth("80%");

        H6 header = new H6("Welcome, " + user.getEmail()+ "in planorama.");
        header.getStyle().set("margin","0 10px");
        
        left_layout.add(header);

        FlexLayout right_layout = new FlexLayout();
        right_layout.setSizeFull();
        right_layout.setJustifyContentMode(JustifyContentMode.END);
        right_layout.setAlignItems(FlexComponent.Alignment.CENTER);
        right_layout.add(settings_button, logout_button);
        right_layout.setWidth("80%");

        add(left_layout, right_layout);
    }
}
