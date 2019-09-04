package com.peanut.order.service.api;

import com.peanut.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "peanut-gateway", path = "/api/item")
public interface GoodsService extends GoodsApi {
}
