package com.example.course_selection_system.vo.response;

import java.util.List;

import com.example.course_selection_system.entity.Enrollments;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrollmentsResponse {
	
	private List<Enrollments>  enrollmentList;

	private String studentId;

	private String courseName;

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
	
	

	public EnrollmentsResponse(List<Enrollments> enrollmentList, String message) {
		super();
		this.enrollmentList = enrollmentList;
		this.message = message;
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

	public List<Enrollments> getEnrollmentList() {
		return enrollmentList;
	}

	public void setEnrollmentList(List<Enrollments> enrollmentList) {
		this.enrollmentList = enrollmentList;
	}

}
