/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */

package com.jakubwawak.planorama.backend.ui.components.windows.windows_anonymous;

import com.jakubwawak.planorama.PlanoramaApplication;
import com.jakubwawak.planorama.backend.services.RegistrationService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Window for registering user to the app
 */
public class RegisterWindow {

    // variables for setting x and y of window
    public String width = "50%";
    public String height = "50%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    TextField email_field;
    PasswordField password_field;
    PasswordField password_repeat_field;
    
    Button register_button;
    /**
     * Constructor
     */
    public RegisterWindow() {
        main_dialog = new Dialog();
        main_dialog.addClassName("window");
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components() {
        // set components

        email_field = new TextField("Email");
        email_field.addClassName("default-input");
        email_field.setWidth("100%");
        email_field.setMaxLength(250);

        password_field = new PasswordField("Password");
        password_field.addClassName("default-input");
        password_field.setWidth("100%");
        password_field.setMaxLength(150);
        
        password_repeat_field = new PasswordField("Repeat password");
        password_repeat_field.addClassName("default-input");
        password_repeat_field.setWidth("100%");

        register_button = new Button("Register", VaadinIcon.PLUS.create(), this::registerButtonClick);
        register_button.addClassName("primary-button");
        register_button.setWidth("50%");
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog() {
        prepare_components();
        // set layout

        main_layout.add(new H1("planorama."),new H6("Create Account"),
         email_field, new H6("Create Password"),password_field, password_repeat_field, register_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");

        main_layout.getStyle().set("border-radius", "25px");
        main_layout.getStyle().set("background-color", backgroundStyle);
        main_layout.getStyle().set("--lumo-font-family", "Monospace");
        main_dialog.add(main_layout);
        main_dialog.setWidth(width);
        main_dialog.setHeight(height);
    }

    /**
     * Function for handling register button click
     * @param event
     */
    private void registerButtonClick(ClickEvent<Button> event) {
        RegistrationService registrationService = new RegistrationService();
        if ( email_field.getValue().isEmpty() || password_field.getValue().isEmpty() || password_repeat_field.getValue().isEmpty() ) {
            return;
        }
        if ( password_field.getValue().equals(password_repeat_field.getValue()) ) {
            int ans = registrationService.registerUser(email_field.getValue(), password_field.getValue());
            if ( ans == 1 ) {
                PlanoramaApplication.showNotification("User registered successfully");
                main_dialog.close();
            } else if ( ans == 0 ) {
                PlanoramaApplication.showNotification("User already registered");
            } else {
                PlanoramaApplication.showNotification("Failed to register user");
            }
        }
        else{
            PlanoramaApplication.showNotification("Passwords do not match");
        }
    }
}
