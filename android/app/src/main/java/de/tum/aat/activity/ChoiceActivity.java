package de.tum.aat.activity;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
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
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.tum.aat.R;
import de.tum.aat.fragment.QRFragment;
import de.tum.aat.model.BadRequestError;
import de.tum.aat.model.ExerciseGroup;
import de.tum.aat.model.GroupTimeslot;
import de.tum.aat.session.SessionObject;
import de.tum.aat.utils.Utils;

public class ChoiceActivity extends AppCompatActivity {

    private static final String TAG = ChoiceActivity.class.getCanonicalName();
    private static final String GROUPS_URL = "http://ase-aat10.appspot.com/rest/groups/{group_id}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SessionObject session = (SessionObject) getApplication();
        if (session.getCredentials() == null || session.getCurStudent() == null) {
            Utils.clearBackStackAndLaunchLogin(this);
        }

//        boolean alarmUp = (PendingIntent.getBroadcast(this, 0,
//                new Intent(this, Alarm.class),
//                PendingIntent.FLAG_NO_CREATE) != null);
//
//        if (alarmUp) {
//            Log.d("myTag", "Alarm is already active");
//        } else {
//            Log.d("myTag", "Alarm not active");
//        }

        final TextView bonusAlert = (TextView) findViewById(R.id.bonus_alert);

        if(session.getCurStudent().exerciseGroup != null) {
            final String groupsUrl = GROUPS_URL.replace("{group_id}", Long.toString(session.getCurStudent().exerciseGroup));
            final RequestQueue queue = Volley.newRequestQueue(ChoiceActivity.this);
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, groupsUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Log.v("RESPONSE", response);
                            handleResponse(response, bonusAlert);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(400 == error.networkResponse.statusCode || 404 == error.networkResponse.statusCode) {
                        try {
                            handleError(new String(error.networkResponse.data, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.w(TAG, "Exception: " + error.getLocalizedMessage());
                        Toast toast = Toast.makeText(ChoiceActivity.this, R.string.unknown_error, Toast.LENGTH_LONG);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(Color.RED);
                        toast.show();
                    }
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
//                Log.v(TAG, session.getCredentials());
                    String auth = "Basic " + session.getCredentials();
                    headers.put("Authorization", auth);
                    return headers;
                }
            };

            queue.add(stringRequest);
        }



        Button presenceQrBtn = (Button) findViewById(R.id.presence_qr_button);
        presenceQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayQRCode(true);
            }
        });

        Button presentationQrBtn = (Button) findViewById(R.id.presentation_qr_button);
        presentationQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayQRCode(false);
            }
        });

        Button exerciseGroupsBtn = (Button) findViewById(R.id.exercise_groups_button);
        exerciseGroupsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceActivity.this, ExerciseListActivity.class);
                startActivity(intent);
            }
        });

        Button profileAndStatisticsBtn = (Button) findViewById(R.id.user_profile_button);
        profileAndStatisticsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceActivity.this, ProfileAndStatisticsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void handleResponse(String response, TextView bonusAlert) {
        final Gson gson = new Gson();
        final ExerciseGroup exerciseGroup = gson.fromJson(response, ExerciseGroup.class);

        GroupTimeslot[] timeslots = exerciseGroup.timeslots;
        if (timeslots != null) {
//            Log.v("HAVE EXERCISES PASSED: ", Boolean.toString(hasLastExercisePassed(timeslots)));
            if(hasLastExercisePassed(timeslots)) {
                bonusAlert.setText(getResources().getString(R.string.bonus_alert));
                bonusAlert.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean hasLastExercisePassed(GroupTimeslot[] timeslots) {
        Long curTime = System.currentTimeMillis();
//        Log.v("CURRENT TIME: ", Long.toString(curTime));
        for (GroupTimeslot timeslot : timeslots) {
            if(timeslot.end > curTime) {
//                Log.v("TIMESLOT: ", Long.toString(timeslot.end));
                return false;
            }
        }

        return true;
    }

    private void handleError(String error) {
        final Gson gson = new Gson();
        final BadRequestError badReqError = gson.fromJson(error, BadRequestError.class);

        Toast toast = Toast.makeText(ChoiceActivity.this, badReqError.localizedMessage, Toast.LENGTH_LONG);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.RED);
        toast.show();
    }

    private void displayQRCode(boolean isAttendance) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = QRFragment.newInstance(isAttendance);
        newFragment.show(ft, "dialog");
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
