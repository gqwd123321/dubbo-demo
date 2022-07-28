package com.gaoqi.dubboprovider.RedisStudy;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

@Component
@Lazy(value = false)
@AllArgsConstructor
public class DelayQueueProducter {
    private RedisDelayQueue redisDelayQueue;

    public void sentTaskMessage(String msg,long delay){
        if(msg!=null){
            String id = UUID.randomUUID().toString().replace("-","");
            RedisTaskItem redisTaskItem = new RedisTaskItem();
            long time = System.currentTimeMillis();
            LocalDateTime createTime = Instant.ofEpochMilli(time).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            redisTaskItem.setDelayTime(time+(delay*1000));
            redisTaskItem.setCreateTime(createTime);
            redisTaskItem.setMsg(msg);
            redisTaskItem.setId(id);
            redisDelayQueue.push(redisTaskItem);
        }
    }
}
