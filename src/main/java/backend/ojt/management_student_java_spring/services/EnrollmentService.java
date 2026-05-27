package backend.ojt.management_student_java_spring.services;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.ojt.management_student_java_spring.domain.entity.Enrollment;
import backend.ojt.management_student_java_spring.repositories.CourseRepository;
import backend.ojt.management_student_java_spring.repositories.EnrollmentRepository;
import backend.ojt.management_student_java_spring.repositories.StudentRepository;
import backend.ojt.management_student_java_spring.utils.SecurityUtils;
import backend.ojt.management_student_java_spring.utils.constains.EnrollmentStatus;
import backend.ojt.management_student_java_spring.utils.constains.UserStatus;
import backend.ojt.management_student_java_spring.utils.exceptions.BadDataException;
import backend.ojt.management_student_java_spring.utils.exceptions.DenyException;
import backend.ojt.management_student_java_spring.utils.exceptions.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class EnrollmentService {
    final EnrollmentRepository enrollmentRepository;
    final CourseRepository courseRepository;
    final StudentRepository studentRepository;

    /**
     * enroll course
     * 
     * @param courseId
     */
    @Transactional
    public void enroll(Long courseId) {
        // student account must exist
        var userId = SecurityUtils.getCurrentUserIdOrNull();
        if (userId == null) {
            throw new DenyException("Students are not logged in!");
        }
        var student = this.studentRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Student not found!"));
        // account student active
        if (student.getUser().getStatus() != UserStatus.ACTIVE) {
            throw new DenyException("Student account inactive!");
        }
        // course must exist
        var course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found!"));
        // student only enroll course 1 turn
        var isEnrolled = this.enrollmentRepository.existsByStudentUserIdAndCourseId(userId, courseId);
        if (isEnrolled) {
            throw new BadDataException("Students have registered for this course!");
        }
        // deadline enroll
        var now = LocalDateTime.now();
        if (now.isAfter(course.getEnrollEndDate())) {
            throw new DenyException("The course registration deadline has passed!");
        }
        // check slot available
        if (course.getCurrentStudents() >= course.getMaxStudents()) {
            throw new BadDataException("The maximum number of registrations for this course has been reached!");
        }
        // total redits 18/semester
        int totalCredits = this.enrollmentRepository.totalCredits(userId, course.getSemester(), course.getYear(),
                EnrollmentStatus.ENROLLED);
        if (totalCredits + course.getCredits() >= 18) {
            throw new DenyException(
                    "Maximum 18 credits per semester!");
        }
        // save enroll
        try {
            var enroll = new Enrollment();
            enroll.setCourse(course);
            enroll.setStudent(student);
            enroll.setStatus(EnrollmentStatus.ENROLLED);
            this.enrollmentRepository.saveAndFlush(enroll);
            // concurrent
            var updated = this.courseRepository.updateSlotStudents(courseId, course.getVersion());
            if (updated <= 0) {
                throw new DenyException("Course is full!");
            }
        } catch (DataIntegrityViolationException ex) {
            throw new BadDataException("Already enrolled!");
        }

    }

}
