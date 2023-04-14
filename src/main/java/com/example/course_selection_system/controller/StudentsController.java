package com.example.course_selection_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.course_selection_system.entity.Students;
import com.example.course_selection_system.service.ifs.StudentsService;
import com.example.course_selection_system.vo.request.StudentsRequest;
import com.example.course_selection_system.vo.response.StudentsResponse;
import com.fasterxml.jackson.annotation.JsonProperty;


@RestController
public class StudentsController {

	@Autowired
	private StudentsService studentsService;


	@PostMapping("/new_student")
	public StudentsResponse newStudent(@RequestBody StudentsRequest request) {
		return studentsService.newStudent(request);
	}

	@PostMapping("/update_student")
	public StudentsResponse updateStudent(@RequestBody StudentsRequest request) {
		return studentsService.updateStudent(request);
	}

	@PostMapping("/delete_student")
	public StudentsResponse delnewStudent(@RequestBody StudentsRequest request) {
		return studentsService.delStudent(request);
	}

	@PostMapping("/get_student")
	public List<Students> getStudent() {
		return studentsService.getStudents();
	}
	
	@PostMapping("/get_enrollments_and_courses_by_student_id")
	@JsonProperty("student_id")
	public StudentsResponse getEnrollmentsAndCoursesByStudentId(@RequestBody String studentId) {
		return studentsService.getEnrollmentsAndCoursesByStudentId(studentId);
	}

}
