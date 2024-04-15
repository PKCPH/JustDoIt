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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.justdoit.helpers.TaskManager;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    Button add;
    AlertDialog dialog;
    LinearLayout layout;
    SharedPreferences sharedPreferences;
    TaskManager taskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //getting saved tasks
        sharedPreferences = getSharedPreferences("Tasks", MODE_PRIVATE);
        taskManager = new TaskManager(sharedPreferences);

        add=findViewById(R.id.add);
        layout=findViewById(R.id.container);

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
                        taskManager.saveTask(taskName);
                        name.setText("");
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
    private void addCard(String name) {
        final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        final CheckBox checkBox = view.findViewById(R.id.checkbox);
        Button delete = view.findViewById(R.id.delete);
        nameView.setText(name);

        boolean finished = taskManager.isTaskFinished(name);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
                taskManager.removeTask(name);
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskManager.toggleTask(name, checkBox.isChecked());
                loadTasks();
            }
        });
        layout.addView(view);
    }


    //adds the loaded tasks to the UI
    private void loadTasks() {
        layout.removeAllViews();
        JSONArray jsonArray = taskManager.getTasksJSONArray();
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