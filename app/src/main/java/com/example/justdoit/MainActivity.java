package com.example.justdoit;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    Button add;
    AlertDialog dialog;
    LinearLayout layout;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        add=findViewById(R.id.add);
        layout=findViewById(R.id.container);

        sharedPreferences = getSharedPreferences("Tasks", MODE_PRIVATE);

        buildDialog();
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.show();
            }
        });
        loadTasks();
    }

    //dialog for when adding a new task
    public void buildDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        final EditText name= view.findViewById(R.id.nameEdit);

        builder.setView(view);
        builder.setTitle("Enter your task")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String taskName = name.getText().toString();
                        addCard(taskName);
                        // Save the task
                        saveTask(taskName);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog = builder.create();
    }

    //add tasks to the UI nad sets a click listener for the delete button
    private void addCard(String name){
        final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView  = view.findViewById(R.id.name);
        Button delete = view.findViewById(R.id.delete);
        nameView.setText(name);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
                removeTask(name);
            }
        });
        layout.addView(view);
    }

    //getting tasks, added the new one and updating array
    private void saveTask(String taskName) {
        JSONArray jsonArray = getTasksJSONArray();
        jsonArray.put(taskName);
        sharedPreferences.edit().putString("tasks", jsonArray.toString()).apply();
    }

    //Retrieve existing tasks, remove the selected task and then update the array of task
    private void removeTask(String taskName) {
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
    private JSONArray getTasksJSONArray() {
        String tasksString = sharedPreferences.getString("tasks", "[]");
        try {
            return new JSONArray(tasksString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    //adds the loaded tasks to the UI
    private void loadTasks() {
        JSONArray jsonArray = getTasksJSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                String taskName = jsonArray.getString(i);
                addCard(taskName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
