package com.bit.module.pb.dao;

import com.bit.module.pb.bean.ExServiceman;
import com.bit.module.pb.vo.ExServicemanVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ExServiceman管理的Dao
 * @author 
 *
 */
@Repository
public interface ExServicemanDao {
	/**
	 * 根据条件查询ExServiceman
	 * @param exServicemanVO
	 * @return
	 */
	List<ExServiceman> findByConditionPage(ExServicemanVO exServicemanVO);
	/**
	 * 查询所有ExServiceman
	 * @return
	 */
	List<ExServiceman> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个ExServiceman
	 * @param id	 	 
	 * @return
	 */
	ExServiceman findById(@Param(value = "id") Long id);
	/**
	 * 批量保存ExServiceman
	 * @param exServicemans
	 */
	void batchAdd(List<ExServiceman> exServicemans);
	/**
	 * 保存ExServiceman
	 * @param exServiceman
	 */
	void add(ExServiceman exServiceman);
	/**
	 * 批量更新ExServiceman
	 * @param exServicemans
	 */
	void batchUpdate(List<ExServiceman> exServicemans);
	/**
	 * 更新ExServiceman
	 * @param exServiceman
	 */
	void update(ExServiceman exServiceman);
	/**
	 * 删除ExServiceman
	 * @param ids
	 */
	void batchDelete(List<Long> ids);
	/**
	 * 删除ExServiceman
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);

	/**
	 * 根据党员ID删除数据
	 * @param memberId
	 */
	void deleteByMemberId(@Param(value = "memberId") Long memberId);

	/**
	 * 根据党员ID 获取退伍信息
	 * @param memberId
	 * @return
	 */
	ExServiceman findByMemberId(@Param(value = "memberId") Long memberId);
}
