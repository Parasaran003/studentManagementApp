package com.spring.studentManagementApp.service.impl;

import java.math.BigDecimal;
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

import com.spring.studentManagementApp.dao.CourseDAO;
import com.spring.studentManagementApp.dao.EnrollmentDAO;
import com.spring.studentManagementApp.dao.StudentDAO;
import com.spring.studentManagementApp.dto.CourseDTO;
import com.spring.studentManagementApp.dto.EnrollmentDTO;
import com.spring.studentManagementApp.dto.EnrollmentSummaryDTO;
import com.spring.studentManagementApp.dto.StudentDTO;
import com.spring.studentManagementApp.model.Courses;
import com.spring.studentManagementApp.model.Enrollment;
import com.spring.studentManagementApp.model.Students;
import com.spring.studentManagementApp.service.EnrollmentService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {
	
	private static final Logger log = LoggerFactory.getLogger(EnrollmentServiceImpl.class);
	@Autowired
    private EnrollmentDAO enrollmentDAO;
	@Autowired
    private StudentDAO studentDAO;
	@Autowired
	private CourseDAO courseDAO;
	@Autowired
	private ModelMapper mapper;
	
	@Override
    public void enrollStudentToCourses(EnrollmentDTO enrollmentDTO) {
        log.info("request from enrollStudentToCourses");

        Students student = studentDAO.findById(enrollmentDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        for(Long courseId : enrollmentDTO.getCourseIds()) {
            Courses course = courseDAO.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("course not found"));

            if(enrollmentDAO.existsByStudentIdAndCourseId(enrollmentDTO.getStudentId(),
                    courseId)) {
                continue;
            }

            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            
            student.getEnrollments().add(enrollment);
            course.getEnrollments().add(enrollment);

            enrollmentDAO.save(enrollment);
        }
    }
	@Override
    public Page<EnrollmentSummaryDTO> getEnrolledStudents(int page, int size) {
        log.info("List of enrolled students from: {}", page);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Direction.DESC, "id"));
        return studentDAO.findEnrolledStudents(pageRequest)
                .map(student -> {
                    EnrollmentSummaryDTO dto = new EnrollmentSummaryDTO();
                    dto.setStudentId(student.getId());
                    dto.setStudentName(student.getFirstName() + " " + student.getLastName());
                    dto.setEmail(student.getEmail());
                    
                    dto.setCourseCount(student.getEnrollments().size());
                    
                    BigDecimal totalFee = student.getEnrollments().stream()
                            .map(enrollment -> enrollment.getCourse().getCourseFee())
                            .filter(fee -> fee != null)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    dto.setTotalFee(totalFee);
                    
                    return dto;
                });
    }
	@Override
	public EnrollmentSummaryDTO findEnrolledStudentCourseDetails(Long studentId) {
		
		return studentDAO.findEnrolledStudentCourseDetails(studentId)
				.map(student -> {
					EnrollmentSummaryDTO dto = new EnrollmentSummaryDTO();
					dto.setStudentId(student.getId());
					dto.setStudentName(student.getFirstName() + " " +student.getLastName());
					dto.setEmail(student.getEmail());
					
					dto.setCourseCount(student.getEnrollments().size());
					BigDecimal totalFee = student.getEnrollments().stream()
							.map(enrollment -> enrollment.getCourse().getCourseFee())
							.filter(fee -> fee != null)
							.reduce(BigDecimal.ZERO, BigDecimal::add);
					dto.setTotalFee(totalFee);
					
					List<CourseDTO> courseList = student.getEnrollments().stream()
							.map(enrollment -> enrollment.getCourse())
							.map(course -> mapper.map(course, CourseDTO.class))
							.collect(Collectors.toList());
					
					dto.setCourseList(courseList);
					
					return dto;
				})
				.orElseThrow(() -> new RuntimeException("Student not found"));
	}
	@Override
	public List<EnrollmentSummaryDTO> getRecentlyEnrolledStudents() {
		log.info("list of recently enrolled students");
		
		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Direction.DESC, "id"));
		return studentDAO.findEnrolledStudents(pageRequest)
				.map(student -> {
					EnrollmentSummaryDTO dto = new EnrollmentSummaryDTO();
					dto.setStudentId(student.getId());
					dto.setStudentName(student.getFirstName() + " " +student.getLastName());
					dto.setEmail(student.getEmail());
					
					dto.setCourseCount(student.getEnrollments().size());
					BigDecimal totalFee = student.getEnrollments().stream()
							.map(enrollment -> enrollment.getCourse().getCourseFee())
							.filter(fee -> fee != null)
							.reduce(BigDecimal.ZERO, BigDecimal::add);
					dto.setTotalFee(totalFee);
					
					return dto;
				})
				.getContent();
	}
}
