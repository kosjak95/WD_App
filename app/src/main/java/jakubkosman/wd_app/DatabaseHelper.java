package jakubkosman.wd_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;
import android.view.View;

import java.security.Signature;
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
    private static final String CONNECTOR_TABLE = "connector";

    SQLiteDatabase db;

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
                + SUBJECT_ID_FOREIGN +" INTEGER, "
                + COLUMN_GROUP_SIGNATURE+" TEXT, "
                + COLUMN_GROUP_VACANCY +" INTEGER,"
                + " FOREIGN KEY ("+SUBJECT_ID_FOREIGN+") REFERENCES "+SUBJECT_TABLE+"("+COLUMN_ID+"))");


        db.execSQL("create table " + CONNECTOR_TABLE
                + " ( "+COLUMN_ID+" INTEGER NOT NULL PRIMARY KEY, "
                + "studentID INTEGER, "
                + "groupID INTEGER, "
                + "FOREIGN KEY (studentID) REFERENCES students(ID), "
                + "FOREIGN KEY (groupID) REFERENCES groups(ID))");


    }

    public void insertStudent()
    {
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
        ContentValues values = new ContentValues();

        String query = "SELECT * From "+SUBJECT_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_SUBJECT, "Programowanie");

        db.insert(SUBJECT_TABLE, null, values);
        db.close();
    }

    public void insertGroup()
    {
        ContentValues values = new ContentValues();

        String query = "SELECT * From "+GROUP_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_GROUP_SIGNATURE, "GR01");
        values.put(COLUMN_GROUP_VACANCY, 30);
        values.put(SUBJECT_ID_FOREIGN, 1);

        db.insert(GROUP_TABLE, null, values);
        db.close();


    }
    public void insertConnector()
    {
        ContentValues values = new ContentValues();

        String query = "SELECT * FROM " + CONNECTOR_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put("ID", count);
        values.put("studentID", 0);
        values.put("groupID", 1);

        db.insert(CONNECTOR_TABLE, null, values);
        db.close();
    }
    public Map<String,String> getGroupsByStudentIndex(String index)
    {
        HashMap result = new HashMap<String,String>() {};

        String query = "SELECT "+COLUMN_ID+" FROM "+STUDENT_TABLE+" WHERE "+COLUMN_INDEX+" = '"+index+"'";
        Cursor user_id = db.rawQuery(query, null);

        int id_user = user_id.getColumnIndex(COLUMN_ID);

        query = "SELECT s."+ COLUMN_SUBJECT+ ", g."+COLUMN_GROUP_SIGNATURE+" FROM "+SUBJECT_TABLE+" as s, "+GROUP_TABLE+ " as g, " + CONNECTOR_TABLE + " as c WHERE c.ID = "+id_user+" AND s.ID = g.SUBJECT_ID";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        String[] groups = new String[c.getCount()];

        while(!c.isAfterLast())
        {
            String subject = c.getString(c.getColumnIndex(COLUMN_SUBJECT));
            String signature = c.getString(c.getColumnIndex(COLUMN_GROUP_SIGNATURE));

            c.moveToNext();

            result.put(subject, signature);
        }
        c.close();
        return result;
    }

    /*
     *  Function returns true if user's index and password are correct
     *  */
    public boolean checkUser(String index, String password)
    {
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

        String query = "SELECT "+ COLUMN_SUBJECT + " FROM " + SUBJECT_TABLE;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast())
        {
            String subject = c.getString(c.getColumnIndex(COLUMN_SUBJECT));
            list.add(subject);

            c.moveToNext();
        }
        return list;
    }

    public List<String> getGroupsBySubject(String subject)
    {
        List<String> list = new ArrayList<>();

        String query = "SELECT " + COLUMN_ID + " FROM " + SUBJECT_TABLE + " WHERE " + COLUMN_SUBJECT+" = '"+subject+"'";
        Cursor sub_q = db.rawQuery(query, null);

        int id_sub = sub_q.getColumnIndex(COLUMN_ID);

        query = "SELECT g." + COLUMN_GROUP_SIGNATURE + " FROM " + GROUP_TABLE + " as g," + SUBJECT_TABLE + " as s WHERE s.ID =" + id_sub;

        sub_q = db.rawQuery(query, null);
        sub_q.moveToFirst();

        while (!sub_q.isAfterLast())
        {

            String signature = sub_q.getString(sub_q.getColumnIndex(COLUMN_GROUP_SIGNATURE));
            list.add(signature);
            sub_q.moveToNext();
        }

        return list;
    }

    public boolean isVacancy(String subject, String group) {

        String query ="";

        return true;

    }
}
