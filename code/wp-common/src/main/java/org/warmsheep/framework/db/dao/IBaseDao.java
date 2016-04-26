package org.warmsheep.framework.db.dao;

import java.util.List;
import java.util.Map;

import org.warmsheep.framework.db.exception.BaseCoreException;
import org.warmsheep.framework.page.PageBean;
import org.warmsheep.framework.page.PageParam;

/**
 * 数据访问层操作支撑接口.
 * 
 */
public interface IBaseDao<T>
{
	/**
	 * 获取Mapper sql命名空间
	 * 
	 */
	String getStatement(String sqlId);
	
	/**
	 * 数据记录存入数据库
	 * 
	 * @param dtRecord 数据记录
	 */
	long insert(T dtRecord) throws BaseCoreException;
	
	/**
	 * 数据块批量存入数据库
	 * 
	 * @param dtRecords 数据记录块
	 */
	int insertBatch(List<T> dtRecords) throws BaseCoreException;
	
	int insertBatch(Map<String, Object> dtRecords, String sqlId) throws BaseCoreException;
	
	/**
	 * 操作数据记录更新到数据库
	 * 
	 * @param dtRecord 数据记录
	 */
	int updateById(T dtRecord) throws BaseCoreException;
	
	/**
	 * 操作数据记录更新到数据库
	 * 
	 * @param dtRecord 数据记录
	 * @param nodeId 需要执行Sql配置节点id，sqlNodeId为空使用默认：SQL_UPDATE_BY
	 */
	int updateBy(T dtRecord, String nodeId) throws BaseCoreException;
	
	/**
	 * 根据过滤条件操作数据记录更新到数据库
	 * 
	 * @param params 过滤条件参数
	 * @param dtRecord 数据记录
	 */
	int updateBy(Map<String, Object> params) throws BaseCoreException;
	
	/**
	 * 根据过滤条件操作数据记录更新到数据库
	 * 
	 * @param params 过滤条件参数
	 * @param dtRecord 数据记录
	 * @param nodeId 需要执行Sql配置节点id，sqlNodeId为空使用默认：SQL_UPDATE_BY
	 */
	int updateBy(Map<String, Object> params, String nodeId) throws BaseCoreException;
	
	/**
	 * 根据数据编号删除数据记录
	 * 
	 * @param id 数据编号
	 */
	int deleteById(long id) throws BaseCoreException;
	
	
	/**
	 * 根据过滤条件删除数据记录
	 * 
	 * @param params 过滤条件参数
	 */
	int deleteBy(Map<String, Object> params) throws BaseCoreException;
	
	/**
	 * 根据过滤条件删除数据记录
	 * @param params
	 * @param nodeId
	 * @return
	 * @throws BaseCoreException
	 */
	int deleteBy(Map<String, Object> params,String nodeId) throws BaseCoreException;
	
	/**
	 * 根据筛选条件和分页参数对数据记录进行分页查询
	 * 
	 * @param pageParam 分页参数对象
	 * @param params 查询筛选条件
	 */
	PageBean listPage(PageParam pageParam, Map<String, Object> params, String countSql, String pageSql) throws BaseCoreException;
	
	/**
	 * 根据筛选条件和分页参数对数据记录进行分页查询
	 * 
	 * @param pageParam 分页参数对象
	 * @param params 查询筛选条件
	 */
	PageBean listPage(PageParam pageParam, Map<String, Object> params) throws BaseCoreException;
	
	/**
	 * 根据数据编号获得数据记录
	 * 
	 * @param id 数据编号
	 * @author tianjing
	 * @date 2015年6月30日 上午11:25:54
	 */
	T getById(long id);
	
	/**
	 * 根据筛选条件查询多条数据记录
	 * 
	 * @param params 条件筛选参数
	 */
	List<T> listBy(Map<String, Object> params);
	
	/**
	 * 根据条件筛选多条数据记录
	 * 
	 * @param params
	 * @param sqlNodeId
	 * @return
	 * @throws BaseCoreException
	 */
	public List<T> listBy(Map<String, Object> params, String sqlNodeId);
	
	/**
	 * 根据筛选条件获得数据记录
	 * 
	 * @param params 条件筛选参数
	 */
	T getBy(Map<String, Object> params);
	
	/**
	 * 根据条件筛选获得数据记录
	 * 
	 * @param params
	 * @param nodeId
	 * @return Entity
	 * @throws BaseCoreException
	 */
	T getBy(Map<String, Object> params, String nodeId);
	
	/**
	 * 根据条件筛选获得数据记录
	 * 
	 * @param params
	 * @param nodeId
	 * @return Entity
	 * @throws BaseCoreException
	 */
	T getBy(T dtRecord, String nodeId);
	
	/**
	 * 根据筛选条件查询数据记录条数
	 * 
	 * @param params 筛选条件。条件可为null
	 */
	long getCntBy(Map<String, Object> params);
}
