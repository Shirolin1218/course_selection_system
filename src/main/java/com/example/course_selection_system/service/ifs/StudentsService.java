package com.example.course_selection_system.service.ifs;

import java.util.List;

import com.example.course_selection_system.entity.Students;
import com.example.course_selection_system.vo.request.StudentsRequest;
import com.example.course_selection_system.vo.response.StudentsResponse;

public interface StudentsService {

	public StudentsResponse newStudent(StudentsRequest request);

	public StudentsResponse updateStudent(StudentsRequest request);

	public StudentsResponse delStudent(StudentsRequest request);

	public List<Students> getStudents();

}
