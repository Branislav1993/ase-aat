package de.tum.aat.rest;

import java.text.SimpleDateFormat;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.ObjectifyService;

import de.tum.aat.Greeting;
import de.tum.aat.Guestbook;

/**
 * Resource which has only one representation.
 *
 */
public class GreetingResource extends ServerResource {

	@Get
	public String represent() throws Exception {
		String greetingIdStr = getAttribute("greeting");
		String guestbookName = "default";

		Greeting greeting;
		try {
			// Key<Greeting> key = Key.create(Greeting.class, greetingIdStr);
			// obj= ObjectifyService.ofy().load().value(greetingIdStr);
			Key<Guestbook> parentKey = Key.create(Guestbook.class, guestbookName);
			Long id = Long.parseLong(greetingIdStr);
			String websafeKey = Key.create(parentKey, Greeting.class, id).getString();
			Key<Greeting> key = Key.create(websafeKey);

			greeting = ObjectifyService.ofy().load().key(key).safe();
		} catch (NotFoundException e) {
			return e.getMessage();
		}

		StringBuilder builder = new StringBuilder();
		builder.append("<greeting>\n");
		builder.append("\t<id>" + greeting.id + "</id>\n");
		builder.append("\t<guestbookName>" + guestbookName + "</guestbookName>\n");
		builder.append("\t<content>" + greeting.content + "</content>\n");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String date = sdf.format(greeting.date);
		builder.append("\t<date>" + date + "</date>\n");
		builder.append("\t<authorId>" + greeting.author_id + "</authorId>\n");
		builder.append("\t<authorEmail>" + greeting.author_email + "</authorEmail>\n");
		builder.append("</greeting>\n");
		return builder.toString();
	}

}
