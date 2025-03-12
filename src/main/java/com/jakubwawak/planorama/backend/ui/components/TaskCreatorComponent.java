/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.planorama.backend.ui.components;

import java.util.ArrayList;

import com.jakubwawak.planorama.backend.entity.Task;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Task creator component
 */
public class TaskCreatorComponent extends VerticalLayout {

    // variables
    Task task;
    boolean advancedMode;

    // components

    // upper header
    TextField taskTitle;
    ComboBox<String> taskStatus;
    Button taskCreateButton;
    HorizontalLayout taskHeaderLayout;

    // left bottom layout
    TextArea taskDescription;
    VerticalLayout subtaskLayout;

    VerticalLayout taskLeftBottomLayout;

    // right bottom layout
    VerticalLayout taskAttachmentsLayout;
    HorizontalLayout taskTagsLayout;
    VerticalLayout taskCommentsLayout;

    VerticalLayout taskRightBottomLayout;


    /**
     * Constructor
     */
    public TaskCreatorComponent(Task task, boolean advancedMode) {
        this.task = task;
        this.advancedMode = advancedMode;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        if (advancedMode){
            prepareTaskHeaderLayout();
        }
        else{
            prepareTaskHeaderLayout();
        }
    }

    /**
     * Prepare task header layout
     */
    void prepareTaskHeaderLayout(){
        taskHeaderLayout = new HorizontalLayout();
        taskHeaderLayout.setAlignItems(Alignment.CENTER);
        taskHeaderLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        taskHeaderLayout.setWidth("100%");

        FlexLayout taskLayoutLeft = new FlexLayout();
        taskLayoutLeft.setAlignItems(Alignment.START);
        taskLayoutLeft.setJustifyContentMode(JustifyContentMode.START);

        FlexLayout taskLayoutRight = new FlexLayout();
        taskLayoutRight.setAlignItems(Alignment.END);
        taskLayoutRight.setJustifyContentMode(JustifyContentMode.END);

        taskTitle = new TextField();
        taskTitle.setLabel("title");
        taskTitle.setPlaceholder("Enter task title");
        taskTitle.setWidth("100%");

        taskStatus = new ComboBox<>();
        taskStatus.setLabel("status");
        taskStatus.setPlaceholder("Select task status");
        taskStatus.setWidth("100%");
        ArrayList<String> statuses = new ArrayList<>();
        statuses.add("TODO");
        statuses.add("HOLD");
        statuses.add("IN PROGRESS");
        statuses.add("DONE");
        taskStatus.setItems(statuses);
        taskStatus.setItemLabelGenerator(item -> item);

        taskCreateButton = new Button("create");
        taskCreateButton.addClassName("button-primary");
        taskCreateButton.setWidth("100%");

        taskLayoutLeft.add(taskTitle);
        taskLayoutRight.add(taskStatus, taskCreateButton);

        taskHeaderLayout.add(taskLayoutLeft, taskLayoutRight);
        add(taskHeaderLayout);
    }


}
