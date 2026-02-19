package com.lab.service;

import com.lab.entity.Competition;
import com.lab.mapper.CompetitionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompetitionService {
    
    @Autowired
    private CompetitionMapper competitionMapper;
    
    public List<Competition> findAll() {
        return competitionMapper.selectList(null);
    }
    
    public Optional<Competition> findById(Long id) {
        return Optional.ofNullable(competitionMapper.selectById(id));
    }
    
    public Competition save(Competition competition) {
        if (competition.getId() == null) {
            competitionMapper.insert(competition);
        } else {
            competitionMapper.updateById(competition);
        }
        return competition;
    }
    
    public void deleteById(Long id) {
        competitionMapper.deleteById(id);
    }
}
