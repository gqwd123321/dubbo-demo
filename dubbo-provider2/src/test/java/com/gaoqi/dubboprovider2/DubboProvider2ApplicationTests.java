package com.gaoqi.dubboprovider2;

import com.gaoqi.dubboprovider2.mapper.SuccessQpsMapper;
import com.gaoqi.dubboprovider2.pojo.SuccessQps;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class DubboProvider2ApplicationTests {

    public static final String REDIS_LOCK = "good_lock";

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    SuccessQpsMapper successQpsMapper;
    @Autowired
    RedissonClient redissonClient;

    @Test
    void contextLoads() {
        SuccessQps successQps = new SuccessQps();
        successQps.setCurrentNum(2);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        successQps.setCurrentTime(sdf.format(date));
        successQpsMapper.insertData(successQps);
    }

    @Test
    void queryDataTest(){
        String id = "1";
        //先查缓存
        SuccessQps successQps = (SuccessQps)redisTemplate.opsForValue().get("user:"+id);
        //缓存没有再查数据库
        if(null==successQps){
            successQps = successQpsMapper.querryData(Integer.parseInt(id));
            //数据库有，写入缓存(设置过期时间)返回结果
            if(null!=successQps){
                redisTemplate.opsForValue().set("user:"+id,successQps,10L, TimeUnit.MINUTES);
                System.out.println("success"+successQps);
                return;
            }
        }
        System.out.println(successQps);
    }

    @Test
    void updateDataTest(){

        SuccessQps successQps = new SuccessQps();
        successQps.setUid(1);
        successQps.setCurrentNum(2);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        successQps.setCurrentTime(sdf.format(date));
        //先删缓存
        boolean flag = redisTemplate.delete("user:"+successQps.getUid());
        //如果删除成功就更改数据库
        if(flag){
            //更新数据库
            int res = successQpsMapper.updateData(successQps);
            //更新失败返回数据库更新失败
            if(res<=0) {
                System.out.println("mysql fail");
                return;
            }
            //更新成功,等待一段时间
            try{
                Thread.sleep(100);
            }catch (Exception e){
                System.out.println(e);
            }
            //再删一次缓存
            redisTemplate.delete("user:"+successQps.getUid());
            System.out.println("success");
            return;
        }
        System.out.println("redis fail");
    }

}
