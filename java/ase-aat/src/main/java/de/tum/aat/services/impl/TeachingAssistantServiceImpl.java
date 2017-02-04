package de.tum.aat.services.impl;

import java.util.List;

import de.tum.aat.dao.TeachingAssistantDAO;
import de.tum.aat.domain.TeachingAssistant;
import de.tum.aat.services.TeachingAssistantService;

public class TeachingAssistantServiceImpl implements TeachingAssistantService {

	private TeachingAssistantDAO sd;

	public TeachingAssistantServiceImpl() {
		sd = new TeachingAssistantDAO();
	}

	@Override
	public TeachingAssistant saveTeachingAssistant(TeachingAssistant s) {
		return sd.saveTeachingAssistant(s);
	}

	@Override
	public TeachingAssistant updateTeachingAssistant(TeachingAssistant s) {
		return sd.updateTeachingAssistant(s);
	}

	@Override
	public TeachingAssistant getTeachingAssistant(long id) {
		return sd.getTeachingAssistant(id);
	}

	@Override
	public List<TeachingAssistant> getTeachingAssistants() {
		return sd.getTeachingAssistants();
	}

	@Override
	public void deleteTeachingAssistant(long id) {
		sd.deleteTeachingAssistant(id);
	}

}
