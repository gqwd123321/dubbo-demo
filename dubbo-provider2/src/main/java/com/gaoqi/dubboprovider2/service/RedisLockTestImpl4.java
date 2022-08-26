package com.gaoqi.dubboprovider2.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisLockTestImpl4 implements RedisLockTest{
    public static final String REDIS_LOCK = "good_lock";
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public String testTask() {
        String res = "";
        //生成一个唯一的uuid，为加锁提供
        String value = UUID.randomUUID().toString().replace("-", "");
        try {
            //设置过期时间
            Boolean flag = redisTemplate.opsForValue().setIfAbsent(REDIS_LOCK, value, 10L, TimeUnit.SECONDS);

            //加锁失败
            if (!flag) {
                System.out.println("抢锁失败");
                //自旋
                Thread.sleep(500);
                testTask();
            }
            //加锁成功就获取库存信息
            Integer result = (Integer) redisTemplate.opsForValue().get("num");
            Integer total = result;
            //如果还有库存，就更新库存
            if (total > 0) {
                Integer realTotal = total - 1;
                redisTemplate.opsForValue().set("num", realTotal);
                System.out.println("服务端库存更新成功，当前库存为：" + realTotal);
                res = "服务端库存更新成功，当前库存为：" + realTotal;
            } else {
                //没有库存了就退出。
                System.out.println("服务端库存更新失败,已经没有库存了");
                res = "服务端库存更新失败,已经没有库存了";
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //定义一个lua脚本，用来解锁。
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] " +
                    "then return redis.call('del', KEYS[1]) " +
                    "else return 0 " +
                    "end";
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptText(script);
            redisScript.setResultType(Long.class);
            Long eval = (Long) redisTemplate.execute(redisScript, Arrays.asList(REDIS_LOCK), value);
            if ("1".equals(eval.toString())) {
                System.out.println("服务端删除锁成功");
            } else {
                System.out.println("服务端删除锁失败");
            }
        }
        return res;
    }
}
