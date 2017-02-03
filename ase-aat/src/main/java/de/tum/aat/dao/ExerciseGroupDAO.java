package de.tum.aat.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.googlecode.objectify.Key;

import de.tum.aat.domain.ExerciseGroup;
import de.tum.aat.domain.Student;
import de.tum.aat.exceptions.GenericException;
import de.tum.aat.exceptions.NotFoundException;

public class ExerciseGroupDAO {

	public ExerciseGroup getExerciseGroup(Key<ExerciseGroup> key) {
		ExerciseGroup s = ofy().load().key(key).now();

		if (s == null) {
			throw new NotFoundException(ExerciseGroup.class);
		}

		return s;
	}

	public ExerciseGroup getExerciseGroup(long id) {
		Key<ExerciseGroup> key = Key.create(ExerciseGroup.class, id);
		ExerciseGroup s = ofy().load().key(key).now();

		if (s == null) {
			throw new NotFoundException(ExerciseGroup.class);
		}

		return s;
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
		ExerciseGroup s = ofy().load().key(key).now();

		if (s == null) {
			throw new NotFoundException(ExerciseGroup.class);
		}

		if (!s.getStudents().isEmpty()) {
			for (Student student : s.getStudents()) {
				student.setExerciseGroup(null);
			}
			throw new GenericException("");
		}

		ofy().delete().key(key).now();
	}

	public ExerciseGroup updateExerciseGroup(ExerciseGroup s) {
		ExerciseGroup oldExerciseGroup = getExerciseGroup(s.getId());

		if (oldExerciseGroup == null) {
			throw new NotFoundException(ExerciseGroup.class);
		}

		oldExerciseGroup = s;
		return saveExerciseGroup(oldExerciseGroup);
	}

}
