package com.lab.controller;

import com.lab.dto.ApiResponse;
import com.lab.entity.ConfigOption;
import com.lab.service.ConfigOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class ConfigOptionController {
    
    @Autowired
    private ConfigOptionService configOptionService;
    
    @GetMapping
    public ApiResponse<Map<String, List<ConfigOption>>> getAllGrouped() {
        return ApiResponse.success(configOptionService.findAllGrouped());
    }
    
    @GetMapping("/{type}")
    public ApiResponse<List<ConfigOption>> getByType(@PathVariable String type) {
        return ApiResponse.success(configOptionService.findByType(type));
    }
    
    @PostMapping
    public ApiResponse<ConfigOption> create(@RequestBody ConfigOption option) {
        return ApiResponse.success(configOptionService.save(option));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<ConfigOption> update(@PathVariable Long id, @RequestBody ConfigOption option) {
        option.setId(id);
        return ApiResponse.success(configOptionService.save(option));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        configOptionService.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }
}
