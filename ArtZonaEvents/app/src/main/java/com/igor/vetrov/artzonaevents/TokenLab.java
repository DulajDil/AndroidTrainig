package com.igor.vetrov.artzonaevents;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.igor.vetrov.artzonaevents.database.VkBaseHelper;
import com.igor.vetrov.artzonaevents.database.VkCursorWrapper;
import com.igor.vetrov.artzonaevents.database.VkDbSchema;

import java.util.ArrayList;
import java.util.List;


public class TokenLab {


    private static TokenLab sTokenLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static final String TAG = "TokenLab";


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
        Log.w(TAG, String.format("Adding token to: %s", t.getToken()));
    }

    public void deleteToken (String token) {
        mDatabase.delete(
                VkDbSchema.TokenTable.NAME,
                VkDbSchema.TokenTable.Cols.TOKEN + " = ?",
                new String[] {token});
    }

    public void deleteTokens() {
        for (Token t: getTokens()) {
            deleteToken(t.getToken());
        }
    }

    /**
     *
     * @return
     */
    public List<Token> getTokens() {
        VkCursorWrapper cursor = queryTokens(null, null);
        List<Token> tokens = new ArrayList<>();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Token token = cursor.getToken();
                tokens.add(token);
                Log.w(TAG, "Get token string: " + token.getToken());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return tokens;
    }

    public void updateToken(Token t) {
        String id = String.valueOf(t.getId());
        String token = t.getToken();
        ContentValues values = getContentValues(t);
        mDatabase.update(
                VkDbSchema.TokenTable.NAME,
                values,
                VkDbSchema.TokenTable.Cols.ID + " = " + id,
                null);
        Log.w(TAG, String.format("Update token to: %s", token));
    }

    private VkCursorWrapper queryTokens(String whereClause, String[] whereArgs) {
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
