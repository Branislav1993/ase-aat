package de.tum.aat.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import de.tum.aat.R;
import de.tum.aat.activity.ChoiceActivity;
import de.tum.aat.activity.LoginActivity;
import de.tum.aat.session.SessionObject;

/**
 * Created by Naum on 1/3/2017.
 */
public class Utils {

    private static final String LOGOUT_URL = "http://ase-aat10.appspot.com/rest/groups/";

    public static void signOut(final Activity activity) {
        final RequestQueue queue = Volley.newRequestQueue(activity);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, LOGOUT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast toast = Toast.makeText(activity, R.string.cant_sign_out, Toast.LENGTH_LONG);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(Color.RED);
                        toast.show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (401 == error.networkResponse.statusCode) {
                    Toast.makeText(activity, "You are signed out", Toast.LENGTH_LONG).show();
                    clearBackStackAndLaunchLogin(activity);
                } else {
                    Log.w(activity.getClass().getCanonicalName(), "Exception: " + error.getLocalizedMessage());
                    Toast toast = Toast.makeText(activity, R.string.unknown_error, Toast.LENGTH_LONG);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextColor(Color.RED);
                    toast.show();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = ":";
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };;

        queue.add(stringRequest);
    }

    public static void clearBackStackAndLaunchLogin(Activity activity) {
        final SessionObject session = (SessionObject) activity.getApplication();
        session.setCredentials(null);
        session.setCurStudent(null);
//        Log.v("UTILS", session.getCredentials() == null ? "prazno" : session.getCredentials());
//        Log.v("UTILS", Long.toString(session.getId() == null ? 0 : session.getId()));
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

}
