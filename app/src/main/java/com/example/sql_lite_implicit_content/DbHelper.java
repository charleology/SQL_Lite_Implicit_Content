package com.example.sql_lite_implicit_content;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public static final String dbname="dbRecs";
    public static final String tblname="tblRecs";

    public DbHelper(Context context){
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " +tblname+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, lname TEXT, fname TEXT, minit TEXT, email TEXT, contact TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+tblname);
        onCreate(db);
    }

    public boolean insertRec(String lname, String fname, String minit, String email, String contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues conVal = new ContentValues();
        conVal.put("lname",lname);
        conVal.put("fname",fname);
        conVal.put("minit",minit);
        conVal.put("email",email);
        conVal.put("contact",contact);
        long result = db.insert(tblname, null, conVal);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getRec(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        if(id == "0"){
            res = db.rawQuery("SELECT * FROM " +tblname,null);
        }
        else{
            res = db.rawQuery("SELECT * FROM " +tblname+ " WHERE id= '"+id+"'",null);
        }
        return res;
    }

    public boolean deleteRec(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tblname, "id = ?", new String[]{id});
        return true;
    }

    public boolean updateRec(String id, String lname, String fname, String minit, String email, String contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues conVal = new ContentValues();
        conVal.put("id", id);
        conVal.put("lname", lname);
        conVal.put("fname",fname);
        conVal.put("minit",minit);
        conVal.put("email", email);
        conVal.put("contact", contact);
        db.update(tblname, conVal, "id = ?", new String[]{id});
        return true;
    }
}
