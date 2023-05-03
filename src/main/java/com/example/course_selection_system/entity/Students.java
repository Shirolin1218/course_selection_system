package com.example.course_selection_system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "students")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Students {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "name")
	private String name;

	public Students() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Students(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
