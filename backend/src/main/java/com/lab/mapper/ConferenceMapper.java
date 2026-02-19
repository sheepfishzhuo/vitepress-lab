package com.lab.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.entity.Conference;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConferenceMapper extends BaseMapper<Conference> {
}
