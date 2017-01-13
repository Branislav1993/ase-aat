package de.tum.aat.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.tum.aat.constants.Constants;
import de.tum.aat.domain.ExerciseGroup;
import de.tum.aat.domain.ExerciseTimeslot;
import de.tum.aat.domain.Student;
import de.tum.aat.exceptions.GenericException;
import de.tum.aat.rest.RestApp;
import de.tum.aat.services.ExerciseGroupService;
import de.tum.aat.services.StudentService;
import de.tum.aat.services.impl.ExerciseGroupServiceImpl;
import de.tum.aat.services.impl.StudentServiceImpl;

public class QRGenerator {

	private StudentService ss;
	private ExerciseGroupService gs;

	public QRGenerator() {
		ss = new StudentServiceImpl();
		gs = new ExerciseGroupServiceImpl();
	}

	public String generateAttendance(long id) {

		Student s = ss.getStudent(id);

		ExerciseGroup eg = gs.getExerciseGroup(s.getExerciseGroup());

		if (eg == null) {
			throw new GenericException("Student is not yet registered for any group.");
		}

		ExerciseTimeslot currentTimeslot = eg.currentTimeslot();

		if (currentTimeslot == null) {
			throw new GenericException("No current exercise session.");
		}

		if (s.getTimeslotsAttended() == null) {
			ArrayList<ExerciseTimeslot> timeslots = new ArrayList<>();
			s.setTimeslotsAttended(timeslots);
		}
		if (!s.getTimeslotsAttended().contains(currentTimeslot)) {
			s.getTimeslotsAttended().add(currentTimeslot);
			return packAttendanceURL(s, currentTimeslot);
		} else {
			throw new GenericException("Already registered attendance.");
		}
	}

	public String packAttendanceURL(Student s, ExerciseTimeslot currentTimeslot) {
		int randomQrAttendanceId = new Random().nextInt(Integer.MAX_VALUE);

		if (RestApp.getQrCodeAttendenceVerifier().get(s.getId()) == null
				|| RestApp.getQrCodeAttendenceVerifier().get(s.getId()).get(currentTimeslot) == null) {
			Map<ExerciseTimeslot, Integer> map = new HashMap<>();
			map.put(currentTimeslot, randomQrAttendanceId);
			RestApp.getQrCodeAttendenceVerifier().put(s.getId(), map);
		}

		return Constants.DOMAIN + "rest/attendance?id=" + s.getId() + "&start=" + currentTimeslot.getStart().getTime()
				+ "&end=" + currentTimeslot.getEnd().getTime() + "&s="
				+ RestApp.getQrCodeAttendenceVerifier().get(s.getId()).get(currentTimeslot);
	}

	public String packPresentationURL(Student s, ExerciseTimeslot currentTimeslot) {
		int randomQrPresentationId = new Random().nextInt(Integer.MAX_VALUE);

		if (RestApp.getQrCodePresentationVerifier().get(s.getId()) == null
				|| RestApp.getQrCodePresentationVerifier().get(s.getId()).get(currentTimeslot) == null) {
			Map<ExerciseTimeslot, Integer> map = new HashMap<>();
			map.put(currentTimeslot, randomQrPresentationId);
			RestApp.getQrCodePresentationVerifier().put(s.getId(), map);
		}

		return Constants.DOMAIN + "rest/presentation?id=" + s.getId() + "&start=" + currentTimeslot.getStart().getTime()
				+ "&end=" + currentTimeslot.getEnd().getTime() + "&s="
				+ RestApp.getQrCodeAttendenceVerifier().get(s.getId()).get(currentTimeslot);
	}

	public String generatePresentation(long id) {

		Student s = ss.getStudent(id);
		ExerciseGroup eg = gs.getExerciseGroup(s.getExerciseGroup());

		if (eg == null) {
			throw new GenericException("Student is not yet registered for any group.");
		}

		ExerciseTimeslot currentTimeslot = eg.currentTimeslot();

		if (currentTimeslot == null) {
			throw new GenericException("No current exercise session.");
		}

		if (s.getTimeslotsAttended() != null && !s.getTimeslotsAttended().contains(currentTimeslot)) {
			s.getTimeslotsAttended().add(currentTimeslot);
			return packPresentationURL(s, currentTimeslot);
		} else {
			throw new GenericException("Register attendance first!");
		}
	}

}
