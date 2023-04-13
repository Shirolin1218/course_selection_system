package com.example.course_selection_system.vo.response;

import java.util.List;

import com.example.course_selection_system.entity.Students;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentsResponse {

	@JsonProperty("student_list")
	private List<Students> studentList;

	private String message;

	public StudentsResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StudentsResponse(String message) {
		super();
		this.message = message;
	}

	public StudentsResponse(List<Students> studentList, String message) {
		super();
		this.studentList = studentList;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Students> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Students> studentList) {
		this.studentList = studentList;
	}

}
