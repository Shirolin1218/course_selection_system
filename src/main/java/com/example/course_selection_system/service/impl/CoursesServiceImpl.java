package com.example.course_selection_system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.course_selection_system.entity.Courses;
import com.example.course_selection_system.entity.Enrollments;
import com.example.course_selection_system.repository.CoursesDao;
import com.example.course_selection_system.repository.EnrollmentsDao;
import com.example.course_selection_system.service.ifs.CoursesService;
import com.example.course_selection_system.vo.request.CoursesRequest;
import com.example.course_selection_system.vo.response.CoursesResponse;

@Service
public class CoursesServiceImpl implements CoursesService {

	@Autowired
	private CoursesDao coursesDao;

	@Autowired
	private EnrollmentsDao enrollmentsDao;

	@Override
	public CoursesResponse newCourse(CoursesRequest request) {
		List<Courses> reqList = request.getCourseList();
		List<Courses> errorList = new ArrayList<>();
		String errorMessage = "";

		if (CollectionUtils.isEmpty(reqList)) {
			return new CoursesResponse("請重新確認輸入。");
		}
		for (Courses item : reqList) {
			String reqId = item.getId();
			String reqName = item.getName();
			if (!StringUtils.hasText(reqName) || !StringUtils.hasText(reqId)) {
				errorMessage = errorMessage + "id或姓名不得為空。 ";
				errorList.add(item);
				continue;
			}
			if (coursesDao.existsById(reqId)) {
				errorMessage = errorMessage + reqId + "已存在。 ";
				errorList.add(item);
				continue;
			}
		}
		if (!errorList.isEmpty()) {
			return new CoursesResponse(errorList, "發生錯誤 " + errorMessage);
		}
		coursesDao.saveAll(reqList);
		return new CoursesResponse(reqList, "新增成功");
	}

	@Override
	public CoursesResponse updateCourse(CoursesRequest request) {
		List<Courses> reqList = request.getCourseList();
		List<Courses> errorList = new ArrayList<>();
		String errorMessage = "";

		if (CollectionUtils.isEmpty(reqList)) {
			return new CoursesResponse("請重新確認輸入。");
		}
		for (Courses item : reqList) {
			String reqId = item.getId();
			String reqName = item.getName();
			if (!StringUtils.hasText(reqName) || !StringUtils.hasText(reqId)) {
				errorMessage = errorMessage + "id或姓名不得為空。 ";
				errorList.add(item);
				continue;
			}
			if (!coursesDao.existsById(reqId)) {
				errorMessage = errorMessage + reqId + "不存在。 ";
				errorList.add(item);
				continue;
			}
		}
		if (!CollectionUtils.isEmpty(errorList)) {
			return new CoursesResponse(errorList, "發生錯誤 " + errorMessage);
		}
		coursesDao.saveAll(reqList);
		return new CoursesResponse(reqList, "更新成功。");
	}

	@Override
	public CoursesResponse delCourse(CoursesRequest request) {
		List<Courses> reqList = request.getCourseList();
		List<Courses> errorList = new ArrayList<>();
		List<List<Enrollments>> enrollmentDelList = new ArrayList<>();

		String message = "";
		if (CollectionUtils.isEmpty(reqList)) {
			return new CoursesResponse("請重新確認輸入。");
		}
		for (Courses course : reqList) {
			String reqId = course.getId();
			String reqName = course.getName();
			if (!StringUtils.hasText(reqName) || !StringUtils.hasText(reqId)) {
				message = message + "id或名稱不得為空。 ";
				errorList.add(course);
				continue;
			}
			if (!coursesDao.existsById(reqId)) {
				message = message + reqId + "不存在。 ";
				errorList.add(course);
				continue;
			}
			// 取出該課程的選課資料加入代刪除List
			enrollmentDelList.add(enrollmentsDao.findByCourseName(reqName));

		}
		if (!CollectionUtils.isEmpty(errorList)) {
			return new CoursesResponse(errorList, "發生錯誤 " + message);
		}
		// 刪除該課程的所有選課資料
		for (List<Enrollments> item : enrollmentDelList) {
			enrollmentsDao.deleteAll(item);
		}

		coursesDao.deleteAll(reqList);
		return new CoursesResponse(reqList, "刪除成功。");
	}

	@Override
	public List<Courses> getCourses() {
		return coursesDao.findAll();
	}

}
