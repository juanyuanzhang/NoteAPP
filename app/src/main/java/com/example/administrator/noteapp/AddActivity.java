package com.example.administrator.noteapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    final Calendar c = Calendar.getInstance();
    public Button btnok, btnset, btncancel;
    public EditText editdate, edittop, editcon;
    public TextView tvadd;
    public Spinner spinner;
    public String new_date, new_top, new_con, new_color;
    private MDBAdapter mdbAdapter;
    private Bundle bundle;
    private int index = 1;
    private ConstraintLayout Layout;
    private int position;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private long datetime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        findview();

        //連接資料庫
        mdbAdapter = new MDBAdapter(this);

        //新增時查詢list最後id預測本次資料id=最後id+1給提醒功能id值
        Log.i("index", String.valueOf(index));
        Cursor alarmcursor = mdbAdapter.addforalarm();
        Log.i("alarm", String.valueOf(alarmcursor.getCount()));

        //如果查詢資料數目不為先預設新增資料ID為查詢最後資料ID值加1
        if (alarmcursor.getCount() != 0)
            index = alarmcursor.getInt(alarmcursor.getColumnIndexOrThrow("_id")) + 1;
        Log.i("index", String.valueOf(index));
        
        //從首頁傳來的INTENT
        bundle = getIntent().getExtras();//取得Intent傳送過來的資料
        if (bundle.getString("key").equals("edit")) {  //確認intent回傳KEY值為edit ，更改標題，取得ID，呼叫querydata()方法，將值show出
            tvadd.setText("編輯便條紙");
            index = bundle.getInt("itemid");
            Cursor cursor = mdbAdapter.querydata(index);//利用ID值查詢資料，在顯示在EditView上
            editdate.setText(cursor.getString(1));
            edittop.setText(cursor.getString(2));
            editcon.setText(cursor.getString(3));
            //設定背景顏色與下拉式選單預設值
            if (cursor.getString(4).equals("#FFFFFFFF")) {
                position = 0;
            } else if (cursor.getString(4).equals("#FF40DFFF")) {
                position = 1;
            } else if (cursor.getString(4).equals("#ff404d")) {
                position = 2;
            } else if (cursor.getString(4).equals("#40ffbf")) {
                position = 3;
            } else if (cursor.getString(4).equals("#ffb940")) {
                position = 4;
            }
            //spinner必須搭配post()方法才會執行選定項目設定
            spinner.post(new Runnable() {
                @Override
                public void run() {
                    spinner.setSelection(position);
                }
            });
        }
        //下拉式選單選擇背景顏色文字設定
        ArrayAdapter<CharSequence> nAdapter = ArrayAdapter.createFromResource(this, R.array.notify_array, android.R.layout.simple_spinner_item);
        spinner.setAdapter(nAdapter);
        //下拉式選單選擇背景顏色設定監聽
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (spinner.getSelectedItemPosition()) {
                    case 0:
                        new_color = "#FFFFFFFF";
                        Layout.setBackgroundColor(Color.parseColor(new_color));
                        break;
                    case 1:
                        new_color = "#FF40DFFF";
                        Layout.setBackgroundColor(Color.parseColor(new_color));
                        break;
                    case 2:
                        new_color = "#ff404d";
                        Layout.setBackgroundColor(Color.parseColor(new_color));
                        break;
                    case 3:
                        new_color = "#40ffbf";
                        Layout.setBackgroundColor(Color.parseColor(new_color));
                        break;
                    case 4:
                        new_color = "#ffb940";
                        Layout.setBackgroundColor(Color.parseColor(new_color));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void findview() {
        btnok = findViewById(R.id.btnok);
        edittop = findViewById(R.id.edittop);
        editcon = findViewById(R.id.editcon);
        editdate = findViewById(R.id.editdate);
        btnok.setOnClickListener(this);
        spinner = findViewById(R.id.spinner); //下拉式選單
        tvadd = findViewById(R.id.tvadd);
        editdate.setOnClickListener(this);
        btnset = findViewById(R.id.btnset);
        btnset.setOnClickListener(this);
        Layout = findViewById(R.id.addlayout);
        btncancel = findViewById(R.id.btncancel);
        btncancel.setOnClickListener(this);
    }

    //按鈕監聽事件處理
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnok:
                //設定AlarmManager傳送廣播時間
                if (datetime != 0)
                    alarmMgr.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), alarmIntent);// 只提醒一次 用set ，一直提醒用setInexactRepeating ( AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY,alarmIntent)
                //設定變數數據存入資料庫
                //將EditView上的數據設給變數
                new_date = editdate.getText().toString();
                new_top = edittop.getText().toString();
                new_con = editcon.getText().toString();

                //如果是新增畫面
                if (bundle.getString("key").equals("add")) {
                    try {
                        //呼叫mdbadapter的方法處理新增
                        mdbAdapter.createdata(new_date, new_top, new_con, new_color); //顏色資料由下拉式選單那設定
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        //回到清單列表
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                    }

                } else {//編輯畫面

                    try {
                        mdbAdapter.updatedata(index, new_date, new_top, new_con, new_color);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                    }
                }
                break;

            case R.id.btnset://設定提醒按鈕
                //給第219行使用變數 日曆一開始的日期
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                //使用日曆Dialog
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        //設定日期text
                        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                        editdate.setText(date);
                        c.setTimeInMillis(System.currentTimeMillis());
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DATE, dayOfMonth);
                        //註冊動態廣播，改用靜態註冊
                        //IntentFilter intentFilter = new IntentFilter("com.example.administrator.noteapp");
                        //MyNoteReceiver myNoteReceiver = new MyNoteReceiver();
                        //registerReceiver(myNoteReceiver, intentFilter);
                        //設定AlarmManager傳送廣播時間與資料
                        alarmMgr = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
                        Intent intent = new Intent("com.example.administrator.noteapp");
                        intent.putExtra("msg", edittop.getText().toString());
                        intent.putExtra("id", index);
                        intent.setClass(AddActivity.this, MyNoteReceiver.class);
                        alarmIntent = PendingIntent.getBroadcast(AddActivity.this, index, intent, PendingIntent.FLAG_UPDATE_CURRENT);//index為廣播判斷是哪一筆通知用
                        datetime = c.getTimeInMillis();//為了給確定按鈕用的
                        Toast.makeText(AddActivity.this, "提醒已設定", Toast.LENGTH_SHORT).show();
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(AddActivity.this, DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, dateSetListener, year, month, dayOfMonth);
                dialog.show();
                break;

            case R.id.btncancel:
                //取消提醒
                alarmMgr = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
                datetime = 0;
                Intent intent = new Intent("com.example.administrator.noteapp");
                intent.putExtra("msg", edittop.getText().toString());
                intent.putExtra("id", index);
                intent.setClass(AddActivity.this, MyNoteReceiver.class);
                alarmIntent = PendingIntent.getBroadcast(AddActivity.this, index, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmMgr.cancel(alarmIntent);
                Toast.makeText(AddActivity.this, "提醒已取消", Toast.LENGTH_SHORT).show();
                break;

            case R.id.editdate://點擊日期可使用日曆
                final Calendar c2 = Calendar.getInstance();
                int year2 = c2.get(Calendar.YEAR);
                int month2 = c2.get(Calendar.MONTH);
                int dayOfMonth2 = c2.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
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
        if (bundle.getString("key").equals("edit")) {
            inflater.inflate(R.menu.deletbar, menu);
        } else {
            inflater.inflate(R.menu.back, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    //menu事件監聽處理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete: //刪除按鈕會出現一個AlerDialog提醒用戶確定是否要刪除
                AlertDialog dialog;
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(this);
                builder.setTitle("NOTE")
                        .setMessage("ARE YOU SURE? DELETE THIS ONE?")
                        .setPositiveButton("SURE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //刪除資料
                                Boolean isDeleted = mdbAdapter.deletedata(index);
                                //一起取消提醒
                                alarmMgr = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
                                Intent intent = new Intent("com.example.administrator.noteapp");
                                intent.putExtra("msg", edittop.getText().toString());
                                intent.putExtra("id", index);
                                intent.setClass(AddActivity.this, MyNoteReceiver.class);
                                alarmIntent = PendingIntent.getBroadcast(AddActivity.this, index, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmMgr.cancel(alarmIntent);
                                Intent i = new Intent(AddActivity.this, MainActivity.class);
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
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;

            case R.id.addback:
                Intent i2 = new Intent(this, MainActivity.class);
                startActivity(i2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
