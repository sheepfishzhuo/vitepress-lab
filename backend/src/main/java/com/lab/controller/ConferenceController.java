package com.lab.controller;

import com.lab.dto.ApiResponse;
import com.lab.entity.Conference;
import com.lab.service.ConferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conferences")
public class ConferenceController {
    
    @Autowired
    private ConferenceService conferenceService;
    
    @GetMapping
    public ApiResponse<List<Conference>> getAllConferences() {
        return ApiResponse.success(conferenceService.findAll());
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Conference> getConferenceById(@PathVariable Long id) {
        return conferenceService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("未找到该会议信息"));
    }
    
    @PostMapping
    public ApiResponse<Conference> createConference(@RequestBody Conference conference) {
        return ApiResponse.success("创建成功", conferenceService.save(conference));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Conference> updateConference(@PathVariable Long id, @RequestBody Conference conference) {
        return conferenceService.findById(id)
                .map(existing -> {
                    conference.setId(id);
                    return ApiResponse.success("更新成功", conferenceService.save(conference));
                })
                .orElse(ApiResponse.error("未找到该会议信息"));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteConference(@PathVariable Long id) {
        conferenceService.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }
}
