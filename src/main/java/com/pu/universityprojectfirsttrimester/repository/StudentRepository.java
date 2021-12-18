package com.pu.universityprojectfirsttrimester.repository;

import com.pu.universityprojectfirsttrimester.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
