package com.gaoqi.dubboprovider.RedisStudy;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class RedisTaskItem {
    //任务的id
    private String id;
    //任务的内容
    private String msg;
    //延时时间（当前时间+延迟时间）
    private Long delayTime;
    //创建时间
    private LocalDateTime createTime;

}
