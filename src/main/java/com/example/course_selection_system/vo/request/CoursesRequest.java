package com.example.course_selection_system.vo.request;

import java.util.List;

import com.example.course_selection_system.entity.Courses;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CoursesRequest {

	@JsonProperty("course_list")
	List<Courses> courseList;

	public CoursesRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CoursesRequest(List<Courses> courseList) {
		super();
		this.courseList = courseList;
	}

	public List<Courses> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<Courses> courseList) {
		this.courseList = courseList;
	}

}
