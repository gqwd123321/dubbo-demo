package com.gaoqi.dubboprovider.RedisStudy;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
@Slf4j
@Lazy(value = false)
public class DelayQueueConsumer {

    @Autowired
    private RedisDelayQueue redisDelayQueue;
//定义定时任务，在一定的时间去在延时队列中取出应该被执行的任务
    //定时任务的误差
//    @Scheduled(cron = "*/1 * * * * *")
    public void consumer(){
        List<RedisTaskItem> list = redisDelayQueue.pull();
        if(null!=list){
            long currentTime = System.currentTimeMillis();
            for(RedisTaskItem item:list){
                if(currentTime>=item.getDelayTime()){
                    log.info("消费消息：{}:消息创建时间：{},消费时间：{}", JSON.toJSONString(item), item.getCreateTime(), LocalDateTime.now());
                    if(redisDelayQueue.remove(item)){
                        log.info("任务已经取出");
                    };
                }
            }
        }
        log.info("目前没有任务");
    }
}
