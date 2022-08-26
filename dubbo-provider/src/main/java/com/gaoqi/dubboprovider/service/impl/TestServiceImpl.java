package com.gaoqi.dubboprovider.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.gaoqi.api.TestService;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@DubboService
@Component
public class TestServiceImpl implements TestService {

    public static final String REDIS_LOCK = "good_lock";

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private Redisson redisson;

    @Override
    @SentinelResource("testTask")
    public String testTask() {
        String res = "";
        //先加锁
        RLock lock = redisson.getLock(REDIS_LOCK);
        lock.lock();
        try {
            //加锁成功就获取库存信息
            Integer result = (Integer) redisTemplate.opsForValue().get("num");
            Integer total = result;
            //如果还有库存，就更新库存
            if (total > 0) {
                Integer realTotal = total - 1;
                redisTemplate.opsForValue().set("num", realTotal);
                System.out.println("8081服务端库存更新成功，当前库存为：" + realTotal);
                res = "8081服务端库存更新成功，当前库存为：" + realTotal;
            } else {
                //没有库存了就退出。
                System.out.println("8081服务端库存更新失败,已经没有库存了");
                res = "8081服务端库存更新失败,已经没有库存了";
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
