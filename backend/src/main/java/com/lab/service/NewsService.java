package com.lab.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lab.entity.News;
import com.lab.mapper.NewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    
    @Autowired
    private NewsMapper newsMapper;
    
    public List<News> findAll() {
        QueryWrapper<News> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("date");
        return newsMapper.selectList(wrapper);
    }
    
    public Optional<News> findById(Long id) {
        return Optional.ofNullable(newsMapper.selectById(id));
    }
    
    public News save(News news) {
        if (news.getId() == null) {
            newsMapper.insert(news);
        } else {
            newsMapper.updateById(news);
        }
        return news;
    }
    
    public void deleteById(Long id) {
        newsMapper.deleteById(id);
    }
}
