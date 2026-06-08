package backend.ojt.management_student_java_spring.domain.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import backend.ojt.management_student_java_spring.utils.constains.StudentMajor;

public interface ResStudent {
    @JsonProperty("user_id")
    Long getUserId();

    @JsonProperty("student_code")
    String getStudentCode();

    @JsonProperty("student_name")
    String getStudentName();

    String getEmail();

    String getPhone();

    StudentMajor getMajor();

    @JsonProperty("count_credits")
    Integer getCountCredits();

    @JsonProperty("course_names")
    java.util.List<String> getCourseNames();
}
