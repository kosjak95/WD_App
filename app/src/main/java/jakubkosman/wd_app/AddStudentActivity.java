package jakubkosman.wd_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddStudentActivity extends AppCompatActivity {

    private Button btnAddStudent;
    private EditText text_index;
    private EditText text_pesel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        btnAddStudent = (Button)findViewById(R.id.button_add_student);
        text_index = (EditText)findViewById(R.id.edit_index_num);
        text_pesel = (EditText) findViewById(R.id.edit_pesel);
    }

    public void addStudent(View view)
    {
        DatabaseHelper db = new DatabaseHelper(this);
        String index = text_index.getText().toString();
        String pesel = text_pesel.getText().toString();
        LoginActivity check_creditentials = new LoginActivity();

        boolean can_add = (check_creditentials.isIndexValid(index) && check_creditentials.isPasswordValid(pesel));
        if(can_add)
            if(db.insertStudent(index, pesel))
                Toast.makeText(getApplicationContext(), "Pomyślnie dodano studenta", Toast.LENGTH_LONG).show();

        if(!can_add)
                Toast.makeText(getApplicationContext(), "Błędne dane osobowe", Toast.LENGTH_LONG).show();

        finish();
    }
}
