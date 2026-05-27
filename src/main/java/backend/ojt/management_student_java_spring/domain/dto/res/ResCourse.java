package backend.ojt.management_student_java_spring.domain.dto.res;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import backend.ojt.management_student_java_spring.utils.constains.CourseSemester;

public interface ResCourse {
    Long getId();

    String getName();

    @JsonProperty("course_code")
    String getCourseCode();

    String getDescription();

    Integer getCredits();

    @JsonProperty(value = "max_students")
    Integer getMaxStudents();

    CourseSemester getSemester();

    Integer getYear();

    @JsonProperty("current_students")
    Integer getCurrentStudents();

    @JsonProperty("lecturer_id")
    Long getLecturerId();

    @JsonProperty("lecturer_code")
    String getLecturerCode();

    @JsonProperty("lecturer_name")
    String getLecturerName();

    @JsonProperty("enroll_start_date")
    LocalDateTime getEnrollStartDate();

    @JsonProperty("enroll_end_date")
    LocalDateTime getEnrollEndDate();

}
