package com.pu.universityprojectfirsttrimester.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeacherDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private boolean available;
}
