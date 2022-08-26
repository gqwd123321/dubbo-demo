package com.gaoqi.dubboprovider.RedisStudy;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component

public class DelayQueueProducter {

    @Autowired
    private RedisDelayQueue redisDelayQueue;

    public void sentTaskMessage(String msg,long delay){
        if(msg!=null){
            //封装任务
            String id = UUID.randomUUID().toString().replace("-","");
            RedisTaskItem redisTaskItem = new RedisTaskItem();
            long time = System.currentTimeMillis();
            LocalDateTime createTime = Instant.ofEpochMilli(time).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            redisTaskItem.setDelayTime(time+(delay*1000));
            redisTaskItem.setCreateTime(createTime);
            redisTaskItem.setMsg(msg);
            redisTaskItem.setId(id);
            //将任务加入延时队列
            redisDelayQueue.push(redisTaskItem);
        }
    }
}
