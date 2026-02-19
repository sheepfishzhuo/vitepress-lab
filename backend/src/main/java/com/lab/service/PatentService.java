package com.lab.service;

import com.lab.entity.Patent;
import com.lab.mapper.PatentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatentService {
    
    @Autowired
    private PatentMapper patentMapper;
    
    public List<Patent> findAll() {
        return patentMapper.selectList(null);
    }
    
    public Optional<Patent> findById(Long id) {
        return Optional.ofNullable(patentMapper.selectById(id));
    }
    
    public Patent save(Patent patent) {
        if (patent.getId() == null) {
            patentMapper.insert(patent);
        } else {
            patentMapper.updateById(patent);
        }
        return patent;
    }
    
    public void deleteById(Long id) {
        patentMapper.deleteById(id);
    }
}
