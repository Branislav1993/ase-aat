package de.tum.aat.rest.student;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import de.tum.aat.domain.Student;
import de.tum.aat.exceptions.NoIdException;
import de.tum.aat.services.StudentService;
import de.tum.aat.services.impl.StudentServiceImpl;

public class StudentExerciseRegistrationResource extends ServerResource {

	private StudentService ss;

	public StudentExerciseRegistrationResource() {
		ss = new StudentServiceImpl();
	}
	

	@Get("json")
	public Student registerExerciseGroupForStudent() {
		long groupId = Long.MIN_VALUE;
		long studentId = Long.MIN_VALUE;
		try {
			groupId = Long.parseLong(getAttribute("groupId"));
			studentId = Long.parseLong(getAttribute("studentId"));
		} catch (Exception e) {
			throw new NoIdException(Student.class);
		}
		return ss.registerExerciseGroupForStudent(groupId, studentId);
	}
	
	
}
