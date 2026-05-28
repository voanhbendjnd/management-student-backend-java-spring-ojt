package backend.ojt.management_student_java_spring.domain.dto.res;

import java.time.LocalDateTime;

public interface LecturerCourse {
    Long getId();

    String getName();

    String getCourseCode();

    Integer getCurrentStudents();

    Integer getMaxStudents();

    LocalDateTime getEnrollStartDate();

    LocalDateTime getEnrollEndDate();

    Long getLecturerId();

    String getLecturerName();

    String getLecturerCode();

    String getLecturerEmail();
}
