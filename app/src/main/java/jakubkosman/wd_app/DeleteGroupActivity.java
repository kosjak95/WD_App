package jakubkosman.wd_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

public class DeleteGroupActivity extends AppCompatActivity {


    private Spinner spinnerSubGroup;
    private Button deleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_group);

        deleteButton = (Button) findViewById(R.id.button_delete_group);

        List<String> spinnerItems = new DatabaseHelper(this).getSubjectAndGroups();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerItems);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubGroup = (Spinner) findViewById(R.id.subgroup_spinner);

        spinnerSubGroup.setAdapter(adapter);

    }
    public void deleteSelectedGroup(View view)
    {
        String selected = spinnerSubGroup.getSelectedItem().toString();

        DatabaseHelper db = new DatabaseHelper(this);
        db.deleteGroup(selected);

        finish();

    }
}
