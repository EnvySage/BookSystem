package com.xs.booksystem.controller;

import com.xs.booksystem.pojo.DO.UserDO;
import com.xs.booksystem.pojo.Result;
import com.xs.booksystem.pojo.VO.UserVO;
import com.xs.booksystem.service.UserService;
import com.xs.booksystem.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sign")
public class LoginRegisterController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;
    /**
     * 注册
     * @param userDO
     * username 用户名
     * password 密码
     * phone 手机号
     * @return
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody UserDO userDO){
        String username = userDO.getUsername();
        if (username == null || username.isEmpty()) return Result.error("用户名不能为空");
        String password = userDO.getPassword();
        if (password == null || password.isEmpty()) return Result.error("密码不能为空");
        if (userService.getUserByUsername(username)!= null){
            return Result.error("用户已存在");
        }
        userDO.setRole("USER");
        userDO.setStatus(1);
        userService.insertUser(userDO);
        return Result.success(userDO.getUsername() + "注册成功");
    }

    /**
     * 登录
     * @param userDO
     * username 用户名
     * password 密码
     * @return token
     */
    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody UserDO userDO){
        String username = userDO.getUsername();
        String password = userDO.getPassword();
        if (username == null || username.isEmpty()) return Result.error("用户名不能为空");
        if (password == null || password.isEmpty()) return Result.error("密码不能为空");
        UserVO userVO = userService.getUserByUsername(username);
        if (userVO == null) return Result.error("用户不存在");
        String token =jwtUtils.generateToken(userVO.getId(), username);
        userVO.setToken(token);
        return Result.success(userVO);
    }

    /**
     * 验证token
     * @param
     * @return
     */
    @PostMapping("/token")
    public Result<UserVO> token(String token){
        boolean validate = jwtUtils.validateToken(token);
        if (validate){
            UserVO userVO = userService.getUserById(jwtUtils.getUserIdFromToken(token));
            return Result.success(userVO);
        }
        else {
            return Result.error("token可能过期");
        }
    }

}
