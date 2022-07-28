package com.gaoqi.api;

public interface UserService {
    String queryUserById(Integer userId);
    String queryUserByUsername(String username);
}
