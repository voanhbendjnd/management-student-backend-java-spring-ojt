package backend.ojt.management_student_java_spring.services;

import org.springframework.stereotype.Service;

import backend.ojt.management_student_java_spring.domain.dto.request.RequestCourse;
import backend.ojt.management_student_java_spring.domain.dto.res.ResCourse;
import backend.ojt.management_student_java_spring.domain.entity.Course;
import backend.ojt.management_student_java_spring.repositories.CourseRepository;
import backend.ojt.management_student_java_spring.repositories.LecturerRepository;
import backend.ojt.management_student_java_spring.utils.constains.CourseSemester;
import backend.ojt.management_student_java_spring.utils.exceptions.RequestDataException;
import backend.ojt.management_student_java_spring.utils.exceptions.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseService {
    final CourseRepository courseRepository;
    final LecturerRepository lecturerRepository;
    /**
     * create course
     * @param request
     * @return
    **/
    public ResCourse createCourse(RequestCourse request) {
        if (this.courseRepository.existsByCourseCodeIgnoreCase(request.getCourseCode())) {
            throw new RequestDataException("Course code aleary exists!");
        }
        var year = request.getEnrollStartDate().getYear();
        var month = request.getEnrollStartDate().getMonth().getValue();
        request.setYear(year);
        request.setSemester(this.getSemesterByMonth(month));
        return this.toRes(this.courseRepository.save(this.toEntity(request)).getId());

    }
    /**
     * convert request to entity
     * @param request
     * @return
    **/
    public Course toEntity(RequestCourse request) {
        var lecturer = this.lecturerRepository.findById(request.getLecturerId())
                .orElseThrow(() -> new NotFoundException("Lecturer not found!"));
        return Course.builder()
                .courseCode(request.getCourseCode())
                .credits(request.getCredits())
                .description(request.getDescription())
                .maxStudents(request.getMaxStudents())
                .name(request.getName())
                .enrollEndDate(request.getEnrollEndDate())
                .enrollStartDate(request.getEnrollStartDate())
                .semester(request.getSemester())
                .lecturer(lecturer)
                .year(request.getYear())
                .build();
    }
    /**
     * to response
     * @param courseId
     * @return
    **/
    public ResCourse toRes(Long courseId) {
        return this.courseRepository.findResById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found!"));
    }
    /**
     * generate semester
     * @param month
     * @return
    **/
    public CourseSemester getSemesterByMonth(int month) {
        return (month > 0 && month < 6) ? CourseSemester.SPRING
                : (month > 5 && month < 9) ? CourseSemester.SUMMER
                        : (month > 8 && month <= 12) ? CourseSemester.FALL
                                : null;
    }
}
