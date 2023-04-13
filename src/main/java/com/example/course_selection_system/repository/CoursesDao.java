package com.example.course_selection_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.course_selection_system.entity.Courses;

@Repository
public interface CoursesDao extends JpaRepository<Courses, String> {
	
	List<Courses> findByName(String name);
}
