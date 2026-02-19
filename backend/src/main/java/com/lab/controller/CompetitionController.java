package com.lab.controller;

import com.lab.dto.ApiResponse;
import com.lab.entity.Competition;
import com.lab.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competitions")
public class CompetitionController {
    
    @Autowired
    private CompetitionService competitionService;
    
    @GetMapping
    public ApiResponse<List<Competition>> getAllCompetitions() {
        return ApiResponse.success(competitionService.findAll());
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Competition> getCompetitionById(@PathVariable Long id) {
        return competitionService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("未找到该竞赛信息"));
    }
    
    @PostMapping
    public ApiResponse<Competition> createCompetition(@RequestBody Competition competition) {
        return ApiResponse.success("创建成功", competitionService.save(competition));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Competition> updateCompetition(@PathVariable Long id, @RequestBody Competition competition) {
        return competitionService.findById(id)
                .map(existing -> {
                    competition.setId(id);
                    return ApiResponse.success("更新成功", competitionService.save(competition));
                })
                .orElse(ApiResponse.error("未找到该竞赛信息"));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCompetition(@PathVariable Long id) {
        competitionService.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }
}
