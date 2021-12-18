package com.pu.universityprojectfirsttrimester.api;

import com.pu.universityprojectfirsttrimester.dto.StudentDto;
import com.pu.universityprojectfirsttrimester.service.StudentService;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentApi {

    private StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAll() {
        return new ResponseEntity<>(studentService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getById(@PathVariable Integer id) {
        return new ResponseEntity<>(studentService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody StudentDto studentDto) {
        List<String> errorMessages = getValidationErrorMessages(studentDto);
        if (!errorMessages.isEmpty())
            return ResponseEntity.badRequest().body(errorMessages);

        studentService.save(studentDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<?> editStudent(@RequestBody StudentDto studentDto) {
        List<String> errorMessages = getValidationErrorMessages(studentDto);
        if (!errorMessages.isEmpty())
            return ResponseEntity.badRequest().body(errorMessages);

        studentService.update(studentDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        studentService.delete(id);

        return ResponseEntity.ok().build();
    }

    public List<String> getValidationErrorMessages(StudentDto studentDto) {
        List<String> errorMessages = new ArrayList<>();
        if (StringUtils.isBlank(studentDto.getFirstName()))
            errorMessages.add("First name is mandatory!");

        if (StringUtils.isBlank(studentDto.getLastName()))
            errorMessages.add("Last name is mandatory!");

        return errorMessages;
    }
}
