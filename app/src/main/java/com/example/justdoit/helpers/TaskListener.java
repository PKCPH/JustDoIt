package com.example.justdoit.helpers;

public interface TaskListener {
    void onTaskAdded(String taskName);
    void onTaskRemoved(String taskName);
}