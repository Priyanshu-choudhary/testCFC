package com.example.WebSecurityExample.controller;

import com.example.WebSecurityExample.MongoRepo.CourseRepo;
import com.example.WebSecurityExample.Pojo.Course.Course;
import com.example.WebSecurityExample.Pojo.Course.CourseDTO.CourseDTO;
import com.example.WebSecurityExample.Service.CourseService;
import com.example.WebSecurityExample.Service.PostService;
import com.example.WebSecurityExample.Service.UserService;
//import org.slf4j.// logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/Course")
//@CrossOrigin(origins = {"https://code-with-challenge.vercel.app", "http://localhost:5173"})
public class CourseController {
//    private static final // logger // logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepo courseRepo;

    @GetMapping
    public Page<CourseDTO> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return courseService.getAllCourses(page, size);
    }

    @GetMapping("/user")
    public Page<CourseDTO> getCoursesByUserName(
            @RequestParam String userName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return courseService.getCoursesByUserName(userName, page, size);
    }

    @GetMapping("/{username}/{skip}/{limit}")
    public ResponseEntity<?> getCourseByUserNameController(@PathVariable String username, @PathVariable int skip ,@PathVariable int limit) {
        try {
//            List<Course> all = courseService.getUserCourses(username);
            List<Course> all = courseService.getUserOneCourses(username,  skip ,  limit);

            if (all != null) {
                return new ResponseEntity<>(all, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // logger.error("Error fetching courses by username", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable String id) {
        try {

            Optional<Course> all = courseService.getUserCoursesByID(id);
            if (all.isPresent()) {
                return new ResponseEntity<>(all, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // logger.error("Error fetching courses by ID", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            // logger.error("creating course for username {}", username);
            String id = courseService.createCourse(course, username);
            // logger.error(" course id {}", id);
            // Wrap the ID in a JSON object
            Map<String, String> response = new HashMap<>();
            response.put("courseId", id);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            // logger.error("Error creating Course", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Map<String, String>> deleteUserById(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            // Assuming courseService.deleteUserById(id, username) returns a boolean indicating success
            boolean deleted = courseService.deleteUserById(id, username);

            if (deleted) {
                response.put("message", "Course deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Course not found or you do not have permission to delete it");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            // logger.error("Error deleting course by ID", e);
            response.put("message", "Error deleting course");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PutMapping("/id/{myId}")
    public ResponseEntity<?> updateCourseById(@PathVariable String myId, @RequestBody Course newdata) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            // logger.error("Try to updating course by ID");
            Course updatedCourse = courseService.updateCourse(myId, newdata, username);
            return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
        } catch (RuntimeException e) {
            // logger.error("Error updating post by ID", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



}
