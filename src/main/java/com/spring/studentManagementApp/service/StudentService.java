package com.spring.studentManagementApp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import com.spring.studentManagementApp.dto.StudentDTO;

public interface StudentService {
	boolean existsByEmailIgnoreCase(String email);
	StudentDTO createStudent(StudentDTO studentDTO);
	Page<StudentDTO> getStudents(int page,int size);
	StudentDTO getStudentById(Long id);
	public boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
	public StudentDTO updateStudent(Long id, StudentDTO studentDTO);
	List<StudentDTO> getAllStudents();
}
