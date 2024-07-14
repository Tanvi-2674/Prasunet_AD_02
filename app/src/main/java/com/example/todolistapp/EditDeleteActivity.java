package com.example.todolistapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class EditDeleteActivity extends AppCompatActivity {

    private EditText editTaskInput;
    private Button editButton, deleteButton;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);

        editTaskInput = findViewById(R.id.editTaskInput);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);

        Intent intent = getIntent();
        String task = intent.getStringExtra("task");
        position = intent.getIntExtra("position", -1);

        editTaskInput.setText(task);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedTask = editTaskInput.getText().toString();
                if (!editedTask.isEmpty()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("editedTask", editedTask);
                    resultIntent.putExtra("position", position);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("position", position);
                setResult(RESULT_FIRST_USER, resultIntent);
                finish();
            }
        });
    }
}
