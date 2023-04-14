package com.example.course_selection_system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.course_selection_system.entity.Courses;
import com.example.course_selection_system.entity.Enrollments;
import com.example.course_selection_system.repository.CoursesDao;
import com.example.course_selection_system.repository.EnrollmentsDao;
import com.example.course_selection_system.service.ifs.EnrollmentsService;
import com.example.course_selection_system.vo.request.EnrollmentsRequest;
import com.example.course_selection_system.vo.response.EnrollmentsResponse;

@Service
public class EnrollmentsServiceImpl implements EnrollmentsService {

	@Autowired
	private EnrollmentsDao enrollmentsDao;

	@Autowired
	private CoursesDao coursesDao;

	@Override
	public EnrollmentsResponse newEnrollments(EnrollmentsRequest request) {

		if (request == null || request.getEnrollment() == null || request.getEnrollment().getStudentId() == null
				|| request.getEnrollment().getCourseName() == null) {
			return new EnrollmentsResponse("請重新確認輸入。");
		}
		// 本次選課資料
		Enrollments enrollment = request.getEnrollment();
		// 本次選課的學生id
		String reqStudent = enrollment.getStudentId();
		// 本次選課的課程名稱
		String reqCourseName = enrollment.getCourseName();
		// 本次選課的課程
		List<Courses> reqCourse = coursesDao.findByName(reqCourseName);

		if (!StringUtils.hasText(reqStudent) || !StringUtils.hasText(reqCourseName)) {
			return new EnrollmentsResponse("學生id或課程名稱不得為空。 ");
		}
		// 限制一個課程至多3人選修
		if (enrollmentsDao.findByCourseName(reqCourseName).size() >= 3) {
			return new EnrollmentsResponse("課程" + reqCourseName + "人數已滿。 ");
		}

		// 建立本次選課編碼
		String enrollmentId = reqStudent + "_" + reqCourseName;
		// 判斷此學生有沒有選過這堂課
		if (enrollmentsDao.existsById(enrollmentId)) {
			return new EnrollmentsResponse("學號" + reqStudent + "已選擇過此課程");
		}

		// 取出此學生id的選課紀錄
		List<Enrollments> reqStudentAllEnrollment = enrollmentsDao.findByStudentId(reqStudent);

		// 存放此學生id已選的課程名稱，使用此筆資料進到course資料庫取資料
		List<String> selectedCourseNameList = new ArrayList<>();

		for (Enrollments item : reqStudentAllEnrollment) {
			String selectedCourse = item.getCourseName();
			selectedCourseNameList.add(selectedCourse);
			/* 判斷是否衝堂==================================================================== */
			for (Courses course : coursesDao.findByName(selectedCourse)) {

				for (Courses thisCourse : reqCourse) {
					// 判斷是否同一個星期
					if (course.getWeek() != thisCourse.getWeek()) {
						continue;
					}
					// 判斷時間是否重疊
					// thisCourse:本次加選的課程 course資料庫取出的已選課程
					if (thisCourse.getStartTime().compareTo(course.getEndTime()) <= 0
							&& thisCourse.getEndTime().compareTo(course.getStartTime()) <= 0) {
						continue;
					}
					return new EnrollmentsResponse(thisCourse + "和" + selectedCourse + "衝堂，無法加選");
				}

			}
			/* 判斷是否衝堂==================================================================== */
		}

		/* 計算總學分有沒有超過10==================================================== */
		int totalCredit = 0;
		for (String selectedCourseName : selectedCourseNameList) {
			// 因不是使用主鍵查詢，取出來會是List<Courses>，但相同課程名稱學分必定相同，所以使用get(0)取第一筆資料即可
			totalCredit += coursesDao.findByName(selectedCourseName).get(0).getCredit();
		}
		// 已選總學分加上此次加選的學分
		totalCredit += coursesDao.findByName(reqCourseName).get(0).getCredit();

		if (totalCredit > 10) {
			return new EnrollmentsResponse("已達學分數上限，加選失敗。");
		}
		/* 計算總學分有沒有超過10==================================================== */

		// 將enrollmentId寫入enrollment以存入資料庫
		enrollment.setEnrollmentId(enrollmentId);
		enrollmentsDao.save(enrollment);
		return new EnrollmentsResponse(reqStudent, reqCourseName, "加選成功");
	}

	@Override
	public EnrollmentsResponse delEnrollments(EnrollmentsRequest request) {

		if (request == null || request.getEnrollment() == null || request.getEnrollment().getStudentId() == null
				|| request.getEnrollment().getCourseName() == null) {
			return new EnrollmentsResponse("請重新確認輸入。");
		}

		String studentId = request.getEnrollment().getStudentId();
		String courseName = request.getEnrollment().getCourseName();
		String enrollmentId = studentId + "_" + courseName;
		if (!enrollmentsDao.existsById(enrollmentId)) {
			return new EnrollmentsResponse("查無此選課紀錄。");
		}
		enrollmentsDao.deleteById(enrollmentId);
		return new EnrollmentsResponse("退選成功。");
	}

	@Override
	public List<Enrollments> getEnrollments() {
		return enrollmentsDao.findAll();
	}

}
