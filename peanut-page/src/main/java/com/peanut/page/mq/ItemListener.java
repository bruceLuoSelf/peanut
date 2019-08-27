package com.peanut.page.mq;

import com.peanut.page.service.PageService;
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
    PageService pageService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name="page.item.insert.queue", durable = "true"),
            exchange = @Exchange(name="peanut.item.exchange", type = ExchangeTypes.TOPIC),
            key={"item.insert", "item.update"}
    ))
    public void listenInsertOrUpdate(Long spuId) {
        if (spuId == null) {
            return;
        }
        // 处理消息，创建静态页
        pageService.createHtml(spuId);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name="page.item.delete.queue", durable = "true"),
            exchange = @Exchange(name="peanut.item.exchange", type = ExchangeTypes.TOPIC),
            key={"item.delete"}
    ))
    public void listenDelete(Long spuId) {
        if (spuId == null) {
            return;
        }
        // 处理消息，对页面进行删除
        pageService.deleteHtml(spuId);
    }
}
