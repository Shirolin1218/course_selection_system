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

			// 課程id格式限制為1到5個英文字加上4個數字
			String patternId = "[a-zA-Z]{1,5}\\\\d{4}";
			// 課程名稱格式限制為2到5個中文字
			String patternName = "[\\\\u4e00-\\\\u9fa5]{2,5}";
//			item.getStartTime();
//			item.getEndTime();
//			item.getWeek();
//			item.getCredit();

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
			// 學生id格式限制
			if (!reqId.matches(patternId)) {
				errorMessage = errorMessage + reqId + "id格式錯誤。 ";
				errorList.add(item);
				continue;
			}
			// 課程名稱格式限制
			if (!reqName.matches(patternName)) {
				errorMessage = errorMessage + reqId + "課程名稱錯誤。 ";
				errorList.add(item);
				continue;
			}

			// 判斷星期，設定為1~7，以外的數字為格式錯誤。
			if (item.getWeek() < 1 || item.getWeek() > 7) {
				errorMessage = errorMessage + reqId + "星期設定錯誤。 ";
				errorList.add(item);
				continue;
			}
			// 判斷時間，開始時間不能在結束時間之後
			if (item.getStartTime() == null || item.getEndTime() == null
					|| item.getStartTime().compareTo(item.getEndTime()) >= 0) {
				errorMessage = errorMessage + reqId + "時間設定錯誤。 ";
				errorList.add(item);
				continue;
			}
			// 學分限制在1~3分
			if (item.getCredit() < 1 || item.getCredit() > 3) {
				errorMessage = errorMessage + reqId + "學分設定錯誤。 ";
				errorList.add(item);
				continue;
			}

		}
		if (!CollectionUtils.isEmpty(errorList)) {
			return new CoursesResponse(errorList, "發生錯誤 " + errorMessage);
		}
		coursesDao.saveAll(reqList);
		return new CoursesResponse(reqList, "新增成功");
	}

	@Override
	public CoursesResponse updateCourse(CoursesRequest request) {
		List<Courses> reqList = request.getCourseList();
		List<Courses> errorList = new ArrayList<>();
		List<List<Enrollments>> upEnrollmentList = new ArrayList<>();
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
			// 判斷星期，設定為1~7，以外的數字為格式錯誤。
			if (item.getWeek() < 1 || item.getWeek() > 7) {
				errorMessage = errorMessage + reqId + "星期設定錯誤。 ";
				errorList.add(item);
				continue;
			}
			// 判斷時間是否有正確輸入，開始時間不能在結束時間之後
			if (item.getStartTime() == null || item.getEndTime() == null
					|| item.getStartTime().compareTo(item.getEndTime()) < 0) {
				errorMessage = errorMessage + reqId + "時間設定錯誤。 ";
				errorList.add(item);
				continue;
			}
			// 學分限制在1~3分
			if (item.getCredit() < 1 || item.getCredit() > 3) {
				errorMessage = errorMessage + reqId + "學分設定錯誤。 ";
				errorList.add(item);
				continue;
			}
			// 存入選課紀錄待修改List
			upEnrollmentList.add(enrollmentsDao.findByCourseName(item.getName()));
		}
		if (!CollectionUtils.isEmpty(errorList)) {
			return new CoursesResponse(errorList, "發生錯誤 " + errorMessage);
		}
		
		for (List<Enrollments> upList : upEnrollmentList) {
			enrollmentsDao.deleteAll(upList);
		}
		coursesDao.saveAll(reqList);
		return new CoursesResponse(reqList, "更新成功。");
	}

	@Override
	public CoursesResponse delCourse(CoursesRequest request) {
		List<Courses> reqList = request.getCourseList();
		List<Courses> errorList = new ArrayList<>();
		List<List<Enrollments>> enrollmentDelList = new ArrayList<>();

		String errorMessage = "";
		if (CollectionUtils.isEmpty(reqList)) {
			return new CoursesResponse("請重新確認輸入。");
		}
		for (Courses course : reqList) {
			String reqId = course.getId();
			String reqName = course.getName();
			if (!StringUtils.hasText(reqName) || !StringUtils.hasText(reqId)) {
				errorMessage = errorMessage + "id或名稱不得為空。 ";
				errorList.add(course);
				continue;
			}
			if (!coursesDao.existsById(reqId)) {
				errorMessage = errorMessage + reqId + "不存在。 ";
				errorList.add(course);
				continue;
			}
			// 取出該課程的選課資料加入待刪除List
			enrollmentDelList.add(enrollmentsDao.findByCourseName(reqName));

		}
		if (!CollectionUtils.isEmpty(errorList)) {
			return new CoursesResponse(errorList, "發生錯誤 " + errorMessage);
		}
		// 刪除該課程的所有選課資料
		for (List<Enrollments> delList : enrollmentDelList) {
			enrollmentsDao.deleteAll(delList);
		}

		coursesDao.deleteAll(reqList);
		return new CoursesResponse(reqList, "刪除成功。");
	}

	@Override
	public List<Courses> getCourses() {
		return coursesDao.findAll();
	}

}
