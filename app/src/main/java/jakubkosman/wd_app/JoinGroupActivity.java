package jakubkosman.wd_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class JoinGroupActivity extends AppCompatActivity {

    private Spinner spinnerSubjects;
    private Spinner spinnerGroups;
    private String index = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        index = this.getIntent().getStringExtra("indexKey");

        List<String> spinnerItems = new DatabaseHelper(this).getSubjects();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerItems);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjects = (Spinner) findViewById(R.id.subject_spinner);
        spinnerGroups = (Spinner) findViewById(R.id.group_spinner);

        spinnerSubjects.setAdapter(adapter);

        spinnerSubjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                List<String> spinnerGroupsItems = new DatabaseHelper(JoinGroupActivity.this).getGroupsBySubject((((AppCompatTextView) selectedItemView).getText()).toString());

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        JoinGroupActivity.this, android.R.layout.simple_spinner_item, spinnerGroupsItems);

                spinnerGroups.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    public void onClick(View view)
    {
        DatabaseHelper db = new DatabaseHelper(this);
        String subject = spinnerSubjects.getSelectedItem().toString();
        String group = spinnerGroups.getSelectedItem().toString();

        if(db.isVacancy(subject,group))
        {
            int output = db.joinStudentToGroup(index, subject, group);

            if(output == 1)
                Toast.makeText(getApplicationContext(), "Juz jesteś w tej grupie", Toast.LENGTH_LONG).show();
            else if (output == 2)
                Toast.makeText(getApplicationContext(), "Byłeś już w grupie z tego przedmiotu, lecz zostałeś przepisany na żądanie", Toast.LENGTH_LONG).show();
            else if(output == 3)
                Toast.makeText(getApplicationContext(), "Zostałeś dopisany do wskazanej grupy", Toast.LENGTH_LONG).show();

            if(output != 1)
                finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Brak miejsca w wybraniej grupie", Toast.LENGTH_LONG).show();
        }
    }
}
