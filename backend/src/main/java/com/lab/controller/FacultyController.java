package com.lab.controller;

import com.lab.dto.ApiResponse;
import com.lab.entity.Faculty;
import com.lab.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty")
public class FacultyController {
    
    @Autowired
    private FacultyService facultyService;
    
    @GetMapping
    public ApiResponse<List<Faculty>> getAllFaculty() {
        return ApiResponse.success(facultyService.findAll());
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Faculty> getFacultyById(@PathVariable Long id) {
        return facultyService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("未找到该教师信息"));
    }
    
    @PostMapping
    public ApiResponse<Faculty> createFaculty(@RequestBody Faculty faculty) {
        return ApiResponse.success("创建成功", facultyService.save(faculty));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Faculty> updateFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
        return facultyService.findById(id)
                .map(existing -> {
                    faculty.setId(id);
                    return ApiResponse.success("更新成功", facultyService.save(faculty));
                })
                .orElse(ApiResponse.error("未找到该教师信息"));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }
}
