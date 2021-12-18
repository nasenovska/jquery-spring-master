package com.pu.universityprojectfirsttrimester.service;

import com.pu.universityprojectfirsttrimester.dto.TeacherDto;
import com.pu.universityprojectfirsttrimester.entity.Teacher;
import com.pu.universityprojectfirsttrimester.repository.TeacherRepository;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Service
public class TeacherService {

    TeacherRepository teacherRepository;

    public List<TeacherDto> getAll() {
        return teacherRepository.findAll()
                .stream()
                .map(t -> new TeacherDto(t.getId(), t.getFirstName(), t.getLastName(), t.isAvailable()))
                .collect(Collectors.toList());
    }

    public TeacherDto getById(Integer id) {
        return teacherRepository.findById(id)
                .map(t -> new TeacherDto(t.getId(), t.getFirstName(), t.getLastName(), t.isAvailable()))
                .orElse(null);
    }

    public void save(TeacherDto teacherDto) {
        Teacher teacher = Teacher.builder()
                .available(teacherDto.isAvailable())
                .firstName(teacherDto.getFirstName())
                .lastName(teacherDto.getLastName())
                .build();

        teacherRepository.save(teacher);
    }

    public void update(TeacherDto teacherDto) {
        Teacher teacher = teacherRepository.findById(teacherDto.getId())
                .orElse(null);

        teacher.setAvailable(teacherDto.isAvailable());
        teacher.setFirstName(teacherDto.getFirstName());
        teacher.setLastName(teacherDto.getLastName());

        teacherRepository.save(teacher);
    }


    public void delete(Integer id) {
        Teacher teacher = teacherRepository.getById(id);
        teacher.getCourses()
                .forEach(course -> course.setTeacher(null));

        teacherRepository.saveAndFlush(teacher);

        teacherRepository.deleteById(id);
    }
}
