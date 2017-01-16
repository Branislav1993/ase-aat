package de.tum.aat.session;

import android.app.Application;

/**
 * Created by Naum on 1/16/2017.
 */
public class SessionObject extends Application {

    private String credentials;

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }


}
