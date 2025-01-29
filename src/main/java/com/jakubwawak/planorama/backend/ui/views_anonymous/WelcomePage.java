/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
*/
package com.jakubwawak.planorama.backend.ui.views_anonymous;

import com.jakubwawak.planorama.PlanoramaApplication;
import com.jakubwawak.planorama.backend.ui.components.LoginComponent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

/**
 * Welcome page - main view for anonymous users
 */
@PageTitle("planorama.")
@Route("welcome")
@RouteAlias("/")
public class WelcomePage extends VerticalLayout {

    Button loginButton;

    LoginComponent loginComponent;

    /**
     * Constructor
     */
    public WelcomePage() {
        addClassName("welcome-page");
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents() {
        loginButton = new Button("Access Planorama", VaadinIcon.SAFE_LOCK.create(), this::loginButtonClick);
        loginButton.addClassName("default-button");

        loginComponent = new LoginComponent();
        loginComponent.setVisible(false);
    }

    /**
     * Function for preparing layout data
     */
    void prepareLayout() {
        prepareComponents();
        add(new H6(PlanoramaApplication.tenantName));
        VerticalLayout main_layout = new VerticalLayout();

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");
        main_layout.add(new H1("Planorama."));
        main_layout.add(loginComponent);
        main_layout.add(loginButton);

        add(main_layout);
        add(new H6("by Jakub Wawak / all rights reserved / kubawawak@gmail.com"));
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    /**
     * Function for handling login button click
     * 
     * @param event Click event
     */
    private void loginButtonClick(ClickEvent<Button> event) {
        if (loginComponent.isVisible()) {
            loginButton.setText("Access Planorama");
            loginComponent.setVisible(false);
            loginButton.addClassName("default-button");
        } else {
            loginButton.setText("Return");
            loginComponent.setVisible(true);
            loginButton.addClassName("secondary-button");
        }
    }
}
