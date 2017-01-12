package de.tum.aat.rest.student;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import de.tum.aat.domain.Student;
import de.tum.aat.exceptions.NoIdException;
import de.tum.aat.exceptions.NotAuthorizedException;
import de.tum.aat.rest.RestApp;
import de.tum.aat.services.StudentService;
import de.tum.aat.services.impl.StudentServiceImpl;

public class StudentIdSpecificResource extends ServerResource {

	private StudentService ss;

	public StudentIdSpecificResource() {
		ss = new StudentServiceImpl();
	}

	@Get("json")
	public Student getStudent() {
		
		if (!RestApp.getInstance().authenticate(getRequest(), getResponse())) {
			throw new NotAuthorizedException();
		}
		
		long id = Long.MIN_VALUE;
		try {
			id = Long.parseLong(getAttribute("id"));
		} catch (Exception e) {
			throw new NoIdException(Student.class);
		}
		return ss.getStudent(id);
	}

	@Delete
	public void deleteStudent() {
		long id = Long.MIN_VALUE;
		try {
			id = Long.parseLong(getAttribute("id"));
		} catch (Exception e) {
			throw new NoIdException(Student.class);
		}

		ss.deleteStudent(id);
	}
}
