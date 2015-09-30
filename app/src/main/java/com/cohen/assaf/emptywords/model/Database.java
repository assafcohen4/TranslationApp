package com.cohen.assaf.emptywords.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 30/09/2015.
 */
public class Database extends SQLiteOpenHelper {

    private static final String WORDS = "WORDS";
    private static final String ID = "ID";
    private static final String ORIGINAL = "ORIGINAL";
    private static final String TRANSLATION = "TRANSLATION";
    public Database(Context context) {
        super(context, "wordsDb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql  =String.format("create table %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s STRING, %s STRING)",
                WORDS, ID, ORIGINAL, TRANSLATION) ;
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void storeWordPair(WordPair pair){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORIGINAL, pair.getOriginal());
        values.put(TRANSLATION, pair.getTranslation());
        db.insert(WORDS, null, values);
        db.close();
    }

    public boolean IsPairAlreadyInDatabase(WordPair pair){
        SQLiteDatabase db = getReadableDatabase();
        String sql = String.format("select %s, %s from %s", ORIGINAL, TRANSLATION, WORDS);
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            String original =  cursor.getString(0);
            String translation = cursor.getString(1);
            if(original.equals(pair.getOriginal()) && translation.equals(pair.getTranslation())){
                return true;
            }

        }
        db.close();
        return false;
    }

    public List<WordPair> getAllWords(){
        List<WordPair> allPairs = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = String.format("select %s, %s from %s", ORIGINAL, TRANSLATION, WORDS);
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            String original =  cursor.getString(0);
            String translation = cursor.getString(1);
            WordPair pair = new WordPair(original, translation);
            allPairs.add(pair);
        }
        db.close();
        return allPairs;
    }
}
