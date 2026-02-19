package com.lab.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("paper")
public class Paper {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    
    private String authors;
    
    private String venue;
    
    private Integer year;
    
    private String type;
    
    private String doi;
    
    @TableField("abstract_text")
    private String abstractText;
    
    private String keywords;
    
    private String pdf;
    
    private String code;
    
    private Integer citations;
    
    private Double impactFactor;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
