package com.pu.universityprojectfirsttrimester.repository;

import com.pu.universityprojectfirsttrimester.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    List<Course> findAllByStudents_Id(Integer id);
}
