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

import com.spring.studentManagementApp.controller.StudentController;
import com.spring.studentManagementApp.dao.StudentDAO;
import com.spring.studentManagementApp.dto.StudentDTO;
import com.spring.studentManagementApp.model.Students;
import com.spring.studentManagementApp.service.StudentService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService{
	
	private static final Logger log = LoggerFactory.getLogger(StudentController.class);

	@Autowired 
	private StudentDAO studentDAO;
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public boolean existsByEmailIgnoreCase(String email) {		
		return studentDAO.existsByEmailIgnoreCase(email);
	}
	
	@Override
	public StudentDTO createStudent(StudentDTO studentDTO) {
		log.info("Create Course Saving Method Called For "+studentDTO.getEmail());
		
		Students students = mapper.map(studentDTO, Students.class);
		
		studentDAO.save(students);
		
		log.info("student saved successfully.");
		
		return mapper.map(students, StudentDTO.class);
	}
	
	@Override
	public Page<StudentDTO> getStudents(int page, int size) {
		log.info("Get Active Courses method Called");
		
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Direction.ASC,"id"));	
		
		return studentDAO.findByActiveTrue(pageRequest)
				.map(students -> mapper.map(students, StudentDTO.class));
	}
	@Override
	public StudentDTO getStudentById(Long id) {
		Students student = studentDAO.findById(id)
		.orElseThrow(() -> new RuntimeException("No course found"));
		
		return mapper.map(student, StudentDTO.class);
	}
	
	@Override
	public boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id) {
		log.info("Email exists validation using id method called for "+email);
		
		return studentDAO.existsByEmailIgnoreCaseAndIdNot(email,id);
	}
	@Override
	public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
		Students student = studentDAO.findById(id)
				.orElseThrow(() -> new RuntimeException("No student found"));
		
		mapper.map(studentDTO, student);
		
		Students updatedStudent = studentDAO.save(student);
		
		return mapper.map(updatedStudent, StudentDTO.class);
	}

	@Override
	public List<StudentDTO> getAllStudents() {
		return studentDAO.findByActiveTrue().stream()
				.map(student -> mapper.map(student, StudentDTO.class))
				.collect(Collectors.toList());
	}
}
