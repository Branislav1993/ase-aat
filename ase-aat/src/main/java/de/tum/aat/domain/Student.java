package de.tum.aat.domain;

import java.io.Serializable;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Serialize;

@Entity
public class Student implements Serializable {

	private static final long serialVersionUID = 832302302449469371L;

	@Id
	private long id;
	private String name;
	private String lastName;
	private String email;
	private Key<ExerciseGroup> exerciseGroup;
	@Serialize
	private List<ExerciseTimeslot> timeslotsAttended;
	private int numberOfPresentations;
	private boolean hasBonus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Key<ExerciseGroup> getExerciseGroup() {
		return exerciseGroup;
	}

	public void setExerciseGroup(Key<ExerciseGroup> exerciseGroup) {
		this.exerciseGroup = exerciseGroup;
	}

	public List<ExerciseTimeslot> getTimeslotsAttended() {
		return timeslotsAttended;
	}

	public void setTimeslotsAttended(List<ExerciseTimeslot> timeslotsAttended) {
		this.timeslotsAttended = timeslotsAttended;
	}

	public int getNumberOfPresentations() {
		return numberOfPresentations;
	}

	public void setNumberOfPresentations(int numberOfPresentations) {
		this.numberOfPresentations = numberOfPresentations;
	}

	public boolean isHasBonus() {
		return hasBonus;
	}

	public void setHasBonus(boolean hasBonus) {
		this.hasBonus = hasBonus;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", lastName=" + lastName + ", exerciseGroup=" + exerciseGroup
				+ ", timeslotsAttended=" + timeslotsAttended + ", numberOfPresentations=" + numberOfPresentations
				+ ", hasBonus=" + hasBonus + "]";
	}

}
