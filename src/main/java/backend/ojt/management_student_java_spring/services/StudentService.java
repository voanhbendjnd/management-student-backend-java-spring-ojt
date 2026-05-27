package backend.ojt.management_student_java_spring.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.ojt.management_student_java_spring.domain.entity.Student;
import backend.ojt.management_student_java_spring.domain.entity.User;
import backend.ojt.management_student_java_spring.repositories.StudentRepository;
import backend.ojt.management_student_java_spring.utils.SecurityUtils;
import backend.ojt.management_student_java_spring.utils.constains.StudentMajor;
import backend.ojt.management_student_java_spring.utils.exceptions.DenyException;
import backend.ojt.management_student_java_spring.utils.exceptions.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class StudentService {
    final StudentRepository studentRepository;

    /**
     * init student when register account
     * 
     * @param user
     **/
    public void createStudent(User user) {
        String year = String.format("%02d",
                LocalDateTime.now().getYear() % 100);
        String studentCode = "S"
                + year
                + String.format("%06d", user.getId());
        this.studentRepository.save(
                Student.builder()
                        .studentCode(studentCode)
                        .user(user)
                        .build());
    }

    @Transactional
    public void registerMajor(StudentMajor major) {
        var userId = SecurityUtils.getCurrentUserIdOrNull();
        if (userId == null) {
            throw new DenyException("Not logged in!");
        }
        int updated = this.studentRepository.updateMajorStudentById(userId, major);
        if (updated <= 0) {
            throw new NotFoundException("Student not found!");
        }

    }
}
