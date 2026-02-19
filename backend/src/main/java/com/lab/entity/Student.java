package com.lab.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("student")
public class Student {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String avatar;
    
    private String degree;
    
    private String advisor;
    
    private Integer year;
    
    @TableField("research_interests")
    private String researchInterests;
    
    private String projects;
    
    private String awards;
    
    private String email;
    
    private String biography;
    
    private String status;
    
    private Integer graduationYear;
    
    private String currentPosition;
    
    private String linkedin;
    
    private String github;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
