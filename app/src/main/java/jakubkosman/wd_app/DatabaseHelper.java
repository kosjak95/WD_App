package jakubkosman.wd_app;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;
import android.view.View;
import android.widget.Toast;

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
    /*
    Insert new student into database
     */
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

    /*
    Insert new subject into database
     */
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

    /*
    Insert new subject group into database
     */
    public void insertGroup()
    {
        ContentValues values = new ContentValues();

        String query = "SELECT * From "+GROUP_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_GROUP_SIGNATURE, "GR02");
        values.put(COLUMN_GROUP_VACANCY, 30);
        values.put(SUBJECT_ID_FOREIGN, 1);

        db.insert(GROUP_TABLE, null, values);
        db.close();
    }

    /*
    Insert new connection between Student and Group
     */
    public void insertConnector(int student_id, int group_id)
    //public void insertConnector()
    {
        ContentValues values = new ContentValues();

        String query = "SELECT * FROM " + CONNECTOR_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToLast();

        int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));

        //@DONE: tutaj nie mozna brac counta, bo jak bedziemy przepisywac gosci, to pewnie sypną sie idiki, i wtedy count nie bedzie pokrywal się z tym jaki powinien zostać wstawiony nastepny  ....???

        values.put("ID", id+1);
        values.put("studentID", student_id);
        values.put("groupID", group_id);

        db.insert(CONNECTOR_TABLE, null, values);
        db.close();
    }
    public void delete()
    {
        db.execSQL("DELETE FROM connector WHERE ID > 1");
    }

    /*
    Get each student groups to display from db
     */
    public Map<String,String> getGroupsByStudentIndex(String index)
    {
        HashMap result = new HashMap<String,String>() {};

        //jak zacznie zawsze dodawac studenta 0 to spradz query na debugu bo pewnie jest źle
        String query = "SELECT "+COLUMN_ID+" FROM "+STUDENT_TABLE+" WHERE "+COLUMN_INDEX+" = '"+index+"'";
        Cursor user_id = db.rawQuery(query, null);

        int id_user = user_id.getColumnIndex(COLUMN_ID);

        query = "SELECT s."+ COLUMN_SUBJECT+ ", g."+COLUMN_GROUP_SIGNATURE+" FROM "+SUBJECT_TABLE+" as s, "+GROUP_TABLE+ " as g, " + CONNECTOR_TABLE + " as c WHERE c.ID = "+id_user+" AND s.ID = g.SUBJECT_ID";

//        SELECT s.SUBJECT, g.SIGNATURE FROM GROUPS as g, STUDENTS as stud, SUBJECTS as S, connector as c, GROUPS as g INNER JOIN SUBJECTS as s ON g.SUBJECT_ID = s.ID, connector as c INNER JOIN STUDENTS as stud ON c.STUDENT_ID = stud.ID, GROUPS as g INNER JOIN connector as c ON c.GROUP_ID = g.ID WHERE stud.NR_INDEX = '128629'

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
        user_id.close();
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


    /*
    Upgrade
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + STUDENT_TABLE;
        db.execSQL(query);
        this.db = db;
    }

    /*
    Get all Subjects names from db
     */
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
        c.close();
        return list;
    }

    /*
    Get all Groups of Subject to display it into SpinEdit
    Student join to group
     */
    public List<String> getGroupsBySubject(String subject)
    {
        List<String> list = new ArrayList<>();

        String query = "SELECT g.SIGNATURE FROM GROUPS as g INNER JOIN SUBJECTS as s ON g.SUBJECT_ID = s.ID WHERE s.SUBJECT ='" + subject + "'";
        Cursor sub_q = db.rawQuery(query, null);
        sub_q.moveToFirst();

        while (!sub_q.isAfterLast())
        {

            String signature = sub_q.getString(sub_q.getColumnIndex(COLUMN_GROUP_SIGNATURE));
            list.add(signature);
            sub_q.moveToNext();
        }

        sub_q.close();
        return list;
    }


    /*
    Check if is enough Vacancy in group to let Student join to.
     */
    public boolean isVacancy(String subject, String group) {

        String query = "SELECT " + COLUMN_ID + " FROM " + SUBJECT_TABLE + " WHERE " + COLUMN_SUBJECT + " = '" + subject + "'";
        //String query = "SELECT * FROM subjects WHERE SUBJECT = 'Programowanie'";
        Cursor cursor = db.rawQuery(query, null);


        cursor.moveToFirst();
        //String id = "";
        //int sub_id = 0;

        //String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
        int sub_id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));

        //cursor.moveToFirst();
        //int sub_id = cursor.getColumnIndex(COLUMN_ID);

        query = "SELECT " + COLUMN_GROUP_VACANCY + " FROM " + GROUP_TABLE + " WHERE " + SUBJECT_ID_FOREIGN + " = " + sub_id + " AND " + COLUMN_GROUP_SIGNATURE + " = '" + group + "'";
        cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        //String vac = cursor.getColumnIndex(COLUMN_GROUP_VACANCY);
        int vacancy = cursor.getInt(cursor.getColumnIndex(COLUMN_GROUP_VACANCY));

        cursor.close();
        if(vacancy<0)
            return false;
        else
            return true;
    }

    /*
    Insert new row to database - it join student to group;
    Returns:
    0 - error
    1 - already in this group,
    2 - in group from this subject but changed,
    3 - just added.
     */
    public int joinStudentToGroup(String index, String subject, String group)
    {
        String query = "SELECT " + COLUMN_ID + " FROM " + STUDENT_TABLE + " WHERE " + COLUMN_INDEX + " = '" + index + "'";
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        int user_id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));

        query = "SELECT "+COLUMN_ID+" FROM "+SUBJECT_TABLE+" WHERE "+COLUMN_SUBJECT+" = '"+subject+"'";
        cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        int subject_id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));

        query = "SELECT "+COLUMN_ID+" FROM "+GROUP_TABLE+" WHERE "+COLUMN_GROUP_SIGNATURE+" = '"+group+"' AND " + SUBJECT_ID_FOREIGN + " = " + subject_id;
        cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        int group_id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));


        if(isInThisGroup(user_id, group_id))
            return 1;
        else if(isInGroupOfThisSubject(user_id, subject_id, group_id))
            return 2;
        else //if(!isInGroupOfThisSubject(user_id, subject_id, group_id))
        {
            insertConnector(user_id, group_id);
            return 3;
        }
