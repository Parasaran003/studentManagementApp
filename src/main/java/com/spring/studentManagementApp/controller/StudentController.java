package com.spring.studentManagementApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.studentManagementApp.dto.CourseDTO;
import com.spring.studentManagementApp.dto.StudentDTO;
import com.spring.studentManagementApp.model.Courses;
import com.spring.studentManagementApp.service.StudentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);
    
    @Autowired
	private StudentService studentService;

    @GetMapping("/new")
    public String showCreateStudent(Model model) {
        log.info("Displaying Add Student form");
        model.addAttribute("studentDto", new StudentDTO());
        return "add-student";
    }
    @GetMapping("/list")
    public String listStudent(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size,Model model
			,@RequestParam(value = "message", required = false) String message) {
        log.info("/students/list - List of courses is called successfully.");
		Page<StudentDTO> students = studentService.getStudents(page,size);
		model.addAttribute("students",students);
		model.addAttribute("message",message);
		return "students";
    }
    @PostMapping("/list")
	public String createStudent(@Valid @ModelAttribute("studentDto") StudentDTO studentDTO,BindingResult bindingResult,Model model,RedirectAttributes redirectAttributes) {
		log.info("/course/list - Create course action started successfully.");
		if(bindingResult.hasErrors()) {
			log.error("/students/list - Create course action failed due to validation error "+bindingResult.getFieldErrors());
			return "add-student";
		}
		if(studentService.existsByEmailIgnoreCase(studentDTO.getEmail())) {
			log.error("/students/list - Email must be unique.");
			bindingResult.rejectValue("email", "error.email", "Email must be unique.");
			return "add-student";
		}
		studentService.createStudent(studentDTO);
		redirectAttributes.addAttribute("message", "Student created successfully.");
		log.info("/students/list - Create student action completed successfully.");
		return "redirect:/students/list";
	}
    @GetMapping("/{id}")
	public String getStudentById(@PathVariable("id") Long id,Model model) {
		log.info("/students/id - Get course by Id method called started successfully.");
		StudentDTO student = studentService.getStudentById(id);
		log.info("/student/id - Course - "+student.getId()+" Found!");
		model.addAttribute("student",student);
		return "view-student";
	}
    @GetMapping("/{id}/edit")
	public String editStudentById(@PathVariable("id") Long id,Model model) {
		log.info("/students/id/edit - Edit course by Id method called started successfully.");
		StudentDTO student = studentService.getStudentById(id);
		model.addAttribute("student",student);
		return "edit-student";
	}
    @PostMapping("/updateStudent/{id}")
	public String updateStudent(@Valid @ModelAttribute("student") StudentDTO studentDTO,@PathVariable("id") Long id,BindingResult bindingResult,Model model,RedirectAttributes redirectAttributes) {
		log.info("/students/list - Update course action started successfully.");
		
		if(bindingResult.hasErrors()) {
			log.error("/course/list - Update course action failed due to validation error "+bindingResult.getFieldErrors());
			return "edit-student";
		}
		
		if(studentService.existsByEmailIgnoreCaseAndIdNot(studentDTO.getEmail(),id)) {
			log.error("/students/list - Email Id must be unique.");
			bindingResult.rejectValue("email", "error.email", "Email must be unique.");
			return "edit-student";
		}
		
		studentService.updateStudent(id, studentDTO);
		
		redirectAttributes.addAttribute("message", "Student updated successfully.");
		
		log.info("/students/list - Update course action completed successfully.");
		
		return "redirect:/students/list";
	}
}
