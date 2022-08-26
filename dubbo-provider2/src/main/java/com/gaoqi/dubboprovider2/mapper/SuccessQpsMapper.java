package com.gaoqi.dubboprovider2.mapper;

import com.gaoqi.dubboprovider2.pojo.SuccessQps;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SuccessQpsMapper {
    int insertData(SuccessQps successQps);
    List<SuccessQps> allData();
    SuccessQps querryData(int num);
    int updateData(SuccessQps successQps);
}
