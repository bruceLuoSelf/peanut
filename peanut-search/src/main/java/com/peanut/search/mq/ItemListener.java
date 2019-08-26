package com.peanut.search.mq;

import com.peanut.search.service.ISearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ljn
 * @date 2019/8/26.
 */
@Component
public class ItemListener {

    @Autowired
    ISearchService searchService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name="search.item.insert.queue", durable = "true"),
            exchange = @Exchange(name="peanut.item.exchange", type = ExchangeTypes.TOPIC),
            key={"item.insert", "item.update"}
    ))
    public void listenInsertOrUpdate(Long spuId) {
        if (spuId == null) {
            return;
        }
        // 处理消息，对索引库进行新增或修改
        searchService.createOrUpdateIndex(spuId);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name="search.item.delete.queue", durable = "true"),
            exchange = @Exchange(name="peanut.item.exchange", type = ExchangeTypes.TOPIC),
            key={"item.delete"}
    ))
    public void listenDelete(Long spuId) {
        if (spuId == null) {
            return;
        }
        // 处理消息，对索引库进行删除
        searchService.deleteIndex(spuId);
    }
}
