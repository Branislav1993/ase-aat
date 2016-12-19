package de.tum.aat.rest.student.exceptions;

import org.restlet.resource.Status;

@Status(value = 403, serialize = true)
public class StudentNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7712798963318904220L;

	public StudentNotFoundException(String message) {
		super(message);
	}

}
