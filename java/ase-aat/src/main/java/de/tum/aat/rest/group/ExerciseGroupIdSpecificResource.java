package de.tum.aat.rest.group;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import de.tum.aat.domain.ExerciseGroup;
import de.tum.aat.exceptions.NoIdException;
import de.tum.aat.services.ExerciseGroupService;
import de.tum.aat.services.impl.ExerciseGroupServiceImpl;

public class ExerciseGroupIdSpecificResource extends ServerResource {

	private ExerciseGroupService ss;

	public ExerciseGroupIdSpecificResource() {
		ss = new ExerciseGroupServiceImpl();
	}

	@Get("json")
	public ExerciseGroup getExerciseGroup() {
		long id = Long.MIN_VALUE;
		try {
			id = Long.parseLong(getAttribute("id"));
		} catch (Exception e) {
			throw new NoIdException(ExerciseGroup.class);
		}
		return ss.getExerciseGroup(id);
	}

	@Delete
	public void deleteExerciseGroup() {
		long id = Long.MIN_VALUE;
		try {
			id = Long.parseLong(getAttribute("id"));
		} catch (Exception e) {
			throw new NoIdException(ExerciseGroup.class);
		}

		ss.deleteExerciseGroup(id);
	}
}
