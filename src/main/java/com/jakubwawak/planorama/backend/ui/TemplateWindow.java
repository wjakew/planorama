/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.planorama.backend.ui;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Window for logging user to the app
 */
public class TemplateWindow {

    // variables for setting x and y of window
    public String width = "";
    public String height = "";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    /**
     * Constructor
     */
    public TemplateWindow() {
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components() {
        // set components
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog() {
        prepare_components();
        // set layout

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