package de.tum.aat.rest.student;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import de.tum.aat.domain.Student;
import de.tum.aat.rest.student.exceptions.StudentNotFoundException;
import de.tum.aat.services.StudentService;
import de.tum.aat.services.impl.StudentServiceImpl;

public class GetStudentResource extends ServerResource {

	private StudentService ss;

	public GetStudentResource() {
		ss = new StudentServiceImpl();
	}

	@Get("json")
	public Student getStudent() {
		long id = Long.parseLong(getAttribute("id"));
		return ss.getStudent(id);
	}

	@Delete
	public void deleteStudent() throws StudentNotFoundException {
		long id = Long.MIN_VALUE;
		try {
			if (getAttribute("id") != null) {
				id = Long.parseLong(getAttribute("id"));
				ss.deleteStudent(id);
			} else {
				throw new StudentNotFoundException("Student can not be found!");
			}
		} catch (Exception e) {
			throw new StudentNotFoundException("Student can not be found!");
		}
	}
}
