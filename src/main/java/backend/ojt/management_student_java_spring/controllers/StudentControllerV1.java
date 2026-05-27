package backend.ojt.management_student_java_spring.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ojt.management_student_java_spring.domain.dto.request.RequestStudent;
import backend.ojt.management_student_java_spring.services.StudentService;
import backend.ojt.management_student_java_spring.utils.annotations.ApiMessage;
import backend.ojt.management_student_java_spring.utils.exceptions.BadDataException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentControllerV1 {
    final StudentService studentService;

    @PreAuthorize("hasAuthority(hasRole('STUDENT'))")
    @PutMapping("/me/major")
    @ApiMessage("Student register major")
    public ResponseEntity<?> registerMajor(@RequestBody RequestStudent request) {
        var major = request.getMajor();
        if (major == null) {
            throw new BadDataException("Major null!");
        }
        this.studentService.registerMajor(major);
        return ResponseEntity.ok("Register major successfully!");
    }
}
