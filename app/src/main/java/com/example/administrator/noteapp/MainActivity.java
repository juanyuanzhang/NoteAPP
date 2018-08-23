package com.example.administrator.noteapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private Intent i;
    private ListView listView;
    private MDBAdapter mdbAdapter;
    private ListAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.mainList);
        mdbAdapter = new MDBAdapter(this);
        showlistView();
    }

    public void showlistView() {
        Cursor cursor = mdbAdapter.listshow();
        dataAdapter = new ListAdapter(this, cursor);
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor itemcursor = (Cursor) listView.getItemAtPosition(position);
                int itemid = itemcursor.getInt(itemcursor.getColumnIndexOrThrow("_id"));
                i = new Intent();
                i.putExtra("itemid", itemid);
                i.putExtra("key", "edit");
                i.setClass(MainActivity.this, AddActivity.class);
                startActivity(i);
            }
        });

    }

    //使用SimpleCursorAdapter
//    public void showlistView(){
//        Cursor cursor = mdbAdapter.listshow();
//        String[] columns = new String[]{
//                mdbAdapter.KEY_DATE,
//                mdbAdapter.KEY_TOP,
//                mdbAdapter.KEY_CONT
//
//
//        };
//        int[] to = new int[]{
//                R.id.tvdate,//list_item
//                R.id.tvnote,
//                R.id.tvcon
//
//        };
//        // 自訂的Adapter 需要使用SimpleCursorAdapter   來取資料放入listView中
//        dataAdapter = new SimpleCursorAdapter(this,R.layout.listitem, cursor, columns, to, 0);
//        listView.setAdapter(dataAdapter);
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbr, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, AddActivity.class);
        switch (item.getItemId()) {
            case R.id.baradd:
                intent.putExtra("key", "add");
                startActivity(intent);
                break;
//            case R.id.baredit:
//                intent.putExtra("key","edit");
//                startActivity(intent);
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
