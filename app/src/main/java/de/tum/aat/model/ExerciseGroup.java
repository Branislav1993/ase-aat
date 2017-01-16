package de.tum.aat.model;

import java.io.Serializable;

/**
 * Created by Naum on 1/16/2017.
 */
public class ExerciseGroup implements Serializable {

    public long id;
    public String name;
    public GroupTimeslot[] timeslots;
    public Student[] students;
    public TeachingAssistant teachingAssistant;

}
