package de.tum.aat.services;

import java.util.List;

import de.tum.aat.domain.Student;

public interface StudentService {

	public Student saveStudent(Student s);

	public Student updateStudent(Student s);

	public Student getStudent(long id);

	public List<Student> getStudents();

	public void deleteStudent(long id);

	public Student registerExerciseGroupForStudent(long groupId, long studentId);

	public Student registerAttendance(long studentId, long start, long end);

	Student registerPresentation(long studentId, long start, long end);

	public Student getStudent(String email);

}
