package com.pu.universityprojectfirsttrimester.api;

import com.pu.universityprojectfirsttrimester.dto.CourseDto;
import com.pu.universityprojectfirsttrimester.dto.Filter;
import com.pu.universityprojectfirsttrimester.dto.CourseSelect;
import com.pu.universityprojectfirsttrimester.service.CourseService;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseApi {

    private CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAll(Filter courseFilter) {
        return new ResponseEntity<>(courseService.getAll(courseFilter), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> get(@PathVariable Integer id) {
        return new ResponseEntity<>(courseService.get(id), HttpStatus.OK);
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<List<CourseDto>> getByStudentId(@PathVariable Integer studentId) {
        return new ResponseEntity<>(courseService.getAllForStudent(studentId), HttpStatus.OK);
    }

    @GetMapping("/select")
    public ResponseEntity<List<CourseSelect>> getCoursesSelectOptions(Filter courseFilter) {
        return new ResponseEntity<>(courseService.getCourseSelectOptions(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody CourseDto courseDto) {
        List<String> errorMessages = getValidationErrorMessages(courseDto);
        if (!errorMessages.isEmpty())
            return ResponseEntity.badRequest().body(errorMessages);

        courseService.save(courseDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping()
    public ResponseEntity<?> editCourse(@RequestBody CourseDto courseDto) {
        List<String> errorMessages = getValidationErrorMessages(courseDto);
        if (!errorMessages.isEmpty())
            return ResponseEntity.badRequest().body(errorMessages);

        courseService.update(courseDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer id) {
        courseService.delete(id);

        return ResponseEntity.ok().build();
    }

    public List<String> getValidationErrorMessages(CourseDto courseDto) {
        List<String> errorMessages = new ArrayList<>();
        if (courseDto.getPrice() == null)
            errorMessages.add("Price is mandatory!");

        if (StringUtils.isBlank(courseDto.getTitle()))
            errorMessages.add("Title is mandatory!");

        return errorMessages;
    }
}
