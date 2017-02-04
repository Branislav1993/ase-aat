package de.tum.aat.exceptions;

import org.restlet.resource.Status;

@Status(value = 401, serialize = false)
public class NotAuthorizedException extends RuntimeException {

	private static final long serialVersionUID = -2929379939016343375L;

	public NotAuthorizedException() {
		super();
	}
}
