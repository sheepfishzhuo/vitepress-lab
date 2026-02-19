package com.lab.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("faculty")
public class Faculty {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String title;
    
    private String role;
    
    private String avatar;
    
    private String email;
    
    private String phone;
    
    private String office;
    
    @TableField("research_interests")
    private String researchInterests;
    
    private String education;
    
    private String biography;
    
    private String googleScholar;
    
    private String researchGate;
    
    private String orcid;
    
    private String personal;
    
    private String github;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
