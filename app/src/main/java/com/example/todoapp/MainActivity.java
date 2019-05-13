package com.example.todoapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.security.InvalidParameterException;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
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

        String[] projections = { TasksTable.Column._ID,
                                TasksTable.Column.NAME,
                                TasksTable.Column.CATEGORY,
                                TasksTable.Column.END_DATE};
        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(TasksTable.CONTENT_URI,
                projections,
                null,
                null,
                TasksTable.Column.NAME);

        ContentValues values = new ContentValues();

//        int count = contentResolver.delete(TasksTable.buildUri(4), null, null);

//        values.put(TasksTable.Column.CATEGORY, "inne2");
//        String selection = TasksTable.Column.CATEGORY + " = 'inne'";
//        int count = contentResolver.update(TasksTable.CONTENT_URI, values, selection, null);
//        Log.d(TAG, "onCreate: count " + count);

//        values.put(TasksTable.Column.NAME, "Bbb");
//        int count = contentResolver.update(TasksTable.buildUri(2), values, null, null);
//        Log.d(TAG, "onCreate: count " + count);

        //        values.put(TasksTable.Column.NAME, "Nowy");
//        values.put(TasksTable.Column.CATEGORY, "inne");
//        values.put(TasksTable.Column.END_DATE, "jutro");
//        Uri uri = contentResolver.insert(TasksTable.CONTENT_URI, values);
//        Log.d(TAG, "onCreate: new uri " + uri);

//        if(cursor != null) {
//            Log.d(TAG, "onCreate: number of rows: " + cursor.getCount());
//            while (cursor.moveToNext()) {
//                for(int i = 0; i < cursor.getColumnCount(); i++) {
//                    Log.d(TAG, "onCreate: " + cursor.getColumnName(i) + "; " + cursor.getString(i));
//                }
//                Log.d(TAG, "onCreate: ------------------");
//            }
//            cursor.close();
//        }

        recyclerView = (RecyclerView) findViewById(R.id.todo_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CursorRecyclerViewAdapter(cursor);
        recyclerView.setAdapter(adapter);

//        final AppDatabase dbHelper = AppDatabase.getInstance(this);
//        try {
//            final SQLiteDatabase db = dbHelper.getReadableDatabase();
//            Cursor cursor = db.query(TodoContent.TABLE_NAME,
//                    new String[] {TodoContent.Column.NAME},
//                    null,
//                    null,
//                    null,
//                    null,
//                    null);
//
//            if(cursor != null) {
//                List<String> names = new ArrayList<>();
//                if(cursor.moveToFirst()) {
//                    do {
//                        names.add(cursor.getString(cursor.getColumnIndex(TodoContent.Column.NAME)));
//                    } while (cursor.moveToNext());
//                } else {
//                    Toast.makeText(MainActivity.this, R.string.no_list_info, Toast.LENGTH_LONG).show();
//                    Log.d(TAG, "onCreate: niema nix");
//                }



//               ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                        MainActivity.this,
//                        R.layout.content_main_list_detail,
//                        R.id.todo_name,
//                        names);
//                todoList.setAdapter(adapter);



                //recyclerView.setLayoutManager(new LinearLayoutManager(this));
//                adapter = new CursorRececlerViewAdapter(null);
//                recyclerView.setAdapter(adapter);
//            }
//            cursor.close();
//            db.close();
//
//        } catch(SQLException e) {
//            Log.d(TAG, "onCreate: baza danych jest niedostępna");
//        }

        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);

        FloatingActionButton fab = findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: fab called");
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
            }
        });

        Log.d(TAG, "onCreate: ends");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        //todo dokonczyc
    }
}
