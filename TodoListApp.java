package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TodoListApp extends AppCompatActivity {

    private ArrayList<String> tasks;
    private ArrayAdapter<String> adapter;
    private EditText taskInput;
    private ListView taskListView;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasks = new ArrayList<>();
        loadTasks();  // Load saved tasks

        taskInput = findViewById(R.id.taskInput);
        taskListView = findViewById(R.id.taskListView);
        addButton = findViewById(R.id.addButton);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasks);
        taskListView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = taskInput.getText().toString();
                if (!task.isEmpty()) {
                    tasks.add(task);
                    adapter.notifyDataSetChanged();
                    taskInput.setText("");
                    saveTasks();  // Save tasks when a new task is added
                } else {
                    Toast.makeText(TodoListApp.this, "Please enter a task", Toast.LENGTH_SHORT).show();
                }
            }
        });

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditDeleteDialog(position);
            }
        });
    }

    private void showEditDeleteDialog(final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_delete, null);
        dialogBuilder.setView(dialogView);

        final EditText editTaskInput = dialogView.findViewById(R.id.editTaskInput);
        Button editButton = dialogView.findViewById(R.id.editButton);
        Button deleteButton = dialogView.findViewById(R.id.deleteButton);

        editTaskInput.setText(tasks.get(position));
        AlertDialog dialog = dialogBuilder.create();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedTask = editTaskInput.getText().toString();
                if (!editedTask.isEmpty()) {
                    tasks.set(position, editedTask);
                    adapter.notifyDataSetChanged();
                    saveTasks();  // Save tasks when a task is edited
                    dialog.dismiss();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasks.remove(position);
                adapter.notifyDataSetChanged();
                saveTasks();  // Save tasks when a task is deleted
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void saveTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.todolistapp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(getString(R.string.tasks), new HashSet<>(tasks));
        editor.apply();
    }

    private void loadTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.todolistapp", MODE_PRIVATE);
        Set<String> taskSet = sharedPreferences.getStringSet("tasks", new HashSet<>());
        tasks.clear();
        tasks.addAll(taskSet);
    }
}
