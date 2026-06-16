package com.spring.studentManagementApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.studentManagementApp.dto.CourseDTO;
import com.spring.studentManagementApp.exception.GlobalExceptionHandler;
import com.spring.studentManagementApp.service.CourseService;

import jakarta.validation.Valid;

import org.springframework.ui.*;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/course")
public class CourseController {
	
	private static final Logger log = LoggerFactory.getLogger(CourseController.class);

	
	@Autowired
	private CourseService courseService;

	@GetMapping("/new")
	public String showCreateCourse(Model model) {
		log.info("/course/new - Is showing successfully.");
		model.addAttribute("courseDto", new CourseDTO());
		return "add-course";
	}
	
	@GetMapping("/list")
	public String listCourses(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size,Model model
			,@RequestParam(value = "message", required = false) String message) {
		log.info("/course/list - List of courses is called successfully.");
		Page<CourseDTO> courses = courseService.getCourses(page,size);
		model.addAttribute("courses",courses);
		model.addAttribute("message",message);
		return "courses";
	}
	
	@PostMapping("/list")
	public String createCourse(@Valid @ModelAttribute("courseDto") CourseDTO courseDTO,BindingResult bindingResult,Model model,RedirectAttributes redirectAttributes) {
		log.info("/course/list - Create course action started successfully.");
		if(bindingResult.hasErrors()) {
			log.error("/course/list - Create course action failed due to validation error "+bindingResult.getFieldErrors());
			return "add-course";
		}
		if(courseService.existsByCourseCode(courseDTO.getCourseCode())) {
			log.error("/course/list - Course Code must be unique.");
			bindingResult.rejectValue("courseCode", "error.courseCode", "Course code must be unique.");
			return "add-course";
		}
		courseService.createCourse(courseDTO);
		redirectAttributes.addAttribute("message", "Course created successfully.");
		log.info("/course/list - Create course action completed successfully.");
		return "redirect:/course/list";
	}
	@GetMapping("/{id}")
	public String getCourseById(@PathVariable("id") Long id,Model model) {
		log.info("/course/id - Get course by Id method called started successfully.");
		CourseDTO course = courseService.getCourseById(id);
		log.info("/course/id - Course - "+course.getCourseCode()+" Found!");
		model.addAttribute("course",course);
		return "view-course";
	}
	@GetMapping("/{id}/edit")
	public String editCourseById(@PathVariable("id") Long id,Model model) {
		log.info("/course/id/edit - Edit course by Id method called started successfully.");
		CourseDTO course = courseService.getCourseById(id);
		model.addAttribute("courseDto",course);
		return "edit-course";
	}
	@PostMapping("/updateCourse/{id}")
	public String updateCourse(@Valid @ModelAttribute("courseDto") CourseDTO courseDTO,@PathVariable("id") Long id,BindingResult bindingResult,Model model,RedirectAttributes redirectAttributes) {
		log.info("/course/list - Update course action started successfully.");
		
		if(bindingResult.hasErrors()) {
			log.error("/course/list - Update course action failed due to validation error "+bindingResult.getFieldErrors());
			return "edit-course";
		}
		
		if(courseService.existsByCourseCodeIgnoreCaseAndIdNot(courseDTO.getCourseCode(),id)) {
			log.error("/course/list - Course Code must be unique.");
			bindingResult.rejectValue("courseCode", "error.courseCode", "Course code must be unique.");
			return "edit-course";
		}
		
		courseService.updateCourse(id, courseDTO);
		
		redirectAttributes.addAttribute("message", "Course updated successfully.");
		
		log.info("/course/list - Update course action completed successfully.");
		
		return "redirect:/course/list";
	}
}
