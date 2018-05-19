package jakubkosman.wd_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WD.db";
    private static final String TABLE_NAME = "students";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_INDEX = "nr_index";
    private static final String COLUMN_PESEL = "pesel";
    SQLiteDatabase db;
    //private static final String TABLE_CREATE = "create table students ( ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
     //       "nr_index text not null , pesel text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NR_INDEX TEXT, PESEL TEXT)");
    }

    public void insertStudent()
    {
        db=this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "SELECT * From students";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_INDEX, "128629");
        values.put(COLUMN_PESEL, "1234567890");

        db.insert(TABLE_NAME, null, values);
        db.close();
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        this.db = db;
    }
}
