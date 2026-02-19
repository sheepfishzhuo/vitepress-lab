package com.lab.controller;

import com.lab.dto.ApiResponse;
import com.lab.entity.Project;
import com.lab.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;
    
    @GetMapping
    public ApiResponse<List<Project>> getAllProjects() {
        return ApiResponse.success(projectService.findAll());
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Project> getProjectById(@PathVariable Long id) {
        return projectService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("未找到该项目信息"));
    }
    
    @PostMapping
    public ApiResponse<Project> createProject(@RequestBody Project project) {
        return ApiResponse.success("创建成功", projectService.save(project));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        return projectService.findById(id)
                .map(existing -> {
                    project.setId(id);
                    return ApiResponse.success("更新成功", projectService.save(project));
                })
                .orElse(ApiResponse.error("未找到该项目信息"));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }
}
