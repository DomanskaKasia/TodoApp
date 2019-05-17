package com.example.todoapp;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "AddTaskActivity";

    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        //adding date
        TextView tv = (TextView) findViewById(R.id.add_date);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = (TextView) findViewById(R.id.add_date);
                showDatePickerDialog();
            }
        });


        //adding task to tasks list
        ((Button) findViewById(R.id.add_request_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: adding btn");

                TextView name = (TextView) findViewById(R.id.add_name);
                TextView date = (TextView) findViewById(R.id.add_date);
                Spinner category = (Spinner) findViewById(R.id.add_category_spinner);

                String nameText = name.getText().toString();
                if(nameText.length() == 0 || nameText.trim().equals("")) {
                    Toast.makeText(AddTaskActivity.this, R.string.name_validation_info, Toast.LENGTH_LONG).show();
                    return;
                }

                String dateText = date.getText().toString();
                if(!dateText.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                    dateText = "";
                }

                ContentValues values = new ContentValues();
                values.put(TasksTable.Column.NAME, nameText);
                values.put(TasksTable.Column.END_DATE, dateText);
                values.put(TasksTable.Column.CATEGORY, category.getSelectedItem().toString());

                getContentResolver().insert(TasksTable.CONTENT_URI, values);

                Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //cancel adding to list
        ((Button) findViewById(R.id.cancel_adding_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: cancel adding btn");

                Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
                setResult(RESULT_FIRST_USER, intent);
                finish();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog dialog = new DatePickerDialog (
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date.setText(dayOfMonth + "/" + month + "/" + year);
    }
}
