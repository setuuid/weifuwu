package com.bit.module.system.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.bit.module.system.bean.UserRelPbOrg;
import com.bit.module.system.vo.UserRelPbOrgVO;
import org.springframework.stereotype.Repository;

/**
 * UserRelPbOrg管理的Dao
 * @author 
 *
 */
@Repository
public interface UserRelPbOrgDao {

	/**
	 * 根据条件查询UserRelPbOrg
	 * @param userRelPbOrgVO
	 * @return
	 */
	List<UserRelPbOrg> findByConditionPage(UserRelPbOrgVO userRelPbOrgVO);

	/**
	 * 查询所有UserRelPbOrg
	 * @return
	 */
	List<UserRelPbOrg> findAll(@Param(value = "sorter") String sorter);

	/**
	 * 通过主键查询单个UserRelPbOrg
	 * @param id	 	 
	 * @return
	 */
	UserRelPbOrg findById(@Param(value = "id") Long id);

	/**
	 * 保存UserRelPbOrg
	 * @param userRelPbOrg
	 */
	void add(UserRelPbOrg userRelPbOrg);

	/**
	 * 更新UserRelPbOrg
	 * @param userRelPbOrg
	 */
	void update(UserRelPbOrg userRelPbOrg);

	/**
	 * 批量删除
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 * 删除UserRelPbOrg
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);

	/**
	 * 批量保存
	 * @param userRelPbOrgs
	 */
    void batchAdd(@Param(value = "list")List<UserRelPbOrg> userRelPbOrgs);

	/**
	 * 更新用户id 删除
	 * @param userId
	 */
    void delByUserId(@Param(value = "userId") Long userId);

	/**
	 * 根据组织id查询统计
	 * @param pborgId
	 * @return
	 */
	int findCountByPbId(@Param(value = "pborgId")Long pborgId);

	/**
	 * 根据用户id查询
	 * @param userId
	 * @return
	 */
    List<UserRelPbOrg> findByUserId(@Param(value = "userId")Long userId);
}
