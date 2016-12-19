package de.tum.aat.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.googlecode.objectify.Key;

import de.tum.aat.domain.Student;

public class StudentDAO {

	public Student getStudent(long id) {
		Key<Student> key = Key.create(Student.class, id);
		return ofy().load().key(key).now();
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
		ofy().delete().key(key).now();
	}

	public Student updateStudent(Student s) {
		Student oldStudent = getStudent(s.getId());
		oldStudent = s;
		return saveStudent(oldStudent);
	}

}
