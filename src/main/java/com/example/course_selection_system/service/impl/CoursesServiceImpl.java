package com.example.course_selection_system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

		for (int i = 0; i < reqList.size(); i++) {
			Courses item = reqList.get(i);
			if (item.getCourseId() == null) {
				errorList.add(item);
				errorMessage += "id為空";
				continue;
			}
			if (coursesDao.existsById(item.getCourseId())) {
				errorList.add(item);
				errorMessage += item.getCourseId() + "已存在。 ";
				continue;
			}
			// 丟入内部方法判斷格式是否正確
			if (!this.checkCourseFormat(item).equals("success")) {
				errorList.add(item);
				errorMessage += this.checkCourseFormat(item);
				continue;
			}
			// 排除同一筆輸入的相同課程名稱之時間設定問題
			for (int j = i + 1; j < reqList.size(); j++) {
				Courses otherItem = reqList.get(j);
				if (!twoCourseWeekAndTimeOk(item, otherItem)) {
					return new CoursesResponse("請重新確認輸入。");
				}
			}
			List<Courses> course = coursesDao.findByName(item.getName());
			for (Courses dbItem : course) {
				if (!twoCourseWeekAndTimeOk(item, dbItem)) {
					/**/ return new CoursesResponse("與資料庫資料衝突。");
				}
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
			if (!coursesDao.existsById(item.getCourseId())) {
				errorList.add(item);
				errorMessage += item.getCourseId() + "不存在。 ";
				continue;
			}
			String checkResult = this.checkCourseFormat(item);
			if (!checkResult.equals("success")) {
				errorList.add(item);
				errorMessage += checkResult;
				continue;
			}
			// 存入選課紀錄待修改List
			upEnrollmentList.add(enrollmentsDao.findByCourseName(item.getName()));
		}

//		for (List<Enrollments> upList : upEnrollmentList) {
//			/*********************************** 尚未判斷更新後衝堂，學分超過等問題**************************************/
//			for(Enrollments upItem : upList) {
//				upItem.getCourseName();
//			}
//			enrollmentsDao.saveAll(upList);
//		}
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
		List<List<Courses>> courseDelList = new ArrayList<>();

		String errorMessage = "";
		if (CollectionUtils.isEmpty(reqList)) {
			return new CoursesResponse("請重新確認輸入。");
		}
		for (Courses course : reqList) {
			String reqName = course.getName();
			if (!StringUtils.hasText(reqName)) {
				errorMessage = errorMessage + "名稱不得為空。 ";
				errorList.add(course);
				continue;
			}
			List<Courses> delCourse = coursesDao.findByName(reqName);
			if (CollectionUtils.isEmpty(delCourse)) {
				errorMessage = errorMessage + reqName + "不存在。 ";
				errorList.add(course);
				continue;
			}
			String reqId = course.getCourseId();

			// request中有課程id，就只將該課程id加入代刪除List並continue
			if (StringUtils.hasText(reqId)) {
				Optional<Courses> courseById = coursesDao.findById(reqId);
				if (!courseById.isPresent()) {
					errorMessage = errorMessage + reqId + "不存在。 ";
					errorList.add(course);
					continue;
				}
				if (!delCourse.contains(courseById.get())) {
					errorMessage = errorMessage + "課程id與課程名稱不符。 ";
					errorList.add(course);
					continue;
				}
				List<Courses> delById = Arrays.asList(courseById.get());
				courseDelList.add(delById);
				// 若此課程id為此課程的最後一個課程id，也要將選課紀錄一起進行刪除
				if (delCourse.size() == 1) {
					enrollmentDelList.add(enrollmentsDao.findByCourseName(reqName));
				}
				continue;
			}
			// 取出該課程的選課資料加入待刪除List
			enrollmentDelList.add(enrollmentsDao.findByCourseName(reqName));
			courseDelList.add(delCourse);

		}
		if (!CollectionUtils.isEmpty(errorList)) {
			return new CoursesResponse("發生錯誤 " + errorMessage);
		}

		// 刪除該課程的所有選課資料
		for (List<Enrollments> delList : enrollmentDelList) {
			enrollmentsDao.deleteAll(delList);
		}
		String delMessage = "";
		// 刪除該課程
		for (List<Courses> delCourse : courseDelList) {
			for (Courses item : delCourse) {
				delMessage += item.getCourseId() + ",";
			}
			coursesDao.deleteAll(delCourse);
		}
		return new CoursesResponse(delMessage + "刪除成功。");
	}

	@Override
	public List<Courses> getCourses() {
		return coursesDao.findAll();
	}

	// 檢查Courses是否符合格式並回傳相應字串訊息
	private String checkCourseFormat(Courses course) {

		String reqId = course.getCourseId();
		String reqName = course.getName();

		// 課程id格式限制為1到5個英文字加上4個數字
		String patternId = "[A-Z][a-z]{1,4}\\d{4}";
		// 課程名稱格式限制為2到5個中文字
		String patternName = "[\\u4e00-\\u9fa5]{2,5}";

		if (!StringUtils.hasText(reqName) || !StringUtils.hasText(reqId)) {
			return "id或姓名不得為空。 ";
		}
		// 學生id格式限制
		if (!reqId.matches(patternId)) {
			return reqId + ":id格式錯誤。 ";
		}
		// 課程名稱格式限制
		if (!reqName.matches(patternName)) {
			return reqId + ":課程名稱錯誤。 ";
		}
		// 判斷星期，設定為1~7，以外的數字為格式錯誤。
		if (course.getWeek() < 1 || course.getWeek() > 7) {
			return reqId + ":星期設定錯誤。 ";
		}
		// 判斷時間，開始時間不能在結束時間之後
		if (course.getStartTime() == null || course.getEndTime() == null
				|| course.getStartTime().compareTo(course.getEndTime()) >= 0) {
			return reqId + ":時間設定錯誤。 ";
		}
		// 學分限制在1~3分
		if (course.getCredit() < 1 || course.getCredit() > 3) {
			return reqId + ":學分設定錯誤。 ";
		}
		return "success";
	}

	// 確認沒問題則回傳true，學分超過上限或衝堂則回傳false
	private boolean twoCourseWeekAndTimeOk(Courses course, Courses otherCourse) {

		// 名稱不一樣則不會衝突，回傳true
		if (!course.getName().equals(otherCourse.getName())) {
			return true;
		}
		// 限制同名稱的課程，課程id要相符
		if (course.getCourseId().equals(otherCourse.getCourseId())) {
			return false;
		}
		if (course.getCourseId().equals(otherCourse.getCourseId())) {
			return false;
		}
		// 學分不相同則衝突，回傳false
		if (course.getCredit() != otherCourse.getCredit()) {
			return false;
		}
		// 星期不一樣則不會衝突，回傳true
		if (course.getWeek() != otherCourse.getWeek()) {
			return true;
		}
		// 時間衝突則回傳false
		if (course.getStartTime().before(otherCourse.getEndTime())
				&& course.getEndTime().after(otherCourse.getStartTime())) {
			return false;
		}
		return true;
	}

	@Override
	public List<Courses> findCoursesByName(String courseName) {
		return coursesDao.findByName(courseName);
	}

	@Override
	public List<Courses> findCoursesByCourseId(String courseId) {
		return coursesDao.findByCourseId(courseId);
	}

}
