package com.example.course_selection_system.vo.response;

import com.example.course_selection_system.entity.Courses;
import com.example.course_selection_system.entity.Students;

public class EnrollmentsResponse {

	private String studentId;

	private String courseName;
	
	private Students students;
	private Courses courses;

	private String message;

	public EnrollmentsResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EnrollmentsResponse(String message) {
		super();
		this.message = message;
	}

	public EnrollmentsResponse(String studentId, String courseName, String message) {
		super();
		this.message = message;
		this.studentId = studentId;
		this.courseName = courseName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

}
