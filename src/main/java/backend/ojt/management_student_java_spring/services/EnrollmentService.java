package backend.ojt.management_student_java_spring.services;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.ojt.management_student_java_spring.domain.dto.res.LecturerCourse;
import backend.ojt.management_student_java_spring.domain.dto.res.ResReportEnroll;
import backend.ojt.management_student_java_spring.domain.dto.res.StudentEnrollmentProjection;
import backend.ojt.management_student_java_spring.domain.entity.Enrollment;
import backend.ojt.management_student_java_spring.domain.entity.User;
import backend.ojt.management_student_java_spring.repositories.CourseRepository;
import backend.ojt.management_student_java_spring.repositories.EnrollmentRepository;
import backend.ojt.management_student_java_spring.repositories.StudentRepository;
import backend.ojt.management_student_java_spring.repositories.UserRepository;
import backend.ojt.management_student_java_spring.utils.SecurityUtils;
import backend.ojt.management_student_java_spring.utils.constains.EnrollmentStatus;
import backend.ojt.management_student_java_spring.utils.constains.UserRole;
import backend.ojt.management_student_java_spring.utils.constains.UserStatus;
import backend.ojt.management_student_java_spring.utils.exceptions.AccessToResourceException;
import backend.ojt.management_student_java_spring.utils.exceptions.BadDataException;
import backend.ojt.management_student_java_spring.utils.exceptions.UnauthorizedException;
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
    final UserRepository userRepository;

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
            throw new UnauthorizedException("Students are not logged in!");
        }
        var student = this.studentRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Student not found!"));
        // check student role
        if (student.getUser().getRole() != UserRole.STUDENT) {
            throw new AccessToResourceException("You are not a student!");
        }
        // account student active
        if (student.getUser().getStatus() != UserStatus.ACTIVE) {
            throw new AccessToResourceException("Student account inactive!");
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
            throw new AccessToResourceException("The course registration deadline has passed!");
        }
        // before dead
        if (now.isBefore(course.getEnrollStartDate())) {
            throw new AccessToResourceException("Course enrollment has not started!");
        }
        // check slot available
        if (course.getCurrentStudents() >= course.getMaxStudents()) {
            throw new BadDataException("The maximum number of registrations for this course has been reached!");
        }
        // total redits 18/semester
        int totalCredits = this.enrollmentRepository.totalCredits(userId, course.getSemester(), course.getYear(),
                EnrollmentStatus.ENROLLED);
        if (totalCredits + course.getCredits() > 18) {
            throw new AccessToResourceException(
                    "Maximum 18 credits per semester!");
        }
        // save enroll
        try {
            var enroll = new Enrollment();
            enroll.setCourse(course);
            enroll.setStudent(student);
            enroll.setStatus(EnrollmentStatus.ENROLLED);
            this.enrollmentRepository.save(enroll);
            // concurrent
            var updated = this.courseRepository.updateSlotStudents(courseId, course.getVersion());
            if (updated <= 0) {
                throw new AccessToResourceException("Course is full!");
            }
        } catch (DataIntegrityViolationException ex) {
            throw new BadDataException("Already enrolled!");
        }

    }

    /**
     * report student enroll course by ID
     * 
     * @param courseId
     * @param pageable
     * @return
     **/
    public ResReportEnroll report(Long courseId, Pageable pageable) {
        var userId = SecurityUtils.getCurrentUserIdOrNull();
        if (userId == null) {
            throw new UnauthorizedException("You are not logged in!");
        }
        var user = this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        if (user.getRole().equals(UserRole.ADMIN) || user.getRole().equals(UserRole.LECTURER)) {
            return this.getReportForAuthorizedUser(user, courseId, pageable);
        }
        throw new AccessToResourceException("You do not have permission!");

    }

    /**
     * view the report if you are a lecturer or administrator
     * 
     * @param user
     * @param courseId
     * @param pageable
     * @return
     **/
    private ResReportEnroll getReportForAuthorizedUser(User user, Long courseId, Pageable pageable) {
        var lecturerCourse = this.courseRepository.findResLecturerCourseById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found!"));
        if (user.getRole() == UserRole.LECTURER && !lecturerCourse.getLecturerId().equals(user.getId())) {
            throw new AccessToResourceException("You do not have permission!");
        }
        return this.toResReport(lecturerCourse, pageable);

    }

    /**
     * covert to report enrollment
     * 
     * @param lecturerCourse
     * @param pageable
     * @return
     **/
    private ResReportEnroll toResReport(LecturerCourse lecturerCourse,
            Pageable pageable) {
        var res = new ResReportEnroll();
        // course
        res.setCourseCode(lecturerCourse.getCourseCode());
        res.setCourseId(lecturerCourse.getId());
        res.setCourseName(lecturerCourse.getName());
        res.setCurrentStudents(lecturerCourse.getCurrentStudents());
        res.setMaxStudents(lecturerCourse.getMaxStudents());
        res.setEnrollStartDate(lecturerCourse.getEnrollStartDate());
        res.setEnrollEndDate(lecturerCourse.getEnrollEndDate());
        // lecturer info
        var lecturerInfor = new ResReportEnroll.LecturerInfo();
        lecturerInfor.setCode(lecturerCourse.getLecturerCode());
        lecturerInfor.setId(lecturerCourse.getLecturerId());
        lecturerInfor.setEmail(lecturerCourse.getLecturerEmail());
        lecturerInfor.setName(lecturerCourse.getLecturerName());
        lecturerInfor.setGender(lecturerCourse.getLecturerGender());
        res.setLecturer(lecturerInfor);
        // student enrolled
        var page = this.enrollmentRepository.findStudentEnrollmentByCourseId(lecturerCourse.getId(),
                EnrollmentStatus.ENROLLED,
                pageable);
        res.setPagination(this.toPaginationResReport(pageable, page));
        res.setEnrollments(page.getContent().stream().map(this::toStudentEnrollment).toList());
        return res;
    }

    /**
     * convert to pagination student enrollment at response report
     * 
     * @param pageable
     * @param page
     * @return
     **/
    private ResReportEnroll.Pagination toPaginationResReport(Pageable pageable,
            Page<StudentEnrollmentProjection> page) {

        var pagination = new ResReportEnroll.Pagination();
        pagination.setPage(pageable.getPageNumber() + 1);
        pagination.setPageSize(pageable.getPageSize());
        pagination.setPages(page.getTotalPages());
        pagination.setTotal(page.getTotalElements());
        return pagination;
    }

    /**
     * convert to student enrollment
     * 
     * @param se
     * @return
     **/
    private ResReportEnroll.StudentEnrollment toStudentEnrollment(StudentEnrollmentProjection se) {
        var res = new ResReportEnroll.StudentEnrollment();
        res.setEnrollAt(se.getEnrollAt());
        res.setStatus(se.getStatus());
        res.setGender(se.getGender());
        res.setStudentCode(se.getStudentCode());
        res.setStudentEmail(se.getStudentEmail());
        res.setStudentId(se.getStudentId());
        res.setStudentName(se.getStudentName());
        return res;
    }

}
