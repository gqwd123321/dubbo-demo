package com.gaoqi.dubboprovider.RedisStudy;


import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class RedisDelayQueue {

    @Autowired
    private RedisTemplate redisTemplate;
    public static final String queueKey = "queueTest";


    //插入任务
    public Boolean push(RedisTaskItem redisTaskItem){
        String s = JSONObject.toJSONString(redisTaskItem);
        Boolean addFlag = redisTemplate.opsForZSet().add(queueKey,s,redisTaskItem.getDelayTime());
        return addFlag;
    }

    //取出任务
    public Boolean remove(RedisTaskItem redisTaskItem){
        String s = JSONObject.toJSONString(redisTaskItem);
        Long tag = redisTemplate.opsForZSet().remove(queueKey,s);
        if(tag>0){
            System.out.println("任务已经删除");
        }
        return tag>0?true:false;
    }
    //获取zset中的任务
    public List<RedisTaskItem> pull(){
        //性能如何？可以用，效率取决于count的大小，控制好count的大小即可。
        Set<String> strings = redisTemplate.opsForZSet().rangeByScore(queueKey,0,System.currentTimeMillis(),0,10);
        if(strings==null){
            return null;
        }
        List<RedisTaskItem>list = new ArrayList<>();
        for(String task:strings){
            list.add(JSONObject.parseObject(task,RedisTaskItem.class));
        }
        return list;
    }
}
