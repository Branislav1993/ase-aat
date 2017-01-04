package de.tum.aat.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import de.tum.aat.R;
import de.tum.aat.model.Exercise;
import de.tum.aat.utils.Utils;

//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.gson.Gson;

public class ExerciseListActivity extends AppCompatActivity {

    private static final String TAG = ExerciseListActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handleResponse("[{\n" +
                "\t\"content\": \"Hello world item\"\n" +
                "}, {\n" +
                "\t\"content\": \"Hello world item 2\"\n" +
                "}]");
    }

    private void handleResponse(String response) {
        final Gson gson = new Gson();
        final Exercise[] exercises = gson.fromJson(response, Exercise[].class);

        final ArrayAdapter<Exercise> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, exercises);
        final ListView listView = (ListView) findViewById(R.id.exercise_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Exercise exercise = (Exercise) listView.getAdapter().getItem(position);
                final Intent intent = new Intent(ExerciseListActivity.this, ExerciseDetailsActivity.class);
                Toast.makeText(ExerciseListActivity.this, "You clicked on " + exercise.content, Toast.LENGTH_SHORT).show();
//              intent.putExtra("id", narocilo.id);
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
