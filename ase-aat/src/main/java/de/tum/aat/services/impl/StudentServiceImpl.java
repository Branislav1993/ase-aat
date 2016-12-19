package de.tum.aat.services.impl;

import java.util.List;

import de.tum.aat.dao.StudentDAO;
import de.tum.aat.domain.Student;
import de.tum.aat.services.StudentService;

public class StudentServiceImpl implements StudentService {

	private StudentDAO sd;

	public StudentServiceImpl() {
		sd = new StudentDAO();
	}

	@Override
	public Student saveStudent(Student s) {
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

}
