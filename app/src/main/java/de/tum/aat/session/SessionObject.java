package de.tum.aat.session;

import android.app.Application;

/**
 * Created by Naum on 1/16/2017.
 */
public class SessionObject extends Application {

    private String credentials;
    private Long id;
    private Long exerciseGroup;

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getExerciseGroup() {
        return exerciseGroup;
    }

    public void setExerciseGroup(Long exerciseGroup) {
        this.exerciseGroup = exerciseGroup;
    }





}
