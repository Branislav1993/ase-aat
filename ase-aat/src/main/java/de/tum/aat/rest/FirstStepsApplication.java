package de.tum.aat.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import de.tum.aat.rest.groups.ExerciseGroupResource;
import de.tum.aat.rest.student.AttendanceResource;
import de.tum.aat.rest.student.GetStudentResource;
import de.tum.aat.rest.student.StudentResource;

public class FirstStepsApplication extends Application {

	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public Restlet createInboundRoot() {
		// Create a router Restlet that routes each call to a
		// new instance of HelloWorldResource.
		Router router = new Router(getContext());

		// Defines only one route
		// router.attachDefault(HelloWorldResource.class);

		router.attach("/", HelloWorldResource.class);
		router.attach("/guestbook/", GuestbookResource.class);
		router.attach("/guestbook/{greeting}", GreetingResource.class);
		// student resources
		router.attach("/students/", StudentResource.class);
		router.attach("/students/{id}", GetStudentResource.class);
		// exercise group resources
		router.attach("/groups/", ExerciseGroupResource.class);
		// teaching assistant resources
		router.attach("/assistants/", TeachingAssistantResource.class);
		
		router.attach("/qrattendance/{id}", AttendanceResource.class);

		return router;
	}
}
