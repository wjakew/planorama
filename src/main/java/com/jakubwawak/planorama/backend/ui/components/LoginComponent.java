/**
 * @author Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.planorama.backend.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

/**
 * LoginComponent class - component for logging in
 */
public class LoginComponent extends VerticalLayout {

    TextField emailField;
    TextField passwordField;
    Button loginButton, registerButton;

    public LoginComponent() {

        addClassName("login-component");

        prepareComponents();
        prepareLayout();

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    /**
     * Prepares components for the login page
     */
    private void prepareComponents() {
        emailField = new TextField("Email");
        emailField.setWidth("100%");
        emailField.addClassName("default-input");
        emailField.setPlaceholder("cool@planorama.com");

        passwordField = new TextField("Password");
        passwordField.setWidth("100%");
        passwordField.addClassName("default-input");
        passwordField.setPlaceholder("secretpassword");

        loginButton = new Button("Login", VaadinIcon.KEY.create());
        loginButton.addClassName("default-button");
        loginButton.setWidth("100%");

        registerButton = new Button("Register", VaadinIcon.PLUS.create());
        registerButton.addClassName("primary-button");
    }

    /**
     * Prepares layout for the login page
     */
    private void prepareLayout() {
        add(emailField, passwordField, loginButton);

        HorizontalLayout buttonLayout = new HorizontalLayout(registerButton);
        buttonLayout.setWidth("100%");
        buttonLayout.setAlignItems(Alignment.CENTER);
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        add(buttonLayout);
    }

}
