package de.tum.aat.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.tum.aat.R;
import de.tum.aat.model.BadRequestError;
import de.tum.aat.model.ExerciseGroup;
import de.tum.aat.session.SessionObject;
import de.tum.aat.utils.Utils;

/**
 * Created by Naum on 1/16/2017.
 */
public class ExerciseGroupsAdapter extends ArrayAdapter<ExerciseGroup> {

    private static final String TAG = ExerciseGroupsAdapter.class.getCanonicalName();
    private static final String GROUP_REGISTER_URL = "http://ase-aat10.appspot.com/rest/groupregister?groupId={group_id}&studentId={student_id}";
    private static final String GROUP_DEREGISTER_URL = "http://ase-aat10.appspot.com/rest/groupderegister?groupId={group_id}&studentId={student_id}";

    private Activity ctx;

    public ExerciseGroupsAdapter(Activity context, ExerciseGroup[] exerciseGroups) {
        super(context, 0, exerciseGroups);
        this.ctx = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final ExerciseGroup exerciseGroup = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_exercise_groups, parent, false);
        }



        // Lookup view for data population
        TextView groupName = (TextView) convertView.findViewById(R.id.group_name);
        TextView numStudents = (TextView) convertView.findViewById(R.id.num_students);
        final ImageButton register = (ImageButton) convertView.findViewById(R.id.register);
        final ImageButton deregister = (ImageButton) convertView.findViewById(R.id.deregister);

        // Populate the data into the template view using the data object
        groupName.setText(exerciseGroup.name);
        numStudents.setText("Students: " + (exerciseGroup.students == null ? "0" : Integer.toString(exerciseGroup.students.length)));

        final SessionObject session = (SessionObject) ctx.getApplication();
        if (session.getCredentials() == null || session.getId() == null) {
            Utils.clearBackStackAndLaunchLogin(ctx);
        }

        if(session.getExerciseGroup() != null && session.getExerciseGroup() == exerciseGroup.id) {
            register.setVisibility(View.INVISIBLE);
            deregister.setVisibility(View.VISIBLE);
        } else {
            register.setVisibility(View.VISIBLE);
            deregister.setVisibility(View.INVISIBLE);
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ctx)
                        .setTitle("Deregister from a group")
                        .setMessage("Are you sure you want to register for exercise group" + exerciseGroup.name + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                register(exerciseGroup.id);
                                session.setExerciseGroup(exerciseGroup.id);
                                register.setVisibility(View.INVISIBLE);
                                deregister.setVisibility(View.VISIBLE);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });

        deregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ctx)
                        .setTitle("Deregister from a group")
                        .setMessage("Are you sure you want to deregister from the exercise group " + exerciseGroup.name + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deregister(exerciseGroup.id);
                                session.setExerciseGroup(null);
                                register.setVisibility(View.VISIBLE);
                                deregister.setVisibility(View.INVISIBLE);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    private void register(Long groupId) {
        final SessionObject session = (SessionObject) ctx.getApplication();
        if (session.getCredentials() == null || session.getId() == null) {
            Utils.clearBackStackAndLaunchLogin(ctx);
        }

        String registerGroup = GROUP_REGISTER_URL.replace("{student_id}", Long.toString(session.getId()));
        registerGroup = registerGroup.replace("{group_id}", Long.toString(groupId));
//        Log.v(TAG, Long.toString(groupId));
//        Log.v(TAG, GROUP_REGISTER_URL);
        final RequestQueue queue = Volley.newRequestQueue(ctx);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, registerGroup,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("RESPONSE", response);
                        Toast.makeText(ctx, R.string.registered, Toast.LENGTH_LONG).show();
//                                handleResponse(response);
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
                    Toast toast = Toast.makeText(ctx, R.string.unknown_error, Toast.LENGTH_LONG);
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

    private void deregister(Long groupId) {
        final SessionObject session = (SessionObject) ctx.getApplication();
        if(session.getCredentials() == null || session.getId() == null) {
            Utils.clearBackStackAndLaunchLogin(ctx);
        }

        String deregisterUrl = GROUP_DEREGISTER_URL.replace("{student_id}", Long.toString(session.getId()));
        deregisterUrl = deregisterUrl.replace("{group_id}", Long.toString(groupId));
//        Log.v(TAG, Long.toString(groupId));
//        Log.v(TAG, GROUP_DEREGISTER_URL);
        final RequestQueue queue = Volley.newRequestQueue(ctx);
        final StringRequest stringRequestDeregister = new StringRequest(Request.Method.GET, deregisterUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("RESPONSE", response);
                        Toast.makeText(ctx, R.string.deregistered, Toast.LENGTH_LONG).show();
//                                handleResponse(response);
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
                    Toast toast = Toast.makeText(ctx, R.string.unknown_error, Toast.LENGTH_LONG);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextColor(Color.RED);
                    toast.show();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String auth = "Basic " + session.getCredentials();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        queue.add(stringRequestDeregister);
    }

    private void handleError(String error) {
        final Gson gson = new Gson();
        final BadRequestError badReqError = gson.fromJson(error, BadRequestError.class);

        Toast toast = Toast.makeText(ctx, badReqError.localizedMessage, Toast.LENGTH_LONG);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.RED);
        toast.show();
    }
}
//}
