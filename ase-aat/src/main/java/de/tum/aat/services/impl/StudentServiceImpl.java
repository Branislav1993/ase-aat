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
import de.tum.aat.rest.RestApp;
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

		Random r = new Random();
		s.setId(r.nextInt(Integer.MAX_VALUE));

		if (s.getEmail() == null || !s.getEmail().contains("@tum.de")
				|| !(s.getEmail().indexOf('.') > 0 && s.getEmail().indexOf('.') < s.getEmail().indexOf('@'))) {
			throw new GenericException("Use your @tum.de email address for the registration!");
		}

		if (s.getPassword() == null || s.getPassword().length() < 1) {
			throw new GenericException("Please insert password!");
		}

		List<Student> all = sd.getStudents();

		if (all == null || !all.isEmpty()) {
			for (Student student : all) {
				if (student.getEmail().equals(s.getEmail()))
					throw new GenericException("Already registered with the same address!");
			}
		}

		s.setName(s.getEmail().substring(0, s.getEmail().indexOf('.')));
		s.setLastName(s.getEmail().substring(s.getEmail().indexOf('.') + 1, s.getEmail().indexOf('@')));

		return sd.saveStudent(s);
	}

	@Override
	public Student updateStudent(Student s) {

		Student oldStudent = getStudent(s.getId());

		if (oldStudent == null) {
			throw new NotFoundException(Student.class);
		}

		if (!oldStudent.getPassword().equals(s.getPassword())) {
			RestApp.changeSecret(oldStudent.getEmail(), s.getPassword());
		}

		String email = oldStudent.getEmail();

		oldStudent = s;
		oldStudent.setEmail(email);

		return sd.updateStudent(oldStudent);
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
