package com.peanut.search.client;

import com.peanut.item.entity.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author ljn
 * @date 2019/8/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;

    @Test
    public void test1() {
        List<Category> categories = categoryClient.queryCategoryListByIds(Arrays.asList(1L, 2L, 3L));
        for (Category category : categories) {
            System.out.println("category = " + category);
        }
    }

}