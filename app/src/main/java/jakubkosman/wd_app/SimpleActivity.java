package jakubkosman.wd_app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SimpleActivity extends AppCompatActivity {

    TextView output;
    EditText percents;
    EditText numbers;
    Button calcBtn;// = (Button) findViewById(R.id.bCalculate);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        output = (TextView)findViewById(R.id.textOutput);
        percents = (EditText)findViewById(R.id.editPercents);
        numbers = (EditText)findViewById(R.id.editNumber);
        calcBtn = (Button) findViewById(R.id.bCalculate);

    }


    public void clickCalculate(View view) {
        float percentage = Float.parseFloat(percents.getText().toString());
        float num = Float.parseFloat((numbers.getText().toString()));

        float out = percentage/100 * num;

        output.setText(Float.toString(out));
    }
}
