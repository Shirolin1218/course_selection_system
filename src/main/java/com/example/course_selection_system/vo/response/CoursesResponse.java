package com.example.course_selection_system.vo.response;

import java.util.List;

import com.example.course_selection_system.entity.Courses;
import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoursesResponse {

	private List<Courses> courseList;

	private String message;

	public CoursesResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CoursesResponse(String message) {
		super();
		this.message = message;
	}

	public CoursesResponse(List<Courses> courseList, String message) {
		super();
		this.courseList = courseList;
		this.message = message;
	}

	public List<Courses> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<Courses> courseList) {
		this.courseList = courseList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
