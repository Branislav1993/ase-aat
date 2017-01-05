package de.tum.aat.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

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
			throw new GenericException("Student is enrolled in the " + s.getExerciseGroup().getName()
					+ ". You can't delete registered student.");
		}

		ofy().delete().key(key).now();
	}

	public Student updateStudent(Student s) {
		Student oldStudent = getStudent(s.getId());

		if (oldStudent == null) {
			throw new NotFoundException(Student.class);
		}

		oldStudent = s;
		return saveStudent(oldStudent);
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

		s.setExerciseGroup(groupKey);

		g.getStudents().add(s);

		ofy().save().entity(g).now();

		return saveStudent(s);
	}

}
