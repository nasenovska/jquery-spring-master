package com.pu.universityprojectfirsttrimester.service;

import com.pu.universityprojectfirsttrimester.dto.StudentDto;
import com.pu.universityprojectfirsttrimester.entity.Course;
import com.pu.universityprojectfirsttrimester.entity.Student;
import com.pu.universityprojectfirsttrimester.repository.CourseRepository;
import com.pu.universityprojectfirsttrimester.repository.StudentRepository;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Value
@Service
public class StudentService {

    StudentRepository studentRepository;
    CourseRepository courseRepository;

    public List<StudentDto> getAll() {
        return studentRepository.findAll()
                .stream()
                .map(t -> new StudentDto(t.getId(), t.getFirstName(), t.getLastName(), getCoursesForStudent(t)))
                .collect(Collectors.toList());
    }

    public StudentDto getById(Integer id) {
        return studentRepository.findById(id)
                .map(t -> new StudentDto(t.getId(), t.getFirstName(), t.getLastName(), getCoursesForStudent(t)))
                .orElse(null);
    }

    public void save(StudentDto studentDto) {
        List<Course> courses = studentDto.getCourseIds()
                .stream()
                .map(courseId -> courseRepository.findById(courseId).get())
                .collect(Collectors.toList());

        Student student = Student.builder()
                .firstName(studentDto.getFirstName())
                .lastName(studentDto.getLastName())
                .courses(courses).build();

        studentRepository.save(student);

    }

    public void update(StudentDto studentDto) {
        Student student = studentRepository.findById(studentDto.getId()).orElse(null);
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());

        List<Course> courses = new ArrayList<>();
        studentDto.getCourseIds()
                .forEach(id -> courses.add(courseRepository.findById(id).get()));
        student.setCourses(courses);
        studentRepository.save(student);
    }

    public void delete(Integer id) {

        studentRepository.deleteById(id);
    }

    private List<Integer> getCoursesForStudent(Student student) {
        List<Integer> courseIds = new ArrayList<>();
        student.getCourses()
                .forEach(course -> courseIds.add(course.getId()));
        return courseIds;
    }
}
