package com.lab.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("conference")
public class Conference {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String location;
    
    private String date;
    
    private String type;
    
    private String participants;
    
    private String description;
    
    private String link;
    
    private String image;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
