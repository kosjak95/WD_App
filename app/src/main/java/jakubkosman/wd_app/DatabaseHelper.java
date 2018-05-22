package jakubkosman.wd_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WD.db";
    private static final String STUDENT_TABLE = "students";
    private static final String GROUP_TABLE = "groups";
    private static final String SUBJECT_TABLE = "subjects";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_INDEX = "NR_INDEX";
    private static final String COLUMN_PESEL = "PESEL";
    private static final String COLUMN_GROUP_SIGNATURE = "SIGNATURE";
    private static final String COLUMN_GROUP_VACANCY = "VACANCY";
    private static final String COLUMN_SUBJECT = "SUBJECT";
    private static final String SUBJECT_ID_FOREIGN = "SUBJECT_ID";

    SQLiteDatabase db;
    //private static final String TABLE_CREATE = "create table students ( ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
     //       "nr_index text not null , pesel text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    //Create needed tables for activity
    //----------------------------------------------------------------------------------------------
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + STUDENT_TABLE
                + "("+COLUMN_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_INDEX+" TEXT, "
                + COLUMN_PESEL+" TEXT)");

        db.execSQL("create table " + SUBJECT_TABLE
                +"("+COLUMN_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_SUBJECT+" TEXT)");

        db.execSQL("create table " + GROUP_TABLE
                + "("+COLUMN_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + SUBJECT_ID_FOREIGN +" INT, "
                + COLUMN_GROUP_SIGNATURE+" TEXT, "
                + COLUMN_GROUP_VACANCY +" INTEGER,"
                + " FOREIGN KEY ("+SUBJECT_ID_FOREIGN+") REFERENCES "+SUBJECT_TABLE+"("+COLUMN_ID+"))");


        db.execSQL("create table connector ( id SMALLINT NOT NULL PRIMARY KEY, studentID SMALLINT NOT NULL, groupID SMALLINT NOT NULL)");

        //db.execSQL("ALTER TABLE conncetor ADD CONSTRAINT stud FOREGIN KEY stud(studentID) REFERENCES students(ID) ON UPDATE RESTRICT ON DELETE RESTRICT");
        //db.execSQL("ALTER TABLE conncetor ADD CONSTRAINT gr FOREGIN KEY gr(groupID) REFERENCES groups(ID) ON UPDATE RESTRICT ON DELETE RESTRICT");


    }

    public void insertStudent()
    {
        db=this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "SELECT * From "+STUDENT_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_INDEX, "128629");
        values.put(COLUMN_PESEL, "12345678900");

        db.insert(STUDENT_TABLE, null, values);
        db.close();
    }
    public void insertSubject()
    {
        db=this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "SELECT * From "+SUBJECT_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_SUBJECT, "Angielski");

        db.insert(SUBJECT_TABLE, null, values);
        db.close();
    }

    public void insertGroup()
    {
        db=this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "SELECT * From "+GROUP_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_GROUP_SIGNATURE, "GR01");
        values.put(COLUMN_GROUP_VACANCY, 30);
        values.put(SUBJECT_ID_FOREIGN, 0);

        db.insert(GROUP_TABLE, null, values);
        db.close();
    }

    public Map<String,String> getGroupsByStudentIndex(String index)
    {
        HashMap result = new HashMap<String,String>() {};

        //TODO: get groups from db
        result.put("Pierwszy","Drugi");

        return result;
    }

    /*
     *  Function returns true if user's index and password are correct
     *  */
    public boolean checkUser(String index, String password)
    {
        db=this.getWritableDatabase();
        String query = "SELECT "+COLUMN_ID+" FROM "+STUDENT_TABLE+" WHERE "+COLUMN_INDEX+" = '"+index+"' AND "+COLUMN_PESEL+" = '"+password+"'";
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();
        if(count>0)
            return true;
        else
            return false;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + STUDENT_TABLE;
        db.execSQL(query);
        this.db = db;
    }

    public List<String> getSubjects() {

        List<String> list = new ArrayList<>();

        //TODO: get subjects from db
        list.add("Pierwszy przedmiot");
        list.add("Drugi przedmiot");

        return list;
    }
}
