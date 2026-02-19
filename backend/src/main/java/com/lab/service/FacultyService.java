package com.lab.service;

import com.lab.entity.Faculty;
import com.lab.mapper.FacultyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {
    
    @Autowired
    private FacultyMapper facultyMapper;
    
    public List<Faculty> findAll() {
        return facultyMapper.selectList(null);
    }
    
    public Optional<Faculty> findById(Long id) {
        return Optional.ofNullable(facultyMapper.selectById(id));
    }
    
    public Faculty save(Faculty faculty) {
        if (faculty.getId() == null) {
            facultyMapper.insert(faculty);
        } else {
            facultyMapper.updateById(faculty);
        }
        return faculty;
    }
    
    public void deleteById(Long id) {
        facultyMapper.deleteById(id);
    }
}
