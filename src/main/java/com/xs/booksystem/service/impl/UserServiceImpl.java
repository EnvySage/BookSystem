package com.xs.booksystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xs.booksystem.pojo.DO.UserDO;
import com.xs.booksystem.pojo.DTO.UserDTO;
import com.xs.booksystem.pojo.VO.UserVO;
import com.xs.booksystem.service.UserService;
import com.xs.booksystem.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 敛
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-12-16 13:41:50
*/
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserVO> queryAllUsers() {
        LambdaQueryWrapper<UserDTO> queryWrapper = new LambdaQueryWrapper<>();
        List<UserDTO> userDTOList = userMapper.selectList(queryWrapper.eq(UserDTO::getStatus,1).eq(UserDTO::getRole,"USER"));
        return userDTOList.stream().map(dto -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(dto,userVO);
            return userVO;
        }).collect(Collectors.toList());

    }

    @Override
    public Integer deleteUsers(List<UserDO> users) {
        Set<Integer> ids = users.stream().map(UserDO::getId).collect(Collectors.toSet());
        LambdaQueryWrapper<UserDTO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserDTO::getId,ids);
        return userMapper.delete(queryWrapper);

    }

    @Override
    public UserVO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDTO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDTO::getUsername,username);
        UserDTO userDTO = userMapper.selectOne(queryWrapper);
        if(userDTO == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDTO,userVO);
        return userVO;
    }

    @Override
    public void insertUser(UserDO userDO) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDO,userDTO);
        userMapper.insert(userDTO);
    }
}
