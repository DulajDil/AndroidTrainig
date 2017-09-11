package com.igor.vetrov.artzonaevents.database;


import android.database.Cursor;
import android.database.CursorWrapper;

import com.igor.vetrov.artzonaevents.Token;

public class VkCursorWrapper extends CursorWrapper{

    public VkCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Token getToken() {
        String token = getString(getColumnIndex(VkDbSchema.TokenTable.Cols.TOKEN));

        Token token1 = new Token();
        token1.setToken(token);

        return token1;
    }
}
