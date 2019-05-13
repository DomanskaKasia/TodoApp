package com.example.todoapp;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.example.todoapp.AppProvider.CONTENT_AUTHORITY;
import static com.example.todoapp.AppProvider.CONTENT_AUTHORITY_URI;

public class TasksTable {
    static final String TABLE_NAME = "Tasks";

    public static class Column {
        public static final String _ID = BaseColumns._ID;
        public static final String NAME = "Name";
        public static final String END_DATE = "Date_of_completion";
        public static final String CATEGORY = "Category";
    }

    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;

    static Uri buildUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    static long getTaskId(Uri uri) {
        return ContentUris.parseId(uri);
    }
}
