package backend.ojt.management_student_java_spring.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.ojt.management_student_java_spring.domain.dto.res.LecturerCourse;
import backend.ojt.management_student_java_spring.domain.dto.res.ResCourse;
import backend.ojt.management_student_java_spring.domain.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
        boolean existsByCourseCodeIgnoreCase(String courseCode);

        @EntityGraph(attributePaths = { "lecturer", "lecturer.user" })
        @Query(value = "select c from Course c where c.id = :courseId")
        Optional<Course> findWithDetailById(@Param("courseId") Long courseId);

        @Query(value = """
                        select c.id as id, c.name as name, c.description as description,
                                c.courseCode as courseCode,
                                c.credits as credits, c.maxStudents as maxStudents, c.semester as semester,
                                        c.year as year, c.currentStudents as currentStudents,
                                                c.enrollStartDate as enrollStartDate, c.enrollEndDate as enrollEndDate,
                                        l.lecturerCode as lecturerCode, u.id as lecturerId,
                                        u.name as lecturerName
                                        from Course c
                                                left join c.lecturer l
                                                left join l.user u
                                                where c.id = :courseId

                        """)
        Optional<ResCourse> findResById(@Param("courseId") Long courseId);

        @Modifying
        @Query(value = "update Course c set c.currentStudents = c.currentStudents + 1, c.version = c.version + 1 where c.id = :courseId and c.currentStudents < c.maxStudents and c.version = :version")
        Integer updateSlotStudents(@Param("courseId") Long courseId,
                        @Param("version") Long version);

        @Query(value = """
                        select c.id as id, c.name as name, c.courseCode as courseCode,
                                        c.currentStudents as currentStudents, c.maxStudents as maxStudents,
                                                        c.enrollStartDate as enrollStartDate, c.enrollEndDate as enrollEndDate,
                                                                        u.id as lecturerId, u.name as lecturerName, l.lecturerCode as lecturerCode, u.email as lecturerEmail
                                                        from Course c
                        join c.lecturer l
                        join l.user u
                        where c.id = :courseId
                        """)
        Optional<LecturerCourse> findResLecturerCourseById(@Param("courseId") Long courseId);
}
