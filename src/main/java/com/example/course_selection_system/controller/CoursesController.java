package com.example.course_selection_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.course_selection_system.entity.Courses;
import com.example.course_selection_system.service.ifs.CoursesService;
import com.example.course_selection_system.vo.request.CoursesRequest;
import com.example.course_selection_system.vo.response.CoursesResponse;

@CrossOrigin
@RestController
public class CoursesController {

	@Autowired
	private CoursesService coursesService;

	@PostMapping("/new_course")
	public CoursesResponse newCourse(@RequestBody CoursesRequest request) {
		return coursesService.newCourse(request);
	}

//	@PostMapping("/update_course")
//	public CoursesResponse updateCourse(@RequestBody CoursesRequest request) {
//		return coursesService.updateCourse(request);
//	}

	@PostMapping("/delete_course")
	public CoursesResponse delCourse(@RequestBody CoursesRequest request) {
		return coursesService.delCourse(request);
	}

	@GetMapping("/get_course")
	public List<Courses> getCourses() {
		return coursesService.getCourses();
	}

	@PostMapping("/get_courses_by_name")
	public List<Courses> findCoursesByName(@RequestBody String courseName) {
		return coursesService.findCoursesByName(courseName);
	}

	@PostMapping("/get_courses_by_course_id")
	public List<Courses> findCoursesByCourseId(@RequestBody String courseId) {
		return coursesService.findCoursesByCourseId(courseId);
	}

}
