package com.pu.universityprojectfirsttrimester.api;

import com.pu.universityprojectfirsttrimester.dto.TeacherDto;
import com.pu.universityprojectfirsttrimester.service.TeacherService;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/teachers")
@AllArgsConstructor
public class TeacherApi {

    private TeacherService teacherService;


    @GetMapping
    public ResponseEntity<List<TeacherDto>> getAll() {
        return new ResponseEntity<>(teacherService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getById(@PathVariable Integer id) {
        return new ResponseEntity<>(teacherService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createTeacher(@RequestBody TeacherDto teacherDto) {
        List<String> errorMessages = getValidationErrorMessages(teacherDto);
        if (!errorMessages.isEmpty())
            return ResponseEntity.badRequest().body(errorMessages);

        teacherService.save(teacherDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping()
    public ResponseEntity<?> editTeacher(@RequestBody TeacherDto teacherDto) {
        List<String> errorMessages = getValidationErrorMessages(teacherDto);
        if (!errorMessages.isEmpty())
            return ResponseEntity.badRequest().body(errorMessages);

        teacherService.update(teacherDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Integer id) {
        teacherService.delete(id);
        return ResponseEntity.ok().build();
    }

    public List<String> getValidationErrorMessages(TeacherDto teacherDto) {
        List<String> errorMessages = new ArrayList<>();
        if (StringUtils.isBlank(teacherDto.getFirstName()))
            errorMessages.add("First name is mandatory!");

        if (StringUtils.isBlank(teacherDto.getLastName()))
            errorMessages.add("Last name is mandatory!");

        return errorMessages;
    }
}
