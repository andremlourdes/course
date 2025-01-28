package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller for managing courses.
 */
   @RestController
   @RequestMapping("/courses")
   @CrossOrigin(origins = "*", maxAge = 3600)
    public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDto courseDto){
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCreationdAt(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseModel));
    }
    /**
     * Endpoint to delete a course by ID.
     *
     * @param courseId the ID of the course to delete
     * @return ResponseEntity with the deletion status
     */
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable (value ="courseId") UUID courseId){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
        courseService.deleteCourse(courseModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully");
    }

    /**
     * Endpoint to update a course by ID.
     *
     * @param courseId the ID of the course to update
     * @param courseDto the course data transfer object
     * @return ResponseEntity with the updated course
     */
    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable (value = "courseId") UUID courseId, @RequestBody
    @Valid CourseDto courseDto){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
        var courseModel = courseModelOptional.get();
        courseModel.setName(courseDto.getName());
        courseModel.setDescription(courseDto.getDescription());
        courseModel.setImgUrl((courseDto.getImageUrl()));
        courseModel.setCourseStatus(CourseStatus.valueOf(courseDto.getCourseStatus()));
        courseModel.setCourseLevel(CourseLevel.valueOf(courseDto.getCourseLevel()));
        courseModel.setLastUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
    }

    /**
     * Endpoint to get all courses.
     *
     * @return ResponseEntity with the list of all courses
     */
    @GetMapping
    public ResponseEntity<List<CourseModel>> getAllCourses(){
        return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll());
    }

    /**
     * Endpoint to get a course by ID.
     *
     * @param courseId the ID of the course to retrieve
     * @return ResponseEntity with the retrieved course
     */
    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable (value = "courseId") UUID courseId){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(courseModelOptional.get());
    }
}