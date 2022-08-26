package com.gaoqi.dubboprovider2.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.gaoqi.dubboprovider2.mapper.SuccessQpsMapper;
import com.gaoqi.dubboprovider2.pojo.SuccessQps;
import com.gaoqi.dubboprovider2.service.RedisLockTest;
import com.gaoqi.dubboprovider2.service.RedisLockTestImpl2;
import com.gaoqi.dubboprovider2.service.RedisLockTestImpl3;
import com.gaoqi.dubboprovider2.service.RedisLockTestImpl4;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@RestController
public class RedisLockTestController {
    @Autowired
    @Qualifier("redisLockTestImpl3")
    RedisLockTest redisLockTest;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    Redisson redisson;

    @Autowired
    SuccessQpsMapper successQpsMapper;

    @RequestMapping("/dlTest")
    public String dlTest(){
        Long start = System.currentTimeMillis();
        String res =  redisLockTest.testTask();
        Long end = System.currentTimeMillis();
        System.out.println("用时："+(end-start));
        return res;
    }
    @RequestMapping("/queryData/{id}")
    public String queryData(@PathVariable String id){
        //先查缓存
        SuccessQps successQps = (SuccessQps)redisTemplate.opsForValue().get("user:"+id);
        //缓存没有再查数据库
        if(null==successQps){
            successQps = successQpsMapper.querryData(Integer.parseInt(id));
            //数据库有，写入缓存(设置过期时间)返回结果
            if(null!=successQps){
                redisTemplate.opsForValue().set("user:"+id,successQps,10L, TimeUnit.MINUTES);
                return "success";
            }
        }
        //数据库没有返回null
        return "querry fail";
    }
    @RequestMapping(value = "/updateData1",method = RequestMethod.POST)
    public String updateData1(SuccessQps successQps){
        //先删缓存
        boolean flag = redisTemplate.delete("user:"+successQps.getUid());
        //如果删除成功就更改数据库
        if(flag){
            //更新数据库
            int res = successQpsMapper.updateData(successQps);
            //更新失败返回数据库更新失败
            if(res<=0) return "mysql fail";
            //更新成功,等待一段时间
            try{
                Thread.sleep(100);
            }catch (Exception e){
                System.out.println(e);
            }
            //再删一次缓存
            redisTemplate.delete("user:"+successQps.getUid());
            return "success";
        }
        return "redis fail";
    }
    @RequestMapping(value = "/updateData2",method = RequestMethod.POST)
    public String updateData2(SuccessQps successQps){
        return "success";
    }

}
