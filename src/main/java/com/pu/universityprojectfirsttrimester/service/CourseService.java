package com.pu.universityprojectfirsttrimester.service;

import com.pu.universityprojectfirsttrimester.dto.CourseDto;
import com.pu.universityprojectfirsttrimester.dto.CourseSelect;
import com.pu.universityprojectfirsttrimester.dto.Filter;
import com.pu.universityprojectfirsttrimester.entity.Course;
import com.pu.universityprojectfirsttrimester.entity.Student;
import com.pu.universityprojectfirsttrimester.entity.Teacher;
import com.pu.universityprojectfirsttrimester.repository.CourseFilterRepository;
import com.pu.universityprojectfirsttrimester.repository.CourseRepository;
import com.pu.universityprojectfirsttrimester.repository.TeacherRepository;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Service
public class CourseService {

    CourseRepository courseRepository;
    CourseFilterRepository courseFilterRepository;
    TeacherRepository teacherRepository;

    public List<CourseDto> getAll(Filter courseFilter) {
        List<Course> courses = courseFilterRepository.getAll(courseFilter);

        return courses.stream()
                .map(c -> new CourseDto(c.getId(), c.getTitle(), c.getPrice(), c.getTeacher() != null ? c.getTeacher().getId() : null))
                .collect(Collectors.toList());
    }

    public CourseDto get(Integer id) {
        return courseRepository.findById(id)
                .map(c -> new CourseDto(c.getId(), c.getTitle(), c.getPrice(), c.getTeacher() != null ? c.getTeacher().getId() : null))
                .orElse(null);
    }

    public void save(CourseDto courseDto) {
        Teacher teacher = null;
        if (courseDto.getTeacherId() != null)
            teacher = teacherRepository.getById(courseDto.getTeacherId());

        Course newCourse = Course.builder()
                .price(courseDto.getPrice())
                .title(courseDto.getTitle())
                .teacher(teacher)
                .build();

        courseRepository.save(newCourse);
    }

    public void update(CourseDto courseDto) {
        Course course = courseRepository.getById(courseDto.getId());
        course.setPrice(courseDto.getPrice());
        course.setTitle(courseDto.getTitle());

        if (courseDto.getTeacherId() != null)
            course.setTeacher(teacherRepository.getById(courseDto.getTeacherId()));
        else
            course.setTeacher(null);

        courseRepository.save(course);
    }

    public void delete(Integer id) {
        Course course = courseRepository.getById(id);
        List<Student> students = course.getStudents();

        if (students != null && !students.isEmpty()) {
            students.forEach(s -> s.getCourses().remove(course));
            courseRepository.saveAndFlush(course);
        }

        courseRepository.deleteById(id);
    }

    public List<CourseSelect> getCourseSelectOptions() {
        return courseRepository.findAll()
                .stream()
                .map(c -> new CourseSelect(c.getId(), c.getTitle()))
                .collect(Collectors.toList());
    }

    public List<CourseDto> getAllForStudent(Integer studentId) {
        List<Course> courses = courseRepository.findAllByStudents_Id(studentId);

        return courses.stream()
                .map(c -> new CourseDto(c.getId(), c.getTitle(), c.getPrice(), c.getTeacher() != null ? c.getTeacher().getId() : null))
                .collect(Collectors.toList());

    }
}
