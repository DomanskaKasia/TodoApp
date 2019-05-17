package com.example.todoapp;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.TaskViewHolder> {
    private static final String TAG = "CursorRecyclerViewAdapt";
    private Cursor cursor;
    private OnButtonClickListener listener;

    public CursorRecyclerViewAdapter(Cursor cursor, OnButtonClickListener listener) {
        Log.d(TAG, "CursorRecyclerViewAdapt: constructor called");
        this.cursor = cursor;
        this.listener = listener;
    }

    interface OnButtonClickListener {
        void deleteClick(Task task);
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "TaskViewHolder";

        TextView name;
        TextView date;
        TextView category;
        ImageButton deleteBtn;

        private TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "TaskViewHolder: constructor called");

            this.name = (TextView) itemView.findViewById(R.id.todo_name);
            this.date = (TextView) itemView.findViewById(R.id.todo_date);
            this.category = (TextView) itemView.findViewById(R.id.todo_category);
            this.deleteBtn = (ImageButton) itemView.findViewById(R.id.delete_img);
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: called");
        return new TaskViewHolder( LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.content_main_list_detail, viewGroup, false) );
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int i) {
        Log.d(TAG, "onBindViewHolder: starts");

        if((cursor == null) || cursor.getCount() == 0) {
            holder.name.setText(R.string.no_tasks_info);
            holder.date.setVisibility(View.GONE);
            holder.category.setVisibility(View.GONE);
            holder.deleteBtn.setVisibility(View.GONE);
        } else {
            if(!cursor.moveToPosition(i)) {
                throw new IllegalStateException("Couldn't move cursor to position " + i);
            }

            final String name = cursor.getString(cursor.getColumnIndex(TasksTable.Column.NAME));
            String date = cursor.getString(cursor.getColumnIndex(TasksTable.Column.END_DATE));
            String category = cursor.getString(cursor.getColumnIndex(TasksTable.Column.CATEGORY));

            final Task task = new Task( cursor.getInt(cursor.getColumnIndex(TasksTable.Column._ID)),
                                name,
                                date,
                                category);

            holder.name.setText(name);
            holder.date.setText(date);
            holder.category.setText(category);
            holder.category.setBackgroundColor(1);
            holder.deleteBtn.setVisibility(View.VISIBLE);

            final View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: button " + name + " clicked");
                    if(listener != null) {
                        listener.deleteClick(task);
                    }
                }
            };

            holder.deleteBtn.setOnClickListener(onClickListener);
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

    //return the old cursor or null
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
