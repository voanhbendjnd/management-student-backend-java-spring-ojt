package backend.ojt.management_student_java_spring.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import backend.ojt.management_student_java_spring.utils.constains.StudentMajor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "students")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student implements backend.ojt.management_student_java_spring.domain.dto.res.ResStudent {

    @Id
    @Column(name = "user_id", nullable = false)
    Long userId; // Đồng bộ với cột [user_id] trong DB

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId // Đồng bộ khóa chính lấy từ Id của thực thể User sang
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "student_code", unique = true, nullable = false)
    String studentCode; // Khớp với cột [student_code]

    @Enumerated(EnumType.STRING)
    @Column(name = "major")
    StudentMajor major; // Khớp với cột [major]

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    List<Enrollment> enrollments = new ArrayList<>();

    @Column(name = "count_credits", nullable = false)
    @Builder.Default
    Integer countCredits = 0; // Khớp với cột [count_credits]

    // Implement ResStudent interface methods
    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public String getStudentName() {
        return user != null ? user.getName() : null;
    }

    @Override
    public String getEmail() {
        return user != null ? user.getEmail() : null;
    }

    @Override
    public String getPhone() {
        return user != null ? user.getPhone() : null;
    }

    @Override
    public java.util.List<String> getCourseNames() {
        if (enrollments == null || enrollments.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return enrollments.stream()
                .map(enrollment -> enrollment.getCourse() != null ? enrollment.getCourse().getName() : null)
                .filter(name -> name != null)
                .toList();
    }
}