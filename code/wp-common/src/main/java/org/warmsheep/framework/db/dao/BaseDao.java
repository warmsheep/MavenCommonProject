package org.warmsheep.framework.db.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.warmsheep.framework.db.entity.BaseEntity;
import org.warmsheep.framework.db.exception.BaseCoreException;
import org.warmsheep.framework.page.PageBean;
import org.warmsheep.framework.page.PageParam;

/**
 * 
 * @描述: 数据访问层基础支撑类.
 */
public abstract class BaseDao<T extends BaseEntity> extends SqlSessionDaoSupport implements IBaseDao<T>
{
	// 日志记录
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseDao.class);
	
	public static final String SQL_INSERT = "insert";
	public static final String SQL_INSERT_BATCH = "insertBatch";
	public static final String SQL_UPDATE_BY_ID = "updateById";
	public static final String SQL_UPDATE_BY = "updateBy";
	public static final String SQL_DELETE_BY_ID = "deleteById";
	public static final String SQL_DELETE_BY = "deleteBy";
	
	public static final String SQL_GET_BY_ID = "getById";
	public static final String SQL_GET_BY = "getBy";
	public static final String SQL_LIST_BY = "listBy";
	public static final String SQL_GET_COUNT_BY = "getCntBy";
	
	public static final String SQL_LIST_PAGE = "listPage";
	public static final String SQL_LIST_PAGE_COUNT = "listPageCount";
	public static final String SQL_COUNT_BY_PAGE_PARAM = "countByPageParam"; // 根据当前分页参数进行统计
	
	/**
	 * 注入SqlSessionTemplate实例(要求Spring中进行SqlSessionTemplate的配置).<br/>
	 * 可以调用sessionTemplate完成数据库操作.
	 */
	private SqlSessionTemplate sessionTemplate;
	
	@Autowired
	public void setSessionTemplate(SqlSessionTemplate sessionTemplate) {
		this.sessionTemplate = sessionTemplate;
	}
	
	public SqlSessionTemplate getSessionTemplate() {
		return sessionTemplate;
	}
	
	@Override
	public long insert(T dtRecord) throws BaseCoreException {
		String sqlNodeId = getStatement(SQL_INSERT);
		if (null == dtRecord) {
			LOGGER.error("数据记录对象为空, insert数据库失败. sqlNodeId={}", sqlNodeId);
			throw new BaseCoreException(BaseCoreException.DT_RECORD_NOT_NULL);
		}
		int optResult = this.sessionTemplate.insert(sqlNodeId, dtRecord);
		if (optResult < 1) {
			LOGGER.error("数据记录insert数据库失败. sqlNodeId={}, optResult={}", sqlNodeId, optResult);
			throw new BaseCoreException(BaseCoreException.INSERT_FAILED);
		}
		LOGGER.debug("数据记录insert数据库. sqlNodeId={}, successRecordCount={}", sqlNodeId, optResult);
		if (null != dtRecord && null != dtRecord.getId())
			return dtRecord.getId();
		else
			throw new BaseCoreException(BaseCoreException.INSERT_FAILED);
	}
	
	@Override
	public int insertBatch(List<T> dtRecords) throws BaseCoreException {
		String sqlNodeId = getStatement(SQL_INSERT_BATCH);
		if (null == dtRecords || dtRecords.isEmpty()) {
			LOGGER.error("数据记录对象为空, insert数据库失败. sqlNodeId={}", sqlNodeId);
			throw new BaseCoreException(BaseCoreException.DT_RECORD_NOT_NULL);
		}
		int optResult = this.sessionTemplate.insert(sqlNodeId, dtRecords);
		// 执行成功记录与提交记录一致
		if (optResult != dtRecords.size()) {
			LOGGER.error("数据记录insert数据库失败. sqlNodeId={}, insertRecordCount={}, successRecordCount={}", sqlNodeId, dtRecords.size(), optResult);
			throw new BaseCoreException(BaseCoreException.INSERT_FAILED);
		}
		LOGGER.debug("数据记录insert数据库. sqlNodeId={}, successRecordCount={}", sqlNodeId, optResult);
		return optResult;
	}
	
	@Override
	public int insertBatch(Map<String, Object> dtRecords, String sqlId) throws BaseCoreException {
		String sqlNodeId = getStatement(sqlId);
		if (null == dtRecords || dtRecords.isEmpty()) {
			LOGGER.error("数据记录对象为空, insert数据库失败. sqlNodeId={}", sqlNodeId);
			throw new BaseCoreException(BaseCoreException.DT_RECORD_NOT_NULL);
		}
		int optResult = this.sessionTemplate.insert(sqlNodeId, dtRecords);
		// 执行成功记录与提交记录一致
		if (optResult < 1) {
			LOGGER.error("数据记录insert数据库失败. sqlNodeId={}, optResult={}", sqlNodeId, optResult);
			throw new BaseCoreException(BaseCoreException.INSERT_FAILED);
		}
		LOGGER.debug("数据记录insert数据库, sqlNodeId={}, successRecordCount={}", sqlNodeId, optResult);
		return optResult;
	}
	
	@Override
	public int updateBy(T dtRecord, String nodeId) throws BaseCoreException {
		if (null == nodeId)
			nodeId = SQL_UPDATE_BY;
		String sqlNodeId = getStatement(nodeId);
		if (null == dtRecord) {
			LOGGER.error("数据记录对象为空, update数据库失败. sqlNodeId={}", sqlNodeId);
			throw new BaseCoreException(BaseCoreException.DT_RECORD_NOT_NULL);
		}
		int optResult = this.sessionTemplate.update(sqlNodeId, dtRecord);
		LOGGER.debug("数据记录update到数据库. sqlNodeId={}, successRecordCount={}", sqlNodeId, optResult);
		return optResult;
	}
	
	@Override
	public int updateById(T dtRecord) throws BaseCoreException {
		return this.updateBy(dtRecord, SQL_UPDATE_BY_ID);
	}
	
	@Override
	public int updateBy(Map<String, Object> params, String nodeId) throws BaseCoreException {
		if (null == nodeId){
			nodeId = SQL_UPDATE_BY;
		}
		String sqlNodeId = getStatement(nodeId);
		if (null == params || params.isEmpty()) {
			LOGGER.error("数据记录对象为空, update数据库失败. sqlNodeId={}", sqlNodeId);
			throw new BaseCoreException(BaseCoreException.DT_RECORD_NOT_NULL);
		}
		int optResult = this.sessionTemplate.update(sqlNodeId, params);
		LOGGER.debug("数据记录update到数据库. sqlNodeId={}, successRecordCount={}", sqlNodeId, optResult);
		return optResult;
	}
	
	@Override
	public int updateBy(Map<String, Object> params) throws BaseCoreException {
		return this.updateBy(params, null);
	}
	
	@Override
	public int deleteById(long id) throws BaseCoreException {
		String sqlNodeId = getStatement(SQL_DELETE_BY_ID);
		int optResult = this.sessionTemplate.delete(sqlNodeId, id);
		if (optResult < 1) {
			LOGGER.error("数据记录del操作失败. sqlNodeId={}, optResult={}", sqlNodeId, optResult);
			throw new BaseCoreException(BaseCoreException.DEL_FAILED);
		}
		LOGGER.debug("数据记录del操作. sqlNodeId={}, optResult={}", sqlNodeId, optResult);
		return optResult;
	}
	
	@Override
	public int deleteBy(Map<String, Object> params) throws BaseCoreException {
		return deleteBy(params,null);
	}
	
	@Override
	public int deleteBy(Map<String, Object> params,String nodeId) throws BaseCoreException {
		if (null == nodeId){
			nodeId = SQL_DELETE_BY;
		}
		String sqlNodeId = getStatement(nodeId);
		if (null == params || params.isEmpty()) {
			LOGGER.error("数据记录对象为空, 数据记录del操作失败. sqlNodeId={}", sqlNodeId);
			throw new BaseCoreException(BaseCoreException.DT_RECORD_NOT_NULL);
		}
		int optResult = this.sessionTemplate.delete(sqlNodeId, params);
		LOGGER.debug("数据记录del操作, sqlNodeId={}, optResult={}", sqlNodeId, optResult);
		return optResult;
	}
	
	@Override
	public PageBean listPage(PageParam pageParam, Map<String, Object> params, String countSql, String pageSql) throws BaseCoreException {
		if (null == pageParam) {
			LOGGER.error("数据pageParam为空, listPage操作失败。");
			throw new BaseCoreException(BaseCoreException.DT_RECORD_NOT_NULL);
		}
		if (null == params)
			params = new HashMap<String, Object>();
		// 根据页面传来的分页参数构造SQL分页参数
		int pageSize = pageParam.getNumPerPage();
		int pageFirst = pageParam.getPageNum();
		params.put("pageSize", pageSize);
		params.put("pageFirst", (pageFirst - 1) * pageSize);
		params.put("startRowNum", (pageFirst - 1) * pageSize);
		params.put("endRowNum", pageFirst * pageSize);
		// 统计总记录数
		Long count = sessionTemplate.selectOne(getStatement(countSql), params);
		// 获取分页数据集
		List<Object> list = sessionTemplate.selectList(getStatement(pageSql), params);
		
		Object isCount = params.get("isCount"); // 是否统计当前分页条件下的数据：1:是，其他为否
		// 构造分页对象
		if (isCount != null && "1".equals(isCount.toString())) {
			Map<String, Object> countResultMap = sessionTemplate.selectOne(getStatement(SQL_COUNT_BY_PAGE_PARAM), params);
			return new PageBean(pageParam.getPageNum(), pageSize, count.intValue(), list, countResultMap);
		}
		else {
			return new PageBean(pageParam.getPageNum(), pageSize, count.intValue(), list);
		}
	}
	
	@Override
	public PageBean listPage(PageParam pageParam, Map<String, Object> params) throws BaseCoreException {
		if (null == pageParam) {
			LOGGER.error("数据pageParam为空, listPage操作失败。");
			throw new BaseCoreException(BaseCoreException.DT_RECORD_NOT_NULL);
		}
		if (null == params)
			params = new HashMap<String, Object>();
		// 根据页面传来的分页参数构造SQL分页参数
		int pageSize = pageParam.getNumPerPage();
		int pageFirst = pageParam.getPageNum();
		params.put("pageSize", pageSize);
		params.put("pageFirst", (pageFirst - 1) * pageSize);
		params.put("startRowNum", (pageFirst - 1) * pageSize);
		params.put("endRowNum", pageFirst * pageSize);
		// 统计总记录数
		Long count = sessionTemplate.selectOne(getStatement(SQL_LIST_PAGE_COUNT), params);
		// 获取分页数据集
		List<Object> list = sessionTemplate.selectList(getStatement(SQL_LIST_PAGE), params);
		
		Object isCount = params.get("isCount"); // 是否统计当前分页条件下的数据：1:是，其他为否
		// 构造分页对象
		if (isCount != null && "1".equals(isCount.toString())) {
			Map<String, Object> countResultMap = sessionTemplate.selectOne(getStatement(SQL_COUNT_BY_PAGE_PARAM), params);
			return new PageBean(pageParam.getPageNum(), pageSize, count.intValue(), list, countResultMap);
		}
		else {
			return new PageBean(pageParam.getPageNum(), pageSize, count.intValue(), list);
		}
	}
	
	@Override
	public T getById(long id) {
		String sqlNodeId = getStatement(SQL_GET_BY_ID);
		LOGGER.debug("数据记录select操作. sqlNodeId={}", sqlNodeId);
		return this.sessionTemplate.selectOne(sqlNodeId, id);
	}
	
	@Override
	public List<T> listBy(Map<String, Object> params) {
		String sqlNodeId = getStatement(SQL_LIST_BY);
		LOGGER.debug("数据记录select操作. sqlNodeId={}", sqlNodeId);
		return this.sessionTemplate.selectList(sqlNodeId, params);
	}
	
	@Override
	public List<T> listBy(Map<String, Object> params, String nodeId) {
		String sqlNodeId = getStatement(nodeId);
		LOGGER.debug("数据记录select操作. sqlNodeId={}", sqlNodeId);
		return this.sessionTemplate.selectList(sqlNodeId, params);
	}
	
	@Override
	public T getBy(Map<String, Object> params) {
		String sqlNodeId = getStatement(SQL_GET_BY);
		LOGGER.debug("数据记录select操作. sqlNodeId={}", sqlNodeId);
		return this.sessionTemplate.selectOne(sqlNodeId, params);
	}
	
	@Override
	public T getBy(Map<String, Object> params, String nodeId) {
		String sqlNodeId = getStatement(nodeId);
		LOGGER.debug("数据记录select操作. sqlNodeId={}", sqlNodeId);
		return this.sessionTemplate.selectOne(sqlNodeId, params);
	}
	
	@Override
	public T getBy(T dtRecord, String nodeId) {
		String sqlNodeId = getStatement(nodeId);
		LOGGER.debug("数据记录select操作. sqlNodeId={}", sqlNodeId);
		return this.sessionTemplate.selectOne(sqlNodeId, dtRecord);
	}
	
	@Override
	public long getCntBy(Map<String, Object> params) {
		String sqlNodeId = getStatement(SQL_GET_COUNT_BY);
		LOGGER.debug("数据记录select操作. sqlNodeId={}", sqlNodeId);
		return this.sessionTemplate.selectOne(sqlNodeId, params);
	}
	
	/**
	 * 获取Mapper sql命名空间
	 * 
	 * @param sql定义id标识
	 */
	public final String getStatement(String sqlId) {
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.append(".").append(sqlId);
		return sb.toString();
	}
}
