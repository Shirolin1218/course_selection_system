package com.example.course_selection_system.vo.request;

import java.util.List;

import com.example.course_selection_system.entity.Students;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentsRequest {

	@JsonProperty("student_list")
	private List<Students> studentList;

	public StudentsRequest() {
		super();
	}

	public StudentsRequest(List<Students> studentList) {
		super();
		this.studentList = studentList;
	}

	public List<Students> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Students> studentList) {
		this.studentList = studentList;
	}

}
