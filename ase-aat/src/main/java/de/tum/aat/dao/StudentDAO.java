package de.tum.aat.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;

import de.tum.aat.domain.ExerciseGroup;
import de.tum.aat.domain.Student;
import de.tum.aat.exceptions.GenericException;
import de.tum.aat.exceptions.NotFoundException;

public class StudentDAO {

	public Student getStudent(long id) {
		Key<Student> key = Key.create(Student.class, id);
		Student s = ofy().load().key(key).now();

		if (s == null) {
			throw new NotFoundException(Student.class);
		}

		return s;
	}

	public List<Student> getStudents() {
		return ofy().load().type(Student.class).list();
	}

	public Student saveStudent(Student s) {
		Key<Student> key = ofy().save().entity(s).now();
		return ofy().load().key(key).now();
	}

	public void deleteStudent(long id) {
		Key<Student> key = Key.create(Student.class, id);
		Student s = ofy().load().key(key).now();

		if (s == null) {
			throw new NotFoundException(Student.class);
		}

		if (s.getExerciseGroup() != null) {
			throw new GenericException(
					"Student is enrolled in the " + s.getExerciseGroup() + ". You can't delete registered student.");
		}

		ofy().delete().key(key).now();
	}

	public Student updateStudent(Student s) {
		return saveStudent(s);
	}

	public Student registerExerciseGroupForStudent(long groupId, long studentId) {
		Key<ExerciseGroup> groupKey = Key.create(ExerciseGroup.class, groupId);
		Key<Student> studentKey = Key.create(Student.class, studentId);

		ExerciseGroup g = ofy().load().key(groupKey).now();
		Student s = ofy().load().key(studentKey).now();

		if (s == null) {
			throw new NotFoundException(Student.class);
		}

		if (g == null) {
			throw new NotFoundException(ExerciseGroup.class);
		}

		s.setExerciseGroup(g.getId());

		if (g.getStudents() == null || g.getStudents().isEmpty()) {
			ArrayList<Student> students = new ArrayList<>();
			students.add(s);
			g.setStudents(students);
		} else {
			if (!g.getStudents().contains(s)) {
				g.getStudents().add(s);
			} else {
				throw new GenericException("Student already registered for the selected exercise session!");
			}
		}

		ofy().save().entity(g).now();

		return saveStudent(s);
	}

	public Student deregisterExerciseGroupForStudent(long groupId, long studentId) {
		Key<ExerciseGroup> groupKey = Key.create(ExerciseGroup.class, groupId);
		Key<Student> studentKey = Key.create(Student.class, studentId);

		ExerciseGroup g = ofy().load().key(groupKey).now();
		Student s = ofy().load().key(studentKey).now();

		if (s == null) {
			throw new NotFoundException(Student.class);
		}

		if (g == null) {
			throw new NotFoundException(ExerciseGroup.class);
		}

		s.setExerciseGroup(null);

		ArrayList<Student> students = (ArrayList<Student>) g.getStudents();

		if (students != null && students.contains(s)) {
			students.remove(s);
			g.setStudents(students);
		} else {
			throw new GenericException("Student is not in the selected exercise group!");
		}

		ofy().save().entity(g).now();

		return saveStudent(s);
	}

}
