package jakubkosman.wd_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "WD.db";
    public static final String TABLE_NAME = "student_table";
    public static final String COL_1 = "student_table";
    public static final String COL_2 = "ID";
    public static final String COL_3 = "Name";
    public static final String COL_4 = "Index";
    public static final String COL_5 = "PESEL";

    public DatabaseHelper(Context context) { //uruchomienie kontruktora tworzy baze
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase(); //just for checking
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, NR_INDEX INTEGER, PESEL INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate(db);
    }
}
