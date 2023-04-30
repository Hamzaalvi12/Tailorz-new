package com.example.tailorz.SQLDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import com.example.tailorz.TailorModels.TailorDesignModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final int version = 1;
    public static final String DB_NAME = "favorite_db";
    public static final String TABLE_NAME = "favorite";
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_IMAGE = "image";
    public static final String FIELD_TAILORNAME = "tailorname";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + FIELD_ID + " TEXT PRIMARY KEY, "
                + FIELD_NAME + " TEXT,"
                + FIELD_TAILORNAME + " TEXT,"
                + FIELD_IMAGE + " TEXT)";
        db.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addToFavorite(TailorDesignModel designModel){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIELD_ID, designModel.getDesign_id());
        values.put(FIELD_TAILORNAME, designModel.getTailor_username());
        values.put(FIELD_NAME, designModel.getDesign_name());
        values.put(FIELD_IMAGE, designModel.getDesign_url());

        try {
            db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public List<TailorDesignModel> getFavoriteCourses(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        List<TailorDesignModel> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                list.add(new TailorDesignModel(
                        cursor.getString(0),
                        cursor.getString(3),
                        cursor.getString(2),
                        cursor.getString(1)));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public boolean alreadyInFavorite(String id){
        List<TailorDesignModel> list = getFavoriteCourses();
        for (int i=0; i< list.size(); i++){
            if(Objects.equals(list.get(i).getDesign_id(), id)){
                return true;
            }
        }
        return false;
    }

    public void removeFromFavorite(String course_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, FIELD_ID+"=?", new String[]{course_id});
        db.close();
    }
}
