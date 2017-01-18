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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.tum.aat.R;
import de.tum.aat.model.BadRequestError;
import de.tum.aat.model.GroupTimeslot;
import de.tum.aat.model.Student;
import de.tum.aat.session.SessionObject;
import de.tum.aat.utils.Utils;

public class ProfileAndStatisticsActivity extends AppCompatActivity {

    private static final String TAG = ProfileAndStatisticsActivity.class.getCanonicalName();
    private static final String STUDENT_URL = "http://ase-aat10.appspot.com/rest/students/{student_id}";

    private TextView name;
    private TextView lastName;
    private TextView email;
    private TextView hasBonus;
    private TextView numAttended;
    private TextView numPresentations;
    private TextView timeslotsAttended;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_and_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (TextView) findViewById(R.id.student_name);
        lastName = (TextView) findViewById(R.id.student_last_name);
        email = (TextView) findViewById(R.id.student_email);
        hasBonus = (TextView) findViewById(R.id.student_has_bonus);
        numAttended = (TextView) findViewById(R.id.student_num_timeslots);
        numPresentations = (TextView) findViewById(R.id.student_num_presentations);
        timeslotsAttended = (TextView) findViewById(R.id.student_timeslots_attended);

        final SessionObject session = (SessionObject) getApplication();
        if (session.getCredentials() == null || session.getId() == null) {
            Utils.clearBackStackAndLaunchLogin(this);
        }

        String studentUrl = STUDENT_URL.replace("{student_id}", Long.toString(session.getId()));
        final RequestQueue queue = Volley.newRequestQueue(ProfileAndStatisticsActivity.this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, studentUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("RESPONSE", response);
                        handleResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(400 == error.networkResponse.statusCode) {
                    try {
                        handleError(new String(error.networkResponse.data, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.w(TAG, "Exception: " + error.getLocalizedMessage());
                    Toast toast = Toast.makeText(ProfileAndStatisticsActivity.this, R.string.unknown_error, Toast.LENGTH_LONG);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextColor(Color.RED);
                    toast.show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String auth = "Basic " + session.getCredentials();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        queue.add(stringRequest);
    }

    private void handleResponse(String response) {
        final Gson gson = new Gson();
        final Student student = gson.fromJson(response, Student.class);

        name.setText(getString(R.string.student_name) + student.name);
        lastName.setText(getString(R.string.student_last_name) + student.lastName);
        email.setText(getString(R.string.student_email) + student.email);
        if(student.hasBonus) {
            hasBonus.setText(getString(R.string.student_has_bonus) + "Yes");
        } else {
            hasBonus.setText(getString(R.string.student_has_bonus) + "No");
        }
        if(student.timeslotsAttended == null) {
            numAttended.setText(getString(R.string.student_num_attended) + "0");
        } else {
            numAttended.setText(getString(R.string.student_num_attended) + Integer.toString(student.timeslotsAttended.length));
        }
        numPresentations.setText(getString(R.string.student_num_presentations) + Integer.toString(student.numberOfPresentations));
        if(student.timeslotsAttended == null || student.timeslotsAttended.length == 0) {
            timeslotsAttended.setText(getString(R.string.student_timeslots_attended) + "No attendances" );
        } else {
            timeslotsAttended.setText(getString(R.string.student_timeslots_attended) + "\n" );
            for (GroupTimeslot timeslot : student.timeslotsAttended) {
                DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                String start = formatter.format(new Date(timeslot.start));
                String end = formatter.format(new Date(timeslot.end));
                timeslotsAttended.setText(timeslotsAttended.getText() + start + " - " + end + "\n");
            }
        }

    }

    private void handleError(String error) {
        final Gson gson = new Gson();
        final BadRequestError badReqError = gson.fromJson(error, BadRequestError.class);

        Toast toast = Toast.makeText(ProfileAndStatisticsActivity.this, badReqError.localizedMessage, Toast.LENGTH_LONG);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.RED);
        toast.show();
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
