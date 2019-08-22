package com.peanut.page.client;

import com.peanut.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author ljn
 * @date 2019/8/12.
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
