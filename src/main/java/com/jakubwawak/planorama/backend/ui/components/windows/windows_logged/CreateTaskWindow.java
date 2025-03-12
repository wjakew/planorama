/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.planorama.backend.ui.components.windows.windows_logged;

import com.jakubwawak.planorama.PlanoramaApplication;
import com.jakubwawak.planorama.backend.entity.Task;
import com.jakubwawak.planorama.backend.entity.User;
import com.jakubwawak.planorama.backend.services.LoginService;
import com.jakubwawak.planorama.backend.ui.components.TaskCreatorComponent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;

/**
 * Window for logging user to the app
 */
public class CreateTaskWindow {

    // variables for setting x and y of window
    public String width = "80%";
    public String height = "80%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    LoginService loginService;

    User current_user;

    Task task;
    
    TaskCreatorComponent taskCreationComponent;

    /**
     * Constructor
     */
    public CreateTaskWindow(Task task) {
        this.task = task;
        main_dialog = new Dialog();
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
        if ( task == null){
            taskCreationComponent = new TaskCreatorComponent(null, true);
        }
        else{
            task = new Task();
            task.userId = current_user.getId();
            taskCreationComponent = new TaskCreatorComponent(task, true);
        }
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog() {
        prepare_components();
        // set layout

        main_layout.add(taskCreationComponent);

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
}