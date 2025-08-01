package com.kaptue.educamer.controller;

import com.kaptue.educamer.dto.response.CourseCatalogDTO;
import com.kaptue.educamer.entity.Category;
import com.kaptue.educamer.repository.CategoryRepository;
import com.kaptue.educamer.security.Permission;
import com.kaptue.educamer.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/catalog")
// Accessible par les élèves et les parents
@PreAuthorize("hasAuthority('" + Permission.COURSE_READ_CATALOG + "')")
public class CatalogController {

    @Autowired private CourseService courseService;
    @Autowired private CategoryRepository categoryRepository;

    @GetMapping("/courses")
    public ResponseEntity<List<CourseCatalogDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getPublishedCourses());
    }

    @GetMapping("/courses/trending")
    public ResponseEntity<List<CourseCatalogDTO>> getTrendingCourses(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(courseService.getTrendingCourses(limit));
    }

    @GetMapping("/courses/top-rated")
    public ResponseEntity<List<CourseCatalogDTO>> getTopRatedCourses(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(courseService.getTopRatedCourses(limit));
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }
    
    @GetMapping("/categories/{categoryId}/courses")
    public ResponseEntity<List<CourseCatalogDTO>> getCoursesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(courseService.getCoursesByCategory(categoryId));
    }
}