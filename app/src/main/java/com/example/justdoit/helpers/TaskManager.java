package com.example.justdoit.helpers;

import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;

public class TaskManager {
    private SharedPreferences sharedPreferences;

    public TaskManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    //getting tasks, added the new one and updating array
    public void saveTask(String taskName) {
        JSONArray jsonArray = getTasksJSONArray();
        jsonArray.put(taskName);
        sharedPreferences.edit().putString("tasks", jsonArray.toString()).apply();
    }

    //Retrieve existing tasks, remove the selected task and then update the array of task
    public void removeTask(String taskName) {
        JSONArray jsonArray = getTasksJSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                if (jsonArray.getString(i).equals(taskName)) {
                    jsonArray.remove(i);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        sharedPreferences.edit().putString("tasks", jsonArray.toString()).apply();
    }

    //gets the array of all our stored tasks in SharePreferences, if doesn't exists then return new JsonArray
    public JSONArray getTasksJSONArray() {
        String tasksString = sharedPreferences.getString("tasks", "[]");
        try {
            return new JSONArray(tasksString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }
}
