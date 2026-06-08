package backend.ojt.management_student_java_spring.domain.dto.request;

import backend.ojt.management_student_java_spring.utils.constains.StudentMajor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateStudentRequest {
    String studentCode;
    String name;
    String email;
    StudentMajor major;
    Integer countCredits;
}
