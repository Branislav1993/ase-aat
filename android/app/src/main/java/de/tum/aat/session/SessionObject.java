package de.tum.aat.session;

import android.app.Application;

import de.tum.aat.model.Student;

/**
 * Created by Naum on 1/16/2017.
 */
public class SessionObject extends Application {

    private String credentials;
    private Student curStudent;

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public Student getCurStudent() {
        return curStudent;
    }

    public void setCurStudent(Student curStudent) {
        this.curStudent = curStudent;
    }
}
