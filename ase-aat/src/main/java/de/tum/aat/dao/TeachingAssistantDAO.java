package de.tum.aat.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.Key;

import de.tum.aat.domain.TeachingAssistant;

public class TeachingAssistantDAO {

	public Key<TeachingAssistant> saveTeachingAssistant(TeachingAssistant ta) {
		return ofy().save().entity(ta).now();
	}

}
