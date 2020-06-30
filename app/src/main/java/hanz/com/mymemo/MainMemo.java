package hanz.com.mymemo;

import java.io.Serializable;
import java.util.Date;

public class MainMemo implements Serializable {
    private String dateTitle;
    private String dateContent;
    private Date dateCreateTime;

    public String getDateTitle() {
        return dateTitle;
    }

    public void setDateTitle(String dateTitle) {
        this.dateTitle = dateTitle;
    }

    public String getDateContent() {
        return dateContent;
    }

    public void setDateContent(String dateContent) {
        this.dateContent = dateContent;
    }

    public Date getDateCreateTime() {
        return dateCreateTime;
    }

    public void setDateCreateTime(Date dateCreateTime) {
        this.dateCreateTime = dateCreateTime;
    }
}
