package com.bit.module.system.controller;

import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.RoleRelResource;
import com.bit.module.system.bean.User;
import com.bit.module.system.service.UserService;
import com.bit.module.system.vo.RefreshTokenVO;
import com.bit.module.system.vo.ResourceVO;
import com.bit.module.system.vo.UserVO;
import com.bit.module.system.vo.VaildCodeVO;
import com.bit.utils.thread.RequestThreadBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@RestController
@RefreshScope
@RequestMapping(value = "/user")
public class UserController {


    @Autowired
    private UserService userService;

    /**
     * 登陆
     * @param userVO
     * @return
     */
    @PostMapping(value = "/login")
    public BaseVo login(@RequestBody UserVO userVO){
        return  userService.login(userVO);
    }

    /**
     * 分页模糊查询
     * @param userVO
     * @return
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody UserVO userVO) {
        return userService.findByConditionPage(userVO);
    }

    /**
     * 根据主键ID查询
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        User user = userService.findById(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(user);
        return baseVo;
    }

    /**
     * 根据主键ID查询--业务
     * @param id
     * @return
     */
    @GetMapping("/findRealById/{id}")
    public BaseVo findRealById(@PathVariable(value = "id") Long id) {
        User user = userService.findRealById(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(user);
        return baseVo;
    }

    /**
     * 新增
     * @param user
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody User user)  {
        userService.add(user);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 修改
     * @param user
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody User user) {
        userService.update(user);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 停止启动
     * @param
     * @return
     */
    @PostMapping("/switchUser")
    public BaseVo switchUser(@RequestBody User user) {
        userService.switchUser(user);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 重置密码
     * @param
     * @return
     */
    @GetMapping("/resetPassword/{id}")
    public BaseVo resetPassword(@PathVariable(value = "id") Long id) {
        userService.resetPassword(id);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 校验用户名唯一
     * @param
     * @return
     */
    @GetMapping("/checkUsername/{username}")
    public BaseVo checkUsername(@PathVariable(value = "username") String username) {
        int count = userService.checkUsername(username);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(count);
        return baseVo;
    }

    /**
     * 注册用户
     * @param
     * @return
     */
    @PostMapping("/registerUser")
    public BaseVo registerUser(@RequestBody User user) {
       return userService.registerUser(user);
    }

    /**
     * 验证用户是否是党员
     * @param user
     * @return
     * @author liuyancheng
     */
    @PostMapping("/verifyUser")
    public BaseVo verifyUser(@RequestBody User user){
        return userService.verifyUser(user);
    }

    /**
     * 发送验证码业务
     * @param  user
     * @return
     * @author liuyancheng
     */
    @PostMapping("/sendVaildCode")
    public BaseVo sendVaildCode(@RequestBody User user){
        if (user.getMobile() == null) {
            throw new BusinessException("请输入手机号");
        }else {
            return userService.sendVaildCode(user);
        }
    }

    /**
     * 验证验证码是否正确
     * @param  vaildCodeVO
     * @return
     * @author liuyancheng
     */
    @PostMapping("/verifyVaildCode")
    public BaseVo verifyVaildCode(@RequestBody VaildCodeVO vaildCodeVO){
        return userService.verifyVaildCode(vaildCodeVO);
    }

    /**
     * 党建app新增
     * @param user
     * @return
     * @author liuyancheng
     */
    @PostMapping("/partyBuildAdd")
    public BaseVo partyBuildAdd(@RequestBody User user){
        return userService.partyBuildAdd(user);
    }

    /**
     * 刷新token
     * @param refreshTokenVO
     * @return
     * @author liuyancheng
     */
    @PostMapping("/refreshToken")
    public BaseVo refreshToken(@RequestBody RefreshTokenVO refreshTokenVO){
        return userService.refreshToken(refreshTokenVO);
    }
}
