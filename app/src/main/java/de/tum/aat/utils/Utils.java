package de.tum.aat.utils;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import de.tum.aat.activity.LoginActivity;

/**
 * Created by Naum on 1/3/2017.
 */
public class Utils {

    public static boolean signOut(Activity activity) {
        clearBackStackAndLaunchLogin(activity);
        Toast.makeText(activity, "You are virtually signed out!", Toast.LENGTH_LONG).show();
        return true;
    }

    public static void clearBackStackAndLaunchLogin(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

}
