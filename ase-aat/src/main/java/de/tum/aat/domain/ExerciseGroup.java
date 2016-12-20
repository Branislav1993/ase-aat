package de.tum.aat.domain;

import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Serialize;

import de.tum.aat.constants.Constants;

@Entity
public class ExerciseGroup {

	@Id
	private long id;
	private String name;
	@Serialize
	private List<ExerciseTimeslot> timeslots;
	@Serialize
	private List<Student> students;
	@Serialize
	private List<TeachingAssistant> teachingAssistant;

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

	public List<ExerciseTimeslot> getTimeslots() {
		return timeslots;
	}

	public void setTimeslots(List<ExerciseTimeslot> timeslots) {
		this.timeslots = timeslots;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public List<TeachingAssistant> getTeachingAssistant() {
		return teachingAssistant;
	}

	public void setTeachingAssistant(List<TeachingAssistant> teachingAssistant) {
		this.teachingAssistant = teachingAssistant;
	}

	@Override
	public String toString() {
		return "ExerciseGroup [id=" + id + ", name=" + name + ", timeslots=" + timeslots + ", students="
				+ students.size() + ", teachingAssistant=" + teachingAssistant + "]";
	}

	public ExerciseTimeslot getCurrentTimeslot() {
		long currentTime = System.currentTimeMillis();
		for (ExerciseTimeslot ts : timeslots) {
			if (currentTime >= (ts.getStart().getTime() - Constants.TIMESLOT_OFFSET)
					&& currentTime <= ts.getEnd().getTime()) {
				return ts;
			}
		}
		return null;
	}

}
