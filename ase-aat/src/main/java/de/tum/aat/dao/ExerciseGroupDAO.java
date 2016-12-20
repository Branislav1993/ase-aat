package de.tum.aat.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.googlecode.objectify.Key;

import de.tum.aat.domain.ExerciseGroup;

public class ExerciseGroupDAO {

	public ExerciseGroup getExerciseGroup(long id) {
		Key<ExerciseGroup> key = Key.create(ExerciseGroup.class, id);
		return ofy().load().key(key).now();
	}

	public List<ExerciseGroup> getExerciseGroups() {
		return ofy().load().type(ExerciseGroup.class).list();
	}

	public ExerciseGroup saveExerciseGroup(ExerciseGroup s) {
		Key<ExerciseGroup> key = ofy().save().entity(s).now();
		return ofy().load().key(key).now();
	}

	public void deleteExerciseGroup(long id) {
		Key<ExerciseGroup> key = Key.create(ExerciseGroup.class, id);
		ofy().delete().key(key).now();
	}

	public ExerciseGroup updateExerciseGroup(ExerciseGroup s) {
		ExerciseGroup oldExerciseGroup = getExerciseGroup(s.getId());
		oldExerciseGroup = s;
		return saveExerciseGroup(oldExerciseGroup);
	}

	public ExerciseGroup getExerciseGroup(Key<ExerciseGroup> key) {
		return ofy().load().key(key).now();
	}

}
