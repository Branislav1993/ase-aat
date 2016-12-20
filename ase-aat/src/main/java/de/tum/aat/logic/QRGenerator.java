package de.tum.aat.logic;

import de.tum.aat.constants.Constants;
import de.tum.aat.domain.ExerciseGroup;
import de.tum.aat.domain.ExerciseTimeslot;
import de.tum.aat.domain.Student;
import de.tum.aat.services.ExerciseGroupService;
import de.tum.aat.services.StudentService;
import de.tum.aat.services.impl.StudentServiceImpl;

public class QRGenerator {

	private StudentService ss;
	private ExerciseGroupService gs;

	public QRGenerator() {
		ss = new StudentServiceImpl();
	}

	public String generateAttendance(long id) {

		Student s = ss.getStudent(id);
		ExerciseGroup eg = gs.getExerciseGroup(s.getExerciseGroup());
		ExerciseTimeslot currentTimeslot = eg.currentTimeslot();

		if (currentTimeslot == null) {
			// TODO: create NoCurrentExerciseSessionException
			return null;
		}

		if (!s.getTimeslotsAttended().contains(currentTimeslot)) {
			s.getTimeslotsAttended().add(currentTimeslot);
			return packURL(s, currentTimeslot);
		} else {
			// TODO: create AlreadyRegisteredAttendanceException
			return null;
		}
	}

	public String packURL(Student s, ExerciseTimeslot currentTimeslot) {
		return Constants.DOMAIN + "attendance?id=" + s.getId() + "&start=" + currentTimeslot.getStart().getTime()
				+ "&end=" + currentTimeslot.getEnd().getTime();
	}

}
