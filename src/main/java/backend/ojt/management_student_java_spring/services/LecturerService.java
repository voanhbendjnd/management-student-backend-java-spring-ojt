package backend.ojt.management_student_java_spring.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import backend.ojt.management_student_java_spring.domain.entity.Lecturer;
import backend.ojt.management_student_java_spring.domain.entity.Student;
import backend.ojt.management_student_java_spring.domain.entity.User;
import backend.ojt.management_student_java_spring.repositories.LecturerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class LecturerService {
    final LecturerRepository lecturerRepository;

    /**
     * init lecturer code
     * 
     * @param user
     **/
    public String generateLecturerCode(Long userId) {
        String year = String.format("%02d",
                LocalDateTime.now().getYear() % 100);
        String lecturerCode = "L"
                + year
                + String.format("%06d", userId);
        return lecturerCode;
    }
    /**
     * init lecturer
     * 
     * @param user
     **/
    public void createLecturer(User user) {
        String year = String.format("%02d",
                LocalDateTime.now().getYear() % 100);
        String lecturerCode = "S"
                + year
                + String.format("%06d", user.getId());
        this.lecturerRepository.save(
                Lecturer.builder()
                        .lecturerCode(lecturerCode)
                        .user(user)
                        .build());
    }
}
