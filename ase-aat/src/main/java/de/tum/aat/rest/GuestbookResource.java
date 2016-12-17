package de.tum.aat.rest;

import java.text.SimpleDateFormat;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import de.tum.aat.Greeting;
import de.tum.aat.Guestbook;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

/**
 * Resource which has only one representation.
 *
 */
public class GuestbookResource extends ServerResource {

    @Get
    public String represent() throws Exception {
    	String guestbookName = "default";
    	Key<Guestbook> theBook = Key.create(Guestbook.class, guestbookName);

        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
          List<Greeting> greetings = ObjectifyService.ofy()
              .load()
              .type(Greeting.class) // We want only Greetings
              .ancestor(theBook)    // Anyone in this book
              .order("-date")       // Most recent first - date is indexed.
              .list();
              
        StringBuilder builder = new StringBuilder();
        builder.append("<guestbook>\n");
      	
      	
        for (Greeting greeting : greetings) {
        	builder.append("\t<greeting>\n");
          	builder.append("\t\t<id>" + greeting.id + "</id>\n");
          	builder.append("\t\t<guestbookName>" + guestbookName + "</guestbookName>\n");
          	builder.append("\t\t<content>" + greeting.content + "</content>\n");
          	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
          	String date = sdf.format(greeting.date);
          	builder.append("\t\t<date>" + date + "</date>\n");
          	builder.append("\t\t<authorId>" + greeting.author_id + "</authorId>\n");
          	builder.append("\t\t<authorEmail>" + greeting.author_email + "</authorEmail>\n");
          	builder.append("\t</greeting>\n");
        }
    	builder.append("</guestbook>\n");
    	
    	return builder.toString();
    }

}
