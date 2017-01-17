package de.tum.aat.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.tum.aat.R;
import de.tum.aat.model.BadRequestError;
import de.tum.aat.model.QRCode;
import de.tum.aat.session.SessionObject;
import de.tum.aat.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QRFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QRFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class QRFragment extends DialogFragment {

    private static final String TAG = QRFragment.class.getCanonicalName();
    private static String QR_URL = "http://ase-aat10.appspot.com/rest/qrattendance/{student_id}";
//    {student_id}

    private OnFragmentInteractionListener mListener;
    private final static int WIDTH = 500;
    private final static int WHITE = 0xFFFFFFFF;
    private final static int BLACK = 0xFF000000;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QRFragment.
     */
    public static QRFragment newInstance(boolean isAttendance) {
        if (!isAttendance) {
            QR_URL = QR_URL.replace("qrattendance", "qrpresentation");
        } else {
            QR_URL = QR_URL.replace("qrpresentation", "qrattendance");
        }
        QRFragment fragment = new QRFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public QRFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SessionObject session = (SessionObject) getActivity().getApplication();
        if(session.getCredentials() == null) {
            Utils.clearBackStackAndLaunchLogin(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qr, container, false);

        Button closeBtn = (Button) view.findViewById(R.id.close_button);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ImageView qrPlaceholder = (ImageView) view.findViewById(R.id.qr_code);
        TextView errorPlaceholder = (TextView) view.findViewById(R.id.error_text);

        getAndRenderBarcode(qrPlaceholder, errorPlaceholder);

        return view;
    }

    private void getAndRenderBarcode(final ImageView qrPlaceholder, final TextView errorPlaceholder) {
        final SessionObject session = (SessionObject) getActivity().getApplication();
        if(session.getCredentials() == null || session.getId() == null) {
            Utils.clearBackStackAndLaunchLogin(getActivity());
        }

        String attendanceUrl = QR_URL.replace("{student_id}", Long.toString(session.getId()));
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, attendanceUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.v("RESPONSE", response);
                        String barcode = getUrl(response);
                        buildImage(barcode, qrPlaceholder);
                        errorPlaceholder.setVisibility(View.INVISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(400 == error.networkResponse.statusCode) {
                    try {
                        handleError(new String(error.networkResponse.data, "UTF-8"), errorPlaceholder);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.w(TAG, "Exception: " + error.getLocalizedMessage());
                    Toast toast = Toast.makeText(getActivity(), R.string.unknown_error, Toast.LENGTH_LONG);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextColor(Color.RED);
                    toast.show();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                final SessionObject session = (SessionObject) getActivity().getApplication();
//                Log.v(TAG, session.getCredentials());
                String auth = "Basic " + session.getCredentials();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        queue.add(stringRequest);
    }

    private void handleError(String error, TextView errorPlaceholder) {
        final Gson gson = new Gson();
        final BadRequestError badReqError = gson.fromJson(error, BadRequestError.class);

        errorPlaceholder.setText(badReqError.localizedMessage);
        errorPlaceholder.setVisibility(View.VISIBLE);

//        Toast toast = Toast.makeText(getActivity(), badReqError.localizedMessage, Toast.LENGTH_LONG);
//        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
//        v.setTextColor(Color.RED);
//        toast.show();
    }

    private void buildImage(String barcode_content, ImageView qrPlaceholder) {
        Bitmap bm = null;
        try {
            bm = encodeAsBitmap(barcode_content);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        if(bm != null) {
            qrPlaceholder.setImageBitmap(bm);
        }
    }

    private String getUrl(String response) {
        final Gson gson = new Gson();
        final QRCode qrCode = gson.fromJson(response, QRCode.class);

        return qrCode.url;
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
