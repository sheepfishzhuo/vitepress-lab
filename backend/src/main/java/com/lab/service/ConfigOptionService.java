package com.lab.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lab.entity.ConfigOption;
import com.lab.mapper.ConfigOptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConfigOptionService {
    
    @Autowired
    private ConfigOptionMapper configOptionMapper;
    
    public List<ConfigOption> findByType(String configType) {
        QueryWrapper<ConfigOption> wrapper = new QueryWrapper<>();
        wrapper.eq("config_type", configType).orderByAsc("sort_order");
        return configOptionMapper.selectList(wrapper);
    }
    
    public Map<String, List<ConfigOption>> findAllGrouped() {
        List<ConfigOption> all = configOptionMapper.selectList(null);
        return all.stream().collect(Collectors.groupingBy(ConfigOption::getConfigType));
    }
    
    public ConfigOption save(ConfigOption option) {
        if (option.getId() == null) {
            configOptionMapper.insert(option);
        } else {
            configOptionMapper.updateById(option);
        }
        return option;
    }
    
    public void deleteById(Long id) {
        configOptionMapper.deleteById(id);
    }
    
    public void deleteByTypeAndValue(String configType, String optionValue) {
        QueryWrapper<ConfigOption> wrapper = new QueryWrapper<>();
        wrapper.eq("config_type", configType).eq("option_value", optionValue);
        configOptionMapper.delete(wrapper);
    }
}
