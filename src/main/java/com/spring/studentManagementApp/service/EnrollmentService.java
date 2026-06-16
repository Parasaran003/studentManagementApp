package com.spring.studentManagementApp.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.spring.studentManagementApp.dto.EnrollmentDTO;
import com.spring.studentManagementApp.dto.EnrollmentSummaryDTO;

public interface EnrollmentService {
	void enrollStudentToCourses(EnrollmentDTO enrollmentDTO);
	Page<EnrollmentSummaryDTO> getEnrolledStudents(int page, int size);
	EnrollmentSummaryDTO findEnrolledStudentCourseDetails(Long studentId);
	List<EnrollmentSummaryDTO> getRecentlyEnrolledStudents();
}
