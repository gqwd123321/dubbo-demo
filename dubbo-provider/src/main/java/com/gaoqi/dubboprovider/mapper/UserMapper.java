package com.gaoqi.dubboprovider.mapper;

import com.gaoqi.dubboprovider.models.User;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Repository
@Mapper
public interface UserMapper {
    User queryUserById(Integer id);
    List<User> queryUserByUsername(String username);
}
