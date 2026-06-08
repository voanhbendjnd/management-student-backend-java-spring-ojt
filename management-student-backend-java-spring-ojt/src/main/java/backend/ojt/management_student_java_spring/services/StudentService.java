package backend.ojt.management_student_java_spring.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.ojt.management_student_java_spring.domain.dto.request.UpdateStudentRequest;
import backend.ojt.management_student_java_spring.domain.entity.Student;
import backend.ojt.management_student_java_spring.domain.entity.User;
import backend.ojt.management_student_java_spring.repositories.StudentRepository;
import backend.ojt.management_student_java_spring.repositories.UserRepository;
import backend.ojt.management_student_java_spring.utils.SecurityUtils;
import backend.ojt.management_student_java_spring.utils.constains.StudentMajor;
import backend.ojt.management_student_java_spring.domain.dto.request.CreateStudentRequest;
import backend.ojt.management_student_java_spring.utils.constains.UserRole;
import backend.ojt.management_student_java_spring.utils.constains.UserStatus;
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
    final UserRepository userRepository;

    /**
     * init student when register account
     *
     * @paramuser
     **/
//    public void createStudent(User user) {
//        String year = String.format("%02d",
//                LocalDateTime.now().getYear() % 100);
//        String studentCode = "S"
//                + year
//                + String.format("%06d", user.getId());
//        this.studentRepository.save(
//                Student.builder()
//                        .studentCode(studentCode)
//                        .user(user)
//                        .build());
//    }

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

    @Transactional(readOnly = true)
    public java.util.List<backend.ojt.management_student_java_spring.domain.dto.res.ResStudent> getAllStudents() {
        return this.studentRepository.findAllWithEnrollments().stream()
                .map(student -> (backend.ojt.management_student_java_spring.domain.dto.res.ResStudent) student)
                .toList();
    }

    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {
        return this.studentRepository.findByIdWithEnrollments(id)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + id));
    }

//    @Transactional
//    public Student createStudentManual(Student student) {
//        // If user is not provided, create a default user
//        if (student.getUser() == null) {
//            User user = User.builder()
//                    .name("Student " + student.getStudentCode())
//                    .email("student" + student.getStudentCode() + "@temp.com")
//                    .role(UserRole.STUDENT)
//                    .status(UserStatus.ACTIVE)
//                    .build();
//            user = userRepository.save(user);
//            student.setUser(user);
//        }
//        return this.studentRepository.save(student);
//    }

    @Transactional
    public Student updateStudent(Long id, UpdateStudentRequest request) {
        Student existingStudent = getStudentById(id);

        if (request.getStudentCode() != null) {
            existingStudent.setStudentCode(request.getStudentCode());
        }
        if (request.getMajor() != null) {
            existingStudent.setMajor(request.getMajor());
        }
        if (request.getCountCredits() != null) {
            existingStudent.setCountCredits(request.getCountCredits());
        }
        if (request.getName() != null || request.getEmail() != null) {
            User user = existingStudent.getUser();
            if (user == null) {
                throw new IllegalStateException("Student missing associated user");
            }
            if (request.getName() != null) {
                user.setName(request.getName());
            }
            if (request.getEmail() != null) {
                String newEmail = request.getEmail();
                if (!newEmail.equalsIgnoreCase(user.getEmail())) {
                    User existingUser = this.userRepository.findByEmailIgnoreCase(newEmail);
                    if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                        throw new IllegalArgumentException("Email đã được sử dụng");
                    }
                    user.setEmail(newEmail);
                }
            }
            this.userRepository.save(user);
        }
        return this.studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        this.studentRepository.delete(student);
    }


//    public void createStudent(User user) {
//
//        this.studentRepository.save(
//                Student.builder()
//                        .studentCode(this.generateStudentCode(user.getId()))
//                        .user(user)
//                        .build());
//    }



//    public String generateStudentCode(Long userId) {
//        String year = String.format("%02d",
//                LocalDateTime.now().getYear() % 100);
//        String studentCode = "S"
//                + year
//                + String.format("%06d", userId);
//        return studentCode;
//    }
//
//
//
//
    public String generateStudentCode(Long userId) {
        String year = String.format("%02d", LocalDateTime.now().getYear() % 100);
        String studentCode = "S" + year + String.format("%06d", userId);
        return studentCode;
    }

    /**
     * init student when register account
     * * @param user
     **/
    @Transactional
    public void createStudent(User user) {
        // 1. Kiểm tra an toàn bắt buộc
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null!");
        }

        // 2. Kiểm tra nếu user đã tồn tại theo email, sử dụng user đã có
        User existingUser = null;
        if (user.getEmail() != null) {
            existingUser = this.userRepository.findByEmailIgnoreCase(user.getEmail());
        }

        if (existingUser != null) {
            user = existingUser;
        } else if (user.getId() == null) {
            // Chỉ lưu user nếu chưa có ID và chưa tồn tại
            user = this.userRepository.save(user);
        }

        // 3. Kiểm tra nếu student đã tồn tại cho user này
        if (this.studentRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("Student already exists for this user!");
        }

        // 4. Tự động sinh mã sinh viên theo logic của bạn
        String studentCode = this.generateStudentCode(user.getId());

        // 5. Khởi tạo Student với Builder
        Student student = Student.builder()
                .user(user)
                .studentCode(studentCode)
                .countCredits(0)
                .build();

        // 6. Lưu xuống Database
        this.studentRepository.save(student);
    }

    @Transactional
    public void createStudent(CreateStudentRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null!");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .build();

        // reuse existing logic: save user if needed
        User existingUser = null;
        if (user.getEmail() != null) {
            existingUser = this.userRepository.findByEmailIgnoreCase(user.getEmail());
        }

        if (existingUser != null) {
            user = existingUser;
        } else {
            user = this.userRepository.save(user);
        }

        if (this.studentRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("Student already exists for this user!");
        }

        String studentCode = this.generateStudentCode(user.getId());

        Student student = Student.builder()
                .user(user)
                .studentCode(studentCode)
                .countCredits(0)
                .major(request.getMajor())
                .build();

        this.studentRepository.save(student);
    }
}
