package com.andjojo.biene;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Task implements Serializable {

    private String taskName;
    private String taskHeader;
    private String userID;
    private String userName;
    private String taskDescription;
    private String cat;

    public Task(JSONObject task) throws JSONException {
        this.taskName = task.getString("req_category_text");
        this.cat= task.getString("req_category");
        if (taskName.contains(";")&&taskName.contains(":")){
            taskHeader=taskName.split(":")[1];
        }
        else{
            taskHeader=cat;
        }
        this.taskDescription=taskName.split(";")[0];
        this.userID= task.getString("req_user_id");
        this.userName= task.getString("req_vorname");
    }
    public Task(String taskName){
        this.taskName = taskName;
    }

    public String getTaskName(){
        return taskName;
    }
    public String getTaskHeader(){
        return taskHeader;
    }
    public String getTaskDescription(){
        return taskDescription;
    }
    public String getCat(){
        return cat;
    }
    public String getUserName(){
        return userName;
    }
    public String getUserID(){
        return userID;
    }
}
