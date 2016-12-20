package de.tum.aat.services.impl;

import java.util.List;

import com.googlecode.objectify.Key;

import de.tum.aat.dao.ExerciseGroupDAO;
import de.tum.aat.domain.ExerciseGroup;
import de.tum.aat.services.ExerciseGroupService;

public class ExerciseGroupServiceImpl implements ExerciseGroupService {

	private ExerciseGroupDAO egd;

	public ExerciseGroupServiceImpl() {
		egd = new ExerciseGroupDAO();
	}

	@Override
	public ExerciseGroup saveExerciseGroup(ExerciseGroup s) {
		return egd.saveExerciseGroup(s);
	}

	@Override
	public ExerciseGroup updateExerciseGroup(ExerciseGroup s) {
		return egd.updateExerciseGroup(s);
	}

	@Override
	public ExerciseGroup getExerciseGroup(long id) {
		return egd.getExerciseGroup(id);
	}

	@Override
	public ExerciseGroup getExerciseGroup(Key<ExerciseGroup> key) {
		return egd.getExerciseGroup(key);
	}

	@Override
	public List<ExerciseGroup> getExerciseGroups() {
		return egd.getExerciseGroups();
	}

	@Override
	public void deleteExerciseGroup(long id) {
		egd.deleteExerciseGroup(id);
	}

}
