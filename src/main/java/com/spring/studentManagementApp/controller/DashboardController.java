package com.spring.studentManagementApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.spring.studentManagementApp.service.DashboardService;
import com.spring.studentManagementApp.service.EnrollmentService;

@Controller
public class DashboardController {
	private static final Logger log = LoggerFactory.getLogger(EnrollmentController.class);

	@Autowired
	private EnrollmentService enrollmentService;
	
	@Autowired
	private DashboardService dashboardService;

	@GetMapping("/dashboard")
	public String dashboard(Model model) {
	    model.addAttribute("dashboardStats", dashboardService.getDashboardStats());
	    model.addAttribute("students", enrollmentService.getRecentlyEnrolledStudents());
	    model.addAttribute("chartData", dashboardService.getChartData()); // New Chart Data
	    return "dashboard";
	}
}
