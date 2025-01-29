/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
*/
package com.jakubwawak.planorama.backend.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * Main application web view
 */
@PageTitle("availog by Jakub Wawak")
@Route("template")
public class TemplateAnonView extends VerticalLayout {

    /**
     * Constructor
     */
    public TemplateAnonView() {
        addClassName("view");
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents() {

    }

    /**
     * Function for preparing layout data
     */
    void prepareLayout() {
        prepareComponents();

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}