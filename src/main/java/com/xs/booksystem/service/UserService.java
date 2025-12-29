package com.xs.booksystem.service;

import com.xs.booksystem.pojo.DO.UserDO;
import com.xs.booksystem.pojo.DTO.UserDTO;
import com.xs.booksystem.pojo.VO.UserVO;

import java.util.List;


/**
* @author 敛
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-12-16 13:41:50
*/
public interface UserService{

    List<UserVO> queryAllUsers();

    Integer deleteUsers(List<UserDO> users);

    UserVO getUserByUsername(String username);

    void insertUser(UserDO userDO);
}
