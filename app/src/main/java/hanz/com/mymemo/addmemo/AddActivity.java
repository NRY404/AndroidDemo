package hanz.com.mymemo.addmemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.Calendar;


import hanz.com.mymemo.R;

public class AddActivity extends AppCompatActivity {

    private EditText data_title;
    private EditText data_content;
    private ImageView complete_btn;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_memo);

        data_title = (EditText) findViewById(R.id.data_title);
        data_content = (EditText) findViewById(R.id.data_content);
        complete_btn = (ImageView) findViewById(R.id.complete_btn);
//创建库
        db = this.openOrCreateDatabase("mymemo",Context.MODE_PRIVATE,null);
//        创建表
        String createTableSQL = "CREATE TABLE IF NOT EXISTS myTable(id integer primary key autoincrement,dateTitle text,dateContent text,dateCreateTime date)";
        db.execSQL(createTableSQL);
        complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加
                ContentValues datas = new ContentValues();
                datas.put("dateTitle",data_title.getText().toString().trim());
                datas.put("dateContent",data_content.getText().toString().trim());
                datas.put("dateCreateTime", Calendar.getInstance().getTimeInMillis());
                db.insert("myTable",null,datas);
                Toast.makeText(getApplicationContext(),"Add Successfully！",Toast.LENGTH_SHORT).show();


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db!=null ||db.isOpen() ){
            db.close();
        }
    }

}
