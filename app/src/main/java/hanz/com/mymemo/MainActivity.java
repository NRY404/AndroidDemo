package hanz.com.mymemo;


import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


import hanz.com.mymemo.addmemo.AddActivity;
import hanz.com.mymemo.remindmemo.AlarmReceiver;

public class MainActivity extends AppCompatActivity {

    private TextView title;
    private EditText search_data;
    private ScrollView scrollView;
    private SearchView searchView;
    private ImageView add_btn;
//    private Spinner mainTheme;
    private Button change;
    private ListView memo_dates;
    private List<MainMemo> dates;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Calendar mCalender;
    private static int sTheme;
    int count;


//    主题切换
    public class mOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final String[] item = new String[]{"灰白","米黄","夜晚"};
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setIcon(R.drawable.theme);
            builder.setTitle("切换主题");
            builder.setItems(item, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (item[which]){
                        case "灰白":
                            sTheme = R.style.AppTheme;
                            recreate();
                            break;
                        case "米黄":
                            sTheme = R.style.AppTheme2;
                            recreate();
                            break;
                        case "夜晚":
                            sTheme = R.style.AppTheme3;
                            recreate();
                            break;
                    }
                }
            });
            builder.create().show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (sTheme!=0){
            setTheme(sTheme);
        }
        setContentView(R.layout.activity_main);

        title = (TextView) findViewById(R.id.title);
        search_data = (EditText) findViewById(R.id.search_data);
        memo_dates = (ListView) findViewById(R.id.memo_datas);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        add_btn = (ImageView) findViewById(R.id.add_btn);
        searchView = (SearchView) findViewById(R.id.searchView);
        change = (Button) findViewById(R.id.change);
//        mainTheme = (Spinner) findViewById(R.id.spinner);

        change.setOnClickListener(new mOnClickListener());
//测试数据
        dates = new ArrayList<>();
        dbFindAll();
        setRemind(true);

//主题切换
//        String[] type = new String[]{"灰白","米黄","夜晚"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item,type);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mainTheme.setAdapter(adapter);
//        mainTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String result = parent.getItemAtPosition(position).toString();
//                switch (result){
//                    case "灰白":
//                        setTheme(R.style.AppTheme);
//                        recreate();
//                        break;
//                    case "米黄":
//                        setTheme(R.style.AppTheme);
//                        break;
//                    case "夜晚":
//                        setTheme(R.style.AppTheme);
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//
//        });


//添加
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转添加页面
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
                //数据库工具类

            }
        });

//    搜索
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbFindOne();
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                onRestart();
                search_data.setText("");
                return false;
            }
        });

//  点击查看
        memo_dates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor upcursor = db.query("myTable",new String[]{"dateTitle,dateContent,dateCreateTime"},null,
                        null,null,null,null);
                upcursor.moveToPosition(position);
                Intent intent = new Intent(MainActivity.this,Note_ContentActivity.class);
                intent.setAction("note");
                intent.putExtra("title",upcursor.getString(0));
                intent.putExtra("content",upcursor.getString(1));
                startActivity(intent);
            }
        });

//  长按删除
        memo_dates.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirm Deletion");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor deleteCursor = db.query("myTable",new String[]{"dateTitle,dateContent,dateCreateTime"},null,
                                null,null,null,null);
                        deleteCursor.moveToPosition(position);
                        Toast.makeText(MainActivity.this, deleteCursor.getString(0)+"is deleted",Toast.LENGTH_SHORT).show();
                        db.delete("myTable","dateTitle=?",new String[]{deleteCursor.getString(0)});
                        onRestart();
                    }
                });
                builder.setNegativeButton("No",null);
                builder.create().show();
                return true;
            }
        });
    }

