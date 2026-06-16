package com.spring.studentManagementApp.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.spring.studentManagementApp.dto.CourseDTO;

public interface CourseService {
	
	CourseDTO createCourse(CourseDTO courseDTO);
	
	boolean existsByCourseCode(String code);
	
	boolean existsByCourseCodeIgnoreCaseAndIdNot(String code,Long id);
	
	Page<CourseDTO> getCourses(int page,int size);
	
	CourseDTO getCourseById(Long id);
	
	CourseDTO updateCourse(Long id,CourseDTO courseDTO);
	
	List<CourseDTO> getAllCourses();
}
