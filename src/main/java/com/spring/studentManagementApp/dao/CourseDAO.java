package com.spring.studentManagementApp.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.spring.studentManagementApp.model.Courses;

@Repository
public interface CourseDAO extends JpaRepository<Courses,Long>{
	boolean existsByCourseCodeIgnoreCase(String code);
	boolean existsByCourseCodeIgnoreCaseAndIdNot(String code,Long id);
	Page<Courses> findByActiveTrue(Pageable pageable);
	List<Courses> findByActiveTrue(Sort sort);
}