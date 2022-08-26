package com.gaoqi.dubboprovider2.service;

import org.redisson.Redisson;
import org.redisson.api.RScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class RedisLockTestImpl3 implements RedisLockTest{

    private static final String STOCK_LUA;

    private static final String STORE = "num";

    static {
        StringBuilder s = new StringBuilder();
        s.append("local isExist = redis.call('exists', KEYS[1]); ");
        s.append("if (tonumber(isExist) > 0) then ");
        s.append("      local goodsNumber = redis.call('get', KEYS[1]);  ");
        s.append("      if (tonumber(goodsNumber) > 0) then ");
        s.append("          redis.call('decr',KEYS[1]);   ");
        s.append("          return 1;   ");
        s.append("      else "
                + "redis.call('del', KEYS[1]);    ");
        s.append("          return 0; ");
        s.append("          end; ");
        s.append("else ");
        s.append("      return -1; ");
        s.append("      end;");
        STOCK_LUA = s.toString();
    }

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private Redisson redisson;

    @Override
    public String testTask() {

        //返回结果初始化
        String res = "";
        //根据lua脚本执行结果处理方法返回结果
//        String value = UUID.randomUUID().toString().replace("-", "");
        String value = null;
        String stock_key = "stock";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(STOCK_LUA);
        redisScript.setResultType(Long.class);
        Long eval = (Long) redisTemplate.execute(redisScript, Arrays.asList(STORE), value);
        if("1".equals(eval.toString())){
           res = "库存扣减成功";
        }
        if("0".equals(eval.toString())){
           res = "库存扣减失败，库存已经为空";
        }
        if("-1".equals(eval.toString())){
            res = "库存为空，无法扣减";
        }
//        try {
//
//            Object eval = redisson.getScript().eval(RScript.Mode.READ_WRITE, STOCK_LUA, RScript.ReturnType.VALUE, Collections.singletonList(stock_key));
//            int num = Integer.parseInt("" + eval);
//            if(num == 1){
//                res = "库存扣减成功";
//            }
//            if(num==0){
//                res = "库存扣减失败，库存已经为空";
//            }
//            if(num==-1){
//                res = "库存为空，无法扣减";
//            }
//            System.out.println("eval=" + eval);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return res;
    }
}
