/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.planorama.backend.ui.components.windows.windows_logged;

import com.jakubwawak.planorama.PlanoramaApplication;
import com.jakubwawak.planorama.backend.entity.User;
import com.jakubwawak.planorama.backend.services.LoginService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.button.Button;

/**
 * Window for settings
 */
public class SettingsWindow {

    // variables for setting x and y of window
    public String width = "50%";
    public String height = "50%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    LoginService loginService;

    User current_user;

    PasswordField password_field;
    PasswordField password_repeat_field;
    Button save_button;

    Button admin_panel;

    /**
     * Constructor
     */
    public SettingsWindow() {
        main_dialog = new Dialog();
        main_dialog.addClassName("window");
        main_layout = new VerticalLayout();
        String session_id = VaadinSession.getCurrent().getAttribute("planorama_session_cookie").toString();
        loginService = new LoginService();
        current_user = loginService.getUserBySessionId(session_id);

        if (current_user != null) {
            prepare_dialog();
        } else {
            PlanoramaApplication.database.log("USER-GET-BY-SESSION-ID", "User not found (session_id: " + session_id + ")");
            VaadinSession.getCurrent().setAttribute("planorama_session_cookie", null);
            PlanoramaApplication.showNotification("Session expired, please login again.");
            UI.getCurrent().navigate("/welcome");
        }
    }

    /**
     * Function for preparing components
     */
    void prepare_components() {
        // set components
        password_field = new PasswordField("Password");
        password_field.setWidth("100%");
        password_field.setMaxLength(150);
        password_field.addClassName("default-input");
        password_field.setPlaceholder("secretpassword");
        password_field.addClassName("default-input");

        password_repeat_field = new PasswordField("Repeat password");
        password_repeat_field.setWidth("100%");
        password_repeat_field.setMaxLength(150);
        password_repeat_field.addClassName("default-input");
        password_repeat_field.setPlaceholder("secretpassword");
        password_repeat_field.addClassName("default-input");

        save_button = new Button("Save Password", VaadinIcon.EDIT.create(), this::save_password_click);
        save_button.addClassName("default-button");
        save_button.setWidth("100%");

        admin_panel = new Button("Admin panel", VaadinIcon.ROCKET.create());
        admin_panel.addClassName("primary-button");
        admin_panel.setWidth("100%");

        admin_panel.addClickListener(e -> {
            UI.getCurrent().navigate("/admin");
        });
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog() {
        prepare_components();
        // set layout
        main_layout.add(new H1("Settings"), password_field, password_repeat_field, save_button);

        if ( current_user.isAdmin() ) {
            main_layout.add(admin_panel);
        }

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
     * Function for saving password
     * @param event
     */
    private void save_password_click(ClickEvent<Button> event) {
        if ( password_field.getValue().equals(password_repeat_field.getValue()) ) {
            loginService.updateUserPassword(current_user, password_field.getValue());
            PlanoramaApplication.showNotification("Password updated successfully");
        }
        else {
            PlanoramaApplication.showNotification("Passwords do not match");
        }
    }
}
