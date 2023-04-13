package com.example.course_selection_system.service.ifs;

import java.util.List;

import com.example.course_selection_system.entity.Enrollments;
import com.example.course_selection_system.vo.request.EnrollmentsRequest;
import com.example.course_selection_system.vo.response.EnrollmentsResponse;

public interface EnrollmentsService {

	public EnrollmentsResponse newEnrollments(EnrollmentsRequest request);

	public EnrollmentsResponse delEnrollments(EnrollmentsRequest request);
	
	public List<Enrollments> getEnrollments();

}
