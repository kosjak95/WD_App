package jakubkosman.wd_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class AddGroupActivity extends AppCompatActivity {

    private Spinner spinnerSubjects;
    private EditText code;
    private EditText vaccancy;
    private Button btnSubbmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        code = (EditText)findViewById(R.id.txt_insert_code);
        vaccancy = (EditText)findViewById(R.id.txt_insert_vaccancy);
        btnSubbmit = (Button)findViewById(R.id.button_add_group) ;

        List<String> spinnerItems = new DatabaseHelper(this).getSubjects();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerItems);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjects = (Spinner) findViewById(R.id.subject_spinner_add_group);

        spinnerSubjects.setAdapter(adapter);

    }

    public void addNewGroup(View view)
    {

        String subject = spinnerSubjects.getSelectedItem().toString();
        String group_code = code.getText().toString();
        int group_vaccancy = Integer.parseInt(vaccancy.getText().toString());

        DatabaseHelper db = new DatabaseHelper(this);
        db.insertGroup(subject, group_code, group_vaccancy);

        Toast.makeText(getApplicationContext(), "Pomyślnie dodano grupę", Toast.LENGTH_LONG).show();
        finish();

    }
}
