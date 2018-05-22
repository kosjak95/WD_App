package jakubkosman.wd_app;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.solver.GoalRow;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Map;

public class ViewGroupsActivity extends AppCompatActivity {

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_groups);

        tableLayout = (TableLayout)findViewById(R.id.table_layout);

        String index = this.getIntent().getStringExtra("indexKey");

        ((TextView)findViewById(R.id.table_title)).setText(index);

        Map<String,String> groups=new DatabaseHelper(this).getGroupsByStudentIndex(index);

        for (Map.Entry<String, String> entry : groups.entrySet())
        {
            TableRow row = new TableRow(this);

            TextView subject = new TextView(this.getApplicationContext());
            subject.setTextSize(20);
            subject.setTextColor(Color.BLACK);
            subject.setText(entry.getKey());


            TextView group = new TextView(this.getApplicationContext());
            group.setTextSize(20);
            group.setTextColor(Color.BLACK);
            group.setText(entry.getValue());

            row.addView(subject);
            row.addView(group);

            TableRow.LayoutParams params = (TableRow.LayoutParams)subject.getLayoutParams();
            TableRow.LayoutParams params1 = (TableRow.LayoutParams)group.getLayoutParams();
            params.weight = 1;
            params1.weight = 1;
            subject.setLayoutParams(params);
            group.setLayoutParams(params1);

            subject.setGravity(Gravity.CENTER);
            group.setGravity(Gravity.CENTER);
            tableLayout.addView(row);
        }
    }
}
