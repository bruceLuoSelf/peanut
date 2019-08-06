package com.peanut.item.service;

import com.peanut.item.entity.SpecGroup;

import java.util.List;

/**
 * @author ljn
 * @date 2019/8/6.
 */
public interface ISpecGroupService {


    List<SpecGroup> queryGroupByCid(Long cid);

    void addSpecGroup(SpecGroup specGroup);

    void deleteSpecGroup(Long id);

    void updateSpecGroup(SpecGroup specGroup);
}