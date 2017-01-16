package de.tum.aat.model;

import java.io.Serializable;

/**
 * Created by Naum on 1/16/2017.
 */
public class TeachingAssistant implements Serializable {

    public long id;
    public String name;
    public String lastName;
    public String email;
    ExerciseGroup[] groups;

}
