package backend.ojt.management_student_java_spring.controllers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ojt.management_student_java_spring.domain.dto.request.RequestCourse;
import backend.ojt.management_student_java_spring.services.CourseService;
import backend.ojt.management_student_java_spring.utils.annotations.ApiMessage;
import backend.ojt.management_student_java_spring.utils.exceptions.RequestErrorException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseControllerV1 {
    final CourseService courseService;

    @PreAuthorize("hasRole('ADMIN')")

    @PostMapping
    @ApiMessage("Create new course with lecturer ID")
    public ResponseEntity<?> createCourse(@Valid @RequestBody RequestCourse request) {
        if (request != null) {
            var endDate = request.getEnrollEndDate();
            var startDate = request.getEnrollEndDate();
            var now = LocalDateTime.now();
            if (endDate.isBefore(now)) {
                throw new RequestErrorException("Enroll end date before now!");
            }
            if (startDate.isAfter(endDate)) {
                throw new RequestErrorException("Enroll start date after enroll end date!");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(this.courseService.createCourse(request));
        }
        throw new RequestErrorException("Data request null!");
    }

}
