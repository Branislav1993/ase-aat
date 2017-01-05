package de.tum.aat.rest.student;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import de.tum.aat.domain.Student;
import de.tum.aat.exceptions.NoIdException;
import de.tum.aat.services.StudentService;
import de.tum.aat.services.impl.StudentServiceImpl;

public class RegisterPresentationResource extends ServerResource {

	private StudentService ss;

	public RegisterPresentationResource() {
		ss = new StudentServiceImpl();
	}

	@Get("json")
	public Student registerExerciseGroupForStudent() {
		long studentId = Long.MIN_VALUE;
		long start = Long.MIN_VALUE;
		long end = Long.MAX_VALUE;
		try {
			studentId = Long.parseLong(getAttribute("id"));
			start = Long.parseLong(getAttribute("start"));
			end = Long.parseLong(getAttribute("end"));
		} catch (Exception e) {
			throw new NoIdException(Student.class);
		}
		return ss.registerPresentation(studentId, start, end);
	}

}
