package hanz.com.mymemo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class Note_ContentActivity extends AppCompatActivity {

    private EditText note_data_title;
    private EditText note_data_content;
    private ImageView note_complete_btn;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note__content);

        note_data_title = (EditText) findViewById(R.id.note_data_title);
        note_data_content = (EditText) findViewById(R.id.note_data_content);
        note_complete_btn = (ImageView) findViewById(R.id.note_complete_btn);

        db = this.openOrCreateDatabase("mymemo",Context.MODE_PRIVATE,null);
        final Intent intent = getIntent();
        String action = intent.getAction();
        if (action.equals("note")){
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            note_data_title.setText(title);
            note_data_content.setText(content);
        }
//        修改保存
        note_complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(Note_ContentActivity.this,note_data_title.getText().toString().trim(),
//                        Toast.LENGTH_SHORT).show();
                if (!note_data_title.getText().toString().trim().equals("")&&
                        !note_data_content.getText().toString().trim().equals("")){
                    ContentValues datas = new ContentValues();
                    datas.put("dateTitle",note_data_title.getText().toString().trim());
                    datas.put("dateContent",note_data_content.getText().toString().trim());
                    datas.put("dateCreateTime", Calendar.getInstance().getTimeInMillis());
                    String title2 = intent.getStringExtra("title");
                    db.update("myTable",datas,"dateTitle=?",new String[]{title2});
                    Toast.makeText(getApplicationContext(),"Update Successfully！",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Note_ContentActivity.this,"error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
