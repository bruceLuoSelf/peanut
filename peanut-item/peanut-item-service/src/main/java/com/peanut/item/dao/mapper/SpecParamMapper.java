package com.peanut.item.dao.mapper;

import com.peanut.item.entity.SpecParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SpecParamMapper extends Mapper<SpecParam> {

    @Select("SELECT * FROM tb_spec_param where group_id = #{id}")
    List<SpecParam> querySpecParamByGroupId(@Param("id")Long id);

    List<SpecParam> selectByMap(Map<String, Object> map);
}