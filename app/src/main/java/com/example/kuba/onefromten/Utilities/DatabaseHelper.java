package com.example.kuba.onefromten.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "OneFromTen.db";
    public static final String TABLE_NAME = "question_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "QUESTION";
    public static final String COL_3 = "CORRECT_ANSWER";
    public static final String COL_4 = "ANSWER_2";
    public static final String COL_5 = "ANSWER_3";
    public static final String COL_6 = "ANSWER_4";


    public DatabaseHelper(@Nullable Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " QUESTION TEXT, CORRECT_ANSWER TEXT, ANSWER_2 TEXT, ANSWER_3 TEXT, ANSWER_4 TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String question, String correctA, String a2, String a3, String a4){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, question);
        contentValues.put(COL_3, correctA);
        contentValues.put(COL_4, a2);
        contentValues.put(COL_5, a3);
        contentValues.put(COL_6, a4);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getQuestions(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);
        return result;
    }

    public boolean updateData(String id, String question, String correctA, String a2, String a3, String a4){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, question);
        contentValues.put(COL_3, correctA);
        contentValues.put(COL_4, a2);
        contentValues.put(COL_5, a3);
        contentValues.put(COL_6, a4);
        db.update(TABLE_NAME, contentValues, "ID + ?", new String[] { id });
        return true;
    }


}
