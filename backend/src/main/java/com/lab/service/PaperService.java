package com.lab.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lab.entity.Paper;
import com.lab.mapper.PaperMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaperService {
    
    @Autowired
    private PaperMapper paperMapper;
    
    public List<Paper> findAll() {
        QueryWrapper<Paper> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("year");
        return paperMapper.selectList(wrapper);
    }
    
    public List<Paper> findByYear(Integer year) {
        QueryWrapper<Paper> wrapper = new QueryWrapper<>();
        wrapper.eq("year", year);
        return paperMapper.selectList(wrapper);
    }
    
    public Optional<Paper> findById(Long id) {
        return Optional.ofNullable(paperMapper.selectById(id));
    }
    
    public Paper save(Paper paper) {
        if (paper.getId() == null) {
            paperMapper.insert(paper);
        } else {
            paperMapper.updateById(paper);
        }
        return paper;
    }
    
    public void deleteById(Long id) {
        paperMapper.deleteById(id);
    }
}
