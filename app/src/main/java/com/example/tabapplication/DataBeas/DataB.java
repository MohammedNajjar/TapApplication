package com.example.tabapplication.DataBeas;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


import com.example.tabapplication.model.ServesUser;

import java.util.ArrayList;

public class DataB extends SQLiteOpenHelper {


    public static final String DB_NAME = "student.db";
    public static final int DB_VIRSION = 1;



    public static final String STUDENT_TB_NAME = "student";
    public static final String STUDENT_CLN_ID = "idNumber";
    public static final String STUDENT_CLN_USRE = "userName";
    public static final String STUDENT_CLN_PASSWORD = "password";


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE "+STUDENT_TB_NAME+"("+STUDENT_CLN_ID + " INTEGER , " +
                STUDENT_CLN_USRE+" TEXT ,"+  STUDENT_CLN_PASSWORD+" TEXT "+ " ) " )  ;


    }

    public void fillData(){
        insertUser(new ServesUser("ReemAhmad",2,"k1353"));
        insertUser(new ServesUser("Sarah Munsor",3,"L4446"));
        insertUser(new ServesUser("Maram Khaled",4,"M2288"));
        insertUser(new ServesUser("Rahaf Fahad",5,"N6754"));
        insertUser(new ServesUser("Joud Abdullah",6,"H7600"));
        insertUser(new ServesUser("Ashwaq Mohammed",7,"A1234"));
        insertUser(new ServesUser("Lames Saad",8,"L6657"));
        insertUser(new ServesUser("Ghadah Ali",9,"G9908"));
        insertUser(new ServesUser("Hanan Hamd",12,"H3344"));
        insertUser(new ServesUser("Malak Fares ",14,"M0122"));


    }

    public DataB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VIRSION); }


    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TB_NAME);
        onCreate(db);

    }



    public boolean insertUser(ServesUser st) {
        Log.d("inser User Log","");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STUDENT_CLN_ID, st.getIdNumber());
        values.put(STUDENT_CLN_USRE, st.getUserName());
        values.put(STUDENT_CLN_PASSWORD, st.getPassword());
        long id = db.insert(STUDENT_TB_NAME, null, values);
        if (id > 0)
            return true;
        return false;
    }


    public ServesUser getStudint(int profileId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+STUDENT_TB_NAME+" WHERE "
                +STUDENT_CLN_ID+" =? ", new String[]{String.valueOf(profileId)});
        if (cursor != null && cursor.moveToFirst()) {

            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(STUDENT_CLN_ID));
            @SuppressLint("Range") String Password = cursor.getString(cursor.getColumnIndex(STUDENT_CLN_PASSWORD));
            @SuppressLint("Range") String User = cursor.getString(cursor.getColumnIndex(STUDENT_CLN_USRE));


            ServesUser s=new ServesUser(User,id,Password);
            cursor.close();
            return s;
        }
        return null;
    }
    public ServesUser getCHStudint(int idNumber , String Password){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+STUDENT_TB_NAME+" WHERE "
                +STUDENT_CLN_ID+" =? AND "+STUDENT_CLN_PASSWORD+"=?", new String[]{String.valueOf(idNumber),Password});
        if (cursor != null && cursor.moveToFirst()) {

            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(STUDENT_CLN_ID));
            @SuppressLint("Range") String p = cursor.getString(cursor.getColumnIndex(STUDENT_CLN_PASSWORD));
            @SuppressLint("Range") String User = cursor.getString(cursor.getColumnIndex(STUDENT_CLN_USRE));

            ServesUser s=new ServesUser(User,id,p);
            cursor.close();
            return s;
        }
        return null;
    }







}
