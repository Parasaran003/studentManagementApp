package com.spring.studentManagementApp.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.studentManagementApp.controller.EnrollmentController;
import com.spring.studentManagementApp.dao.CourseDAO;
import com.spring.studentManagementApp.dao.EnrollmentDAO;
import com.spring.studentManagementApp.dao.StudentDAO;
import com.spring.studentManagementApp.dto.DashboardStatsDTO;
import com.spring.studentManagementApp.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {
	
	private static final Logger log = LoggerFactory.getLogger(EnrollmentController.class);
	
	@Autowired
    private EnrollmentDAO enrollmentDAO;
	@Autowired
    private StudentDAO studentDAO;
	@Autowired
	private CourseDAO courseDAO;

	@Override
	public DashboardStatsDTO getDashboardStats() {
		long totalStudents = studentDAO.count();
		long totalCourse = courseDAO.count();
		String topPerformingCourse = getTopPerformingCourse();
		
		YearMonth currentMonth = YearMonth.now();
		LocalDate startDay = currentMonth.atDay(1);
		LocalDate endDay = currentMonth.atEndOfMonth();			
		LocalDateTime startDate = startDay.atStartOfDay();
		LocalDateTime endDate = endDay.atTime(LocalTime.MAX);
		
		long studentEnrolledThisMonth = enrollmentDAO.countDistinctStudentByEnrollDateBetween(startDate, endDate);
		
		DashboardStatsDTO dashboardStatsDTO = new DashboardStatsDTO();
		dashboardStatsDTO.setTotalStudents(totalStudents);
		dashboardStatsDTO.setTotalCourses(totalCourse);
		dashboardStatsDTO.setTopPerformingCourse(topPerformingCourse);
		dashboardStatsDTO.setStudentsEnrolledThisMonth(studentEnrolledThisMonth);
		
		return dashboardStatsDTO;
	}
	
	@Override
	public Map<String, Long> getChartData() {
	    return enrollmentDAO.findAll()
	            .stream()
	            .collect(Collectors.groupingBy(e -> e.getCourse().getCourseName(), Collectors.counting()));
	}

	private String getTopPerformingCourse() {
		
		return enrollmentDAO.findAll()
				.stream()
				.collect(Collectors.groupingBy(e -> e.getCourse().getCourseName(), Collectors.counting()))
				.entrySet()
				.stream()
				.max(Map.Entry.comparingByValue()) //(java, 6) (angular, 5) (react, 2)
				.map(Map.Entry::getKey)
				.orElse("N/A");
		
	}
	
}
