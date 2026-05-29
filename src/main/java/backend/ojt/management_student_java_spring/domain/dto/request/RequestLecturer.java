package backend.ojt.management_student_java_spring.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RequestLecturer(
        Long id,
        @JsonProperty("academic_title") String academicTitle,
        String department) {

}
