package de.tum.aat.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import de.tum.aat.R;
import de.tum.aat.model.ExerciseGroup;

/**
 * Created by Naum on 1/16/2017.
 */
public class ExerciseGroupsAdapter extends ArrayAdapter<ExerciseGroup> {

    private static final String TAG = ExerciseGroupsAdapter.class.getCanonicalName();
    private Context ctx;

    public ExerciseGroupsAdapter(Context context, ExerciseGroup[] exerciseGroups) {
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
        TextView teachingAssistant = (TextView) convertView.findViewById(R.id.group_ta);
        // Populate the data into the template view using the data object
        groupName.setText("Exercise group " + exerciseGroup.name);
        if(exerciseGroup.teachingAssistant == null) {
            teachingAssistant.setText("Instructor: unknown");
        } else {
            teachingAssistant.setText("Instructor: " + exerciseGroup.teachingAssistant.name);
        }


//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final RequestQueue queue = Volley.newRequestQueue(ctx);
//                final StringRequest stringRequest = new StringRequest(Request.Method.POST, String.format(ADD_IN_CART, product.id),
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Toast.makeText(ctx, "Izdelek " + product.naziv + " je bil dodan v košarici", Toast.LENGTH_LONG).show();
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(ctx, "An error occurred.", Toast.LENGTH_LONG).show();
//                        Log.w(this.getClass().getCanonicalName(), "Exception: " + error.getLocalizedMessage());
//                    }
//                });
//
//                queue.add(stringRequest);
//            }
//        });
        // Return the completed view to render on screen
        return convertView;
    }


}
//}
