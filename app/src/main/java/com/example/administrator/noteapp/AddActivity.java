package com.example.administrator.noteapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static com.example.administrator.noteapp.R.drawable.white;

public class AddActivity extends AppCompatActivity implements View.OnClickListener  {
    public Button btnok,btnset;
    public EditText editdate,edittop,editcon;
    public TextView tvadd;
    public Spinner spinner ;
    public String new_date ,new_top,new_con,new_notify;
    private MDBAdapter mdbAdapter;
    Bundle bundle;
    int index;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        findview();
        mdbAdapter = new MDBAdapter(this);
        bundle = getIntent().getExtras();
        if(bundle.getString("key").equals("edit")){  //確認intent回傳KEY值為edit ，更改標題，取得ID，呼叫querydata()方法，將值show出
            tvadd.setText("編輯便條紙");
            index = bundle.getInt("itemid");
            Cursor cursor = mdbAdapter.querydata(index);
            editdate.setText(cursor.getString(1));
            edittop.setText(cursor.getString(2));
            editcon.setText(cursor.getString(3));

        }

        ArrayAdapter<CharSequence>nAdapter = ArrayAdapter.createFromResource(this,R.array.notify_array,android.R.layout.simple_spinner_item);
        spinner.setAdapter(nAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch(spinner.getSelectedItemPosition()) {
                    case 0:
                        new_notify="#FFFFFFFF" ;
                    break;
                    case 1:
                        new_notify="#FF40DFFF" ;
                        break;
                    case 2:
                        new_notify="#ff404d" ;
                        break;
                    case 3:
                        new_notify="#40ffbf" ;
                        break;
                    case 4:
                        new_notify="#ffb940" ;
                        break;
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    private void findview(){
        btnok = findViewById(R.id.btnok);
        edittop = findViewById(R.id.edittop);
        editcon= findViewById(R.id.editcon);
        editdate= findViewById(R.id.editdate);
        btnok.setOnClickListener(this);
        spinner = findViewById(R.id.spinner); //下拉式選單
        tvadd = findViewById(R.id.tvadd);
        editdate.setOnClickListener(this);
        btnset = findViewById(R.id.btnset);
        btnset.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnok:

                new_date=editdate.getText().toString();
                new_top=edittop.getText().toString();
                new_con=editcon.getText().toString();
                //new_notify =
                if(bundle.getString("key").equals("add")){
                    try {
                        //呼叫adapter的方法處理新增
                        mdbAdapter.createdata(new_date, new_top, new_con, new_notify);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        //回到列表
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                    }

                }else{

                    try{
                        mdbAdapter.updatedata(index, new_date, new_top, new_con, new_notify);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                    }
                }
                break;

            case R.id.btnset:
                final Calendar c =Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String date = year+"-"+(month+1)+"-"+dayOfMonth;
                        editdate.setText(date);
                        Notification notification = new Notification.Builder(AddActivity.this)
                                .setSmallIcon(R.mipmap.note)
                                .setContentTitle("這是TEDST")
                                .setContentText("這是TEST")
                                .setWhen(System.currentTimeMillis()).build();
                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        manager.notify(1,notification);
                    }
                };

                DatePickerDialog dialog = new DatePickerDialog(AddActivity.this, DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, dateSetListener, year, month, dayOfMonth);
                dialog.show();


                break;

            case R.id.editdate:
                final Calendar c2 =Calendar.getInstance();
                int year2 = c2.get(Calendar.YEAR);
                int month2 = c2.get(Calendar.MONTH);
                int dayOfMonth2 = c2.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String date = year+"-"+(month+1)+"-"+dayOfMonth;
                        editdate.setText(date);
                    }
                };
                DatePickerDialog dialog2 = new DatePickerDialog(AddActivity.this, DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, dateSetListener2, year2, month2, dayOfMonth2);
                dialog2.show();

        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        if(bundle.getString("key").equals("edit")) {
            inflater.inflate(R.menu.deletbar, menu);
        }else {
            inflater.inflate(R.menu.back, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog dialog = null;
                AlertDialog.Builder builder = null;
                builder = new AlertDialog.Builder(this);
                builder.setTitle("NOTE")
                        .setMessage("ARE YOU SURE? DELETE THIS ONE?")
                        .setPositiveButton("SURE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Boolean isDeleted = mdbAdapter.deletedata(index);
                                Intent i  = new Intent(AddActivity.this,MainActivity.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;

            case R.id.back:
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
                break;

            case R.id.addback:
                Intent i2 = new Intent(this,MainActivity.class);
                startActivity(i2);
                break;
            }
        return super.onOptionsItemSelected(item);
    }
}
