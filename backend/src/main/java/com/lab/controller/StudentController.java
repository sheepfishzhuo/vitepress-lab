package com.lab.controller;

import com.lab.dto.ApiResponse;
import com.lab.entity.Student;
import com.lab.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping
    public ApiResponse<List<Student>> getAllStudents() {
        return ApiResponse.success(studentService.findAll());
    }
    
    @GetMapping("/status/{status}")
    public ApiResponse<List<Student>> getStudentsByStatus(@PathVariable String status) {
        return ApiResponse.success(studentService.findByStatus(status));
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Student> getStudentById(@PathVariable Long id) {
        return studentService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("未找到该学生信息"));
    }
    
    @PostMapping
    public ApiResponse<Student> createStudent(@RequestBody Student student) {
        return ApiResponse.success("创建成功", studentService.save(student));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.findById(id)
                .map(existing -> {
                    student.setId(id);
                    return ApiResponse.success("更新成功", studentService.save(student));
                })
                .orElse(ApiResponse.error("未找到该学生信息"));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }
}
