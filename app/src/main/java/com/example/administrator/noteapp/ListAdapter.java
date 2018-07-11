package com.example.administrator.noteapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by juan on 2018/7/11.
 */
//取代SimpleCursorAdapter 的CursorAdapter
public class ListAdapter extends CursorAdapter {
    LayoutInflater inflater;
    public ListAdapter(Context context, Cursor c) {
        super(context, c, 0);
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return inflater.inflate(R.layout.listitem,viewGroup,false);  //利用inflate取得Layout物件
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        LinearLayout layout = view.findViewById(R.id.listlayout2);
        TextView tvdate = view.findViewById(R.id.tvdate);
        TextView tvnote = view.findViewById(R.id.tvnote);
        TextView tvcon = view.findViewById(R.id.tvcon);

        tvdate.setText(cursor.getString(cursor.getColumnIndexOrThrow("date")));
        tvnote.setText(cursor.getString(cursor.getColumnIndexOrThrow("top")));
        tvcon.setText(cursor.getString(cursor.getColumnIndexOrThrow("cont")));
        layout.setBackgroundColor(Color.parseColor(cursor.getString(4)));
    }
}
