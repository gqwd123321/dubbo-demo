package com.gaoqi.dubboprovider;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class DistributedLockTest {
    public static final String REDIS_LOCK = "good_lock";


    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void dlTest(){
        //进行50次该操作
        for(int i=0;i<50;i++){
            //先加锁
            String value = UUID.randomUUID().toString().replace("-","");

            try{
                //设置过期时间
                Boolean flag = redisTemplate.opsForValue().setIfAbsent(REDIS_LOCK,value,10L, TimeUnit.SECONDS);

                //加锁失败
                if(!flag){
                    System.out.println("抢锁失败");
//                    //自旋
                    Thread.sleep(500);
                    dlTest();
                }
                Integer result = (Integer)redisTemplate.opsForValue().get("num");
                Integer total = result;
                if(total>0){
                    Integer realTotal = total-1;
                    redisTemplate.opsForValue().set("num",realTotal);
                    System.out.println("8081服务端库存更新成功，当前库存为："+realTotal);
                }else{
                    System.out.println("8081服务端库存更新失败,已经没有库存了");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //定义一个lua脚本
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] " +
                        "then return redis.call('del', KEYS[1]) " +
                        "else return 0 " +
                        "end";
                DefaultRedisScript<Long>redisScript=new DefaultRedisScript<>();
                redisScript.setScriptText(script);
                redisScript.setResultType(Long.class);
                Long eval = (Long) redisTemplate.execute(redisScript, Arrays.asList(REDIS_LOCK),value);
                if("1".equals(eval.toString())){
                    System.out.println("8081服务端删除锁成功");
                }else{
                    System.out.println("8081服务端删除锁失败");
                }
            }
        }
    }
}
