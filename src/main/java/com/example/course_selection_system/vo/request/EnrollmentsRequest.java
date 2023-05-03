package com.example.course_selection_system.vo.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnrollmentsRequest {

	@JsonProperty("student_id")
	private String studentId;

	@JsonProperty("course_list")
	private List<String> courseList;

	public List<String> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<String> courseList) {
		this.courseList = courseList;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
}
