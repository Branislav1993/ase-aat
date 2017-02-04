package de.tum.aat.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.tum.aat.R;
import de.tum.aat.model.ExerciseGroup;
import de.tum.aat.model.GroupTimeslot;
import de.tum.aat.session.SessionObject;
import de.tum.aat.utils.Utils;

public class ExerciseDetailsActivity extends AppCompatActivity {

    private static String GROUP_REGISTER_URL = "http://ase-aat10.appspot.com/rest/groupregister?groupId={group_id}&studentId={student_id}";
    private static final String TAG = ExerciseDetailsActivity.class.getCanonicalName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ExerciseGroup group = (ExerciseGroup) getIntent().getSerializableExtra("group");

        TextView groupName = (TextView) findViewById(R.id.gr_name);
        TextView numStudents = (TextView) findViewById(R.id.gr_students);
        TextView sessions = (TextView) findViewById(R.id.sessions);

        groupName.setText(group.name);
        if(group.students == null) {
            numStudents.setText("Number of registered students: 0");
        } else {
            numStudents.setText("Number of registered students: " + group.students.length);
        }
        if(group.timeslots != null && group.timeslots.length != 0) {
            sessions.setText("Exercise sessions:\n");
            for (GroupTimeslot timeslot : group.timeslots) {
                DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                String start = formatter.format(new Date(timeslot.start));
                String end = formatter.format(new Date(timeslot.end));
                sessions.setText(sessions.getText() + start + " - " + end + "\n");
            }
        } else {
            sessions.setText("Exercise sessions: not set yet");
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
        if (id == R.id.action_sign_out) {
            Utils.signOut(this);
        }

        return super.onOptionsItemSelected(item);
    }

}
