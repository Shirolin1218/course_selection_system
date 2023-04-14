package com.example.course_selection_system.entity;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "courses")
public class Courses {
	@Id
	@Column(name = "id") // 課堂代號
	private String id;
	@Column(name = "name") // 課堂名稱
	private String name;
	@Column(name = "week") // 1~7代表星期一到星期日
	private int week;
	@Column(name = "start_time") // 課堂開始時間
	@JsonProperty("start_time")
	private Time startTime;
	@Column(name = "end_time") // 課堂結束時間
	@JsonProperty("end_time")
	private Time endTime;
	@Column(name = "credit") // 課堂學分
	private int credit;

	public Courses() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Courses(String id, String name, int week, Time startTime, Time endTime, int credit) {
		super();
		this.id = id;
		this.name = name;
		this.week = week;
		this.startTime = startTime;
		this.endTime = endTime;
		this.credit = credit;
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

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

}
