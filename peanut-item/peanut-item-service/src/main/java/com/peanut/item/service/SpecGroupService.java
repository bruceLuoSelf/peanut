package com.peanut.item.service;

import com.peanut.enums.ExceptionEnum;
import com.peanut.exception.PeanutException;
import com.peanut.item.dao.mapper.SpecGroupMapper;
import com.peanut.item.dao.mapper.SpecParamMapper;
import com.peanut.item.entity.SpecGroup;
import com.peanut.item.entity.SpecParam;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author ljn
 * @date 2019/8/6.
 */
@Service
public class SpecGroupService implements ISpecGroupService {

    @Autowired
    SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    @Override
    public List<SpecGroup> queryGroupByCid(Long cid) {
        Example example = new Example(SpecGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cid", cid);
        List<SpecGroup> specGroups = specGroupMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(specGroups)) {
            throw new PeanutException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return specGroups;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSpecGroup(SpecGroup specGroup) {
        specGroupMapper.insert(specGroup);
    }

    @Override
    public void deleteSpecGroup(Long id) {
        specGroupMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateSpecGroup(SpecGroup specGroup) {
        specGroupMapper.updateByPrimaryKey(specGroup);
    }

    @Override
    public List<SpecParam> queryGroupParams(Long gid) {
        List<SpecParam> list = specParamMapper.querySpecParamByGroupId(gid);
        if (CollectionUtils.isEmpty(list)) {
            throw new PeanutException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return list;
    }
}
