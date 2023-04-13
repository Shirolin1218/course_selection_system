package com.example.course_selection_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.course_selection_system.entity.Enrollments;

@Repository
public interface EnrollmentsDao extends JpaRepository<Enrollments, String> {

	List<Enrollments> findByCourseName(String courseName);

	List<Enrollments> findByStudentId(String studentId);

}
