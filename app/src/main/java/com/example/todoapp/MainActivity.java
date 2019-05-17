package com.example.todoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.security.InvalidParameterException;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
                                                    CursorRecyclerViewAdapter.OnButtonClickListener {
    private static final String TAG = "MainActivity";

    public static final int LOADER_ID = 0;

    private CursorRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: starts");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showListOfTasks();

        FloatingActionButton fab = findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: fab called");
                startActivityForResult( new Intent(MainActivity.this, AddTaskActivity.class),
                            1);
            }
        });

        Log.d(TAG, "onCreate: ends");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            switch (resultCode) {
                //save button was selected
                case RESULT_OK:
                    showListOfTasks();
                    Toast.makeText(this, R.string.added_task_info, Toast.LENGTH_LONG).show();
                    break;
                //cancel button was selected
                case RESULT_FIRST_USER:
                    break;
                //return was selected
                case RESULT_CANCELED:
                    showUnsuccessfulAddedDialog();
            }
        }
    }

//    LoaderManager
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        Log.d(TAG, "onCreateLoader: called with " + i);
        String[] columns = {TasksTable.Column._ID, TasksTable.Column.NAME,
                                TasksTable.Column.END_DATE, TasksTable.Column.CATEGORY};
        if(i == LOADER_ID) {
            return new CursorLoader(this,
                    TasksTable.CONTENT_URI,
                    columns,
                    null,
                    null,
                    null);
        } else {
            throw new InvalidParameterException("Invalid loader i: " + i);
        }
    }

//    LoaderManager
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished: called");
        int count = -1;

        if(cursor != null) {
            while (cursor.moveToNext()) {
                for(int i = 0; i < cursor.getColumnCount(); i++) {
                    Log.d(TAG, "onCreate: " + cursor.getColumnName(i) + "; " + cursor.getString(i));
                }
                Log.d(TAG, "onCreate: ------------------");
            }
            count = cursor.getCount();
        }
        Log.d(TAG, "onLoadFinished: count is " + count);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: called");
        adapter.swapCursor(null);
    }

//    CursorRecyclerViewAdapter
    @Override
    public void deleteClick(Task task) {
        getContentResolver().delete(TasksTable.buildUri(task.getId()), null, null);
        showListOfTasks();
    }

    private void showListOfTasks() {
        String[] projections = {TasksTable.Column._ID,
                TasksTable.Column.NAME,
                TasksTable.Column.CATEGORY,
                TasksTable.Column.END_DATE};

        Cursor cursor = getContentResolver().query(TasksTable.CONTENT_URI,
                projections,
                null,
                null,
                TasksTable.Column._ID + " DESC");

        recyclerView = (RecyclerView) findViewById(R.id.todo_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CursorRecyclerViewAdapter(cursor, this);
        recyclerView.setAdapter(adapter);

        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
    }

    private void showUnsuccessfulAddedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.uncorrect_added_info)
                .setCancelable(true)
                .setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(
                                new Intent(MainActivity.this, AddTaskActivity.class),
                                1);
                    }
                })
                .setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}
