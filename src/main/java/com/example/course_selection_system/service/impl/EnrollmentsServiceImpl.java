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
import com.example.course_selection_system.repository.StudentsDao;
import com.example.course_selection_system.service.ifs.EnrollmentsService;
import com.example.course_selection_system.vo.request.EnrollmentsRequest;
import com.example.course_selection_system.vo.response.EnrollmentsResponse;

@Service
public class EnrollmentsServiceImpl implements EnrollmentsService {

	@Autowired
	private EnrollmentsDao enrollmentsDao;

	@Autowired
	private CoursesDao coursesDao;

	@Autowired
	private StudentsDao studentDao;

	@Override
	public EnrollmentsResponse newEnrollments(EnrollmentsRequest request) {

		if (request == null || request.getCourseList() == null || request.getStudentId() == null) {
			return new EnrollmentsResponse("請重新確認輸入。");
		}
		// 本次選課的學生id
		String reqStudent = request.getStudentId();

		if (!StringUtils.hasText(reqStudent)) {
			return new EnrollmentsResponse("學生id不得為空。 ");
		}
		// 判斷此學生id是否存在資料庫
		if (!studentDao.existsById(reqStudent)) {
			return new EnrollmentsResponse("學生id不存在。 ");
		}

		// 本次選課的課程名稱list
		List<String> courseNameList = request.getCourseList();
		List<Enrollments> enrollmentList = enrollmentsDao.findByStudentId(reqStudent);

		// 對courseList進行判斷
		for (String courseName : courseNameList) {
			// 判斷課程名稱是否存在
			if (CollectionUtils.isEmpty(coursesDao.findByName(courseName))) {
				return new EnrollmentsResponse(courseName + "課程不存在。 ");
			}
			// 限制一個課程至多3人選修
			if (enrollmentsDao.findByCourseName(courseName).size() >= 3) {
				return new EnrollmentsResponse("課程" + courseName + "人數已滿。 ");
			}
			// 取出此學生id的選課資料
			for (Enrollments enrillment : enrollmentList) {
				// 判斷是否重複選課
				if (courseName.equals(enrillment.getCourseName())) {
					return new EnrollmentsResponse("學號" + reqStudent + "已選擇過" + courseName + "課程");
				}
			}
		}
		// 上面已經判斷完學生id和課程名稱沒有異常

		// 下面判斷選課内容是否衝堂
		// 先將以前選的課程名稱加入list
		for (Enrollments enrillment : enrollmentList) {
			courseNameList.add(enrillment.getCourseName());
		}
		// 呼叫内部方法判斷
		String checkResult = this.checkOneStudentCourseTimeAndCredit(courseNameList);
		if (!checkResult.equals("success")) {
			return new EnrollmentsResponse(checkResult);
		}

		List<Enrollments> newEnrollmentList = new ArrayList<>();

		for (String courseName : courseNameList) {
			Enrollments item = new Enrollments(reqStudent, courseName);
			item.setEnrollmentId(reqStudent + "_" + courseName);
			newEnrollmentList.add(item);
		}

		enrollmentsDao.saveAll(newEnrollmentList);
		return new EnrollmentsResponse(newEnrollmentList, "加選成功");

//
//		for (Enrollments enrollment : enrollmentList) {
//			// 本次選課資料
////			Enrollments enrollment = request.getEnrollment();
//			// 本次選課的學生id
//			String reqStudent = enrollment.getStudentId();
//			// 本次選課的課程名稱
//			String reqCourseName = enrollment.getCourseName();
//			// 本次選課的課程
//			List<Courses> reqCourse = coursesDao.findByName(reqCourseName);
//
//			if (!StringUtils.hasText(reqStudent) || !StringUtils.hasText(reqCourseName)) {
//				return new EnrollmentsResponse("學生id或課程名稱不得為空。 ");
//			}
//			// 判斷此學生id是否存在
//			if (!studentDao.existsById(reqStudent)) {
//				return new EnrollmentsResponse("學生id不存在。 ");
//			}
//			// 判斷課程名稱是否存在
//			if (CollectionUtils.isEmpty(coursesDao.findByName(reqCourseName))) {
//				return new EnrollmentsResponse("課程不存在。 ");
//			}
//			// 限制一個課程至多3人選修
//			if (enrollmentsDao.findByCourseName(reqCourseName).size() >= 3) {
//				return new EnrollmentsResponse("課程" + reqCourseName + "人數已滿。 ");
//			}
//
//			// 建立本次選課編碼
//			String enrollmentId = reqStudent + "_" + reqCourseName;
//			// 判斷此學生有沒有選過這堂課
//			if (enrollmentsDao.existsById(enrollmentId)) {
//				return new EnrollmentsResponse("學號" + reqStudent + "已選擇過此課程");
//			}
//
//			// 取出此學生id的選課紀錄
//			List<Enrollments> reqStudentAllEnrollment = enrollmentsDao.findByStudentId(reqStudent);
//
//			// 存放此學生id已選的課程名稱，使用此筆資料進到course資料庫取資料
//			List<String> selectedCourseNameList = new ArrayList<>();
//
//			for (Enrollments item : reqStudentAllEnrollment) {
//				String selectedCourse = item.getCourseName();
//				selectedCourseNameList.add(selectedCourse);
//				/* 判斷是否衝堂==================================================================== */
//				for (Courses course : coursesDao.findByName(selectedCourse)) {
//					for (Courses thisCourse : reqCourse) {
//						// 判斷是否同一個星期
//						if (course.getWeek() != thisCourse.getWeek()) {
//							continue;
//						}
//						// 判斷時間是否重疊
//						// thisCourse:本次加選的課程 course資料庫取出的已選課程
//						if (thisCourse.getStartTime().compareTo(course.getEndTime()) <= 0
//								&& thisCourse.getEndTime().compareTo(course.getStartTime()) <= 0) {
//							continue;
//						}
//						return new EnrollmentsResponse(thisCourse + "和" + selectedCourse + "衝堂，無法加選");
//					}
//
//				}
//				/* 判斷是否衝堂==================================================================== */
//			}
//
//			/* 計算總學分有沒有超過10==================================================== */
//			int totalCredit = 0;
//			for (String selectedCourseName : selectedCourseNameList) {
//				// 因不是使用主鍵查詢，取出來會是List<Courses>，但相同課程名稱學分必定相同，所以使用get(0)取第一筆資料即可
//				totalCredit += coursesDao.findByName(selectedCourseName).get(0).getCredit();
//			}
//			// 已選總學分加上此次加選的學分
//			totalCredit += coursesDao.findByName(reqCourseName).get(0).getCredit();
//
//			if (totalCredit > 10) {
//				return new EnrollmentsResponse("已達學分數上限，加選失敗。");
//			}
//			/* 計算總學分有沒有超過10==================================================== */
//			
//		}

	}

