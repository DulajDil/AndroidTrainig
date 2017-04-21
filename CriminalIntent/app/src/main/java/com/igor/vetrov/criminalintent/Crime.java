package com.igor.vetrov.criminalintent;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private String mTime;
    private boolean mSolved;
    private String mStringDateFormat;

    public static String gettingTime(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(date);
    }

    public static String gettingDate(Date date) {
//        DateFormat dateFormat = android.text.format.DateFormat.getLongDateFormat(context);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        return dateFormat.format(date);
    }

    public Crime() {
        // Генерирование уникального идентификатора
        mId = UUID.randomUUID();
        mDate = new Date();
        mTime = gettingTime(mDate);
        mStringDateFormat = gettingDate(mDate);
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public String getStringDateFormat() {
        return mStringDateFormat;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public String getTime() {
        return mTime;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDate(Date date) {
        mDate = date;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        mStringDateFormat = dateFormat.format(mDate);
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
