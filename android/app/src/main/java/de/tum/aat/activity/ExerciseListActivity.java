package de.tum.aat.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.HashMap;
import java.util.Map;

import de.tum.aat.R;
import de.tum.aat.adapter.ExerciseGroupsAdapter;
import de.tum.aat.model.ExerciseGroup;
import de.tum.aat.session.SessionObject;
import de.tum.aat.utils.Utils;

public class ExerciseListActivity extends AppCompatActivity {

    private static final String TAG = ExerciseListActivity.class.getCanonicalName();
    private static final String GROUPS_URL = "http://ase-aat10.appspot.com/rest/groups/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SessionObject session = (SessionObject) getApplication();
        if(session.getCredentials() == null) {
            Utils.clearBackStackAndLaunchLogin(this);
        }

        setContentView(R.layout.activity_exercise_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final RequestQueue queue = Volley.newRequestQueue(ExerciseListActivity.this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, GROUPS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("RESPONSE", response);
                        handleResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w(TAG, "Exception: " + error.getLocalizedMessage());
                Toast toast = Toast.makeText(ExerciseListActivity.this, R.string.groups_error, Toast.LENGTH_LONG);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setTextColor(Color.RED);
                toast.show();
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

    private void handleResponse(String response) {
        final Gson gson = new Gson();
        final ExerciseGroup[] exerciseGroups = gson.fromJson(response, ExerciseGroup[].class);

//        final ArrayAdapter<ExerciseGroup> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, exerciseGroups);
        ExerciseGroupsAdapter adapter = new ExerciseGroupsAdapter(this, exerciseGroups);
//        setListAdapter(adapter);
        final ListView listView = (ListView) findViewById(R.id.exercise_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ExerciseGroup exerciseGroup = (ExerciseGroup) listView.getAdapter().getItem(position);
                final Intent intent = new Intent(ExerciseListActivity.this, ExerciseDetailsActivity.class);
//                Toast.makeText(ExerciseListActivity.this, "You clicked on " + exerciseGroup, Toast.LENGTH_SHORT).show();
                intent.putExtra("group", exerciseGroup);
                startActivity(intent);
            }
        });
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
