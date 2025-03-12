/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.planorama.backend.entity;

import java.io.File;
import java.sql.Timestamp;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jakubwawak.planorama.PlanoramaApplication;

import org.json.JSONException;

/**
 * Task entity
 */
public class Task {
    
    public String taskTitle;
    
    public ObjectId userId;

    public String taskDescription;
    public Timestamp taskCreatedAt;
    public Timestamp taskUpdatedAt;

    public String taskStatus; // DONE, IN_PROGRESS, TODO, HOLD, CANCELLED
    public boolean taskPublic;

    public String taskColorHex;

    public JSONArray taskComments;
    public JSONArray taskSubtasks;
    public JSONArray taskAttachments;
    public JSONArray taskTags;
    public JSONArray taskHistory;

    /**
     * Constructor
     */
    public Task(){
        taskTitle = "";
        taskDescription = "";
        taskCreatedAt = new Timestamp(System.currentTimeMillis());
        taskUpdatedAt = new Timestamp(System.currentTimeMillis());
        taskStatus = "TODO";
        taskPublic = false;
        taskColorHex = "#000000";
        taskComments = new JSONArray();
        taskSubtasks = new JSONArray();
        taskAttachments = new JSONArray();
        taskTags = new JSONArray();
        taskHistory = new JSONArray();
    }

    /**
     * Constructor
     * @param document
     */
    public Task(Document document){
        taskTitle = document.getString("taskTitle");
        taskDescription = document.getString("taskDescription");
        taskCreatedAt = new Timestamp(document.getDate("taskCreatedAt").getTime());
        taskUpdatedAt = new Timestamp(document.getDate("taskUpdatedAt").getTime());
        taskStatus = document.getString("taskStatus");
        taskPublic = document.getBoolean("taskPublic");
        taskColorHex = document.getString("taskColorHex");
        taskComments = (JSONArray) document.get("taskComments");
        taskSubtasks = (JSONArray) document.get("taskSubtasks");
        taskAttachments = (JSONArray) document.get("taskAttachments");
        taskTags = (JSONArray) document.get("taskTags");
        taskHistory = (JSONArray) document.get("taskHistory");
    }

    /**
     * Get task as Document
     * @return Document
     */
    public Document getTaskAsDocument(){
        Document document = new Document();
        document.append("taskTitle", taskTitle);
        document.append("taskDescription", taskDescription);
        document.append("taskCreatedAt", taskCreatedAt);
        document.append("taskUpdatedAt", taskUpdatedAt);
        document.append("taskStatus", taskStatus);
        document.append("taskPublic", taskPublic);
        document.append("taskColorHex", taskColorHex);
        document.append("taskComments", taskComments);
        document.append("taskSubtasks", taskSubtasks);
        document.append("taskAttachments", taskAttachments);
        document.append("taskTags", taskTags);
        document.append("taskHistory", taskHistory);
        return document;
    }

    /**
     * Add comment to task
     * @param comment
     * @return
     */
    public int addComment(String comment){
        taskComments.put(comment);
        addHistory("Comment added: " + comment);
        return taskComments.length();
    }

    /**
     * Add subtask to task
     * @param subtask
     * @param subtaskDone
     * @param subtaskCreatedAt
     * @return
     */
    public int addSubtask(String subtask, boolean subtaskDone, Timestamp subtaskCreatedAt){
        JSONObject subtaskObject = new JSONObject();
        try {
            subtaskObject.put("subtask", subtask);
            subtaskObject.put("subtaskDone", subtaskDone);
            subtaskObject.put("subtaskCreatedAt", subtaskCreatedAt);
            taskSubtasks.put(subtaskObject);
            addHistory("Subtask added: " + subtask);
        } catch (JSONException e) {
            PlanoramaApplication.database.log("TASK-ADD-SUBTASK", "Error adding subtask to task: " + subtask + "("+e.toString()+")");
        }
        return taskSubtasks.length();
    }

    /**
     * Add attachment to task
     * @param file
     * @return
     */
    public int addAttachement(File file){
        try{
            JSONObject attachmentObject = new JSONObject();
            attachmentObject.put("attachmentName", file.getName());
            attachmentObject.put("absolutePath", file.getAbsolutePath());
            attachmentObject.put("attachmentSize", file.length());
            attachmentObject.put("attachmentTimestamp", new Timestamp(file.lastModified()));
            taskAttachments.put(attachmentObject);
            addHistory("Attachment added: " + file.getName());
        } catch (JSONException e) {
            PlanoramaApplication.database.log("TASK-ADD-ATTACHMENT", "Error adding attachment to task: " + file.getName() + "("+e.toString()+")");
        }
        return taskAttachments.length();
    }

    /**
     * Add tag to task
     * @param tag
     * @return
     */
    public int addTag(String tag){
        taskTags.put(tag);
        addHistory("Tag added: " + tag);
        return taskTags.length();
    }

    /**
     * Add history to task
     * @param history
     * @return
     */
    public int addHistory(String history){
        try{
            JSONObject historyObject = new JSONObject();
            historyObject.put("history", history);
            historyObject.put("historyTimestamp", new Timestamp(System.currentTimeMillis()));
            taskHistory.put(historyObject);
        } catch (JSONException e) {
            PlanoramaApplication.database.log("TASK-ADD-HISTORY", "Error adding history to task: " + history + "("+e.toString()+")");
        }
        return taskHistory.length();
    }
    
}
