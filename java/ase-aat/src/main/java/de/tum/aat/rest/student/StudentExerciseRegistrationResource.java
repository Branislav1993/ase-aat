package de.tum.aat.rest.student;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import de.tum.aat.domain.Student;
import de.tum.aat.exceptions.GenericException;
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
			groupId = Long.parseLong(getQueryValue("groupId"));
			studentId = Long.parseLong(getQueryValue("studentId"));
		} catch (Exception e) {
			throw new GenericException("Provide both student and exercise group ID in the right format!");
		}
		return ss.registerExerciseGroupForStudent(groupId, studentId);
	}

}
