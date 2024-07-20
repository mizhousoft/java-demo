package com.mizhousoft.flowable.workflow;

/**
 * TaskData
 *
 * @version
 */
public class TaskData
{
	private String id;

	private String day;

	private String name;

	/**
	 * 构造函数
	 *
	 * @param id
	 * @param day
	 * @param name
	 */
	public TaskData(String id, String day, String name)
	{
		super();
		this.id = id;
		this.day = day;
		this.name = name;
	}

	/**
	 * 获取id
	 * 
	 * @return
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * 设置id
	 * 
	 * @param id
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * 获取day
	 * 
	 * @return
	 */
	public String getDay()
	{
		return day;
	}

	/**
	 * 设置day
	 * 
	 * @param day
	 */
	public void setDay(String day)
	{
		this.day = day;
	}

	/**
	 * 获取name
	 * 
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * 设置name
	 * 
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("{\"id\":\"").append(id).append("\", \"day\":\"").append(day).append("\", \"name\":\"").append(name).append("\"}");
		return builder.toString();
	}
}
