package backend.ojt.management_student_java_spring.configs;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import backend.ojt.management_student_java_spring.domain.entity.Course;
import backend.ojt.management_student_java_spring.domain.entity.Enrollment;
import backend.ojt.management_student_java_spring.domain.entity.Lecturer;
import backend.ojt.management_student_java_spring.domain.entity.Student;
import backend.ojt.management_student_java_spring.domain.entity.User;
import backend.ojt.management_student_java_spring.repositories.CourseRepository;
import backend.ojt.management_student_java_spring.repositories.EnrollmentRepository;
import backend.ojt.management_student_java_spring.repositories.LecturerRepository;
import backend.ojt.management_student_java_spring.repositories.StudentRepository;
import backend.ojt.management_student_java_spring.repositories.UserRepository;
import backend.ojt.management_student_java_spring.utils.constains.CourseSemester;
import backend.ojt.management_student_java_spring.utils.constains.EnrollmentStatus;
import backend.ojt.management_student_java_spring.utils.constains.LecturerAcademicTitle;
import backend.ojt.management_student_java_spring.utils.constains.LecturerDepartment;
import backend.ojt.management_student_java_spring.utils.constains.StudentMajor;
import backend.ojt.management_student_java_spring.utils.constains.UserGender;
import backend.ojt.management_student_java_spring.utils.constains.UserRole;
import backend.ojt.management_student_java_spring.utils.constains.UserStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/*
* Class init data when build project if data empty
*/
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    final PasswordEncoder passwordEncoder;
    final UserRepository userRepository;
    final LecturerRepository lecturerRepository;
    final StudentRepository studentRepository;
    final CourseRepository courseRepository;
    final EnrollmentRepository enrollmentRepository;

    @Override
    public void run(String... args) throws Exception {

        System.out.println(">>> START INIT DATABASE <<<");

        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("123123"));
            admin.setGender(UserGender.MALE);
            admin.setPhone("0900000001");
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(UserStatus.ACTIVE);

            User lecturerUser1 = new User();
            lecturerUser1.setName("Nguyen Van Lecturer");
            lecturerUser1.setEmail("lecturer1@gmail.com");
            lecturerUser1.setPassword(passwordEncoder.encode("123123"));
            lecturerUser1.setGender(UserGender.MALE);
            lecturerUser1.setPhone("0900000002");
            lecturerUser1.setRole(UserRole.LECTURER);
            lecturerUser1.setStatus(UserStatus.ACTIVE);

            User lecturerUser2 = new User();
            lecturerUser2.setName("Tran Thi Lecturer");
            lecturerUser2.setEmail("lecturer2@gmail.com");
            lecturerUser2.setPassword(passwordEncoder.encode("123123"));
            lecturerUser2.setGender(UserGender.FEMALE);
            lecturerUser2.setPhone("0900000003");
            lecturerUser2.setRole(UserRole.LECTURER);
            lecturerUser2.setStatus(UserStatus.ACTIVE);

            User studentUser1 = new User();
            studentUser1.setName("Student One");
            studentUser1.setEmail("student1@gmail.com");
            studentUser1.setPassword(passwordEncoder.encode("123123"));
            studentUser1.setGender(UserGender.MALE);
            studentUser1.setPhone("0900000004");
            studentUser1.setRole(UserRole.STUDENT);
            studentUser1.setStatus(UserStatus.ACTIVE);

            User studentUser2 = new User();
            studentUser2.setName("Student Two");
            studentUser2.setEmail("student2@gmail.com");
            studentUser2.setPassword(passwordEncoder.encode("123123"));
            studentUser2.setGender(UserGender.FEMALE);
            studentUser2.setPhone("0900000005");
            studentUser2.setRole(UserRole.STUDENT);
            studentUser2.setStatus(UserStatus.ACTIVE);

            User studentUser3 = new User();
            studentUser3.setName("Student Three");
            studentUser3.setEmail("student3@gmail.com");
            studentUser3.setPassword(passwordEncoder.encode("123123"));
            studentUser3.setGender(UserGender.MALE);
            studentUser3.setPhone("0900000006");
            studentUser3.setRole(UserRole.STUDENT);
            studentUser3.setStatus(UserStatus.ACTIVE);

            userRepository.saveAll(List.of(
                    admin,
                    lecturerUser1,
                    lecturerUser2,
                    studentUser1,
                    studentUser2,
                    studentUser3));

            Lecturer lecturer1 = new Lecturer();
            lecturer1.setUser(lecturerUser1);
            lecturer1.setLectureCode("LEC001");
            lecturer1.setDepartment(LecturerDepartment.COMPUTER_SCIENCE);
            lecturer1.setAcademicTitle(LecturerAcademicTitle.MSc);

            Lecturer lecturer2 = new Lecturer();
            lecturer2.setUser(lecturerUser2);
            lecturer2.setLectureCode("LEC002");
            lecturer2.setDepartment(LecturerDepartment.COMPUTER_SCIENCE);
            lecturer2.setAcademicTitle(LecturerAcademicTitle.PhD);

            lecturerRepository.saveAll(List.of(
                    lecturer1,
                    lecturer2));

            Student student1 = new Student();
            student1.setUser(studentUser1);
            student1.setStudentCode("ST001");
            student1.setMajor(StudentMajor.SOFTWARE_ENGINEERING);

            Student student2 = new Student();
            student2.setUser(studentUser2);
            student2.setStudentCode("ST002");
            student2.setMajor(StudentMajor.CYBER_SECURITY);

            Student student3 = new Student();
            student3.setUser(studentUser3);
            student3.setStudentCode("ST003");
            student3.setMajor(StudentMajor.ARTIFICIAL_INTELLIGENCE);

            studentRepository.saveAll(List.of(
                    student1,
                    student2,
                    student3));

            Course javaCourse = new Course();
            javaCourse.setCourseCode("JAVA101");
            javaCourse.setName("Java Programming");
            javaCourse.setDescription("Core Java Course");
            javaCourse.setCredits(3);
            javaCourse.setMaxStudents(2);
            javaCourse.setCurrentStudents(0);
            javaCourse.setLecturer(lecturer1);
            javaCourse.setSemester(CourseSemester.FALL);
            javaCourse.setYear(2026);

            Course springCourse = new Course();
            springCourse.setCourseCode("SPRING202");
            springCourse.setName("Spring Boot");
            springCourse.setDescription("Spring Boot Advanced");
            springCourse.setCredits(4);
            springCourse.setMaxStudents(3);
            springCourse.setCurrentStudents(0);
            springCourse.setLecturer(lecturer1);
            springCourse.setSemester(CourseSemester.SPRING);
            springCourse.setYear(2026);

            Course securityCourse = new Course();
            securityCourse.setCourseCode("SEC303");
            securityCourse.setName("Cyber Security");
            securityCourse.setDescription("Security Fundamentals");
            securityCourse.setCredits(3);
            securityCourse.setMaxStudents(2);
            securityCourse.setCurrentStudents(0);
            securityCourse.setLecturer(lecturer2);
            securityCourse.setSemester(CourseSemester.SUMMER);
            securityCourse.setYear(2026);

            courseRepository.saveAll(List.of(
                    javaCourse,
                    springCourse,
                    securityCourse));

            Enrollment enrollment1 = new Enrollment();
            enrollment1.setStudent(student1);
            enrollment1.setCourse(javaCourse);
            enrollment1.setStatus(EnrollmentStatus.ENROLLED);

            Enrollment enrollment2 = new Enrollment();
            enrollment2.setStudent(student2);
            enrollment2.setCourse(javaCourse);
            enrollment2.setStatus(EnrollmentStatus.ENROLLED);

            Enrollment enrollment3 = new Enrollment();
            enrollment3.setStudent(student3);
            enrollment3.setCourse(securityCourse);
            enrollment3.setStatus(EnrollmentStatus.ENROLLED);

            enrollmentRepository.saveAll(List.of(
                    enrollment1,
                    enrollment2,
                    enrollment3));

            System.out.println(">>> INIT DATABASE SUCCESS <<<");

        } else {
            System.out.println(">>> SKIP INIT DATABASE <<<");
        }
    }
}
