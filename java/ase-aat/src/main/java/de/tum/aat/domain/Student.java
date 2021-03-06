package de.tum.aat.domain;

import java.io.Serializable;
import java.util.List;

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
	private String password;
	private Long exerciseGroupId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getExerciseGroup() {
		return exerciseGroupId;
	}

	public void setExerciseGroup(Long exerciseGroupId) {
		this.exerciseGroupId = exerciseGroupId;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", lastName=" + lastName + ", exerciseGroup=" + exerciseGroupId
				+ ", timeslotsAttended=" + timeslotsAttended + ", numberOfPresentations=" + numberOfPresentations
				+ ", hasBonus=" + hasBonus + "]";
	}

}
