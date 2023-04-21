package com.example.course_selection_system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "enrollments")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Enrollments {

	@Id
	@Column(name = "enrollment_id") // 由學生id和課程名稱生成的主鍵
	private String enrollmentId;
	@Column(name = "student_id")
	private String studentId;
	@Column(name = "course_name")
	private String courseName;
	

	public Enrollments() {
		super();
	}

	public Enrollments(String studentId, String courseName) {
		super();
		this.studentId = studentId;
		this.courseName = courseName;
		
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getEnrollmentId() {
		return enrollmentId;
	}

	public void setEnrollmentId(String enrollmentId) {
		this.enrollmentId = enrollmentId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}


}
