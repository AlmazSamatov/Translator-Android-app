package com.example.almaz.translator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE words (word TEXT, translation TEXT, isDeleted INTEGER, " +
                "sourcePosition INTEGER, targetPosition INTEGER, sourceLanguage TEXT, " +
                "targetLanguage TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS words");
        onCreate(db);
    }

    public void insertWord(Word item) {
        if (!isInDataBase(item)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("word", item.getWord());
            contentValues.put("translation", item.getTranslation());
            contentValues.put("isDeleted", 0);
            contentValues.put("sourcePosition", item.getSourcePosition());
            contentValues.put("targetPosition", item.getTargetPosition());
            contentValues.put("sourceLanguage", item.getSourceLanguage());
            contentValues.put("targetLanguage", item.getTargetLanguage());
            db.insert("words", null, contentValues);
        }
    }

    public int[] getLanguages(String word, String sourceLanguage, String targetLanguage) {
        int[] languages = new int[2];
        long count = DatabaseUtils.queryNumEntries(this.getReadableDatabase(), "words",
                "word = ? ", new String[]{word});
        System.out.print(String.valueOf(count));
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT sourcePosition, targetPosition FROM words WHERE " +
                " word = '" + word + "' AND sourceLanguage = '" + sourceLanguage + "' AND " +
                "targetLanguage = '" + targetLanguage + "'", null);

        cursor.moveToFirst();

        languages[0] = cursor.getInt(cursor.getColumnIndex("sourcePosition"));
        languages[1] = cursor.getInt(cursor.getColumnIndex("targetPosition"));

        return languages;
    }

    public void setDeleted(Word item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("word", item.getWord());
        contentValues.put("translation", item.getTranslation());
        contentValues.put("isDeleted", 1);
        contentValues.put("sourcePosition", item.getSourcePosition());
        contentValues.put("targetPosition", item.getTargetPosition());
        contentValues.put("sourceLanguage", item.getSourceLanguage());
        contentValues.put("targetLanguage", item.getTargetLanguage());
        db.update("words", contentValues, "word = ? AND sourcePosition = ? AND targetPosition = ?",
                new String[]{item.getWord(), String.valueOf(item.getSourcePosition()),
                        String.valueOf(item.getTargetPosition())});
    }

    public void deleteWord(Word item){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("words", "isDeleted = ? AND word = ? AND sourcePosition = ? AND targetPosition = ?",
                new String[]{"0", item.getWord(), String.valueOf(item.getSourcePosition()),
                String.valueOf(item.getTargetPosition())});
    }

    public void deleteAllWords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM words");
    }

    public boolean isInDataBase(Word word) {
        long count = DatabaseUtils.queryNumEntries(this.getReadableDatabase(), "words",
                "word = ? AND sourcePosition = ? AND targetPosition = ? AND isDeleted = ?",
                new String[]{word.getWord(), String.valueOf(word.getSourcePosition()),
                        String.valueOf(word.getTargetPosition()), "0"});
        if (count == 0)
            return false;
        else
            return true;
    }

    public ArrayList<Word> getAllWords() {
        ArrayList<Word> arrayList = new ArrayList<Word>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("words", "isDeleted = ?", new String[]{String.valueOf(1)});

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT word, translation, sourcePosition, targetPosition FROM words", null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Word item = new Word(cursor.getString(cursor.getColumnIndex("word")),
                    cursor.getString(cursor.getColumnIndex("translation")),
                    cursor.getInt(cursor.getColumnIndex("sourcePosition")),
                    cursor.getInt(cursor.getColumnIndex("targetPosition")));
            if (!item.isEmpty()) {
                arrayList.add(item);
            }
            cursor.moveToNext();
        }

        return arrayList;
    }


}
