package hanz.com.mymemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainAdapter extends BaseAdapter {

    private List<MainMemo> dates = null;
    private Context context = null;
    public MainAdapter( Context context,List<MainMemo> dates){
        this.context = context;
        this.dates = dates;
    }
    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.memo_item,null);
            //控件组装
            viewHolder.dateTitle = (TextView) convertView.findViewById(R.id.dateTitle);
            viewHolder.dateContent = (TextView) convertView.findViewById(R.id.date_content);
            viewHolder.dateCreateTime = (TextView) convertView.findViewById(R.id.date_current_time);
            //数据
            MainMemo mainMemo = dates.get(position);
            viewHolder.dateTitle.setText(mainMemo.getDateTitle());
            //处理长度
            String shortCut =mainMemo.getDateContent();
            if (mainMemo.getDateContent().length() > 10){
                shortCut = shortCut.substring(0,10);
            }
            viewHolder.dateContent.setText(shortCut);
            //转换日期，如果是当天，则显示当天日期，否则显示2020/xx/xx
            String dateStr = formDate(mainMemo.getDateCreateTime());

            viewHolder.dateCreateTime.setText(dateStr);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
        //控件操作
    }
    private String formDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String parmDateStr = dateFormat.format(date);
        if (parmDateStr.equals(dateFormat.format(new Date(Calendar.getInstance().getTimeInMillis())))){
            return "Today";
        }else {
            return parmDateStr;
        }
    }
    public class ViewHolder{
        private TextView dateTitle;
        private TextView dateContent;
        private TextView dateCreateTime;
    }

}
