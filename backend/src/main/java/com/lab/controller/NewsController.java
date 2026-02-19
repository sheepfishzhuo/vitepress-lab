package com.lab.controller;

import com.lab.dto.ApiResponse;
import com.lab.entity.News;
import com.lab.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    
    @Autowired
    private NewsService newsService;
    
    @GetMapping
    public ApiResponse<List<News>> getAllNews() {
        return ApiResponse.success(newsService.findAll());
    }
    
    @GetMapping("/{id}")
    public ApiResponse<News> getNewsById(@PathVariable Long id) {
        return newsService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("未找到该新闻信息"));
    }
    
    @PostMapping
    public ApiResponse<News> createNews(@RequestBody News news) {
        return ApiResponse.success("创建成功", newsService.save(news));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<News> updateNews(@PathVariable Long id, @RequestBody News news) {
        return newsService.findById(id)
                .map(existing -> {
                    news.setId(id);
                    return ApiResponse.success("更新成功", newsService.save(news));
                })
                .orElse(ApiResponse.error("未找到该新闻信息"));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }
}
