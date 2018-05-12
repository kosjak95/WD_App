package jakubkosman.wd_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class NoteActivity extends AppCompatActivity {

    Bundle note = new Bundle();
    EditText editText;
    private String path = Environment.getExternalStorageDirectory().toString() + "/WDApp/Notes";
    private final int MEMORY_ACCES = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("note", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editText = (EditText) findViewById(R.id.notepad1);
        editText.setText(note.getString("noteText"));

        if(ActivityCompat.shouldShowRequestPermissionRationale(NoteActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {}
        else
        {
            ActivityCompat.requestPermissions(NoteActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MEMORY_ACCES);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case MEMORY_ACCES:
                if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Jeśli nie zostanie udzielone pozwolenie to zapis się nie wykona ", Toast.LENGTH_LONG).show();

                }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //zapisywać można do bazy sql lite
        //do sharp preference? zapis w pamieci apliakcji - stosowany do zapisywania ustawien
        //do JSONa
        createDir();
        createFile();
        finish();
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onRestart() {
        Log.d("note", "onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d("note", "onResume");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d("note", "onStop");
        note.putString("noteText", editText.getText().toString());
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.d("note", "onStart");
        note.putString("noteText", editText.getText().toString());
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d("note", "onPause");
        note.putString("noteText", editText.getText().toString());
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d("note", "onDestroy");
        super.onDestroy();
    }

    public void createDir()
    {
        File folder = new File(path);
        if(!folder.exists())
        {
            try
            {
             folder.mkdir();
            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }

    public void createFile()
    {
        File file = new File(path+"/"+System.currentTimeMillis()+".txt");
        FileOutputStream fOut;
        OutputStreamWriter outputStreamWriter;
        try
        {
            fOut = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fOut);
            outputStreamWriter.append(editText.getText());
            outputStreamWriter.close();
            fOut.close();
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


    }


}
