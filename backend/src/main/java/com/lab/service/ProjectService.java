package com.lab.service;

import com.lab.entity.Project;
import com.lab.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    
    @Autowired
    private ProjectMapper projectMapper;
    
    public List<Project> findAll() {
        return projectMapper.selectList(null);
    }
    
    public Optional<Project> findById(Long id) {
        return Optional.ofNullable(projectMapper.selectById(id));
    }
    
    public Project save(Project project) {
        if (project.getId() == null) {
            projectMapper.insert(project);
        } else {
            projectMapper.updateById(project);
        }
        return project;
    }
    
    public void deleteById(Long id) {
        projectMapper.deleteById(id);
    }
}
