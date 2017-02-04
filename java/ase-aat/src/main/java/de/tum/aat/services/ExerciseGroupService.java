package de.tum.aat.services;

import java.util.List;

import com.googlecode.objectify.Key;

import de.tum.aat.domain.ExerciseGroup;

public interface ExerciseGroupService {

	public ExerciseGroup saveExerciseGroup(ExerciseGroup eg);

	public ExerciseGroup updateExerciseGroup(ExerciseGroup eg);

	public ExerciseGroup getExerciseGroup(long id);
	
	public ExerciseGroup getExerciseGroup(Key<ExerciseGroup> key);

	public List<ExerciseGroup> getExerciseGroups();

	public void deleteExerciseGroup(long id);

}
