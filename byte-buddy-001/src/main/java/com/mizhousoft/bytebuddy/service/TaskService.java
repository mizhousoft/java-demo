package com.mizhousoft.bytebuddy.service;

import com.mizhousoft.bytebuddy.entity.Task;

/**
 * 任务服务
 *
 */
public interface TaskService
{
	/**
	 * 保存任务
	 * 
	 * @param task
	 */
	void saveTask(Task task);
	
	/**
	 * 保存任务
	 * 
	 * @param task
	 * @param creator
	 */
	void saveTask(Task task, String creator);

	/**
	 * 根据ID删除
	 * 
	 * @param id
	 * @return
	 */
	Task deleteById(int id);

	/**
	 * 根据ID获取
	 * 
	 * @param id
	 * @return
	 */
	Task getById(int id);

	/**
	 * 启动任务
	 * 
	 * @param task
	 */
	void startTask(Task task);
}
