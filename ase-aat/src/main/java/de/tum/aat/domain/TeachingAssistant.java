package de.tum.aat.domain;

import java.io.Serializable;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Serialize;

@Entity
public class TeachingAssistant implements Serializable {

	private static final long serialVersionUID = 1571892595861671294L;

	@Id
	private long id;
	private String name;
	private String lastName;
	private String email;
	@Serialize
	private List<ExerciseGroup> groups;

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

	public List<ExerciseGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<ExerciseGroup> groups) {
		this.groups = groups;
	}

	@Override
	public String toString() {
		return "TeachingAssistant [id=" + id + ", name=" + name + ", lastName=" + lastName + ", email=" + email
				+ ", groups=" + groups + "]";
	}

}
