package de.tum.aat.rest;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Resource which has only one representation.
 *
 */
public class HelloWorldResource extends ServerResource {

//    @Get("xml")
    @Get
    public String represent() {
    	StringBuilder builder = new StringBuilder();
    	builder.append("<greeting>\n");
    	builder.append("\t<id>1</id>\n");
    	builder.append("\t<guestbookName>default</guestbookName>\n");
    	builder.append("\t<content>Example greeting!</content>\n");
    	builder.append("\t<userEmail>computer@example.com</userEmail>\n");
    	builder.append("</greeting>\n");
    	return builder.toString();
    }

}
