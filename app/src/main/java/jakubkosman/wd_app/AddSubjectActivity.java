package jakubkosman.wd_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddSubjectActivity extends AppCompatActivity {

    private EditText sub_name;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        sub_name = (EditText) findViewById(R.id.text_sub_name);
        add = (Button)findViewById(R.id.button_add_subject);

    }

    public void addSubjectClick(View view)
    {
        DatabaseHelper db = new DatabaseHelper(this);
        String name = sub_name.getText().toString();
        if(name.length() > 0 )
            if(db.insertSubject(name))
                Toast.makeText(getApplicationContext(), "Pomyślnie dodano przedmiot", Toast.LENGTH_LONG).show();
        finish();

    }
}
