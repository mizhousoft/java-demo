package com.mizhousoft.bytebuddy.service;

/**
 * AccessService
 *
 */
public abstract class AccessService
{
	public JdbcAccess access = new JdbcAccess();

	/**
	 * 获取access
	 * 
	 * @return
	 */
	public JdbcAccess getAccess()
	{
		return access;
	}
}
