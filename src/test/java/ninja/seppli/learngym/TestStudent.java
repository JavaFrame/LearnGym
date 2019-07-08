package ninja.seppli.learngym;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ninja.seppli.learngym.exception.NoGradeYetException;
import ninja.seppli.learngym.model.Course;
import ninja.seppli.learngym.model.Student;
import ninja.seppli.learngym.model.StudentCourse;
import ninja.seppli.learngym.model.StudentManager;
import ninja.seppli.learngym.model.Subject;
import ninja.seppli.learngym.model.Teacher;
import ninja.seppli.learngym.model.TeacherManager;

/**
 * Tests the student class
 *
 * @author sebi
 *
 */
public class TestStudent {
	/**
	 * the course
	 */
	private Course course;
	/**
	 * the math subject
	 */
	private Subject math;
	/**
	 * the french subject
	 */
	private Subject french;
	/**
	 * the student
	 */
	private Student student;

	/**
	 * the student course
	 */
	private StudentCourse studentCourse;

	/**
	 * sets up the environement
	 */
	@BeforeEach
	public void setup() {
		Teacher teacher = new TeacherManager().add("Franz", "Meier");
		course = new Course("7abj", teacher);

		math = new Subject("Math", teacher);
		course.getSubjects().add(math);

		french = new Subject("French", teacher);
		course.getSubjects().add(french);

		student = new StudentManager().add("Kim", "Müller");
		studentCourse = course.addStudent(student);
	}

	/**
	 * Tests if the stream works
	 */
	@Test
	public void testStreamOfGrades() {
		math.setGrade(student, 6);
		french.setGrade(student, 3);
		math.setGrade(student, 4);
		double[] grades = studentCourse.getGrades().stream().mapToDouble(Double::doubleValue).sorted().toArray();
		assertArrayEquals(new double[] { 3, 4 }, grades);
	}

	/**
	 * tests rounding of grades
	 *
	 * @throws NoGradeYetException
	 */
	@Test
	public void testRounding1() throws NoGradeYetException {
		math.setGrade(student, 5);
		french.setGrade(student, 5.5f);
		assertEquals(5.5f, studentCourse.getAverage());
	}

	/**
	 * tests rounding of grades
	 *
	 * @throws NoGradeYetException
	 */
	@Test
	public void testRounding2() throws NoGradeYetException {
		math.setGrade(student, 5);
		french.setGrade(student, 6);
		assertEquals(5.5f, studentCourse.getAverage());
	}

	/**
	 * tests rounding of grades
	 *
	 * @throws NoGradeYetException
	 */
	@Test
	public void testRounding3() throws NoGradeYetException {
		math.setGrade(student, 5.5f);
		french.setGrade(student, 6);
		assertEquals(6d, studentCourse.getAverage());
	}

	/**
	 * tests adding grades
	 *
	 * @throws NoGradeYetExcep tion
	 */
	@Test
	public void testAddGrade() throws NoGradeYetException {
		math.setGrade(student, 5.2f);
		assertEquals(5f, studentCourse.getAverage());
	}

	/**
	 * tests prom/prov decision making
	 */
	@Test
	public void testPromDecision1() {
		math.setGrade(student, 3);
		french.setGrade(student, 5);
		math.setGrade(student, 5);
		assertFalse(studentCourse.isProv());
	}

	/**
	 * tests prom/prov decision making
	 */
	@Test
	public void testPromDecision2() {
		math.setGrade(student, 3);
		french.setGrade(student, 5);
		assertTrue(studentCourse.isProv());
	}

	/**
	 * tests prom/prov decision making
	 */
	@Test
	public void testPromDecision3() {
		math.setGrade(student, 5);
		assertFalse(studentCourse.isProv());
	}
}
