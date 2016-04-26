package org.warmsheep.framework.db.entity;

import java.io.Serializable;

/**
 * 
 * @描述: 基础实体类，包含各实体公用属性 .
 */
public class BaseEntity implements Serializable, Cloneable
{
	
	private static final long serialVersionUID = 1L;
	private Long id;
	
	/**
	 * 版本号
	 */
	private Integer version = 1;
	
	/**
	 * 删除状态 1 已删除 0 未删除
	 */
	private Integer deleteStatus = 0;
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	/**
	 * 版本号
	 */
	public Integer getVersion()
	{
		return version;
	}
	
	/**
	 * 版本号
	 */
	public void setVersion(Integer version)
	{
		this.version = version;
	}
	
	/**
	 * 删除状态 1 已删除 0 未删除
	 */
	public Integer getDeleteStatus()
	{
		return deleteStatus;
	}
	
	/**
	 * 删除状态 1 已删除 0 未删除
	 */
	public void setDeleteStatus(Integer deleteStatus)
	{
		this.deleteStatus = deleteStatus;
	}
	
}
