package com.lab.controller;

import com.lab.dto.ApiResponse;
import com.lab.entity.Patent;
import com.lab.service.PatentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patents")
public class PatentController {
    
    @Autowired
    private PatentService patentService;
    
    @GetMapping
    public ApiResponse<List<Patent>> getAllPatents() {
        return ApiResponse.success(patentService.findAll());
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Patent> getPatentById(@PathVariable Long id) {
        return patentService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("未找到该专利信息"));
    }
    
    @PostMapping
    public ApiResponse<Patent> createPatent(@RequestBody Patent patent) {
        return ApiResponse.success("创建成功", patentService.save(patent));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Patent> updatePatent(@PathVariable Long id, @RequestBody Patent patent) {
        return patentService.findById(id)
                .map(existing -> {
                    patent.setId(id);
                    return ApiResponse.success("更新成功", patentService.save(patent));
                })
                .orElse(ApiResponse.error("未找到该专利信息"));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePatent(@PathVariable Long id) {
        patentService.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }
}
