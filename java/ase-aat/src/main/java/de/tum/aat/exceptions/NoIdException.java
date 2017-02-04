package de.tum.aat.exceptions;

import org.restlet.resource.Status;

@Status(value = 400, serialize = true)
public class NoIdException extends RuntimeException {

	private static final long serialVersionUID = -2744074572726238286L;

	public NoIdException(Class<?> c) {
		super(generateMessage(c));
	}

	private static String generateMessage(Class<?> c) {
		String message;

		String[] fullName = c.getName().split(".");

		String className = fullName[fullName.length - 1];

		switch (className) {
		case "Student":
			message = "Student not found, provide proper ID!";
			break;
		case "ExerciseGroup":
			message = "Exercise Group not found, provide proper ID!";
			break;
		case "TeachingAssistant":
			message = "Teaching Assistant not found, provide proper ID!";
			break;
		default:
			message = "Not found, provide proper ID!";
		}

		return message;
	}

}
