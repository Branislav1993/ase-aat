package de.tum.aat.services.impl;

import java.util.List;
import java.util.Random;

import de.tum.aat.dao.ExerciseGroupDAO;
import de.tum.aat.dao.StudentDAO;
import de.tum.aat.domain.ExerciseGroup;
import de.tum.aat.domain.ExerciseTimeslot;
import de.tum.aat.domain.Student;
import de.tum.aat.exceptions.GenericException;
import de.tum.aat.exceptions.NotFoundException;
import de.tum.aat.services.StudentService;

public class StudentServiceImpl implements StudentService {

	private StudentDAO sd;
	private ExerciseGroupDAO egd;

	public StudentServiceImpl() {
		sd = new StudentDAO();
		egd = new ExerciseGroupDAO();
	}

	@Override
	public Student saveStudent(Student s) {
		if (s == null) {
			throw new NotFoundException(Student.class);
		}

		s.setId(new Random().nextLong());

		if (s.getEmail() == null || !s.getEmail().contains("@tum.de")
				|| !(s.getEmail().indexOf('.') > 0 && s.getEmail().indexOf('.') < s.getEmail().indexOf('@'))) {
			throw new GenericException("Use your @tum.de email address for the registration!");
		}

		List<Student> all = sd.getStudents();
		for (Student student : all) {
			if (student.getEmail().equals(s.getEmail()))
				throw new GenericException("Already registered with the same address!");
		}

		s.setName(s.getEmail().substring(0, s.getEmail().indexOf('.')));
		s.setLastName(s.getEmail().substring(s.getEmail().indexOf('.') + 1, s.getEmail().indexOf('@')));

		return sd.saveStudent(s);
	}

	@Override
	public Student updateStudent(Student s) {
		return sd.updateStudent(s);
	}

	@Override
	public Student getStudent(long id) {
		return sd.getStudent(id);
	}

	@Override
	public List<Student> getStudents() {
		return sd.getStudents();
	}

	@Override
	public void deleteStudent(long id) {
		sd.deleteStudent(id);
	}

	@Override
	public Student registerExerciseGroupForStudent(long groupId, long studentId) {
		return sd.registerExerciseGroupForStudent(groupId, studentId);
	}

	@Override
	public Student registerAttendance(long studentId, long start, long end) {
		Student s = getStudent(studentId);
		ExerciseGroup g = egd.getExerciseGroup(s.getExerciseGroup());
		ExerciseTimeslot ts = new ExerciseTimeslot(start, end);

		if (!g.currentTimeslot().equals(ts)) {
			throw new GenericException("No current session!");
		}

		s.getTimeslotsAttended().add(ts);

		return saveStudent(s);
	}

	@Override
	public Student registerPresentation(long studentId, long start, long end) {
		Student s = getStudent(studentId);
		ExerciseGroup g = egd.getExerciseGroup(s.getExerciseGroup());
		ExerciseTimeslot ts = new ExerciseTimeslot(start, end);

		if (!g.currentTimeslot().equals(ts)) {
			throw new GenericException("No current session!");
		}

		int numberOfPresentations = s.getNumberOfPresentations();
		s.setNumberOfPresentations(numberOfPresentations++);

		return saveStudent(s);
	}

}
