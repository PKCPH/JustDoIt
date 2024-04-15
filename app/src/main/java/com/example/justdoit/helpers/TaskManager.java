package com.example.justdoit.helpers;

import android.content.SharedPreferences;

import com.example.justdoit.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(tasksString);
            jsonArray = sortTasks(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonArray = new JSONArray();
        }
        return jsonArray;
    }

    //making two collections of finished and unfinshed and returning them combined into one JSONArray sorted
    private JSONArray sortTasks(JSONArray jsonArray) {
        List<String> unfinishedTasks = new ArrayList<>();
        List<String> finishedTasks = new ArrayList<>();

        // Separate tasks into unfinished and finished lists
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                String task = jsonArray.getString(i);
                if (task.endsWith("\u2713")) {
                    finishedTasks.add(task);
                } else {
                    unfinishedTasks.add(task);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(unfinishedTasks);
        Collections.sort(finishedTasks);
        unfinishedTasks.addAll(finishedTasks);
        JSONArray sortedArray = new JSONArray(unfinishedTasks);
        return sortedArray;
    }

    //marks the selected tasks as done with check mark
    public void toggleTask(String taskName, boolean finished) {
        JSONArray jsonArray = getTasksJSONArray();
        JSONArray newJsonArray = new JSONArray();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                String existingTaskName = jsonArray.getString(i);
                if (existingTaskName.equals(taskName)) {
                    // Update the state of the task
                    if (finished) {
                        newJsonArray.put(existingTaskName + "\u2713");
                        finished = true;
                    } else {
                    }
                } else {
                    newJsonArray.put(existingTaskName);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Save the updated tasks array
        sharedPreferences.edit().putString("tasks", newJsonArray.toString()).apply();
    }

    //finds matching task and check if string has checkmark
    public boolean isTaskFinished(String taskName) {
        JSONArray jsonArray = getTasksJSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                String existingTaskName = jsonArray.getString(i);
                if (existingTaskName.equals(taskName + "\u2713")) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}