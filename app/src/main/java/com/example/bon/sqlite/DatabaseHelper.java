package com.example.bon.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseHelper extends SQLiteOpenHelper {

     String TableName ="table_name";

    public DatabaseHelper(Context context){
        super(context,"database_name",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create table query
        String sQuery ="create table "+ TableName
                +"(id INTEGER primary key autoincrement,text TEXT,date TEXT)";
        //Execute query
        sqLiteDatabase.execSQL(sQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop table query
        String sQuery ="drop table if exists " + TableName;
        //Execute query
        sqLiteDatabase.execSQL(sQuery);
        //Create new table
        onCreate(sqLiteDatabase);
    }

    public void insert(String text,String date){
        SQLiteDatabase database =getWritableDatabase();
        ContentValues values =new ContentValues();
        //add values
        values.put("text",text);
        values.put("date",date);
        //Insert values in database
        database.insertWithOnConflict(TableName,null,
                values,SQLiteDatabase.CONFLICT_REPLACE);
        //Close database
        database.close();
    }

    public void update(String id,String text){
        //Initialize database
        SQLiteDatabase database =getWritableDatabase();
        //Update query
        String sQuery ="update "+ TableName + " set text='" + text
                +"'where id='" + id +"'";
        //Execute query
        database.execSQL(sQuery);
        //Close database
        database.close();
    }

    public void delete(String id){
        //Initialize database
        SQLiteDatabase database =getWritableDatabase();
        //Delete query
        String sQuery ="detele from "+ TableName + " where id='" + id +"'";
        //Execute query
        database.execSQL(sQuery);
        //Close database
        database.close();
    }

    public void truncate(){
        //Ini
        SQLiteDatabase database =getWritableDatabase();

        String sQuery1 = "delete from " + TableName;

        String sQuery2 ="delete from sqlite_sequence where name='"
                + TableName +"'";

        database.execSQL(sQuery1);
        database.execSQL(sQuery2);
        //
        database.close();
    }

    public JSONArray getArray(){
        //Ini
        SQLiteDatabase database =getReadableDatabase();

        JSONArray jsonArray = new JSONArray();

        String sQuery ="select * from " + TableName;

        Cursor cursor = database.rawQuery(sQuery,null);
        //Check condition

        if (cursor.moveToFirst()){
            //When cursor move to firt item
            do {
                //Initialize json object
                JSONObject object =new JSONObject();

                try {
                    //Put all values in object
                        object.put("id",cursor.getString(0));
                        object.put("text",cursor.getString(1));
                        object.put("date",cursor.getString(2));
                        //add values in json array
                        jsonArray.put(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }while (cursor.moveToNext());
            }
            //Close cursor
            cursor.close();
            //
            database.close();
            return jsonArray;

        }

}
