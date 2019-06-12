package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.User;
import com.bit.module.system.vo.RefreshTokenVO;
import com.bit.module.system.vo.UserVO;
import com.bit.module.system.vo.VaildCodeVO;

import java.util.List;

public interface UserService {

  /**
   * 登陆
   * @param userVO
   * @return
   */
  public BaseVo login(UserVO userVO) ;

  /**
   *
   * @param userVO
   * @return
   */
  public BaseVo list(UserVO userVO) ;

  /**
   * 分页查询
   * @param userVO
   * @return
   */
  BaseVo findByConditionPage(UserVO userVO);

  /**
   * 查看
   * @param id
   * @return
   */
  User findById(Long id);

  /**
   * 保存
   * @param user
   */
  void add(User user);

  /**
   * 修改
   * @param user
   */
  void update(User user);

  /**
   * 停止启动
   * @param user
   */
  void switchUser(User user);

  /**
   * 重置密码
   * @param id
   */
  void resetPassword(Long id) ;

  /**
   * 业务 查看
   * @param id
   * @return
   */
  User findRealById(Long id);

  /**
   * 校验username唯一
   * @param username
   * @return
   */
  int checkUsername(String username);

  /**
   * 注册
   * @param user
   * @return
   */
    BaseVo registerUser(User user);

  /**
   * 验证用户是否是党员
   * @param user
   * @return
   */
  BaseVo verifyUser(User user);

  /**
   * 发送短信验证码
   * @param user
   * @return
   */
  BaseVo sendVaildCode(User user);

  /**
   * 校验验证码是否正确
   * @param vaildCodeVO
   * @return
   */
  BaseVo verifyVaildCode(VaildCodeVO vaildCodeVO);

  /**
   * 党建app add方法
   * @param user
   * @return
   */
  BaseVo partyBuildAdd(User user);

  /**
   * 刷新token
   * @param refreshTokenVO
   * @return
   */
  BaseVo refreshToken(RefreshTokenVO refreshTokenVO);

  /**
   * 查询所有
   * @return
   */
  List<User> findAll();
}
