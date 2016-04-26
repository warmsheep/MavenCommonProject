package org.warmsheep.framework.db.exception;

import org.warmsheep.util.properties.ConfigReader;


/**
 * 基础的异常类（所有异常继承这个）
 * 
 * 
 * 
 */
public class BaseCoreException extends Exception
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 数据记录对象不能为空
	 */
	public static final String DT_RECORD_NOT_NULL = "db.error.dtRecordNotNull";
	/**
	 * 参数条件对象不能为空
	 */
	public static final String PARAMS_NOT_NULL = "db.error.paramsNotNull";
	/**
	 * 数据记录存入数据库失败
	 */
	public static final String INSERT_FAILED = "db.error.insertFailed";
	/**
	 * 数据记录更新到数据库失败
	 */
	public static final String UPDATE_FAILED = "db.error.updateFailed";
	/**
	 * 数据记录del数据库操作失败
	 */
	public static final String DEL_FAILED = "db.error.delFailed";
	/**
	 * 查询数据记录失败
	 */
	public static final String SELECT_FAILED = "db.error.selectFailed";
	/**
	 * 实体对象不能为空
	 */
	public static final String ENTITY_OBJECT_NOT_NULL = "basic.error.entityObjectNotNull";
	/**
	 * 实体对象ID不能为空
	 */
	public static final String ENTITY_ID_NOT_NULL = "basic.error.entityIdNotNull";
	/**
	 * 违法的TOKEN
	 */
	public static final String TOKEN_IS_ILLICIT = "db.error.tokenIsIllicit";
	private String code;
	private String msg;
	
	public BaseCoreException(String code)
	{
		super(ConfigReader.getContextProperty(code) == null ? code : ConfigReader.getContextProperty(code));
		this.code = code;
		this.msg = ConfigReader.getContextProperty(code) == null ? code : ConfigReader.getContextProperty(code);
	}
	
	public String getCode() {
		return code;
	}
	
	public String getMsg() {
		return msg;
	}
}
