package com.example.course_selection_system.service.ifs;

import java.util.List;

import com.example.course_selection_system.entity.Courses;
import com.example.course_selection_system.vo.request.CoursesRequest;
import com.example.course_selection_system.vo.response.CoursesResponse;

public interface CoursesService {

	public CoursesResponse newCourse(CoursesRequest request);

	public CoursesResponse updateCourse(CoursesRequest request);

	public CoursesResponse delCourse(CoursesRequest request);

	public List<Courses> getCourses();

	public List<Courses> findCoursesByName(String courseName);
	
	public List<Courses> findCoursesByCourseId(String courseId);
}
