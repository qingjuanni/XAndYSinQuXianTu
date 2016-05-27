package org.achartengine.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * user:zhengqingju
 * Date:2016/5/17
 * Time:11:16
 * Description:Page Function.
 */

public class DbHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String DB_NAME = "sensor.db";
    private static DbHelper instance;
    private static final  String create_sql = "CREATE TABLE IF NOT EXISTS sensor_test ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "x INT NOT NULL,"
            + "y FLOAT NOT NULL,"
            + "timeline INTEGER NOT NULL)";

    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context);
        }
        return instance;
    }


    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(create_sql);
    }




    public Cursor rawQuery(){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM sensor_test ", null);
        return cursor;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
