package com.example.todoapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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

public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "AddTaskActivity";

    private ListView todoList;
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

        //adding to list
        ((Button) findViewById(R.id.add_request_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: adding btn");

                todoList = findViewById(R.id.todo_list);

                final AppDatabase dbHelper = AppDatabase.getInstance(AddTaskActivity.this);
                try {
                    final SQLiteDatabase db = dbHelper.getWritableDatabase();

                    TextView name = (TextView) findViewById(R.id.add_name);
                    TextView date = (TextView) findViewById(R.id.add_date);
                    Spinner category = (Spinner) findViewById(R.id.add_category_spinner);

                    dbHelper.insertTodo(db, String.valueOf(name.getText()),
                            String.valueOf(date.getText()), String.valueOf(category.getSelectedItem()));

                    db.close();

                    startActivity(new Intent(AddTaskActivity.this, MainActivity.class));

                } catch(SQLException e) {
                    Log.d(TAG, "onCreate: baza danych jest niedostępna");
                }
            }
        });

        //cancel adding to list
        ((Button) findViewById(R.id.cancel_adding_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: cancel adding btn");
                startActivity(new Intent(AddTaskActivity.this, MainActivity.class));
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
