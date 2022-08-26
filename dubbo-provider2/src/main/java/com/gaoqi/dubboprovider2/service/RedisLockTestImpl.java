package com.gaoqi.dubboprovider2.service;


import com.gaoqi.dubboprovider2.mapper.SuccessQpsMapper;
import com.gaoqi.dubboprovider2.pojo.SuccessQps;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class RedisLockTestImpl implements RedisLockTest{


    public static final String REDIS_LOCK = "good_lock";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    SuccessQpsMapper successQpsMapper;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    private Redisson redisson;

    @Override
//    @Transactional
    public String testTask() {
        String res = "";
        //使用redission加锁
        RLock lock = redisson.getLock(REDIS_LOCK);
        lock.lock();
        //加锁成功就获取库存信息
        Integer result = (Integer) redisTemplate.opsForValue().get("num");
        try {
            //如果还有库存，就更新库存
            if (result > 0) {
                Integer realTotal = result - 1;
                //使用redission操作
                redisTemplate.opsForValue().set("num", realTotal);
                System.out.println("库存更新成功，当前库存为：" + realTotal);
                //更新成功就插入数据库
                SuccessQps successQps = new SuccessQps();
                successQps.setCurrentNum(realTotal);
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
                successQps.setCurrentTime(sdf.format(date));
                successQpsMapper.insertData(successQps);


                res = "库存更新成功，当前库存为：" + realTotal;
            } else {
                //没有库存了就退出。
                System.out.println("库存更新失败,已经没有库存了");
                res = "库存更新失败,已经没有库存了";
            }
            return res;
        }  finally {
            //释放锁
            if(lock.isLocked()&&lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }
}
