package de.tum.aat.activity;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import de.tum.aat.R;
import de.tum.aat.fragment.QRFragment;
import de.tum.aat.utils.Utils;

public class ChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
