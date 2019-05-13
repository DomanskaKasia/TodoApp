package com.example.todoapp;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.TaskViewHolder> {
    private static final String TAG = "CursorRecyclerViewAdapt";
    private Cursor cursor;

    public CursorRecyclerViewAdapter(Cursor cursor) {
        Log.d(TAG, "CursorRecyclerViewAdapt: constructor called");
        this.cursor = cursor;
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "TaskViewHolder";

        CheckBox checkBox = null;
        TextView name = null;
        TextView date = null;
//        ImageButton editBtn = null;
        ImageButton deleteBtn = null;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "TaskViewHolder: constructor called");

            this.checkBox = (CheckBox) itemView.findViewById(R.id.todo_checkBox);
            this.name = (TextView) itemView.findViewById(R.id.todo_name);
            this.date = (TextView) itemView.findViewById(R.id.todo_date);
//            this.editBtn = (ImageButton) itemView.findViewById(R.id.edit_img);
            this.deleteBtn = (ImageButton) itemView.findViewById(R.id.delete_img);
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: new view requested");
        return new TaskViewHolder( LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.content_main_list_detail, viewGroup, false) );
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: starts");

        if((cursor == null) || cursor.getCount() == 0) {
            taskViewHolder.checkBox.setVisibility(View.GONE);
            taskViewHolder.name.setText(R.string.no_tasks_info);
            taskViewHolder.deleteBtn.setVisibility(View.GONE);
        } else {
            if(!cursor.moveToPosition(i)) {
                throw new IllegalStateException("Couldn't move cursor to position " + i);
            }
            taskViewHolder.checkBox.setVisibility(View.VISIBLE);
            taskViewHolder.name.setText(cursor.getString(cursor.getColumnIndex(TasksTable.Column.NAME)));
            taskViewHolder.date.setText(cursor.getString(cursor.getColumnIndex(TasksTable.Column.END_DATE)));
            taskViewHolder.deleteBtn.setVisibility(View.VISIBLE); //todo onclicklistener
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: starts");
        if(cursor == null || cursor.getCount() == 0) {
            return 1; // 1, because of single ViewHolder with info
        }
        return cursor.getCount();
    }

    //todo dodac komentarz
    Cursor swapCursor(Cursor newCursor) {
        if(newCursor == cursor) {
            return null;
        }

        final Cursor oldCursor = cursor;
        cursor = newCursor;
        if(newCursor != null) {
            //notify observers about the new cursor
            notifyDataSetChanged();
        } else {
            //notify the observers about lack of data
            notifyItemRangeRemoved(0, getItemCount());
        }
        return oldCursor;
    }
}
