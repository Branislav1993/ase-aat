package de.tum.aat.model;

import java.io.Serializable;

/**
 * Created by Naum on 1/16/2017.
 */
public class Student implements Serializable {

    public Long id;
    public String name;
    public String lastName;
    public String email;
    public String password;
    public GroupTimeslot[] timeslotsAttended;
    public Integer numberOfPresentations;
    public Boolean hasBonus;
    public Long exerciseGroup;

}
