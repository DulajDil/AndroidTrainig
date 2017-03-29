package com.igor.vetrov.artzona;


import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class Event {

    private UUID mId;
    private String mTitle;

    public Event() {
        // Генерирование уникального идентификатора
        mId = UUID.randomUUID();
   }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
