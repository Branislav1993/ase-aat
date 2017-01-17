package de.tum.aat.rest;

import java.util.HashMap;
import java.util.Map;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;

import de.tum.aat.dao.StudentDAO;
import de.tum.aat.domain.ExerciseTimeslot;
import de.tum.aat.domain.Student;
import de.tum.aat.rest.assistant.TeachingAssistantIdSpecificResource;
import de.tum.aat.rest.assistant.TeachingAssistantResource;
import de.tum.aat.rest.group.ExerciseGroupIdSpecificResource;
import de.tum.aat.rest.group.ExerciseGroupResource;
import de.tum.aat.rest.student.AttendanceResource;
import de.tum.aat.rest.student.LoginResource;
import de.tum.aat.rest.student.PresentationResource;
import de.tum.aat.rest.student.RegisterAttendanceResource;
import de.tum.aat.rest.student.RegisterPresentationResource;
import de.tum.aat.rest.student.StudentExerciseDeregistrationResource;
import de.tum.aat.rest.student.StudentExerciseRegistrationResource;
import de.tum.aat.rest.student.StudentIdSpecificResource;
import de.tum.aat.rest.student.StudentResource;

public class RestApp extends Application {

	private ChallengeAuthenticator authenticatior;
	private StudentDAO sd = new StudentDAO();
	private Context context = getContext();
	private boolean optional = true;
	private ChallengeScheme challengeScheme = ChallengeScheme.HTTP_BASIC;
	private String realm = "ASEAAT_REALM";
	private static MapVerifier verifier = new MapVerifier();
	private static Map<Long, Map<ExerciseTimeslot, Integer>> qrCodeAttendenceVerifier = new HashMap<>();
	private static Map<Long, Map<ExerciseTimeslot, Integer>> qrCodePresentationVerifier = new HashMap<>();

	public static Map<Long, Map<ExerciseTimeslot, Integer>> getQrCodeAttendenceVerifier() {
		return qrCodeAttendenceVerifier;
	}

	public static void setQrCodeAttendenceVerifier(Map<Long, Map<ExerciseTimeslot, Integer>> qrCodeAttendenceVerifier) {
		RestApp.qrCodeAttendenceVerifier = qrCodeAttendenceVerifier;
	}

	public static Map<Long, Map<ExerciseTimeslot, Integer>> getQrCodePresentationVerifier() {
		return qrCodePresentationVerifier;
	}

	public static void setQrCodePresentationVerifier(Map<Long, Map<ExerciseTimeslot, Integer>> qrCodePresentationVerifier) {
		RestApp.qrCodePresentationVerifier = qrCodePresentationVerifier;
	}

	private ChallengeAuthenticator createAuthenticator() {

		for (Student s : sd.getStudents()) {
			verifier.getLocalSecrets().put(s.getEmail(), s.getPassword().toCharArray());
		}

		ChallengeAuthenticator auth = new ChallengeAuthenticator(context, optional, challengeScheme, realm, verifier) {
			@Override
			protected boolean authenticate(Request request, Response response) {
				if (request.getChallengeResponse() == null) {
					return false;
				} else {
					return super.authenticate(request, response);
				}
			}
		};

		return auth;
	}

	public static RestApp getInstance() {
		return (RestApp) getCurrent();
	}

	@Override
	public Restlet createInboundRoot() {

		this.authenticatior = createAuthenticator();

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
		router.attach("/groupderegister", StudentExerciseDeregistrationResource.class);

		router.attach("/attendance", RegisterAttendanceResource.class);
		router.attach("/presentation", RegisterPresentationResource.class);
		
		router.attach("/login", LoginResource.class);

		authenticatior.setNext(router);

		return authenticatior;
	}

	public boolean authenticate(Request request, Response response) {
		if (!request.getClientInfo().isAuthenticated()) {
			authenticatior.challenge(response, false);
			return false;
		}
		return true;
	}

	public static void changeSecret(String email, String password) {
		verifier.getLocalSecrets().put(email, password.toCharArray());
	}

}
