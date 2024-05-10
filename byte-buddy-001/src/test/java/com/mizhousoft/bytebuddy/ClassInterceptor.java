package com.mizhousoft.bytebuddy;

import org.junit.jupiter.api.Test;

import com.mizhousoft.bytebuddy.entity.Task;
import com.mizhousoft.bytebuddy.interceptor.TimingInterceptor;
import com.mizhousoft.bytebuddy.service.TaskService;
import com.mizhousoft.bytebuddy.service.impl.TaskServiceImpl;

/**
 * ClassInterceptor
 *
 * @version
 */
public class ClassInterceptor
{
	@Test
	public void test() throws Exception
	{
		TimingInterceptor interceptor = new TimingInterceptor();
		TaskService taskService = interceptor.getProxy(TaskServiceImpl.class);

		Task task = taskService.deleteById(1);

		taskService.saveTask(task);
	}
}
