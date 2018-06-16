package jakubkosman.wd_app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String user_index;
    private boolean admin = false;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user_index = getIntent().getStringExtra("indexKey");

        //decide is user_id in admin mode
        DatabaseHelper db = new DatabaseHelper(this);

        button1 = (Button) findViewById(R.id.button_join);
        button2 = (Button) findViewById(R.id.button_view);
        button3 = (Button) findViewById(R.id.button_add_student);
        button4 = (Button) findViewById(R.id.button_delete_group);
        textView = (TextView) findViewById(R.id.textView);

        admin = db.isAdmin(user_index);
        if (admin) {
            button1.setText(R.string.WD_add_subject);
            button2.setText(R.string.WD_add_group);
            button3.setVisibility(View.VISIBLE);
            button4.setVisibility(View.VISIBLE);
            textView.setText(R.string.main_hello_admin);
        }
        else
        {
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view)
    {
        Intent intent;
        switch (view.getId())
        {
            case R.id.button_join:
                if(!admin)
                    intent = new Intent(MainActivity.this, JoinGroupActivity.class);
                else
                    intent = new Intent(MainActivity.this, AddSubjectActivity.class);
                intent.putExtra("indexKey", user_index);
                startActivity(intent);
                break;
            case R.id.button_view:
                if(!admin)
                    intent = new Intent(MainActivity.this, ViewGroupsActivity.class);
                else
                    intent = new Intent(MainActivity.this, AddGroupActivity.class);
                intent.putExtra("indexKey",user_index);
                startActivity(intent);
                break;
            case R.id.button_add_student:
                intent = new Intent(MainActivity.this, AddStudentActivity.class);
                intent.putExtra("indexKey",user_index);
                startActivity(intent);
                break;
            case R.id.button_delete_group:
                intent = new Intent(MainActivity.this, DeleteGroupActivity.class);
                intent.putExtra("indexKey",user_index);
                startActivity(intent);
                break;
        }

    }
}
