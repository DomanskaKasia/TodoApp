package com.example.todoapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class AppProvider extends ContentProvider {
    private static final String TAG = "AppProvider";

    private AppDatabase dbHelper;

    static final String CONTENT_AUTHORITY = "com.example.todoapp.provider";
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final int TASKS = 100;
    private static final int TASKS_ID = 101;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(CONTENT_AUTHORITY, TasksTable.TABLE_NAME, TASKS);
        uriMatcher.addURI(CONTENT_AUTHORITY, TasksTable.TABLE_NAME + "/#", TASKS_ID);
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate: called");
        dbHelper = AppDatabase.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query: called with uri " + uri);
        final int match = uriMatcher.match(uri);
        Log.d(TAG, "query: match is " + match);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (match) {
            case TASKS:
                queryBuilder.setTables(TasksTable.TABLE_NAME);
                break;
            case TASKS_ID:
                queryBuilder.setTables(TasksTable.TABLE_NAME);
                long taskId = TasksTable.getTaskId(uri);
                queryBuilder.appendWhere(TasksTable.Column._ID + " = " + taskId);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        try {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        } catch (NullPointerException e) {
            Log.d(TAG, "query: cannot get content resolver");
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(TAG, "getType: called");
        final int match = uriMatcher.match(uri);
        Log.d(TAG, "query: match is: " + match);

        switch (match) {
            case TASKS:
                return TasksTable.CONTENT_TYPE;
            case TASKS_ID:
                return TasksTable.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert: called with uri " + uri);
        final int match = uriMatcher.match(uri);
        Log.d(TAG, "query: match is " + match);

        final SQLiteDatabase db;
        Uri newUri;
        long recordId;

        if(match == TASKS) {
                db = dbHelper.getWritableDatabase();
                recordId = db.insert(TasksTable.TABLE_NAME, null, values);
                if(recordId >= 0) {
                    newUri = TasksTable.buildUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
        } else {
            throw new IllegalArgumentException("Unknown uri " + uri);
        }

        if(recordId >= 0) {
            Log.d(TAG, "insert: setting notify change with " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        }

        Log.d(TAG, "insert: return uri " + newUri);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete: called with uri: " + uri);
        final int match = uriMatcher.match(uri);
        Log.d(TAG, "query: match is " + match);

        final SQLiteDatabase db;
        int count;
        StringBuilder criteria;

        switch (match) {
            case TASKS:
                db = dbHelper.getWritableDatabase();
                count = db.delete(TasksTable.TABLE_NAME, selection, selectionArgs);
                break;
            case TASKS_ID:
                db = dbHelper.getWritableDatabase();
                long taskId = TasksTable.getTaskId(uri);
                criteria = new StringBuilder(TasksTable.Column._ID + " = " + taskId);

                if(selection != null && selection.length() > 0) {
                    criteria.append(" AND (" + selection + ")");
                }

                count = db.delete(TasksTable.TABLE_NAME, criteria.toString(), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        if(count > 0) {
            Log.d(TAG, "delete: setting notify change with " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        }

        Log.d(TAG, "delete: return count " + count);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "update: called with uri " + uri);
        final int match = uriMatcher.match(uri);
        Log.d(TAG, "query: match is " + match);

        final SQLiteDatabase db;
        int count;
        StringBuilder criteria;

        switch (match) {
            case TASKS:
                db = dbHelper.getWritableDatabase();
                count = db.update(TasksTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TASKS_ID:
                db = dbHelper.getWritableDatabase();
                long taskId = TasksTable.getTaskId(uri);
                criteria = new StringBuilder(TasksTable.Column._ID + " = " + taskId);

                if(selection != null && selection.length() > 0) {
                    criteria.append(" AND (" + selection + ")");
                }

                count = db.update(TasksTable.TABLE_NAME, values, criteria.toString(), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        if(count > 0) {
            Log.d(TAG, "update: setting notify change with " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        }

        Log.d(TAG, "update: return count " + count);
        return count;
    }
}
