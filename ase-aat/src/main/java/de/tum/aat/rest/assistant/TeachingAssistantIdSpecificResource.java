package de.tum.aat.rest.assistant;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import de.tum.aat.domain.TeachingAssistant;
import de.tum.aat.exceptions.NoIdException;
import de.tum.aat.services.TeachingAssistantService;
import de.tum.aat.services.impl.TeachingAssistantServiceImpl;

public class TeachingAssistantIdSpecificResource extends ServerResource {

	private TeachingAssistantService ss;

	public TeachingAssistantIdSpecificResource() {
		ss = new TeachingAssistantServiceImpl();
	}

	@Get("json")
	public TeachingAssistant getTeachingAssistant() {
		long id = Long.MIN_VALUE;
		try {
			id = Long.parseLong(getAttribute("id"));
		} catch (Exception e) {
			throw new NoIdException(TeachingAssistant.class);
		}
		return ss.getTeachingAssistant(id);
	}

	@Delete
	public void deleteTeachingAssistant() {
		long id = Long.MIN_VALUE;
		try {
			id = Long.parseLong(getAttribute("id"));
		} catch (Exception e) {
			throw new NoIdException(TeachingAssistant.class);
		}

		ss.deleteTeachingAssistant(id);
	}
}
