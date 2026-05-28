package backend.ojt.management_student_java_spring.domain.dto.res;

import java.time.LocalDateTime;

import backend.ojt.management_student_java_spring.utils.constains.EnrollmentStatus;

public interface StudentEnrollmentProjection {
    Long getStudentId();

    String getStudentName();

    String getStudentCode();

    String getStudentEmail();

    EnrollmentStatus getStatus();

    LocalDateTime getEnrollAt();
}
