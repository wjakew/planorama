/**
 * @author Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.planorama.backend.ui.components;

import com.jakubwawak.planorama.PlanoramaApplication;
import com.jakubwawak.planorama.backend.services.LoginService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;

import oshi.util.tuples.Pair;

/**
 * LoginComponent class - component for logging in
 */
public class LoginComponent extends VerticalLayout {

    TextField emailField;
    PasswordField passwordField;
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

        passwordField = new PasswordField("Password");
        passwordField.setWidth("100%");
        passwordField.addClassName("default-input");
        passwordField.setPlaceholder("secretpassword");

        loginButton = new Button("Login", VaadinIcon.KEY.create(), this::loginButtonClick);
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

    /**
     * Function for handling login button click
     * @param event
     */
    private void loginButtonClick(ClickEvent<Button> event) {
        String email = emailField.getValue();
        String password = passwordField.getValue();
        LoginService loginService = new LoginService();
        Pair<Integer, String> loginResult = loginService.loginUser(email, password);
        int result = loginResult.getA();
        if (result == 1) {
            VaadinSession.getCurrent().setAttribute("planorama_session_cookie", loginResult.getB());
            UI.getCurrent().navigate("/dashboard");
            PlanoramaApplication.showNotification("Login successful, redirecting to dashboard...");
        } else if (result == 0) {
            PlanoramaApplication.showNotification("User not found, please check your email and password.");
            passwordField.clear();
        } else if (result == -1) {
            PlanoramaApplication.showNotification("Wrong password, please try again.");
            passwordField.clear();
        } else if (result == -2) {
            PlanoramaApplication.showNotification("Failed to create session, please try again.");
            passwordField.clear();
        }
    }
}
