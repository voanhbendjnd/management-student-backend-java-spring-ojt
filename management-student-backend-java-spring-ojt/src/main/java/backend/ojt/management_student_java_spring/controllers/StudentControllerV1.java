package backend.ojt.management_student_java_spring.controllers;

import java.util.List;

import backend.ojt.management_student_java_spring.domain.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ojt.management_student_java_spring.domain.dto.request.RequestStudent;
import backend.ojt.management_student_java_spring.domain.dto.request.UpdateStudentRequest;
import backend.ojt.management_student_java_spring.domain.dto.res.ResStudent;
import backend.ojt.management_student_java_spring.domain.entity.Student;
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

    @GetMapping
    @ApiMessage("Get all students")
    public ResponseEntity<List<ResStudent>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    @ApiMessage("Get student by id")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

//    @PostMapping
//    @ApiMessage("Create new student")
//    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
//        return ResponseEntity.ok(studentService.createStudentManual(student));
//    }

    @PostMapping
    @ApiMessage("Create new student")
    public ResponseEntity<?> createStudent(@RequestBody backend.ojt.management_student_java_spring.domain.dto.request.CreateStudentRequest request) {
        // Gọi xuống Service để xử lý tạo Sinh viên từ DTO
        this.studentService.createStudent(request);

        return ResponseEntity.status(HttpStatus.CREATED).body("Create student successfully!");
    }



    @PutMapping("/{id}")
    @ApiMessage("Update student")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody UpdateStudentRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete student")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

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
