package de.tum.aat.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.googlecode.objectify.Key;

import de.tum.aat.domain.Student;
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

}
