package de.tum.aat.services;

import java.util.List;

import de.tum.aat.domain.TeachingAssistant;

public interface TeachingAssistantService {

	public TeachingAssistant saveTeachingAssistant(TeachingAssistant s);

	public TeachingAssistant updateTeachingAssistant(TeachingAssistant s);

	public TeachingAssistant getTeachingAssistant(long id);

	public List<TeachingAssistant> getTeachingAssistants();

	public void deleteTeachingAssistant(long id);

}
