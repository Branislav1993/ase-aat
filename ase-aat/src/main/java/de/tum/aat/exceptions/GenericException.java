package de.tum.aat.exceptions;

import org.restlet.resource.Status;

@Status(value = 400, serialize = true)
public class GenericException extends RuntimeException {

	private static final long serialVersionUID = -2929379939016343375L;

	public GenericException(String message) {
		super(message);
	}

}
