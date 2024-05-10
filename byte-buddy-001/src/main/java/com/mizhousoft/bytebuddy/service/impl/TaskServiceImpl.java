package com.mizhousoft.bytebuddy.service.impl;

import com.mizhousoft.bytebuddy.entity.Task;
import com.mizhousoft.bytebuddy.service.AccessService;
import com.mizhousoft.bytebuddy.service.TaskService;

/**
 * 任务服务
 *
 */
public class TaskServiceImpl extends AccessService implements TaskService
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveTask(Task task)
	{
		saveTask(task, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveTask(Task task, String creator)
	{
		getAccess().saveProcess();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Task deleteById(int id)
	{
		Task task = getById(id);
		if (null != task)
		{
			System.out.println("Delete task successfully.");
		}

		return task;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Task getById(int id)
	{
		Task task = new Task();
		task.setId(1);
		task.setName("Task1");

		return task;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startTask(Task task)
	{

	}
}
