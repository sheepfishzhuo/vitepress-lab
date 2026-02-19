package com.lab.service;

import com.lab.entity.Conference;
import com.lab.mapper.ConferenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConferenceService {
    
    @Autowired
    private ConferenceMapper conferenceMapper;
    
    public List<Conference> findAll() {
        return conferenceMapper.selectList(null);
    }
    
    public Optional<Conference> findById(Long id) {
        return Optional.ofNullable(conferenceMapper.selectById(id));
    }
    
    public Conference save(Conference conference) {
        if (conference.getId() == null) {
            conferenceMapper.insert(conference);
        } else {
            conferenceMapper.updateById(conference);
        }
        return conference;
    }
    
    public void deleteById(Long id) {
        conferenceMapper.deleteById(id);
    }
}
