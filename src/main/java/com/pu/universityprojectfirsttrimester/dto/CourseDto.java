package com.pu.universityprojectfirsttrimester.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseDto {
    private Integer id;
    private String title;
    private Double price;
    private Integer teacherId;
}
