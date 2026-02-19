package com.lab.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("config_option")
public class ConfigOption {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("config_type")
    private String configType;
    
    @TableField("option_value")
    private String optionValue;
    
    @TableField("option_label")
    private String optionLabel;
    
    @TableField("sort_order")
    private Integer sortOrder = 0;
}
