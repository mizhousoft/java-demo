package com.mizhousoft.flowable.workflow;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
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
public class StuLeaveCandidateGroups
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
		repositoryService.createDeployment().addClasspathResource("bpmn/leave-CandidateGroups.bpmn20.xml").deploy();
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

		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		taskService.complete(task.getId(), variables);
	}

	@Test
	public void queryOwner()
	{
		List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery().startedBy("wangjun").list();
		System.out.println(instances);

		List<HistoricProcessInstance> list = processEngine.getHistoryService()
		        .createHistoricProcessInstanceQuery()
		        .startedBy("wangjun")
		        .unfinished()
		        .list();

		System.out.println(list);
	}

	@Test
	public void teacherList()
	{
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("a").list();
		List<TaskData> taskVOList = tasks.stream().map(task -> {
			Map<String, Object> variables = taskService.getVariables(task.getId());
			System.out.println(variables);

			return new TaskData(task.getId(), variables.get("day").toString(), variables.get("studentName").toString());
		}).collect(Collectors.toList());

		System.out.println(taskVOList);

		Task task = tasks.get(0);

		Map<String, Object> vars = runtimeService.getVariables(task.getExecutionId());
		System.out.println(vars);
	}

	@Test
	public void teacherSetInstVars()
	{
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("a").list();
		Task task = tasks.get(0);

		runtimeService.setVariable(task.getExecutionId(), "executea", "executea");
		runtimeService.setVariableLocal(task.getExecutionId(), "executeb", "executeb");

		Map<String, Object> vars = runtimeService.getVariables(task.getExecutionId());
		System.out.println(vars);

		vars = runtimeService.getVariablesLocal(task.getExecutionId());
		System.out.println(vars);
	}

	@Test
	public void teacherSetTaskVars()
	{
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("a").list();
		Task task = tasks.get(0);

		taskService.setVariable(task.getId(), "testa", "testa");
		taskService.setVariableLocal(task.getId(), "testb", "testb");

		Map<String, Object> vars = taskService.getVariables(task.getId());
		System.out.println(vars);

		vars = taskService.getVariablesLocal(task.getId());
		System.out.println(vars);
	}

	@Test
	public void teacherClaim()
	{
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("a").list();
		Task task = tasks.get(0);

		taskService.claim(task.getId(), "wanglaoshi");
	}

	@Test
	public void teacherSetAssignee()
	{
		List<Task> tasks = taskService.createTaskQuery().taskAssignee("wanglaoshi").list();
		Task task = tasks.get(0);

		taskService.setAssignee(task.getId(), "jinlaoshi");
	}

	@Test
	public void teacherSetAssignee2()
	{
		List<Task> tasks = taskService.createTaskQuery().taskAssignee("jinlaoshi").list();
		Task task = tasks.get(0);

		taskService.setAssignee(task.getId(), null);
	}

	@Test
	public void teacherClaim2()
	{
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("a").list();
		Task task = tasks.get(0);

		taskService.claim(task.getId(), "lilaoshi");
	}

	@Test
	public void teacherDelegateTask()
	{
		List<Task> tasks = taskService.createTaskQuery().taskAssignee("lilaoshi").list();
		Task task = tasks.get(0);

		taskService.setOwner(task.getId(), "lilaoshi");
		taskService.delegateTask(task.getId(), "oulaoshi");
	}

	@Test
	public void teacherApply()
	{
		List<Task> tasks = taskService.createTaskQuery().taskAssignee("oulaoshi").list();
		Task task = tasks.get(0);

		// 通过审核
		HashMap<String, Object> map = new HashMap<>();
		map.put("outcome", "通过");

		taskService.resolveTask(task.getId());
		taskService.complete(task.getId(), map);
	}

	@Test
	public void teacherReject()
	{
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("a").list();
		Task task = tasks.get(0);

		// 通过审核
		HashMap<String, Object> map = new HashMap<>();
		map.put("outcome", "驳回");
		taskService.complete(task.getId(), map);
	}

	@Test
	public void deanList()
	{
		// 此处.taskCandidateGroup("b")的值“b”即是画流程图时辅导员审批节点"分配用户-候选组"中填写的值
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("b").list();
		List<TaskData> taskVOList = tasks.stream().map(task -> {
			Map<String, Object> variables = taskService.getVariables(task.getId());
			return new TaskData(task.getId(), variables.get("day").toString(), variables.get("studentName").toString());
		}).collect(Collectors.toList());

		System.out.println(taskVOList);
	}

	@Test
	public void deanApply()
	{
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("b").list();

		// 通过审核
		HashMap<String, Object> map = new HashMap<>();
		map.put("outcome", "通过");
		for (Task task : tasks)
		{
			taskService.complete(task.getId(), map);
		}
	}

	@Test
	public void deanReject()
	{
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("b").list();
		Task task = tasks.get(0);

		// 通过审核
		HashMap<String, Object> map = new HashMap<>();
		map.put("outcome", "驳回");
		// for (Task task : tasks) {
		// taskService.complete(task.getId(), map);
		// }
		taskService.complete(task.getId(), map);
	}

	@Test
	public void studentSubmitAgain()
	{
		Task task = taskService.createTaskQuery().taskAssignee("may").singleResult();

		Map<String, Object> map = new HashMap<>();
		map.put("day", 3);
		taskService.complete(task.getId(), map);
	}
}
