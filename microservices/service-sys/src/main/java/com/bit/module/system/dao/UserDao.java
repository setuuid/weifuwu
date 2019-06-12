package com.bit.module.system.dao;

import com.bit.module.system.bean.User;
import com.bit.module.system.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User管理的Dao
 * @author 
 *
 */
@Repository
public interface UserDao {

	/**
	 * 根据条件查询User
	 * @param userVO
	 * @return
	 */
	List<User> findByConditionPage(UserVO userVO);

	/**
	 * 查询所有User
	 * @return
	 */
	List<User> findAll(@Param(value = "sorter") String sorter);

	/**
	 * 通过主键查询单个User
	 * @param id	 	 
	 * @return
	 */
	User findById(@Param(value = "id") Long id);

	/**
	 * 保存User
	 * @param user
	 */
	void add(User user);

	/**
	 * 更新User
	 * @param user
	 */
	void update(User user);

	/**
	 * 删除User
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);


	/**
	 * 根据用户名查询用户信息
	 * @param username
	 * @return
	 */
	User findByUsername(@Param(value = "username") String username);

	/**
	 * 停止启动
	 * @param user
	 */
	void switchUser(User user);

	/**
	 * 重置密码
	 * @param user
	 */
	void resetPassword(User user);

	/**
	 * 校验用户名唯一
	 * @param username
	 * @return
	 */
	int findCountByUsername(@Param(value = "username") String username);

}
