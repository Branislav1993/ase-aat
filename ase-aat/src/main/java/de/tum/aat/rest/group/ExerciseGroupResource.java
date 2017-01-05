package de.tum.aat.rest.group;

import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import de.tum.aat.domain.ExerciseGroup;
import de.tum.aat.services.ExerciseGroupService;
import de.tum.aat.services.impl.ExerciseGroupServiceImpl;

public class ExerciseGroupResource extends ServerResource {

	private ExerciseGroupService egs;

	public ExerciseGroupResource() {
		egs = new ExerciseGroupServiceImpl();
	}

	@Get("json")
	public List<ExerciseGroup> getExerciseGroups() {
		return egs.getExerciseGroups();
	}

	@Post("json")
	public ExerciseGroup saveExerciseGroup(ExerciseGroup s) {
		return egs.saveExerciseGroup(s);
	}

	@Put
	public ExerciseGroup updateExerciseGroup(ExerciseGroup s) {
		return egs.updateExerciseGroup(s);
	}
}
