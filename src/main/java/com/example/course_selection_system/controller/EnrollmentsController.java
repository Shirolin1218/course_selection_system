package com.example.course_selection_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.course_selection_system.entity.Enrollments;
import com.example.course_selection_system.service.ifs.EnrollmentsService;
import com.example.course_selection_system.vo.request.EnrollmentsRequest;
import com.example.course_selection_system.vo.response.EnrollmentsResponse;

@CrossOrigin
@RestController
public class EnrollmentsController {

	@Autowired
	private EnrollmentsService enrollmentsService;

	@PostMapping("/new_enrollment")
	public EnrollmentsResponse newEnrollment(@RequestBody EnrollmentsRequest request) {
		return enrollmentsService.newEnrollments(request);
	}

//	@PostMapping("/update_course")
//	public EnrollmentsResponse updateCourse(@RequestBody EnrollmentsRequest request) {
//		return enrollmentsService.updateEnrollments(request);
//	}

	@PostMapping("/delete_enrollment")
	public EnrollmentsResponse delEnrollment(@RequestBody EnrollmentsRequest request) {
		return enrollmentsService.delEnrollments(request);
	}

	@PostMapping("/get_enrollment")
	public List<Enrollments> getEnrollment() {
		return enrollmentsService.getEnrollments();
	}
}
