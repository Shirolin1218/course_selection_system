package com.example.course_selection_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.course_selection_system.entity.Enrollments;
import com.example.course_selection_system.service.ifs.EnrollmentsService;
import com.example.course_selection_system.vo.request.EnrollmentsRequest;
import com.example.course_selection_system.vo.response.EnrollmentsResponse;

@RestController
public class EnrollmentsController {

	@Autowired
	private EnrollmentsService enrollmentsService;
	
	@PostMapping("/new_enrollment")
	public EnrollmentsResponse newCourse(@RequestBody EnrollmentsRequest request) {
		return enrollmentsService.newEnrollments(request);
	}

//	@PostMapping("/update_course")
//	public EnrollmentsResponse updateCourse(@RequestBody EnrollmentsRequest request) {
//		return enrollmentsService.updateEnrollments(request);
//	}

	@PostMapping("/delete_enrollment")
	public EnrollmentsResponse delCourse(@RequestBody EnrollmentsRequest request) {
		return enrollmentsService.delEnrollments(request);
	}

	@PostMapping("/get_enrollment")
	public List<Enrollments> getCourses() {
		return enrollmentsService.getEnrollments();
	}
}
