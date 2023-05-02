package com.example.course_selection_system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.course_selection_system.entity.Courses;
import com.example.course_selection_system.entity.Enrollments;
import com.example.course_selection_system.entity.Students;
import com.example.course_selection_system.repository.CoursesDao;
import com.example.course_selection_system.repository.EnrollmentsDao;
import com.example.course_selection_system.repository.StudentsDao;
import com.example.course_selection_system.service.ifs.StudentsService;
import com.example.course_selection_system.vo.request.StudentsRequest;
import com.example.course_selection_system.vo.response.StudentsResponse;

@Service
public class StudentsServiceImpl implements StudentsService {

	@Autowired
	private StudentsDao studentsDao;

	@Autowired
	private EnrollmentsDao enrollmentsDao;

	@Autowired
	private CoursesDao coursesDao;

	@Override
	public StudentsResponse newStudent(StudentsRequest request) {

		if (request == null || CollectionUtils.isEmpty(request.getStudentList())) {
			return new StudentsResponse("請重新確認輸入。");
		}

		List<Students> reqList = request.getStudentList();
		List<Students> errorList = new ArrayList<>();
		String errorMessage = "";

		for (Students item : reqList) {
			String reqId = item.getId();
			String reqName = item.getName();
			// 學生id格式限制為1個大寫英文字加上7個數字
			String patternId = "[A-Z]\\d{7}";

			if (!StringUtils.hasText(reqName) || !StringUtils.hasText(reqId)) {
				errorMessage = errorMessage + "id或姓名不得為空。 ";
				errorList.add(item);
				continue;
			}

			if (!reqId.matches(patternId)) {
				errorMessage = errorMessage + "id" + " 格式錯誤。 ";
				errorList.add(item);
				continue;
			}
			if (reqName.length() > 40) {
				errorMessage = errorMessage + "名稱過長。 ";
				errorList.add(item);
				continue;
			}
			if (studentsDao.existsById(reqId)) {
				errorMessage = errorMessage + reqId + "已存在。 ";
				errorList.add(item);
				continue;
			}

		}

		if (!CollectionUtils.isEmpty(errorList)) {
			return new StudentsResponse(errorList, "發生錯誤 " + errorMessage);
		}

		studentsDao.saveAll(reqList);
		return new StudentsResponse(reqList, "新增成功");
	}

	@Override
	public StudentsResponse updateStudent(StudentsRequest request) {
		if (request == null || CollectionUtils.isEmpty(request.getStudentList())) {
			return new StudentsResponse("請重新確認輸入。");
		}
		List<Students> reqList = request.getStudentList();
		List<Students> errorList = new ArrayList<>();
		String errorMessage = "";

		for (Students item : reqList) {
			String reqId = item.getId();
			String reqName = item.getName();
			if (!StringUtils.hasText(reqName) || !StringUtils.hasText(reqId)) {
				errorMessage = errorMessage + "id或姓名不得為空。 ";
				errorList.add(item);
				continue;
			}
			if (!studentsDao.existsById(reqId)) {
				errorMessage = errorMessage + reqId + "不存在。 ";
				errorList.add(item);
				continue;
			}

		}
		if (!CollectionUtils.isEmpty(errorList)) {
			return new StudentsResponse(errorList, "發生錯誤 " + errorMessage);
		}

		studentsDao.saveAll(reqList);
		return new StudentsResponse(reqList, "更新成功。");
	}

	@Override
	public StudentsResponse delStudent(StudentsRequest request) {
		List<Students> reqList = request.getStudentList();
		List<Students> errorList = new ArrayList<>();

		List<List<Enrollments>> enrollmentsDelList = new ArrayList<>();
		String errorMessage = "";
		if (CollectionUtils.isEmpty(reqList)) {
			return new StudentsResponse("請重新確認輸入。");
		}
		for (Students item : reqList) {
			String reqId = item.getId();
			String reqName = item.getName();
			if (!StringUtils.hasText(reqName) || !StringUtils.hasText(reqId)) {
				errorMessage = errorMessage + "id或姓名不得為空。 ";
				errorList.add(item);
				continue;
			}
			if (!studentsDao.existsById(reqId)) {
				errorMessage = errorMessage + reqId + "不存在。 ";
				errorList.add(item);
				continue;
			}
			// 待刪除的選課紀錄List
			enrollmentsDelList.add(enrollmentsDao.findByStudentId(reqId));

		}
		if (!CollectionUtils.isEmpty(errorList)) {
			return new StudentsResponse(errorList, "發生錯誤 " + errorMessage);
		}
		// 先刪除此學生id的選課資料
		for (List<Enrollments> delList : enrollmentsDelList) {
			enrollmentsDao.deleteAll(delList);
		}
		studentsDao.deleteAll(reqList);
		return new StudentsResponse(reqList, "刪除成功。");
	}

	@Override
	public List<Students> getStudents() {
		return studentsDao.findAll();
	}

	@Override
	public StudentsResponse getEnrollmentsAndCoursesByStudentId(String studentId) {

		Optional<Students> reqStudentOp = studentsDao.findById(studentId);
		if (!reqStudentOp.isPresent()) {
			return new StudentsResponse("學號:" + studentId + "不存在");
		}
		Students reqStudent = reqStudentOp.get();
		List<List<Courses>> selectedCoursesList = new ArrayList<>();

		List<Enrollments> reqEnrollmentList = enrollmentsDao.findByStudentId(studentId);
		for (Enrollments reqEnrollment : reqEnrollmentList) {
			selectedCoursesList.add(coursesDao.findByName(reqEnrollment.getCourseName()));
		}

		return new StudentsResponse(reqStudent, selectedCoursesList, "取得學號:" + studentId + " 的已選課程資料");
	}

}
