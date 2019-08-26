package com.peanut.page.service;

import com.peanut.item.entity.*;
import com.peanut.page.client.BrandClient;
import com.peanut.page.client.CategoryClient;
import com.peanut.page.client.GoodsClient;
import com.peanut.page.client.SpecificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ljn
 * @date 2019/8/21.
 */
@Service
@Slf4j
public class PageService {
    @Autowired
    GoodsClient goodsClient;

    @Autowired
    SpecificationClient specificationClient;

    @Autowired
    BrandClient brandClient;

    @Autowired
    CategoryClient categoryClient;

    @Autowired
    TemplateEngine templateEngine;

    @Value("${peanut.page.destPath}")
    private  String destPath;

    public Map<String, Object> loadModel(Long spuId) {
        Spu spu = goodsClient.querySpuById(spuId);
        List<Sku> skus = spu.getSkus();
        SpuDetail spuDetail = spu.getSpuDetail();
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        List<Category> categories = categoryClient.queryCategoryListByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<SpecGroup> specParams = specificationClient.queryGroupsByCid(spu.getCid3());
        Map<String, Object> model = new HashMap<>();
        model.put("title", spu.getTitle());
        model.put("subTitle", spu.getSubTitle());
        model.put("skus", skus);
        model.put("detail", spuDetail);
        model.put("brand", brand);
        model.put("categories", categories);
        model.put("specs", specParams);
        return model;
    }

    @Async
    public void createHtml(Long spuId) {
        // 上下文
        Context context = new Context();
        context.setVariables(loadModel(spuId));
        // 输出流
        File file = new File(destPath, spuId + ".html");
        if (file.exists()) {
            file.delete();
        }
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file, "UTF-8");
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            log.info("[静态页服务] 生成静态页发生异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteHtml(Long spuId) {
        File file = new File(destPath, spuId + ".html");
        if (file.exists()) {
            file.delete();
        }
    }
}
