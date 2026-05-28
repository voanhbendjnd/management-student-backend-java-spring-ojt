package backend.ojt.management_student_java_spring.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
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
    @Modifying
    @Query(value = "update Student s set s.major = :major where s.id = :studentId")
    int updateMajorStudentById(@Param("studentId") Long studentId, @Param("major") StudentMajor major);

    @EntityGraph(attributePaths = { "user" })
    @Query(value = "select s from Student s where s.id = :studentId")
    Optional<Student> findWithDetailById(@Param("studentId") Long studentId);
}