//  查询所有数据
    private void dbFindAll() {
        count = 1;
        dates.clear();
        boolean a=false;
        db = this.openOrCreateDatabase("mymemo", Context.MODE_PRIVATE,null);
        Cursor cs = db.rawQuery("select name from sqlite_master where type='table'",null);
        while (cs.moveToNext()){
            String name = cs.getString(0);
            if (name.equals("myTable")){
                a = true;
            }
            Log.i("System.out",name);
        }
        if (a){
            cursor = db.query("myTable",null,null,
                    null,null,null,"id ASC");
    //        query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit)方法各参数的含义：
    //        table：表名。相当于select语句from关键字后面的部分。如果是多表联合查询，可以用逗号将两个表名分开。
    //        columns：要查询出来的列名。相当于select语句select关键字后面的部分。
    //        selection：查询条件子句，相当于select语句where关键字后面的部分，在条件子句允许使用占位符“?”
    //        selectionArgs：对应于selection语句中占位符的值，值在数组中的位置与占位符在语句中的位置必须一致，否则就会有异常。
    //        groupBy：相当于select语句group by关键字后面的部分
    //        having：相当于select语句having关键字后面的部分
    //        orderBy：相当于select语句order by关键字后面的部分，如：personid desc, age asc;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                String DataTitle = cursor.getString(1);
                String DataContent = cursor.getString(2);
    //            String DataCreateTime = cursor.getString(2);

                MainMemo memo = new MainMemo();
                memo.setDateTitle(cursor.getString(cursor.getColumnIndex("dateTitle")));
                memo.setDateContent(DataContent);
                memo.setDateCreateTime(new Date(cursor.getLong(3)));

                dates.add(memo);
                count++;
                cursor.moveToNext();

                MainAdapter adapter= new MainAdapter(this, dates);
                memo_dates.setAdapter(adapter);
            }
        }
    }
//  关键词查询
    private void dbFindOne(){
        count = 1;
        dates.clear();
        String searchdata = search_data.getText().toString();
//        Toast.makeText(MainActivity.this,searchdata,Toast.LENGTH_SHORT).show();
        cursor = db.query("myTable",new String[]{"dateTitle,dateContent,dateCreateTime"},
                "dateTitle like ? or dateContent like ?",new String[]{"%"+searchdata+"%","%"+searchdata+"%"},null,null,
                "id ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String DataTitle = cursor.getString(0);
            String DataContent = cursor.getString(1);
//            String DataCreateTime = cursor.getString(2);

            MainMemo memo = new MainMemo();
            memo.setDateTitle(DataTitle);
            memo.setDateContent(DataContent);
            memo.setDateCreateTime(new Date(cursor.getLong(2)));

            dates.add(memo);
            count++;
            cursor.moveToNext();

            MainAdapter adapter= new MainAdapter(this, dates);
            memo_dates.setAdapter(adapter);
        }
    }
//  定时提醒
    private void setRemind(boolean b){
        mCalender = Calendar.getInstance();//得到日历实例，为了下面获取时间
        mCalender.setTimeInMillis(System.currentTimeMillis());
        long systemTime = System.currentTimeMillis();//获取当前毫秒值
        mCalender.setTimeInMillis(System.currentTimeMillis());//日历的年月日和当前同步
        mCalender.setTimeZone(TimeZone.getTimeZone("GMT+8"));//时区时间差
        mCalender.set(Calendar.HOUR_OF_DAY,13);//设置在几点提醒
        mCalender.set(Calendar.MINUTE,31);//设置在分钟提醒
        mCalender.set(Calendar.SECOND,0);//设置在秒提醒
        mCalender.set(Calendar.MILLISECOND,0);
        long selectTime = mCalender.getTimeInMillis();//获取上面设置的14点30 的毫秒值
        if (systemTime > selectTime){
            mCalender.add(Calendar.DAY_OF_MONTH,1);//如果当前时间大于设置时间，从第二天开始
        }
        //AlarmReceiver.class 为广播接受者
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this,0,intent,0);
        //得到AlarmManager 实例
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (b){
            am.set(AlarmManager.RTC_WAKEUP,mCalender.getTimeInMillis(),pi);
//            Toast.makeText(this,"Open Alarm",Toast.LENGTH_SHORT).show();
        }else {
            am.cancel(pi);
            Toast.makeText(this,"Closed Alarm",Toast.LENGTH_SHORT).show();
        }
        // 单次提醒
//        am.set(AlarmManager.RTC_WAKEUP,mCalender.getTimeInMillis(),pi);
//        Toast.makeText(this,"Open Alarm",Toast.LENGTH_SHORT).show();
        //  重复提醒
        //am.setRepeating(AlarmManager.RTC_WAKEUP,mCalender.getTimeInMillis(),(1000*60*60*24),pi);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MainActivity","onRestart");
        dbFindAll();
    }
}
