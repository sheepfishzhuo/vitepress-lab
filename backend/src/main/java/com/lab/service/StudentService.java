package com.lab.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lab.entity.Student;
import com.lab.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    
    @Autowired
    private StudentMapper studentMapper;
    
    public List<Student> findAll() {
        return studentMapper.selectList(null);
    }
    
    public List<Student> findByStatus(String status) {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        return studentMapper.selectList(wrapper);
    }
    
    public Optional<Student> findById(Long id) {
        return Optional.ofNullable(studentMapper.selectById(id));
    }
    
    public Student save(Student student) {
        if (student.getId() == null) {
            studentMapper.insert(student);
        } else {
            studentMapper.updateById(student);
        }
        return student;
    }
    
    public void deleteById(Long id) {
        studentMapper.deleteById(id);
    }
}
