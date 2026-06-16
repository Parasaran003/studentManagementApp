package com.spring.studentManagementApp.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.spring.studentManagementApp.controller.CourseController;
import com.spring.studentManagementApp.dao.CourseDAO;
import com.spring.studentManagementApp.dto.CourseDTO;
import com.spring.studentManagementApp.model.Courses;
import com.spring.studentManagementApp.service.CourseService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
	
	private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

	@Autowired
	private CourseDAO courseDAO;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public boolean existsByCourseCode(String code) {
		log.info("Course Code Validation Method Called For "+code);
		
		return courseDAO.existsByCourseCodeIgnoreCase(code);
	}

	@Override
	public boolean existsByCourseCodeIgnoreCaseAndIdNot(String code, Long id) {
		log.info("Course exists validation using code and id method called for "+code);
		
		return courseDAO.existsByCourseCodeIgnoreCaseAndIdNot(code,id);
	}

	@Override
	public CourseDTO createCourse(CourseDTO courseDTO) {
		log.info("Create Course Saving Method Called For "+courseDTO.getCourseCode());
		
		Courses courses = mapper.map(courseDTO, Courses.class);
		
		courseDAO.save(courses);
		
		log.info("Course saved successfully.");
		
		return mapper.map(courses, CourseDTO.class);
	}

	@Override
	public Page<CourseDTO> getCourses(int page, int size) {
		log.info("Get Active Courses method Called");
		
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Direction.ASC,"id"));	
		
		return courseDAO.findByActiveTrue(pageRequest)
				.map(course -> mapper.map(course, CourseDTO.class));
	}

	@Override
	public CourseDTO getCourseById(Long id) {
		Courses course = courseDAO.findById(id)
		.orElseThrow(() -> new RuntimeException("No course found"));
		
		return mapper.map(course, CourseDTO.class);
	}

	@Override
	public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
		Courses course = courseDAO.findById(id)
				.orElseThrow(() -> new RuntimeException("No course found"));
		
		mapper.map(courseDTO, course);
		
		Courses updatedCourse = courseDAO.save(course);
		
		return mapper.map(updatedCourse, CourseDTO.class);
	}

	@Override
	public List<CourseDTO> getAllCourses() {
		return courseDAO.findByActiveTrue(Sort.by("courseName")).stream()
				.map(course -> mapper.map(course, CourseDTO.class))
				.collect(Collectors.toList());
	}
	
}
