package com.gaoqi.dubboprovider2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class RedisLockTestImpl2 implements RedisLockTest{


    Lock lock = new ReentrantLock();

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public String testTask() {
        String res = "";
        lock.lock();
        try{
            Integer result = (Integer) redisTemplate.opsForValue().get("num");
            Integer total = result;
            //如果还有库存，就更新库存
            if (total > 0) {
                Integer realTotal = total - 1;
                redisTemplate.opsForValue().set("num", realTotal);
                System.out.println("库存更新成功，当前库存为：" + realTotal);
                res = "库存更新成功，当前库存为：" + realTotal;
            } else {
                //没有库存了就退出。
                System.out.println("库存更新失败,已经没有库存了");
                res = "库存更新失败,已经没有库存了";
            }
            return res;
        }catch (Exception e){
            lock.unlock();
        }finally {
            lock.unlock();
        }
        return res;
    }
}
