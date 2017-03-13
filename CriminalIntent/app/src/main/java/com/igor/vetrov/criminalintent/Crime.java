package com.igor.vetrov.criminalintent;


import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mStringDateFormat;

    public Crime() {
        // Генерирование уникального идентификатора
        mId = UUID.randomUUID();
        mDate = new Date();
//        DateFormat dateFormat = android.text.format.DateFormat.getLongDateFormat(context);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        mStringDateFormat = dateFormat.format(mDate);
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

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
