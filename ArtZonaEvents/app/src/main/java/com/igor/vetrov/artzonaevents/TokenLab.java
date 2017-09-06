package com.igor.vetrov.artzonaevents;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.igor.vetrov.artzonaevents.database.VkBaseHelper;
import com.igor.vetrov.artzonaevents.database.VkCursorWrapper;
import com.igor.vetrov.artzonaevents.database.VkDbSchema;



public class TokenLab {


    private static TokenLab sTokenLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static TokenLab get(Context context) {
        if (sTokenLab == null) {
            sTokenLab = new TokenLab(context);
        }
        return sTokenLab;
    }

    public TokenLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new VkBaseHelper(mContext).getWritableDatabase();
    }

    public void addToken(Token t) {
        ContentValues values = getContentValues(t);
        mDatabase.insert(VkDbSchema.TokenTable.NAME, null, values);
    }

    public void deleteToken() {
        mDatabase.delete(VkDbSchema.TokenTable.NAME, VkDbSchema.TokenTable.Cols.ID + " = ?", new String[] { String.valueOf(1) });
    }

    public void updateToken(Token t) {
        String id = String.valueOf(t.getId());
        ContentValues values = getContentValues(t);
        mDatabase.update(VkDbSchema.TokenTable.NAME, values, VkDbSchema.TokenTable.Cols.ID + " = ?", new String[] {id});
    }

    public Token getToken() {
        Token token = null;
        VkCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            token = cursor.getToken();
        } finally {
            cursor.close();
        }

        return token;
    }

    private VkCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                VkDbSchema.TokenTable.NAME,
                null, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new VkCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Token t) {
        ContentValues values = new ContentValues();
        values.put(VkDbSchema.TokenTable.Cols.TOKEN, t.getToken());
        values.put(VkDbSchema.TokenTable.Cols.ID, t.getId());
        return values;
    }
}
