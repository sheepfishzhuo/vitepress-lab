package com.lab.controller;

import com.lab.dto.ApiResponse;
import com.lab.entity.Paper;
import com.lab.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/papers")
public class PaperController {
    
    @Autowired
    private PaperService paperService;
    
    @GetMapping
    public ApiResponse<List<Paper>> getAllPapers() {
        return ApiResponse.success(paperService.findAll());
    }
    
    @GetMapping("/year/{year}")
    public ApiResponse<List<Paper>> getPapersByYear(@PathVariable Integer year) {
        return ApiResponse.success(paperService.findByYear(year));
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Paper> getPaperById(@PathVariable Long id) {
        return paperService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("未找到该论文信息"));
    }
    
    @PostMapping
    public ApiResponse<Paper> createPaper(@RequestBody Paper paper) {
        return ApiResponse.success("创建成功", paperService.save(paper));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Paper> updatePaper(@PathVariable Long id, @RequestBody Paper paper) {
        return paperService.findById(id)
                .map(existing -> {
                    paper.setId(id);
                    return ApiResponse.success("更新成功", paperService.save(paper));
                })
                .orElse(ApiResponse.error("未找到该论文信息"));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePaper(@PathVariable Long id) {
        paperService.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }
}
