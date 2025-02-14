package org.flowable.holidayrequest;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class HolidayRequest implements CommandLineRunner {

	@Autowired
	private DeploymentService deploymentService;

	private ProcessEngine processEngine;

	public static void main(String[] args) {
		SpringApplication.run(HolidayRequest.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Build the Process Engine
		ProcessEngine processEngine = InitializeProcessEngine.initialize();

		// Deploy the process definition
		Deployment deployment = deploymentService.deployProcessDefinition(processEngine);

		// Query the process definition
		RepositoryService repositoryService = processEngine.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()) // Query based on the deployment ID
				.singleResult();

		// Output the process definition name
		if (processDefinition != null) {
			System.out.println("Found process definition : " + processDefinition.getName());
		} else {
			System.out.println("No process definition found for the deployment.");
		}

		RuntimeService runtimeService = StartProcessInstance.start(processEngine);

		// Collect user input
		Map<String, Object> variables1 = UserInput.getUserInput();
		// Complete the user task
		CompleteUserTask.completeTask(processEngine, variables1);
		System.out.println("User Task completed successfully!");

		// Manager approval input
		Map<String, Object> variables2 = ManagerInput.getManagerInput(processEngine);
		CompleteManagerTask.completeTask(processEngine, variables2);

		// Process Instance Completed
		System.out.println("Process Instance Completed!");

	}
}


