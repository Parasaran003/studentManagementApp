package com.spring.studentManagementApp.service;

import java.util.Map;

import com.spring.studentManagementApp.dto.DashboardStatsDTO;

public interface DashboardService {
	DashboardStatsDTO getDashboardStats();
	Map<String, Long> getChartData();
}
