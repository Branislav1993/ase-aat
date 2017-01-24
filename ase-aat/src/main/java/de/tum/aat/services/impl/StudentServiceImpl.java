package de.tum.aat.services.impl;

import java.util.ArrayList;
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

		// Extracting name and last name from email
		String name = s.getEmail().substring(0, s.getEmail().indexOf('.'));
		String lastName = s.getEmail().substring(s.getEmail().indexOf('.') + 1, s.getEmail().indexOf('@'));

		// Capitalizing name and last name
		name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();

		s.setName(name);
		s.setLastName(lastName);

		Student student = sd.saveStudent(s);
		RestApp.changeSecret(student.getEmail(), student.getPassword());

		return student;
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
	public Student deregisterExerciseGroupForStudent(long groupId, long studentId) {
		return sd.deregisterExerciseGroupForStudent(groupId, studentId);
	}

	@Override
	public Student registerAttendance(long studentId, long start, long end) {
		Student s = getStudent(studentId);
		ExerciseGroup g = egd.getExerciseGroup(s.getExerciseGroup());
		ExerciseTimeslot ts = new ExerciseTimeslot(start, end);

		if (!ts.equals(g.currentTimeslot())) {
			throw new GenericException("No current session!");
		}

		if (s.getTimeslotsAttended() == null) {
			ArrayList<ExerciseTimeslot> timeslots = new ArrayList<>();
			s.setTimeslotsAttended(timeslots);
		}

		s.getTimeslotsAttended().add(ts);

		if (s.getTimeslotsAttended().size() >= 10 && s.getNumberOfPresentations() > 0) {
			s.setHasBonus(true);
		}

		return sd.saveStudent(s);
	}

	@Override
	public Student registerPresentation(long studentId, long start, long end) {
		Student s = getStudent(studentId);
		ExerciseGroup g = egd.getExerciseGroup(s.getExerciseGroup());
		ExerciseTimeslot ts = new ExerciseTimeslot(start, end);

		if (!ts.equals(g.currentTimeslot())) {
			throw new GenericException("No current session!");
		}

		int numberOfPresentations = s.getNumberOfPresentations();
		s.setNumberOfPresentations(++numberOfPresentations);

		if (s.getTimeslotsAttended().size() >= 10 && s.getNumberOfPresentations() > 0) {
			s.setHasBonus(true);
		}

		return sd.saveStudent(s);
	}

	@Override
	public Student getStudent(String email) {
		List<Student> students = getStudents();
		for (Student s : students) {
			if (s.getEmail().equals(email)) {
				return s;
			}
		}
		throw new GenericException("No student with the given email!");
	}

}
