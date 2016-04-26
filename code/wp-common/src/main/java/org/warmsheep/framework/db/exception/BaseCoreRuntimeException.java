package org.warmsheep.framework.db.exception;

import org.warmsheep.util.properties.ConfigReader;


/**
 * 基础的运行时异常（所有的运行时异常继承此类）
 * @author Warmsheep
 *
 */
public class BaseCoreRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String msg;
	
	public BaseCoreRuntimeException(String code){
		super(ConfigReader.getContextProperty(code));
		this.code = code;
		this.msg = ConfigReader.getContextProperty(code);
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
	
}
