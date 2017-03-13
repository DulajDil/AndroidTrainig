package com.igor.vetrov.artzona;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class Jobs {

    private UUID mId;
    private String mTitle;

    public Jobs() {
        // Генерирование уникального идентификатора
        mId = UUID.randomUUID();
    }

    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
