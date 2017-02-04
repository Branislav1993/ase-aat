package de.tum.aat.rest.assistant;

import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import de.tum.aat.domain.TeachingAssistant;
import de.tum.aat.services.TeachingAssistantService;
import de.tum.aat.services.impl.TeachingAssistantServiceImpl;

public class TeachingAssistantResource extends ServerResource {

	private TeachingAssistantService ss;

	public TeachingAssistantResource() {
		ss = new TeachingAssistantServiceImpl();
	}

	@Get("json")
	public List<TeachingAssistant> getTeachingAssistants() {
		return ss.getTeachingAssistants();
	}

	@Post
	public TeachingAssistant saveTeachingAssistant(TeachingAssistant s) {
		return ss.saveTeachingAssistant(s);
	}

	@Put
	public TeachingAssistant updateTeachingAssistant(TeachingAssistant s) {
		return ss.updateTeachingAssistant(s);
	}
}
