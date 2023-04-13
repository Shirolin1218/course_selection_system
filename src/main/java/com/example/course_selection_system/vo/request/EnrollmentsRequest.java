package com.example.course_selection_system.vo.request;

import com.example.course_selection_system.entity.Enrollments;

public class EnrollmentsRequest {

	private Enrollments enrollment;

	public Enrollments getEnrollment() {
		return enrollment;
	}

	public void setEnrollment(Enrollments enrollment) {
		this.enrollment = enrollment;
	}
}
