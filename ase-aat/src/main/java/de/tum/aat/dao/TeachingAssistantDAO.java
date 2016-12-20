package de.tum.aat.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.googlecode.objectify.Key;

import de.tum.aat.domain.TeachingAssistant;
import de.tum.aat.exceptions.NotFoundException;

public class TeachingAssistantDAO {

	public TeachingAssistant getTeachingAssistant(long id) {
		Key<TeachingAssistant> key = Key.create(TeachingAssistant.class, id);
		TeachingAssistant s = ofy().load().key(key).now();

		if (s == null) {
			throw new NotFoundException(TeachingAssistant.class);
		}

		return s;
	}

	public List<TeachingAssistant> getTeachingAssistants() {
		return ofy().load().type(TeachingAssistant.class).list();
	}

	public TeachingAssistant saveTeachingAssistant(TeachingAssistant s) {
		Key<TeachingAssistant> key = ofy().save().entity(s).now();
		return ofy().load().key(key).now();
	}

	public void deleteTeachingAssistant(long id) {
		Key<TeachingAssistant> key = Key.create(TeachingAssistant.class, id);
		TeachingAssistant s = ofy().load().key(key).now();

		if (s == null) {
			throw new NotFoundException(TeachingAssistant.class);
		}

		ofy().delete().key(key).now();
	}

	public TeachingAssistant updateTeachingAssistant(TeachingAssistant s) {
		TeachingAssistant oldTeachingAssistant = getTeachingAssistant(s.getId());

		if (oldTeachingAssistant == null) {
			throw new NotFoundException(TeachingAssistant.class);
		}

		oldTeachingAssistant = s;
		return saveTeachingAssistant(oldTeachingAssistant);
	}

}
