package com.andjojo.biene;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {

    private String taskName;
    private String userID;
    private String userName;
    private String taskDescription;
    private String cat;

    public Task(JSONObject task) throws JSONException {
        this.taskName = task.getString("req_category_text");
        this.cat= task.getString("req_category");
        this.userID= task.getString("req_user_id");
        this.userName= task.getString("req_vorname") +" "+ task.getString("req_nachname");
    }
    public Task(String taskName){
        this.taskName = taskName;
    }

    public String getTaskName(){
        return taskName;
    }
}
