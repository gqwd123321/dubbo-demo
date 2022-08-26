package com.gaoqi.dubboprovider;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisDataStructureTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void testString(){
        //添加
        redisTemplate.opsForValue().set("name","gaoqi");
        //获取
        System.out.println(redisTemplate.opsForValue().get("name"));
        //获取长度
        System.out.println(redisTemplate.opsForValue().size("name"));
        //++操作
        redisTemplate.opsForValue().increment("num");
        //--操作
        redisTemplate.opsForValue().decrement("num");
        //获取指定位置的key
        System.out.println(redisTemplate.opsForValue().get("name",0,2));
        //设置过期时间
        redisTemplate.opsForValue().set("name1","gaoqi",20, TimeUnit.MINUTES);
        //如果不存在再创建
        redisTemplate.opsForValue().setIfAbsent("name","gaoqi");
        //查看过期时间
        System.out.println(redisTemplate.opsForValue().getOperations().getExpire("name1"));
    }

    @Test
    public void testList(){
        //添加
        redisTemplate.opsForList().leftPush("list","one");
        redisTemplate.opsForList().rightPush("list","two");
        redisTemplate.opsForList().rightPush("list","three");
        //取出(可以规定一次取几个，用list接收)
        List<String> list = redisTemplate.opsForList().leftPop("list",2);
        for(String s:list){
            System.out.println(s);
        }
        redisTemplate.opsForList().rightPop("list");
        //获取长度
        redisTemplate.opsForList().size("list");
        //替换指定位置的值
        redisTemplate.opsForList().set("list",0,"1");
        //截取[index1，index2]区间的list
        redisTemplate.opsForList().trim("list",0,1);
        //获取list所有元素
        redisTemplate.opsForList().range("list",0,-1);
        //删除
        redisTemplate.opsForList().remove("list",0,"one");
    }
    @Test
    public void testSet(){
        //添加
        redisTemplate.opsForSet().add("set","aa","bb","cc","dd");
        //获取
        Set set = redisTemplate.opsForSet().members("set");
        System.out.println("set = "+set);
        //获取长度
        redisTemplate.opsForSet().size("set");
        //随机获取
        redisTemplate.opsForSet().randomMember("set");
        //判断元素是否在集合中
        System.out.println(redisTemplate.opsForSet().isMember("set","aa"));
        //移动key1的元素到key2中
        System.out.println(redisTemplate.opsForSet().move("set","bb","set2"));
        //随机弹出一个元素
        redisTemplate.opsForSet().pop("set");
        //移除（可以批量移除）
        redisTemplate.opsForSet().remove("set","aa","bb");
        //取差集（可以和任意集合取，比如list）
        redisTemplate.opsForSet().difference("set","set1");
        //取交集
        redisTemplate.opsForSet().intersect("set","set1");
        //取合集
        redisTemplate.opsForSet().union("set","set1");
    }
    @Test
    public void testZset(){
//        //添加
        redisTemplate.opsForZSet().add("zSet","a",1);
        redisTemplate.opsForZSet().add("zSet","b",1);
        redisTemplate.opsForZSet().add("zSet","c",1);
//        //获取
        Set zSet = redisTemplate.opsForZSet().range("zSet",0,-1);
        System.out.println(zSet);
        //根据成员获取元素（这里还需要探索）
        RedisZSetCommands.Range range = new RedisZSetCommands.Range();
        range.lt("b");
        zSet = redisTemplate.opsForZSet().rangeByLex("zSet",range);
        System.out.println(zSet);
        //根据分数获取元素
        zSet = redisTemplate.opsForZSet().rangeByScore("zSet",1,2);
        System.out.println(zSet);
        //获取分数
        redisTemplate.opsForZSet().score("aaa",1);
        //+分
        redisTemplate.opsForZSet().incrementScore("zSet","a",1);
        //获取大小
        redisTemplate.opsForZSet().size("zSet");
        //获取大小2
        redisTemplate.opsForZSet().zCard("zSet");
        //统计区间数量
        redisTemplate.opsForZSet().count("zSet",0,1);
        //获取指定元素在集合中的索引
        Long rank = redisTemplate.opsForZSet().rank("zSet","a");
        //获取倒序的索引
        rank = redisTemplate.opsForZSet().rank("zSet","a");
        //移除
        redisTemplate.opsForZSet().remove("zSet","a");
        //根据分数移除
        redisTemplate.opsForZSet().removeRangeByScore("zSet",1,2);
    }
    @Test
    public void testHash(){
        //添加
        redisTemplate.opsForHash().put("hashValue","map1","map1-1");
        //获取值
        List<Object>list=redisTemplate.opsForHash().values("hashValue");
        //获取键值对
        Map<Object,Object> map = redisTemplate.opsForHash().entries("hashValue");
        //判断键的值是否存在，如果存在返回该值，不存在返回null
        Object mapValue=redisTemplate.opsForHash().get("hashValue","map1");
        //判断是否有指定的键
        boolean flag = redisTemplate.opsForHash().hasKey("hashValue","map1");
        //获取键
        Set<Object>keySet = redisTemplate.opsForHash().keys("hashValue");
        //删除
        redisTemplate.opsForHash().delete("hashValue","map1");

    }
    //对比zrange 与 zrangebyscore的效率
    @Test
    public void taskZrange(){
        //先放1w条数据到zset里（用了一个小时，这是不行的）
//        long startTime = System.currentTimeMillis();
//        for(int i=0;i<100000;i++){
//            redisTemplate.opsForZSet().add("zset",i,i);
//        }
//        long endTime = System.currentTimeMillis();
//        System.out.println("插入10w条数据用时："+(endTime-startTime));
        //取100条，计算时间
//        long startTime = System.currentTimeMillis();
//        redisTemplate.opsForZSet().rangeByScore("zset",0,100000,90000,100000);
//        long endTime = System.currentTimeMillis();
//        System.out.println("取10条数据用时："+(endTime-startTime));
        //取1000条计算时间
//        long startTime = System.currentTimeMillis();
//        redisTemplate.opsForZSet().rangeByScore("zset",0,System.currentTimeMillis(),0,1000);
//        long endTime = System.currentTimeMillis();
//        System.out.println("取1000条数据用时："+(endTime-startTime));
        //取所有，计算时间
        long startTime = System.currentTimeMillis();
        redisTemplate.opsForZSet().rangeByScore("zset",0,100000,0,1000);
        long endTime = System.currentTimeMillis();
        System.out.println("取全部数据用时："+(endTime-startTime));
    }
}
