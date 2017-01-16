package de.tum.aat.rest.student;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import de.tum.aat.domain.Student;
import de.tum.aat.exceptions.GenericException;
import de.tum.aat.exceptions.NotAuthorizedException;
import de.tum.aat.rest.RestApp;
import de.tum.aat.services.StudentService;
import de.tum.aat.services.impl.StudentServiceImpl;

public class LoginResource extends ServerResource {

	private StudentService ss;

	public LoginResource() {
		ss = new StudentServiceImpl();
	}

	@Get("json")
	public Student loginStudent() {

		if (!RestApp.getInstance().authenticate(getRequest(), getResponse())) {
			throw new NotAuthorizedException();
		}

		String email = "";
		try {
			email = getAttribute("email");
		} catch (Exception e) {
			throw new GenericException("Please provide an email!");
		}
		return ss.getStudent(email);
	}

}
