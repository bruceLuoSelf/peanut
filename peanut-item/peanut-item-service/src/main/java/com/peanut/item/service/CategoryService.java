package com.peanut.item.service;

import com.peanut.enums.ExceptionEnum;
import com.peanut.exception.PeanutException;
import com.peanut.item.dao.mapper.CategoryMapper;
import com.peanut.item.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据parentId查询商品分类
     * @param parentId
     * @return
     */
    @Override
    public List<Category> getCategoryList(Long parentId) {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", parentId);
        List<Category> categoryList = categoryMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(categoryList)) {
            throw new PeanutException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return categoryList;
    }
}
