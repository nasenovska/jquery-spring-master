package com.pu.universityprojectfirsttrimester.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "teacher")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Teacher implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "firstName", length = 255, nullable = false)
    private String firstName;

    @Column(name = "lastName", length = 255, nullable = false)
    private String lastName;

    @Column(name = "available")
    private boolean available;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, orphanRemoval = false)
    private List<Course> courses;

}
