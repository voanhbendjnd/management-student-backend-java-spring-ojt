package backend.ojt.management_student_java_spring.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.ojt.management_student_java_spring.domain.entity.Student;
import backend.ojt.management_student_java_spring.domain.entity.User;
import backend.ojt.management_student_java_spring.repositories.StudentRepository;
import backend.ojt.management_student_java_spring.utils.SecurityUtils;
import backend.ojt.management_student_java_spring.utils.constains.StudentMajor;
import backend.ojt.management_student_java_spring.utils.constains.UserRole;
import backend.ojt.management_student_java_spring.utils.constains.UserStatus;
import backend.ojt.management_student_java_spring.utils.exceptions.UnauthorizedException;
import backend.ojt.management_student_java_spring.utils.exceptions.AccessToResourceException;
import backend.ojt.management_student_java_spring.utils.exceptions.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class StudentService {
    final StudentRepository studentRepository;

    /**
     * init student code
     * 
     * @param user
     **/
    public String generateStudentCode(Long userId) {
        String year = String.format("%02d",
                LocalDateTime.now().getYear() % 100);
        String studentCode = "S"
                + year
                + String.format("%06d", userId);
        return studentCode;
    }

    /**
     * init student when register account
     * 
     * @param user
     **/
    public void createStudent(User user) {

        this.studentRepository.save(
                Student.builder()
                        .studentCode(this.generateStudentCode(user.getId()))
                        .user(user)
                        .build());
    }

    @Transactional
    public void registerMajor(String major) {
        var userId = SecurityUtils.getCurrentUserIdOrNull();
        if (userId == null) {
            throw new UnauthorizedException("Not logged in!");
        }
        var student = this.studentRepository.findWithDetailById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found!"));
        if (student.getUser().getStatus().equals(UserStatus.INACTIVE)) {
            throw new AccessToResourceException("You do not have permission!");

        }
        if (!student.getUser().getRole().equals(UserRole.STUDENT)) {
            throw new AccessToResourceException("You do not have permission!");
        }
        if (major != null) {
            student.setMajor(StudentMajor.valueOf(major));

        }
    }
}
