package com.xs.booksystem.mapper;

import com.xs.booksystem.pojo.DTO.SystemConfigDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 敛
* @description 针对表【system_config(系统配置表)】的数据库操作Mapper
* @createDate 2025-12-16 13:41:50
* @Entity com.xs.booksystem.pojo.DTO.SystemConfig
*/
@Mapper
public interface SystemConfigMapper extends BaseMapper<SystemConfigDTO> {


}
