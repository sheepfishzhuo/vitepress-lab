package com.lab.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("project")
public class Project {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    
    private String principalInvestigator;
    
    private String members;
    
    private String funding;
    
    private String amount;
    
    private String startDate;
    
    private String endDate;
    
    private String status;
    
    private String description;
    
    private String outcomes;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
