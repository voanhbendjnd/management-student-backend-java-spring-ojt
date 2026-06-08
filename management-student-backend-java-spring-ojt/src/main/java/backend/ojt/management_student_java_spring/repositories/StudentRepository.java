package backend.ojt.management_student_java_spring.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.ojt.management_student_java_spring.domain.entity.Student;
import backend.ojt.management_student_java_spring.utils.constains.StudentMajor;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    @Query("""
            select distinct s
            from Student s
                left join fetch s.enrollments e
                left join fetch e.course
            """)
    java.util.List<Student> findAllWithEnrollments();

    @Query("""
            select s
            from Student s
                left join fetch s.enrollments e
                left join fetch e.course
            where s.userId = :id
            """)
    Optional<Student> findByIdWithEnrollments(@Param("id") Long id);

    @Modifying
    @Query(value = "update Student s set s.major = :major where s.userId = :studentId")
    int updateMajorStudentById(@Param("studentId") Long studentId, @Param("major") StudentMajor major);
}
