package com.mizhousoft.flowable.workflow;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mizhousoft.flowable.boot.FlowableApplication;

/**
 * StuLeaveWorkFlow
 *
 * @version
 */
@SpringBootTest(classes = FlowableApplication.class)
public class StuLeaveSetAssignee
{
	@Autowired
	private ProcessEngine processEngine;

	private RepositoryService repositoryService;

	private RuntimeService runtimeService;

	private TaskService taskService;

	@BeforeEach
	public void before() throws IOException
	{
		repositoryService = processEngine.getRepositoryService();
		runtimeService = processEngine.getRuntimeService();
		taskService = processEngine.getTaskService();
	}

	@Test
	public void deploy()
	{
		repositoryService.createDeployment().addClasspathResource("bpmn/leave-initiator.bpmn20.xml").deploy();
	}

	@Test
	public void studentStart()
	{
		Authentication.setAuthenticatedUserId("wangjun");

		Map<String, Object> map = new HashMap<>();
		map.put("day", 3);
		map.put("studentName", "may");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("stuLeave", map);

		Map<String, Object> variables = new HashMap<>();
		variables.put("start", "start");
		variables.put("action", "apply");

		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		taskService.complete(task.getId(), variables, true);
	}

	@Test
	public void claim()
	{
		List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("a").list();
		Task task = tasks.get(0);

		taskService.claim(task.getId(), "wanglaoshi");
	}

	@Test
	public void setAssignee()
	{
		List<Task> tasks = taskService.createTaskQuery().taskAssignee("wanglaoshi").list();
		Task task = tasks.get(0);

		taskService.setAssignee(task.getId(), "liliang");
		taskService.setOwner(task.getId(), "wanglaoshi");
	}

	@Test
	public void complete()
	{
		List<Task> tasks = taskService.createTaskQuery().taskAssignee("liliang").list();
		Task task = tasks.get(0);

		// 通过审核
		HashMap<String, Object> map = new HashMap<>();
		map.put("outcome", "通过");

		taskService.complete(task.getId(), map);
	}
}
