package de.tum.aat.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import de.tum.aat.rest.assistant.TeachingAssistantIdSpecificResource;
import de.tum.aat.rest.assistant.TeachingAssistantResource;
import de.tum.aat.rest.group.ExerciseGroupIdSpecificResource;
import de.tum.aat.rest.group.ExerciseGroupResource;
import de.tum.aat.rest.student.AttendanceResource;
import de.tum.aat.rest.student.PresentationResource;
import de.tum.aat.rest.student.RegisterAttendanceResource;
import de.tum.aat.rest.student.RegisterPresentationResource;
import de.tum.aat.rest.student.StudentExerciseRegistrationResource;
import de.tum.aat.rest.student.StudentIdSpecificResource;
import de.tum.aat.rest.student.StudentResource;

public class FirstStepsApplication extends Application {

	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());

		router.attach("/", HelloWorldResource.class);
		router.attach("/guestbook/", GuestbookResource.class);
		router.attach("/guestbook/{greeting}", GreetingResource.class);
		// student resources
		router.attach("/students/", StudentResource.class);
		router.attach("/students/{id}", StudentIdSpecificResource.class);
		// exercise group resources
		router.attach("/groups/", ExerciseGroupResource.class);
		router.attach("/groups/{id}", ExerciseGroupIdSpecificResource.class);
		// teaching assistant resources
		router.attach("/assistants/", TeachingAssistantResource.class);
		router.attach("/assistants/{id}", TeachingAssistantIdSpecificResource.class);
		// attendance registration resource
		router.attach("/qrattendance/{id}", AttendanceResource.class);
		// presentation registration resource
		router.attach("/qrpresentation/{id}", PresentationResource.class);
		// group registration resource
		router.attach("/groupregister", StudentExerciseRegistrationResource.class);

		router.attach("/attendance", RegisterAttendanceResource.class);
		router.attach("/presentation", RegisterPresentationResource.class);

		return router;
	}
}
