package com.peanut.search.client;

import com.peanut.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author ljn
 * @date 2019/8/12.
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