	@Override
	public EnrollmentsResponse delEnrollments(EnrollmentsRequest request) {

		if (request == null || request.getCourseList() == null || request.getStudentId() == null) {
			return new EnrollmentsResponse("請重新確認輸入。");
		}
		String studentId = request.getStudentId();
		String errormessage = "";
		List<String> delCourseList = request.getCourseList();
		List<Enrollments> selectedList = enrollmentsDao.findByStudentId(studentId);
		if (CollectionUtils.isEmpty(selectedList)) {
			return new EnrollmentsResponse("學生id不存在或尚未選修任何課程");
		}
		List<Enrollments> delEnrollmentList = new ArrayList<>();
		boolean contain = false;
		for (String delCourse : delCourseList) {
			for (Enrollments selected : selectedList) {
				if (delCourse.equals(selected.getCourseName())) {
					// 放進List中待比較
					delEnrollmentList.add(selected);
					contain = true;
					break;
				}
			}
			// 若遍歷時delCourseList有任何selectedList沒有的課程名稱則回傳
			if (!contain) {
				errormessage += "你未選修" + delCourse + "課程。 ";
			}
			contain = false;
		}
		// 比較已選清單是否包含待刪除清單的所有項目
		if (!selectedList.containsAll(delEnrollmentList)) {
			return new EnrollmentsResponse("刪除名單中有未選修的課程。 ");
		}
		if (StringUtils.hasText(errormessage)) {
			return new EnrollmentsResponse(errormessage);
		}
		enrollmentsDao.deleteAll(delEnrollmentList);
		return new EnrollmentsResponse("退選成功。");
	}

	@Override
	public List<Enrollments> getEnrollments() {
		return enrollmentsDao.findAll();
	}

	// 確認沒問題則回傳true，學分超過上限或衝堂則回傳false
	private String checkOneStudentCourseTimeAndCredit(List<String> courseNameList) {
		int totalCredit = 0;
		for (String courseName : courseNameList) {
			totalCredit += coursesDao.findByName(courseName).get(0).getCredit();
		}
		if (totalCredit > 10) {
			return "學分超過上限";
		}
		// 使用courseNameList到course資料庫取出各課程時間進行比較判斷是否衝堂
		for (int i = 0; i < courseNameList.size(); i++) {
			String courseName = courseNameList.get(i);
			List<Courses> courseList = coursesDao.findByName(courseName);
			for (int j = i + 1; j < courseNameList.size(); j++) {
				String otherCourseName = courseNameList.get(j);
				List<Courses> otherCourseList = coursesDao.findByName(otherCourseName);
				for (Courses course : courseList) {
					for (Courses otherCourse : otherCourseList) {
						if (course.getWeek() != otherCourse.getWeek()) {
							continue;
						}
						if (course.getStartTime().before(otherCourse.getEndTime())
								&& course.getEndTime().after(otherCourse.getStartTime())) {
							return "課程:" + courseName + "，和課程:" + otherCourseName + "衝堂";
						}
					}
				}
			}
		}
		return "success";
	}

}
