package com.example.course_selection_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.course_selection_system.entity.Students;

@Repository
public interface StudentsDao extends JpaRepository<Students, String> {

}