//        return 0;
    }

    /*
    funciotn check if user is in given group
     */
    private boolean isInThisGroup(int user_id, int group_id)
    {
        String query = "SELECT * FROM " + CONNECTOR_TABLE + " WHERE studentID = " + user_id + " AND groupID = " + group_id;
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        if(cursor.getCount()>0)
            return true;

        return false;
    }

    /*
    function check if user is in group from this subject,
    if true, function should remove him from actual and join to given,
    return true if succesfull
     */
    //@DONE DEBUG IF THIS IS WORKING PROPPER, AND AFTER DELETE WE HAVE TO INSERT NEW VALUE
    private boolean isInGroupOfThisSubject(int user_id, int subject_id, int group_id)
    {
        String query = "SELECT " + COLUMN_ID + " FROM " + GROUP_TABLE + " WHERE " + SUBJECT_ID_FOREIGN + " = " + subject_id;
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            int temp_group = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));

            String q = "SELECT * FROM " + CONNECTOR_TABLE + " WHERE studentID = " + user_id + " AND groupID = " + temp_group;
            Cursor temp_cursor = db.rawQuery(q, null);
            temp_cursor.moveToFirst();

            if(temp_cursor.getCount() > 0)
            {
                String id = Integer.toString(temp_group);
                db.delete(CONNECTOR_TABLE, COLUMN_ID + " =?", new String[]{id});

                insertConnector(user_id, group_id);

                return true;

            }


            cursor.moveToNext();
        }
        return false;
    }
}
