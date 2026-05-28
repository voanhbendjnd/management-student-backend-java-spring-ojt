package backend.ojt.management_student_java_spring.controllers;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ojt.management_student_java_spring.domain.dto.request.RequestCourse;
import backend.ojt.management_student_java_spring.services.CourseService;
import backend.ojt.management_student_java_spring.services.EnrollmentService;
import backend.ojt.management_student_java_spring.utils.annotations.ApiMessage;
import backend.ojt.management_student_java_spring.utils.exceptions.BadDataException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseControllerV1 {
    final CourseService courseService;
    final EnrollmentService enrollmentService;

    @PostMapping
    @ApiMessage("Create new course with lecturer ID")
    public ResponseEntity<?> createCourse(@Valid @RequestBody RequestCourse request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.courseService.createCourse(request));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{id}/enroll")
    @ApiMessage("Enrollment course")
    public ResponseEntity<String> enroll(@Positive @PathVariable("id") Long courseId) {
        this.enrollmentService.enroll(courseId);
        return ResponseEntity.ok("Enroll successfully!");
    }

    @GetMapping("/{id}/enroll/report")
    @ApiMessage("Get report course enroll")
    public ResponseEntity<?> report(@Positive @PathVariable("id") Long courseId, Pageable pageable) {
        return ResponseEntity.ok(this.enrollmentService.report(courseId, pageable));
    }
}
