package de.tum.aat.model;

import java.io.Serializable;

/**
 * Created by Naum on 1/16/2017.
 */
public class Student implements Serializable {

    public long id;
    public String name;
    public String lastName;
    public String email;
    public String password;
    public GroupTimeslot[] timeslotsAttended;
    public int numberOfPresentations;
    public boolean hasBonus;
    public int exerciseGroup;

}
