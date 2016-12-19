package de.tum.aat.rest.student;

import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import de.tum.aat.domain.Student;
import de.tum.aat.services.StudentService;
import de.tum.aat.services.impl.StudentServiceImpl;

public class StudentResource extends ServerResource {

	private StudentService ss;

	public StudentResource() {
		ss = new StudentServiceImpl();
	}

	@Get("json")
	public List<Student> getStudents() {
		return ss.getStudents();
	}

	@Post
	public Student saveStudent(Student s) {
		return ss.saveStudent(s);
	}

	@Put
	public Student updateStudent(Student s) {
		return ss.updateStudent(s);
	}

}
