package de.tum.aat.session;

import android.app.Application;

/**
 * Created by Naum on 1/16/2017.
 */
public class SessionObject extends Application {

    private Long id;
    private String credentials;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }


}
