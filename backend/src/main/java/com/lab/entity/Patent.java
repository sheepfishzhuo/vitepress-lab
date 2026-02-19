package com.lab.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("patent")
public class Patent {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    
    private String type;
    
    private String inventors;
    
    private String patentNo;
    
    private String status;
    
    private String date;
    
    private String description;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
