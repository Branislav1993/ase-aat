package de.tum.aat.exceptions;

import org.restlet.resource.Status;

@Status(value = 404, serialize = true)
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7712798963318904220L;

	public NotFoundException(Class<?> c) {
		super(generateMessage(c));
	}

	private static String generateMessage(Class<?> c) {
		String message;

		String className = c.getName();

		switch (className) {
		case "Student":
			message = "Student not found by the given ID!";
			break;
		case "ExerciseGroup":
			message = "Exercise Group not found by the given ID!";
			break;
		case "TeachingAssistant":
			message = "Teaching Assistant not found by the given ID!";
			break;
		default:
			message = "Not found by the given ID!";
		}

		return message;
	}

}
